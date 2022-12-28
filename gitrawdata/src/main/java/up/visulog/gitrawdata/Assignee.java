package up.visulog.gitrawdata;

/**
 * This class represents the assignee of an issue
 */
public class Assignee {
    /**
     * <b>id</b> is the id of the assignee
     */
    private int id;
    /**
     * <b>name</b> is the name of the assignee
     */
    private String name;
    /**
     * <b>username</b> is the username of the assignee
     */
    private String username;

    /**
     * Construct the Assignee
     * @param id is the id of the assignee
     * @param name is the name of the assignee
     * @param username is the username of the assignee
     */
    public Assignee(int id,String name,String username){
        this.id = id;
        this.name = name;
        this.username = username;
    }

    /**
     * The <b>id</b> getter
     * @return the id of the assignee
     */
    public int getId() {
        return id;
    }

    /**
     * <b>name</b> getter
     * @return the name of the assignee
     */
    public String getName() {
        return name;
    }

    /**
     * <b>username</b> getter
     * @return the username of the assignee
     */
    public String getUsername() {
        return username;
    }

    /**
     * Change the <b>Assignee</b> to a JSON formatted String
     * @return a string that contains the <b>Assignee</b> with his/her parameters
     */
    @Override
    public String toString() {
        return "Assignee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
