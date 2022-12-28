package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.webgen.WebGen;

import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.time.Month;

/**
 * This class represents a plugin : CountLinesPerAuthorPerDatePlugin.
 * It counts <b>the lines added or deleted per Date and by Author</b>.
 * The user can choose if he wants the <b>added</b> or <b>deleted</b> lines, if he wants to sort them by <b>Days</b>, <b>Weeks</b> or <b>Months</b>.
 * The lines will be automatically sorted by Author.
 * The plugin can also be used on all branches, but only on the current branch.
 * <p> If the user want to have the lines <b>added</b> or <b>deleted</b> per Date without the author, he can use the CountLinesPerDate plugin.</p>
 * <p> If The user want to have the lines <b>added</b> or <b>deleted</b> by Author without the date, he can use the CountLinesPerAuthor plugin.</p>
 * @see CountLinesPerDatePlugin
 * @see CountLinesPerAuthorPlugin
 */

public class CountLinesPerAuthorPerDatePlugin implements AnalyzerPlugin {
	
	// VARIABLES
	/**
	 * General configuration of the plugin
	 */
    private final Configuration configuration;
    
    /**
     * The value change if the user wants the number of lines per days, per weeks and per months.
     * The value sort the lines per months, this is a default value.
     */
    private String howToSort = "months";
    
    /**
     * The plugin count the added or deleted lines according to the content of the variable.
     * If the boolean is "true", the plugin count the lines added.
     * If the boolean is "false", the plugin count the lines deleted.
     */
    private boolean lines;
    
    /**
     * The plugin count the lines for all branches or the branch where the user is according to the content of the variable.
     * If the boolean is "true", the plugin count the lines for all branches.
     * If the boolean is "false", the plugin count the lines for the branch where the user is.
     */
    private boolean allBranches;
    /**
     * Result of the plugin
     */
    private Result result;
    
    
    // CONSTRUCTOR
    /**
     * Constructs a CountLinesPerAuthorPerDate object given the general Configuration, the way of sorting, the lines added or deleted and a boolean that describes if the plugin should be used on all branches or not
     * @param generalConfiguration the generalConfiguration
     * @param howToSort the way of sorting
     * @param lines the lines added or deleted
     * @param allBranches for all branches or not
     */
    public CountLinesPerAuthorPerDatePlugin(Configuration generalConfiguration, String howToSort, boolean lines, boolean allBranches) {
        this.configuration = generalConfiguration;
        this.howToSort = howToSort;
        this.lines = lines;
        this.allBranches = allBranches;
    }
    
    /**
     * Return the result of the plugin
     * @param gitLog a list of the commits
     * @return the result of the plugin
     */
    public Result processLog(List<Commit> gitLog) {
    	List<Commit> gitLog2 = sameAuthor(gitLog);
    	
    	var result = new Result();
    	
    	// first, we find the commits sorting per the days
    	Map<LocalDate, List<Commit>> commitsPerDate = sortCommitsPerDays(gitLog2);
    	
    	// then, we create a new Map with a Key which associates an author and his number of added/deleted lines
    	for(var commitsPerDays : commitsPerDate.entrySet()) {
    		LocalDate m = commitsPerDays.getKey();
    		Map<String, Integer> linesAddedDeleted = linesPerAuthor(commitsPerDays.getValue());
    		result.linesPerAuthorPerDate.put(m, linesAddedDeleted);
    	}
    	
    	// now, the linesPerAuthorPerDate attribute associates the list of authors (and its number of added/deleted lines) per days
    	// we just have to sort them by month or week
    	
    	sortTheResultPerMonthsOfPerWeeks(result);
    	return result;
    }
    
    /**
     * Return a list of commits, but the authors do not appear twice
     * @param gitLog
     * @return the list of commits, but the authors do not appear twice
     */
    public List<Commit> sameAuthor(List<Commit> gitLog) {
    	List<Commit> gitLog2 = new LinkedList<Commit>();
    	Map<String,String> emailToName = new HashMap<String,String>();
    	for(var commit : gitLog) {
    		String[] a = commit.author.split(" ");
    		String email = a[a.length-1];
    		if(emailToName.get(email) == null) {
            	emailToName.put(email, commit.author);
            }
    	}
    	for(var commit : gitLog) {
    		String[] a = commit.author.split(" ");
    		String email = a[a.length-1];
    		if(emailToName.containsKey(email)) {
    			String author = emailToName.get(email);
    			String[] nameTab = author.split(" ");
    			String name = "";
    			for(int i=0; i<nameTab.length-1; i++) {
    				name += nameTab[i] + " ";
    			}
    			Commit m = new Commit(commit.id, name, commit.date, commit.description, commit.mergedFrom);
    			m.linesAdded = commit.linesAdded;
    			m.linesDeleted = commit.linesDeleted;
    			gitLog2.add(m);
            }
    	}
    	return gitLog2;
    }
    
