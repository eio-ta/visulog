package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.webgen.WebGen;

import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.time.Month;

/**
 * This class represents a plugin : CountLinesPerDatePlugin.
 * It counts <b>the lines added or deleted per Date</b>.
 * The user can choose if he wants the <b>added</b> or <b>deleted</b> lines, if he wants to sort them by <b>Days</b>, <b>Weeks</b> or <b>Months</b>.
 * The plugin can also be used on all branches, but only on the current branch.
 * <p> If the user want to have the lines <b>added</b> or <b>deleted</b> by Author and per Date, he can use the CountLinesPerAuthorPerDate plugin.</p>
 * <p> If The user want to have the lines <b>added</b> or <b>deleted</b> by Author without the date, he can use the CountLinesPerAuthor plugin.</p>
 * @see CountLinesPerAuthorPerDatePlugin
 * @see CountLinesPerAuthorPlugin
 */
public class CountLinesPerDatePlugin implements AnalyzerPlugin {
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
     * Constructs a CountLinesPerDate object given the general Configuration, the way of sorting, the lines added or deleted and a boolean that describes if the plugin should be used on all branches or not
     * @param generalConfiguration the generalConfiguration
     * @param howToSort the way of sorting
     * @param lines the lines added or deleted
     * @param allBranches for all branches or not
     */
    public CountLinesPerDatePlugin(Configuration generalConfiguration, String howToSort, boolean lines, boolean allBranches) {
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
    	var result = new Result();
    	// change the values of the object Result
    	countLinesAddedOrDeletedPerDays(gitLog, result);
    	
    	// sort the commits per Months and per Weeks
    	if(howToSort.equals("months")) {
    		Map<LocalDate, Integer> res = new TreeMap<>();
        	for (var date : result.commitsPerDate.entrySet()) {
        		// change the key of the number of the commits
            	// for example, the key of the commits that were made in January 2020 will be January 1, 2020
        		LocalDate m = LocalDate.of(date.getKey().getYear(), date.getKey().getMonth(), 1);
            	var nb = res.getOrDefault(m, 0);
            	res.put(m, nb + date.getValue());            
            }
        	// change the key and the value of result.commitsPerDate
        	result.commitsPerDate.clear();
        	result.commitsPerDate.putAll(res);
    	}
    	
    	// sort the commits per Months and per Weeks
    	else if(howToSort.equals("weeks")) {
    		Map<String, Integer> res = new TreeMap<>();
        	for (var date : result.commitsPerDate.entrySet()) {
        		// change the key of the number of the commits in String
            	// for example, the key of the commits that were made in January 2020 will be "2020 Week 1"
            	String m = Integer.toString(date.getKey().getYear()) + " " + Integer.toString(date.getKey().getDayOfYear()/7);
            	var nb = res.getOrDefault(m, 0);
            	res.put(m, nb + date.getValue());            
            }
         // change the key and the value of result.commitsPerWeeks
            result.commitsPerWeeks.putAll(res);
    	}
    	return result;
    }
    
