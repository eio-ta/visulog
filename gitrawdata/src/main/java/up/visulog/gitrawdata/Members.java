package up.visulog.gitrawdata;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * This class represents all the members of a given project
 */
public class Members {
    /**
     * <b>members</b> a <b>Collection</b> of all the members
     */
    private Collection<Member> members;

    /**
     * <b>Members</b> Constructor
     */
    public Members(){
        this.members = null;
    }

    /**
     * <b>members</b> Getter
     * @return a <b>Collection</b> of all the members
     */
    public Collection<Member> getMembers() {
        return members;
    }

    /**
     * This function changes from the JSON file created in <b>APIreponse</b> to a Java Object which here is <b>Members</b>
     * @param privateToken is a String that describes your private token to auth in your project
     * @param idProject is an int that describes the id of your project
     * @return a <b>Collection</b> of all the members
     */
    public Collection<Member> getMembersFromJson(String privateToken,int idProject){
        //Creating results for members
        Collection<Member> result = new ArrayList<Member>();
        //APIresponse to get members
        APIresponse membersAPI = new APIresponse();
        //sending the request to the API
        membersAPI.createLogFileForMembers(privateToken,idProject);
        //Using GSON to parse the json file
        Gson logs = new Gson();
        try (Reader reader = new FileReader("../GitLog/resultsMembers.json")) {
            //Changing from JSON to Issues(Object java)
            Collection<Member> membersList = Arrays.asList(logs.fromJson(reader, Member[].class));
            //Results are the notes of the comment list
            result = membersList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
