package up.visulog.gitrawdata;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * This class represents the issues in a project
 */
public class Issues {
    /**
     * <b>issues</b> is a collection containing all the issues in a given project
     */
    private Collection<Issue> issues;

    /**
     * <b>Issues</b> constructor
     */
    public Issues(){
        this.issues = null;
    }

    /**
     * <b>issues</b> getter
     * @return the collection that contains all the issues
     */
    public Collection<Issue> getIssues() {
        return issues;
    }

    /**
     * This function changes from the JSON file created in <b>APIreponse</b> to a Java Object which here is <b>Issues</b>
     * @param privateToken is a String that describes your private token to auth in your project
     * @param idProject is an int that describes the id of your project
     * @return a <b>Collection</b> containing multiple <b>Issue</b>
     */
    public Collection<Issue> getIssues(String privateToken,int idProject){
        //Creating results for Issue
        Collection<Issue> result = new ArrayList<Issue>();
        //APIresponse to get issues
        APIresponse comments = new APIresponse();
        //sending the request to the API
        comments.createLogFileForIssues(privateToken,idProject);
        //Using GSON to parse the json file
        Gson logs = new Gson();
        try (Reader reader = new FileReader("../GitLog/resultsIssues.json")) {
            //Changing from JSON to Issues(Object java)
            Collection<Issue> issuesList = Arrays.asList(logs.fromJson(reader, Issue[].class));
            //Results are the notes of the comment list
            result = issuesList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * This function changes from the JSON file created in <b>APIreponse</b> to a Java Object which here is <b>Issues</b>
     * @param idProject is an int that describes the id of your project
     * @return a collection of issues
     * @deprecated
     */
    @Deprecated
    public Collection<Issue> getIssues(int idProject){
        return getIssues("1m1pdKszBNnTtCHS9KtS",idProject);
    }
    /**
     * Change the <b>Issues</b> to a JSON formatted String
     * @return a string that contains the <b>Issues</b> with their parameters
     */
    @Override
    public String toString() {
        return "Issues{" +
                "issues=" + issues +
                '}';
    }
}