    /**
     * Sort the list of lines added or deleted per days
     * @param gitLog a list of the commits
     * @param result the result
     */
    public void countLinesAddedOrDeletedPerDays(List<Commit> gitLog, Result result) {
    	// browse the list of commits and count them according to the date of the commit
    	for (var commit : gitLog) {
    		//The plugin don't count the line added/deleted from the merged commit
        	//because the lines will added and deleted twice
    		if(commit.mergedFrom == null) {
	        	LocalDate m = commit.date.toLocalDate();
	        	var oldNbLines = result.commitsPerDate.getOrDefault(m, 0);
	        	int nbLines = 0;
	        	if(lines) {
	        		nbLines = oldNbLines + commit.linesAdded;
	        	} else {
	        		nbLines = oldNbLines + commit.linesDeleted;
	        	}
	        	// enter the values in the map
	        	result.commitsPerDate.put(m, nbLines);
    		}
        }
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
        private Map<LocalDate, Integer> commitsPerDate = new TreeMap<>();
        
        /**
         * Result of the plugin per weeks
         */
        private Map<String, Integer> commitsPerWeeks = new TreeMap<>();
        
        // I chose a TreeMap<>() because the objects are sorted with the keys naturally
        // String = a -> z
        // int = 0,1,2...
        
        /**
         * Return the result of the plugin
         * @param lines
         */
        Map<LocalDate, Integer> getCommitsPerDate() {
            return commitsPerDate;
        }
        
        @Override
        public String getResultAsString() {
        	if(commitsPerWeeks.size() != 0) {
        		return commitsPerWeeks.toString();
        	}
            return commitsPerDate.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
        	StringBuilder html = new StringBuilder();
        	String s = "";
        	// the variable s is filled according to the way of sorting
        	
        	//check if the user wants the number of the lines added/deleted or the number of commits
        	if(lines) {
        		s += "<div>Number of lines added of commits per " + howToSort + ": <ul>";
        	} else {
        		s += "<div>Number of lines deleted per " + howToSort + ": <ul>";
        	}
        	
        	//display the commits (or lines added/deleted) by the way of sorting
        	if(howToSort.equals("days")) {
        		for(var item : commitsPerDate.entrySet()) {
        			s += "<li>" + item.getKey().getDayOfMonth() + " " + item.getKey().getMonth().name() +  " " + item.getKey().getYear() + ": " + item.getValue() + "</li>";
        		}
        	} else if(howToSort.equals("weeks")) {
        		for(var item : commitsPerWeeks.entrySet()) {
        			// Example of a item || Key : "2020 1" | Value : 30
        			s += "<li> Week " + item.getKey().substring(item.getKey().length()-2, item.getKey().length()) + " (" + item.getKey().substring(0,4) + ") : " + item.getValue() + "</li>";
        		}
        	} else {
        		for(var item : commitsPerDate.entrySet()) {
        			s += "<li>"  + item.getKey().getMonth().name() + " (" + item.getKey().getYear() + ") : " + item.getValue() + "</li>";
        		}
        	}
        	s += "</ul></div>";
        	html.append(s);
            return html.toString();
        }
        
        @Override
        public void getResultAsHtmlDiv(WebGen wg) {
            ArrayList<String> labels = new ArrayList<String>();
            ArrayList<Integer> data = new ArrayList<Integer>();

            if(howToSort.equals("days") || howToSort.contentEquals("months")) {
            	var labels0 = commitsPerDate.entrySet().iterator().next().getKey();
        		LocalDate cmp = labels0;
        		LocalDate expected = cmp;
        		for(var item : commitsPerDate.entrySet()) {
        			cmp = item.getKey();
        			if(!cmp.equals(expected)) {
        				while(!expected.equals(cmp)) {
        					if(howToSort.equals("days")) {
        						labels.add(expected.getDayOfMonth() + " " + expected.getMonth().name() + " " + expected.getYear());
        						expected = expected.plusDays(1);
        					} else {
        						labels.add(item.getKey().getMonth().name() + " " + item.getKey().getYear());
                				expected = expected.plusMonths(1);
        					}
        					data.add(0);
        				}
        			}
        			if(howToSort.equals("days")) {
        				labels.add(item.getKey().getDayOfMonth() + " " + item.getKey().getMonth().name() + " " + item.getKey().getYear());
        				expected = expected.plusDays(1);
					} else {
						labels.add(item.getKey().getMonth().name() + " " + item.getKey().getYear());
        				expected = expected.plusMonths(1);
					}
                    data.add(item.getValue());
        		}
        	} else {
        		var labels0 = commitsPerWeeks.entrySet().iterator().next().getKey();
        		System.out.println(commitsPerWeeks);
        		System.out.println(labels0);

        		int cmp = 0;
       			try {
       				cmp = Integer.parseInt(labels0.substring(labels0.length()-2, labels0.length()));  
       			}
    			catch(NumberFormatException e){
    				cmp = Integer.parseInt(labels0.substring(labels0.length()-1, labels0.length()));
    			}      
       			int expected = cmp;
        		int lastCurrentYear = Integer.parseInt(commitsPerWeeks.entrySet().iterator().next().getKey().substring(0,4));
        		for(var item : commitsPerWeeks.entrySet()) {
           			try {
           				cmp = Integer.parseInt(item.getKey().substring(item.getKey().length()-2, item.getKey().length()));  
           			}
        			catch(NumberFormatException e){
        				cmp = Integer.parseInt(item.getKey().substring(item.getKey().length()-1, item.getKey().length()));
        			}                 	
           			System.out.print(cmp);
           			System.out.println(expected);

           			if(cmp != expected) {
        				while(expected != cmp) {
        					labels.add("Week " + expected + " (" + lastCurrentYear + ")");
            				data.add(0);
            				expected ++;
            				if(expected == 53) {
            					expected = 0;
            					lastCurrentYear++;
            				}
        				}
        			}
        			labels.add("Week " + item.getKey().substring(item.getKey().length()-2, item.getKey().length()) + " (" + lastCurrentYear + ")");
        			data.add(item.getValue());
    				expected ++;
    				if(expected == 53) {
    					expected = 0;
    					lastCurrentYear++;
    				}        		
    			}
        	}
            
            wg.addChart("line", "Number of Lines " + (lines ? "Added" : "Deleted") + " Per " + howToSort, "Lines " + (lines ? "added" : "deleted"), labels, data);
        }

    }
}

