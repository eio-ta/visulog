package up.visulog.gitrawdata;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * This class represents the Assignees of all the issues
 */
public class Assignees {
    /**
     * <b>assignees</b> is a collection that contains multiple <b>Assignee</b>
     */
    private Collection<Assignee> assignees;

    /**
     * <b>Assignees</b> Constructor
     * It sets the Collection to null
     */
    public Assignees(){
        this.assignees = null;
    }

    /**
     * <b>Assignees</b> Constructor
     * @param assignees is a collection of <b>Assignee</b>
     */
    public Assignees(Collection<Assignee> assignees){
        this.assignees = assignees;
    }

    /**
     * <b>assignees</b> getter
     * @return a collection that contains multiple <b>Assignee</b>
     */
    public Collection<Assignee> getAssigneesList() {
        return assignees;
    }

    /**
     * This function changes from the JSON file created in <b>APIreponse</b> to a Java Object which here is <b>Assignees</b>
     * @param privateToken is a String that describes your private token to auth in your project
     * @param idProject is an int that describes the id of your project
     * @return a collection that contains multiple <b>Assignee</b>
     */
    public Collection<Assignees> parseAssigneeFromLog(String privateToken,int idProject){
        //Creating results for assignee
        Collection<Assignees> result = new ArrayList<>();
        //APIresponse to get the assignee from an issue
        APIresponse assigneesAPI = new APIresponse();
        //sending the request to the API
        assigneesAPI.createLogFileForIssues(privateToken,idProject);
        //Using GSON to parse the json file
        Gson logs = new Gson();
        try (Reader reader = new FileReader("../GitLog/resultsIssues.json")) {
            //Changing from JSON to Assignees(Object java)
            Collection<Assignees> assignees = Arrays.asList(logs.fromJson(reader, Assignees[].class));
            //Results are the assignees
            result = assignees;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Change the <b>Assignees</b> to a JSON formatted String
     * @return a string that contains the <b>Assignees</b> with their parameters
     */
    @Override
    public String toString() {
        return "Assignees{" +
                "assigneesList=" + assignees +
                '}';
    }
}
