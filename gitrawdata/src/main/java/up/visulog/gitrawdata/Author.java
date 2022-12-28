package up.visulog.gitrawdata;

/**
 * This class represents an Author
 */
public class Author {
    /**
     * <b>id</b> is the id of the assignee
     */
    private final int id;
    /**
     * <b>name</b> is the name of the assignee
     */
    private final String name;
    /**
     * <b>username</b> is the username of the assignee
     */
    private final String username;
    /**
     * Construct the <b>Author</b>
     * @param id is the id of the assignee
     * @param name is the name of the assignee
     * @param username is the username of the assignee
     */
    public Author(int id,String name,String username){
        this.id = id;
        this.name = name;
        this.username = username;
    }

    /**
     * <b>id</b> getter
     * @return the id of the author
     */
    public int getId(){
        return this.id;
    }
    /**
     * <b>name</b> getter
     * @return the name of the assignee
     */
    public String getName(){
        return this.name;
    }
    /**
     * <b>username</b> getter
     * @return the username of the assignee
     */
    public String getUsername() {
        return username;
    }
    /**
     * Change the <b>Author</b> to a JSON formatted String
     * @return a string that contains the <b>Author</b> with his/her parameters
     */
    @Override
    public String toString() {
            return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
