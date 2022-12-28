package up.visulog.gitrawdata;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the comments in all the issues
 */
public class Comments {
    /**
     * <b>id</b> the id of the issue
     */
    private final String id;
    /**
     * <b>notes</b> a list that contains multiple comments
     */
    private final List<Comment> notes;

    /**
     * <b>Comments</b> constructor
     */
    public Comments(){
        this.id = null;
        this.notes = null;
    }

    /**
     * <b>Comments</b> constructor
     * @param notes a list that contains multiple comments
     * @param id the id of the issue
     */
    public Comments(List<Comment> notes,String id){
        this.notes = notes;
        this.id = id;
    }

    /**
     * <b>id</b> getter
     * @return the id of the comments
     */
    public String getId() {
        return id;
    }

    /**
     * <b>notes</b> getter
     * @return a list that contains multiple comments
     */
    public List<Comment> getNotes() {
        return notes;
    }

    /**
     *
     * This function changes from the JSON file created in <b>APIreponse</b> to a Java Object which here is <b>Comments</b>
     * @param privateToken is a String that describes your private token to auth in your project
     * @param id the id of the issue
     * @param idProject is an int that describes the id of your project
     * @return a collection that contains multiple <b>Comments</b>
     */
    public Collection<Comments> parseCommentsFromLog(String privateToken,int id,int idProject){
        //Creating results for Comments
        Collection<Comments> result = new ArrayList<Comments>();
        //APIresponse to get the comments from an issue
        APIresponse comments = new APIresponse();
        //sending the request to the API
        comments.createLogFileForOneIssue(privateToken,idProject,id);
        //Using GSON to parse the json file
        Gson logs = new Gson();
        try (Reader reader = new FileReader("../GitLog/resultsIssue"+String.valueOf(id)+".json")) {
            //Changing from JSON to Comments(Object java)
            Collection<Comments> commentsList = Arrays.asList(logs.fromJson(reader, Comments[].class));
            //Results are the notes of the comment list
            result = commentsList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * This function changes from the JSON file created in <b>APIreponse</b> to a Java Object which here is <b>Comments</b>
     * @param id the id of the issue
     * @param idProject is an int that describes the id of your project
     * @return a collection of comments
     * @deprecated
     */
    @Deprecated
    public Collection<Comments> parseCommentsFromLog(int id,int idProject){
        return parseCommentsFromLog("1m1pdKszBNnTtCHS9KtS",idProject,id);
    }

    /**
     * Change the <b>Comments</b> to a JSON formatted String
     * @return a string that contains the <b>Comments</b> with its parameters
     */
    @Override
    public String toString() {
        return "Comments{" +
                "notes=" + notes +
                '}';
    }
}