    /**
     * Return a Map with the day in key and the list of commits with the commits that have been made during the day in value
     * @param gitLog the list of commits
     * @return a Map with the day in key and the list of commits with the commits that have been made during the day in value
     */
    public Map<LocalDate, List<Commit>> sortCommitsPerDays(List<Commit> gitLog) {
    	Map<LocalDate, List<Commit>> res = new TreeMap<>();
    	for (var commit : gitLog) {
    		LocalDate date = commit.date.toLocalDate();
    		
    		List<Commit> list = new LinkedList<>();
        	for (var commit2 : gitLog) {
        		LocalDate c = commit2.date.toLocalDate();
        		if(c.equals(date)) {
        			list.add(commit2);
        		}
            }
        	
	        res.put(date, list);
        }
    	return res;
    }
    
    /**
     * Return a Map with the author in Key and the number of lines the author made per Days
     * @param gitLog the list of the commits
     * @return a Map with the author in Key and the number of lines the author made per Days
     */
    public Map<String, Integer> linesPerAuthor(List<Commit> gitLog) {
    	Map<String, Integer> res = new HashMap<>();
        for (var commit : gitLog) {
    		// The plugin don't count the line added/deleted from the merged commit because the lines will added and deleted twice
    		if(commit.mergedFrom == null) {
	        	String m = commit.author;
	        	var oldNbLines = res.getOrDefault(m, 0);
	        	int nbLines = 0;
	        	if(lines) {
	        		nbLines = oldNbLines + commit.linesAdded;
	        	} else {
	        		nbLines = oldNbLines + commit.linesDeleted;
	        	}
	        	res.put(m, nbLines);
    		}
        }
        return res;
    }
    
    /**
     * Updates the results of the Plugin according to the way of sorting
     * @param result the reslt of the plugin
     */
    public void sortTheResultPerMonthsOfPerWeeks(Result result) {
    	// sort the commits per Months
    	if(howToSort.equals("months")) {
    		// first, we create a new Map
    		Map<LocalDate, Map<String, Integer>> res = new TreeMap<>();
    		// we sort the number of added/deleted lines per months
        	for(var date : result.linesPerAuthorPerDate.entrySet()) {
        		LocalDate m = LocalDate.of(date.getKey().getYear(), date.getKey().getMonth(), 1);
        		// we create a new Map which gives the right number of lines to the authors
        		Map<String, Integer> res2 = authorsAndMonths(m, result);
        		res.put(m, res2);
        	}
        	
        	// change the key and the value of result.commitsPerDate
        	result.linesPerAuthorPerDate.clear();
        	result.linesPerAuthorPerDate.putAll(res);
    	}
    	
    	// sort the commits per Weeks
    	else if(howToSort.equals("weeks")) {
    		// first, we create a new Map
    		Map<String, Map<String, Integer>> res = new TreeMap<>();
    		// we sort the number of added/deleted lines per weeks
        	for(var date : result.linesPerAuthorPerDate.entrySet()) {
        		String m = Integer.toString(date.getKey().getYear()) + " Week " + Integer.toString(date.getKey().getDayOfYear()/7);
        		// we create a new Map which gives the right number of lines to the authors
        		Map<String, Integer> res2 = authorAndWeeks(m, result);
        		res.put(m, res2);
        	}
    		
         // change the key and the value of result.commitsPerWeeks
            result.linesPerAuthorPerWeeks.putAll(res);
    	}
    }
 
