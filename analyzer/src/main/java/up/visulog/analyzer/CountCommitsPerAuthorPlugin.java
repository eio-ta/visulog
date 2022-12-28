package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.webgen.WebGen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Counts the number of commits for each different author of the git project.
 */
public class CountCommitsPerAuthorPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;
    private boolean allBranches;

    /**
     * Constructor
     * @param generalConfiguration stores the path of the git project to analyze
     * @param allBranches if true, the result will be computed on all the branches of the git project, if false, just on the current branch
     */
    public CountCommitsPerAuthorPlugin(Configuration generalConfiguration, boolean allBranches) {
        this.configuration = generalConfiguration;
        this.allBranches = allBranches;
    }

    /**
     * Goes through a list of commits in order to count the number of commits for each author whose commits appear in the list.
     * @param gitLog a list of commits
     * @return a Result object which contains a HashMap which links authors to the number of commits they have done
     */
    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        Map<String,String>emailToName = new HashMap<String,String>();
        for (var commit : gitLog) {
        	String[] author = commit.author.split(" ");
        	String email = author[author.length - 1];
        	if(emailToName.get(email) == null) {
        		emailToName.put(email, commit.author);
        	}
            var nb = result.commitsPerAuthor.getOrDefault(email, 0);
            result.commitsPerAuthor.put(email, nb + 1);
        }
        for(var e : emailToName.entrySet()) {
        	int nbCommit = result.commitsPerAuthor.remove(e.getKey());
        	result.commitsPerAuthor.put(e.getValue(), nbCommit);
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
     * Stores the number of commits for each author, and manages how this data is outputted.
     */
    static class Result implements AnalyzerPlugin.Result {
        /**
         * Links the authors to the number of commits they have done.
         */
        private final Map<String, Integer> commitsPerAuthor = new HashMap<>();

        /**
         * Returns a Map with the authors in key and the number of commits in value
         * @return a Map with the authors in key and the number of commits in value
         */
        Map<String, Integer> getCommitsPerAuthor() {
            return commitsPerAuthor;
        }

        @Override
        public String getResultAsString() {
            return commitsPerAuthor.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Commits per author: <ul>");
            for (var item : commitsPerAuthor.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
        
        @Override
        public void getResultAsHtmlDiv(WebGen wg) {
            ArrayList<String> authorOfCommits = new ArrayList<String>();
            ArrayList<Integer> numberOfCommits= new ArrayList<Integer>();
            for(var data : getCommitsPerAuthor().entrySet()){
            	String[] nameTab = data.getKey().split(" ");
            	String name = "";
            	for(int i=0; i<nameTab.length-1; i++) {
            		name += nameTab[i] + " ";
            	}
                authorOfCommits.add(name);
                numberOfCommits.add(data.getValue());    
            }

            wg.addChart("bar", "Number of commits per member", "Number of commits", authorOfCommits, numberOfCommits);
        }
    }
}
