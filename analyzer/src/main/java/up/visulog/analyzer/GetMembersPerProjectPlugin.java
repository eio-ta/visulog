package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.*;
import up.visulog.webgen.WebGen;

import java.util.*;

/**
 * Gets a list of all the members of a GitLab project.
 * More precisely, this plugin gets for each member of the project its avatar, full name, username and a link to its GitLab profile.
 */
public class GetMembersPerProjectPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    /**
     * Constructor
     * @param generalConfiguration stores the id and private token of the GitLab project to analyze
     */
    public GetMembersPerProjectPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    /**
     * Generates a result object in order to store the list of members.
     * @param extensionsLog a Collection of Member objects
     * @return a Result object which contains a Collection of Member objects
     */
    static Result processLog(Collection<Member> membersLog) {
        var result = new Result();
        result.membersList = membersLog;
        return result;
    }

    @Override
    public void run() {
        //Create an api to get Members
        Members apiMembers = new Members();
        //Get results of members
        Collection<Member> resMembers =  apiMembers.getMembersFromJson(configuration.getPrivateToken(),configuration.getIdProject());
        //Process the members
        result = processLog(resMembers);
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    /**
     * Stores the list of members, and manages how this data is outputted.
     */
    static class Result implements AnalyzerPlugin.Result {
        private Collection<Member> membersList = new ArrayList<>();

        /**
         * Return the list of the members
         * @return the list of the members
         */
        Collection<Member> getMembers() {
            return membersList;
        }

        @Override
        public String getResultAsString() {
            return membersList.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Members:");
            html.append("<table class=\"members\"");
            html.append("<tr><th>Avatar</th><th>Full Name</th><th>Username</th></tr>");
            for (var item : membersList) {
                html.append("<tr><td><img src=\""+item.getAvatar_url()+"\"></td><td>"+item.getUsername()+"</td><td><a href=\""+item.getWeb_url()+"\">"+item.getUsername()+"</a></tr>");
            }
            html.append("</table>");
            return html.toString();
        }
        
        @Override
        public void getResultAsHtmlDiv(WebGen wg) {
            wg.addListMembers(membersList, "List of members");
        }
        
    }
}