    /**
     * Returns plugin results for a special month
     * @param r the result of the plugin
     * @param months the special month
     */
    public Map<String, Integer> authorsAndMonths(LocalDate months, Result r) {
    	Map<String, Integer> res = new HashMap<>();
    	for(var date : r.linesPerAuthorPerDate.entrySet()) {
    		LocalDate m = LocalDate.of(date.getKey().getYear(), date.getKey().getMonth(), 1);
    		if(months.equals(m)) {
    			for(var lines : date.getValue().entrySet()) {
    				String a = lines.getKey();
                	var nb = res.getOrDefault(a, 0);
                	res.put(a, nb + lines.getValue());
    			}
    		}
    	}
    	return res;
    }
    
    /**
     * Returns plugin results for a special week
     * @param r the result of the plugin
     * @param week a special week
     */
    public Map<String, Integer> authorAndWeeks(String week, Result r) {
    	Map<String, Integer> res = new HashMap<>();
    	for(var date : r.linesPerAuthorPerDate.entrySet()) {
    		String m = Integer.toString(date.getKey().getYear()) + " Week " + Integer.toString(date.getKey().getDayOfYear()/7);
    		if(week.equals(m)) {
    			for(var lines : date.getValue().entrySet()) {
    				String a = lines.getKey();
                	var nb = res.getOrDefault(a, 0);
                	res.put(a, nb + lines.getValue());
    			}
    		}
    	}
    	return res;
    }
    
