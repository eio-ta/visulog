package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.*;
import up.visulog.webgen.WebGen;

import java.util.*;

/**
 * Counts the number of comments for each different member of a GitLab project.
 * Comments include the comments under the issues but also other traces of activity such as a change of label on an issue
 */
public class CountCommentsPerAuthorPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    /**
     * Constructor
     * @param generalConfiguration stores the id and private token of the GitLab project to analyze
     */
    public CountCommentsPerAuthorPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    /**
     * Goes through a collection of comments in order to count the number of comments for each author.
     * @param commentsLog a collection of comments
     * @return a Result object which contains a HashMap which links authors to the number of comments they have done
     */
    static Result processLog(Collection<Comments> commentsLog) {
        var result = new Result();
        for (var comments : commentsLog) {
            //Get all the comments
            for (var comment : comments.getNotes()) {
                //check every comments and process the log
                var nb = result.commentPerAuthor.getOrDefault(comment.getAuthor().getName(), 0);
                result.commentPerAuthor.put(comment.getAuthor().getName(), nb + 1);
            }
        }
        return result;
    }

    @Override
    public void run() {
        //Create an api to get Issues
        Issues apiIssues = new Issues();
        //Create an api to get Comments
        Comments apiComments = new Comments();
        //Get results of issues
        Collection<Issue> resIssues =  apiIssues.getIssues(configuration.getPrivateToken(),configuration.getIdProject());
        //Create a result list for all comments
        Collection<Comments> resultsAllComments = new ArrayList<>();
        for(var issue:resIssues){
            //Get comments of every Issue
            var idIssue = issue.getIid();
            var resultComment = apiComments.parseCommentsFromLog(configuration.getPrivateToken(),idIssue,configuration.getIdProject());
            //add Comments in the list
            resultsAllComments.addAll(resultComment);
        }
        //Process the comments
        result = processLog(resultsAllComments);
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    /**
     * Stores the number of comments for each author, and manages how this data is outputted.
     */
    static class Result implements AnalyzerPlugin.Result {
        /**
         * Links the authors to the number of comments they have done.
         */
        private final Map<String, Integer> commentPerAuthor = new HashMap<>();

        /**
         * Returns a Map with the authors in key and the number of comments in value
         * @return a Map with the authors in key and the number of comments in value
         */
        Map<String, Integer> getCommentsPerAuthor() {
            return commentPerAuthor;
        }

        @Override
        public String getResultAsString() {
            return commentPerAuthor.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Comments per author: <ul>");
            for (var item : commentPerAuthor.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
        
        @Override
        public void getResultAsHtmlDiv(WebGen wg) {
        	ArrayList<String> authorOfComment = new ArrayList<String>();
        	ArrayList<Integer> numberOfComments = new ArrayList<Integer>();
        	
        	for(var item : commentPerAuthor.entrySet()) {
                authorOfComment.add(item.getKey());
                numberOfComments.add(item.getValue());
        	}

            wg.addChart("bar", "Number of comments per member", "Number of comments", authorOfComment, numberOfComments);
        }
    }
}
