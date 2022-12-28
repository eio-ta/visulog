package up.visulog.gitrawdata;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * This class represent one comment in an issue
 */
public class Comment {
    /**
     * <b>id</b> the id of the comment
     */
    private final String id;
    /**
     * <b>author</b> the author of the comment
     */
    private final Author author;
    /**
     * <b>body</b> the body of the comment
     */
    private final String body;
    /**
     * <b>date</b> the date of the comment
     */
    private final Date date;

    /**
     * <b>Comment</b> constructor
     * @param id the id of the comment
     * @param author the author of the comment
     * @param body the body of the comment
     * @param date the date of the comment
     */
    public Comment(String id, Author author, String body, Date date){
        this.id = id;
        this.author = author;
        this.body = body;
        this.date = date;
    }

    /**
     * <b>author</b> getter
     * @return the author
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * <b>date</b> getter
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * <b>body</b> getter
     * @return the body of the comment
     */
    public String getBody() {
        return body;
    }

    /**
     * <b>id</b> getter
     * @return the id of the comment
     */
    public String getId() {
        return id;
    }


    /**
     * Change the <b>Comment</b> to a JSON formatted String
     * @return a string that contains the <b>Comment</b> with its parameters
     */
    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", author=" + author.toString() +
                ", body='" + body + '\'' +
                ", date=" + date +
                '}';
    }
}