    @Override
    public void run() {
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath(), allBranches));
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }    
    
    /**
     * This class represents the result of the Plugin.
     */
    public class Result implements AnalyzerPlugin.Result {
    	/**
    	 * Result of the plugin per days or per months
    	 */
        private Map<LocalDate, Map<String, Integer>> linesPerAuthorPerDate = new TreeMap<>();
        
        /**
         * Result of the plugin per weeks
         */
        private Map<String, Map<String, Integer>> linesPerAuthorPerWeeks = new TreeMap<>();
        
        /**
         * Return the lines per Author and per Months or per Days
         * @return
         */
        Map<LocalDate, Map<String, Integer>> getLinesPerAuthorPerDate() {
            return linesPerAuthorPerDate;
        }
        
        /**
         * Return the lines per Author and per Weeks
         * @return
         */
        Map<String, Map<String, Integer>> getLinesPerAuthorPerWeeks() {
            return linesPerAuthorPerWeeks;
        }
        
        @Override
        public String getResultAsString() {
        	if(linesPerAuthorPerWeeks.size() != 0) {
        		return linesPerAuthorPerWeeks.toString();
        	}
        	return linesPerAuthorPerDate.toString();
        }
        
        @Override
        public String getResultAsHtmlDiv() {
        	StringBuilder html = new StringBuilder();
        	String s = "";
        	//I create this variable to change the output easily
        	// the variable s is filled according to the way of sorting
        	
        	// check if the user wants the number of the lines added/deleted or the number of commits
        	if(lines) {
        		s += "<div>Number of lines added of commits per " + howToSort + " and per author : <ul><br>";
        	} else {
        		s += "<div>Number of lines deleted per " + howToSort + " and per author : <ul><br>";
        	}
        	
        	// display the commits (or lines added/deleted) by the way of sorting
        	if(howToSort.equals("days")) {
        		for(var item : linesPerAuthorPerDate.entrySet()) {
        			s += "<ul>" + item.getKey().getDayOfMonth() + " " + item.getKey().getMonth().name() +  " " + item.getKey().getYear() + "<br>";
        			Map<String, Integer> lines = item.getValue();
            		for(var l : lines.entrySet()) {
            			s += "<li>" + l.getKey() + " : " + l.getValue() + "</li><br>";
            		}
            		s+= "</ul><br>";
        		}
        	} else if(howToSort.equals("weeks")) {
        		for(var item : linesPerAuthorPerWeeks.entrySet()) {
        			s += "<ul>Week " + item.getKey().substring(item.getKey().length()-2, item.getKey().length()) + " (" + item.getKey().substring(0,4) + ")<br>";
        			Map<String, Integer> lines = item.getValue();
            		for(var l : lines.entrySet()) {
            			s += "<li>" + l.getKey() + " : " + l.getValue() + "</li><br>";
            		}
            		s+= "</ul><br>";
        		}
        	} else {
        		for(var item : linesPerAuthorPerDate.entrySet()) {
        			s += "<ul>" + item.getKey().getMonth().name() + " " + item.getKey().getYear() + "<br>";
        			Map<String, Integer> lines = item.getValue();
            		for(var l : lines.entrySet()) {
            			s += "<li>" + l.getKey() + " : " + l.getValue() + "</li><br>";
            		}
            		s+= "</ul><br>";
        		}
        	}
        	s += "</ul></div>";
        	html.append(s);
            return html.toString();
        }
        
        @Override
        public void getResultAsHtmlDiv(WebGen wg) {
        	ArrayList<String> labels = new ArrayList<String>();
        	HashMap<String, ArrayList<Integer>> datasets = new HashMap<String, ArrayList<Integer>>();
        	int nbr = 0;
        	
            if(howToSort.equals("days") || howToSort.equals("months")) {
            	var labels0 = linesPerAuthorPerDate.entrySet().iterator().next().getKey();
        		LocalDate cmp = labels0;
        		LocalDate expected = cmp;
        		for(var item : linesPerAuthorPerDate.entrySet()) {
        			cmp = item.getKey();
        			if(!cmp.equals(expected)) {
        				while(!expected.equals(cmp)) {
        					if(howToSort.equals("days")) {
        						labels.add(expected.getDayOfMonth() + " " + expected.getMonth().name() + " " + expected.getYear());
        						expected = expected.plusDays(1);
        					} else {
        						labels.add(expected.getMonth().name() + " " + expected.getYear());
        						expected = expected.plusMonths(1);
        					}
            				nbr++;
        				}
        			}
        			if(howToSort.equals("days")) {
        				labels.add(item.getKey().getDayOfMonth() + " " + item.getKey().getMonth().name() +  " " + item.getKey().getYear());
        				expected = expected.plusDays(1);
					} else {
						labels.add(item.getKey().getMonth().name() + " " + item.getKey().getYear());
						expected = expected.plusMonths(1);
					}
            		Map<String, Integer> lines = item.getValue();
            		for(var l : lines.entrySet()) {
            			String author = l.getKey();
            			var authorAndInteger = datasets.getOrDefault(author, new ArrayList<Integer>());
            			while(authorAndInteger.size() != nbr) {
            				authorAndInteger.add(0);
            			}
            			authorAndInteger.add(l.getValue());
            			datasets.put(author, authorAndInteger);
            		}
            		nbr++;
        		}
            } else {
            	var labels0 = linesPerAuthorPerWeeks.entrySet().iterator().next().getKey();
            	int cmp = 0;
       			try {
       				cmp = Integer.parseInt(labels0.substring(labels0.length()-2, labels0.length()));  
       			}
    			catch(NumberFormatException e){
    				cmp = Integer.parseInt(labels0.substring(labels0.length()-1, labels0.length()));
    			}            
       			int expected = cmp;
        		int lastCurrentYear = Integer.parseInt(linesPerAuthorPerWeeks.entrySet().iterator().next().getKey().substring(0,4));
        		for(var item : linesPerAuthorPerWeeks.entrySet()) {
           			try {
           				cmp = Integer.parseInt(item.getKey().substring(item.getKey().length()-2, item.getKey().length()));  
           			}
        			catch(NumberFormatException e){
        				cmp = Integer.parseInt(item.getKey().substring(item.getKey().length()-1, item.getKey().length()));
        			}  
           			if(cmp != expected) {
        				while(expected != cmp) {
        					labels.add("Week " + expected + " (" + lastCurrentYear + ")");
            				expected ++;
            				if(expected == 53) {
            					expected = 0;
            					lastCurrentYear++;
            				}            			
            				nbr++;
        				}
        			}
        			labels.add("Week " + item.getKey().substring(item.getKey().length()-2, item.getKey().length()) + " (" + lastCurrentYear + ")");
    				Map<String, Integer> lines = item.getValue();
            		for(var l : lines.entrySet()) {
            			String author = l.getKey();
            			var authorAndInteger = datasets.getOrDefault(author, new ArrayList<Integer>());
            			while(authorAndInteger.size() != nbr) {
            				authorAndInteger.add(0);
            			}
            			authorAndInteger.add(l.getValue());
            			datasets.put(author, authorAndInteger);
            		}
            		nbr++;
    				expected ++;
    				if(expected == 53) {
    					expected = 0;
    					lastCurrentYear++;
    				}
    			}
            }
            
            wg.addChart("Number of lines " +(lines ? "added" : "deleted") + " per author and per " + howToSort, labels, datasets);
        }
        
    }
}


