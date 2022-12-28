package up.visulog.analyzer;

import up.visulog.analyzer.CountCommitsPerAuthorPlugin.Result;
import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.webgen.WebGen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * This class represents the plugin that counts the contribution of each member of the project
 * Each line "belongs" to the last person who has modified this line
 */

public class CountContributionPlugin implements AnalyzerPlugin{
	/**
	 * General configuration of the plugin
	 */
	private final Configuration configuration;
	
	
	/**
	 * This is where the result is stored
	 */
	private Result result;
	
	/**
	 * Constructor
	 * @param generalConfiguration
	 */
	public CountContributionPlugin(Configuration generalConfiguration) {
	    this.configuration = generalConfiguration;
	}
	
	/**
	 * Returns a HashMap that links the email with the name of the member
	 * @return a HashMap that links the email with the name of the member
	 */
	public HashMap<String, String> getName() {
		List<Commit> commits = Commit.parseLogFromCommand(configuration.getGitPath(), false);
		HashMap<String, String>emailToName = new HashMap<String, String>();
		
		for(var commit : commits) {
			String[]author = commit.author.split(" ");
			String email = author[author.length - 1];
			if(emailToName.get(email) == null) {
				emailToName.put(email, commit.author);
			}
		}
		
		return emailToName;
	}
	
	/**
	 * Calculate the percentage of contribution for each member and return the result of the plugin
	 * @param LinesPerEmail a HashMap that links for each member's email the number of lines that belong to him
	 * @param emailToName a HashMap that links for each members, his email with his name
	 * @return the result of the plugin
	 */
	public Result processLog(HashMap<String, Integer>LinesPerEmail, HashMap<String, String>emailToName) {
		var result = new Result();
	    double tot = 0;
	    
	    //Calculate the total number of lines assigned for each members
	    for(var ass : emailToName.entrySet()) {
	    	double lines;
	    	Integer l = LinesPerEmail.get(ass.getKey());
	    	if(l == null) lines = 0;
	    	else lines = l;
	    	result.contributionPerAuthor.put(ass.getValue(), lines);
	    	tot += lines;
	    }
	    
	    //Calculate the percentage for each
	    for(var ass : result.contributionPerAuthor.entrySet()) {
	    	double percent = (ass.getValue() / tot) * 100;
	    	ass.setValue(percent);
	    }
	    
	    return result;
	}
	
	@Override
	public void run() {
	    result = processLog(Commit.countLinesContribution(configuration.getGitPath()), getName());
	}
	
	@Override
	public Result getResult() {
	    if (result == null) run();
	    return result;
	}
	
	/**
	 * This class store the result of the plugin
	 *
	 */
	static class Result implements AnalyzerPlugin.Result {
		/**
		 * This is a HashMap which links the author's name with his contribution (in percentage)
		 */
	    private final Map<String,Double> contributionPerAuthor = new HashMap<>();
	
	    /**
	     * Return the HashMap which links the author's name with his contribution (in percentage)
	     * @return the HashMap which links the author's name with his contribution (in percentage)
	     */
	    Map<String, Double> getContributionPerAuthor() {
	        return contributionPerAuthor;
	    }
	
	    @Override
	    public String getResultAsString() {
	        return contributionPerAuthor.toString();
	    }
	
	    @Override
	    public String getResultAsHtmlDiv() {
	        StringBuilder html = new StringBuilder("<div>Contribution per author: <ul>");
	        for (var item : contributionPerAuthor.entrySet()) {
	        	String percent;
	        	Double value = item.getValue();
	        	if(value == 0.0)
	        		percent = String.valueOf(value).substring(0,3);
	        	else if(value < 10)
	        		percent = String.valueOf(value).substring(0,4);
	        	else 
	        		percent = String.valueOf(value).substring(0,5);
	        	percent += " %";
	        	
	            html.append("<li>").append(item.getKey()).append(": ").append(percent).append("</li>");
	        }
	        html.append("</ul></div>");
	        return html.toString();
	    }
	    
	    @Override
	    public void getResultAsHtmlDiv(WebGen wg) {
			ArrayList<String> authorOfCommit = new ArrayList<String>();
			ArrayList<Double> percentageOfContribution = new ArrayList<Double>();
			for(var data : getContributionPerAuthor().entrySet()){
				String[] nameTab = data.getKey().split(" ");
	        	String name = "";
	        	for(int i=0; i<nameTab.length-1; i++) {
	        		name += nameTab[i] + " ";
	        	}
				authorOfCommit.add(name);
				percentageOfContribution.add(data.getValue());
			}
			wg.addChartDouble("pie", "Contribution", "Contribution", authorOfCommit, percentageOfContribution);
	    }
	}
	    
}
