package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.*;
import up.visulog.webgen.WebGen;

import java.util.*;

/**
 * This class represents the plugin that counts the number of assigned gitlab issue per member of the git project
 *
 */
public class CountIssuesPerMemberPlugin implements AnalyzerPlugin {
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
    public CountIssuesPerMemberPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    /**
     * Calculate the number of gitlab issues per member and return the result of the plugin
     * @param assigneesLog the collection of the issues assignees
     * @return the result of the plugin
     */
    static Result processLog(Collection<Assignees> assigneesLog) {
        var result = new Result();
        for (var assignees : assigneesLog) {
            //check every issue to get the assignee
                for(var assignee : assignees.getAssigneesList()){
                var nb = result.issuesPerMember.getOrDefault(assignee.getName(), 0);
                result.issuesPerMember.put(assignee.getName(), nb + 1);
            }
        }
        return result;
    }

    @Override
    public void run() {
        //Create an api to get Issues
        Assignees apiIssues = new Assignees();
        //Create an api to get Comments
        //Get results of issues
        Collection<Assignees> resAssignee =  apiIssues.parseAssigneeFromLog(configuration.getPrivateToken(),configuration.getIdProject());

        result = processLog(resAssignee);
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    /**
     * This class store the result of the plugin
     */
    static class Result implements AnalyzerPlugin.Result {
    	/**
    	 * This HashMap associate for each members of the project, the number of assigned gitlab issue
    	 */
        private final Map<String, Integer> issuesPerMember = new HashMap<>();

        /**
         * Returns the HashMap that associate for each members of the project, the number of assigned gitlab issue
         * @return the HashMap that associate for each members of the project, the number of assigned gitlab issue
         */
        Map<String, Integer> getIssuesPerMember() {
            return issuesPerMember;
        }

        @Override
        public String getResultAsString() {
            return issuesPerMember.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Issues per members: <ul>");
            for (var item : issuesPerMember.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
        
        @Override
        public void getResultAsHtmlDiv(WebGen wg) {
            ArrayList<String>  members = new ArrayList<String>();
            ArrayList<Integer> numberOfIssues = new ArrayList<Integer>();

            for(var item : issuesPerMember.entrySet()) {
                members.add(item.getKey());
                numberOfIssues.add(item.getValue());
            }
            wg.addChart("bar", "Number of issues per member", "Number of issues", members, numberOfIssues);
        }
    }
}
