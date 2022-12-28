package up.visulog.analyzer;

import up.visulog.analyzer.CountCommitsPerAuthorPlugin.Result;
import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.webgen.WebGen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Counts the number of lines added or deleted for each different author of the git project.
 */
public class CountLinesPerAuthorPlugin implements AnalyzerPlugin{
	 private final Configuration configuration;
	    private Result result;
	    
	    // true means sort lines added, false means sort lines deleted
	    private boolean sortLineAdded;
	    private boolean allBranches;

		/**
		 * Constructor
		 * @param generalConfiguration stores the path of the git project to analyze
		 * @param sortLineAdded if true, the plugin will count the lines added, if false, it will count the lines deleted
		 * @param allBranches if true, the result will be computed on all the branches of the git project, if false, just on the current branch
		 */
	    public CountLinesPerAuthorPlugin(Configuration generalConfiguration, boolean sortLineAdded, boolean allBranches) {
	        this.configuration = generalConfiguration;
	        this.sortLineAdded = sortLineAdded;
	        this.allBranches = allBranches;
	    }

		/**
		 * Goes through a list of commits in order to count the number of the lines added or deleted per author.
		 * @param gitLog a list of commits
		 * @return a Result object which contains a HashMap which links authors to the number of lines they have added/deleted in total
		 */
	    public Result processLog(List<Commit> gitLog) {
	        var result = new Result();
	        Map<String,String>emailToName = new HashMap<String,String>();
	        for (var commit : gitLog) {
	        	// I decide to not count the line added/deleted from the merged commit
	        	// because the person who merges a branch, collects all the lines added and deleted 
	        	if(commit.mergedFrom == null) {
	        		String[] author = commit.author.split(" ");
	            	String email = author[author.length - 1];
	            	if(emailToName.get(email) == null) {
	            		emailToName.put(email, commit.author);
	            	}
	        		var oldNbLines = result.linesPerAuthor.getOrDefault(email, 0);
		        	Integer nbLines;
		        	if(sortLineAdded) nbLines = oldNbLines + commit.linesAdded;
		        	else nbLines = oldNbLines + commit.linesDeleted;
		        	result.linesPerAuthor.put(email, nbLines);
	        	}
	        	
	        }
	        for(var e : emailToName.entrySet()) {
             	int nbCommit = result.linesPerAuthor.remove(e.getKey());
             	result.linesPerAuthor.put(e.getValue(), nbCommit);
             }
	        return result;
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
		 * Stores the number of lines added or deleted for each author, and manages how this data is outputted.
		 */
	    public class Result implements AnalyzerPlugin.Result {
			/**
			 * Links the authors to the number of lines they have added or deleted.
			 */
			private final Map<String, Integer> linesPerAuthor = new HashMap<>();

	        @Override
	        public String getResultAsString() {
	            return linesPerAuthor.toString();
	        }

	        @Override
	        public String getResultAsHtmlDiv() {
	            StringBuilder html = new StringBuilder("<div>Commits per author: <ul>");
	            for (var item : linesPerAuthor.entrySet()) {
	                html.append("<li>"  + (sortLineAdded ? "Lines Added by " : "Lines Deleted by ")).append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
	            }
	            html.append("</ul></div>");
	            return html.toString();
	        }

	        @Override
	        public void getResultAsHtmlDiv(WebGen wg) {
				ArrayList<String> labels = new ArrayList<String>();
				ArrayList<Integer> data = new ArrayList<Integer>();
				for (var item : linesPerAuthor.entrySet()) {
					String[] nameTab = item.getKey().split(" ");
	            	String name = "";
	            	for(int i=0; i<nameTab.length-1; i++) {
	            		name += nameTab[i] + " ";
	            	}
					labels.add(name);
					data.add(item.getValue());
				}
				wg.addChart("bar", "Number of lines "+(sortLineAdded ? "added" : "deleted")+" per member", sortLineAdded ? "Lines Added" : "Lines Deleted", labels, data);
	        }
	    }
}
