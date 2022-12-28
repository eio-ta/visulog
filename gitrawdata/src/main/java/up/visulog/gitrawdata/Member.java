package up.visulog.gitrawdata;

/**
 * This class represents a member of the project
 */
public class Member {
    /**
     * <b>id</b> the id of the member
     */
    private int id;
    /**
     * <b>name</b> the name of the member
     */
    private String name;
    /**
     * <b>username</b> the username of the member
     */
    private String username;
    /**
     * <b>avatar_url</b> the url of the member's avatar
     */
    private String avatar_url;
    /**
     * <b>web_url</b> the url of the member's profile
     */
    private String web_url;

    /**
     * <b>Member</b> Constructor
     * @param id the id of the member
     * @param name the name of the member
     * @param username the username of the member
     * @param avatar_url the url of the member's avatar
     * @param web_url the url of the member's profile
     */
    public Member(int id,String name,String username,String avatar_url,String web_url){
        this.id = id;
        this.name = name;
        this.username = username;
        this.avatar_url = avatar_url;
        this.web_url = web_url;
    }

    /**
     * <b>username</b> Getter
     * @return the username of the member
     */
    public String getUsername() {
        return username;
    }

    /**
     * <b>name</b> Getter
     * @return the name of the member
     */
    public String getName() {
        return name;
    }

    /**
     * <b>id</b> Getter
     * @return the id of the member
     */
    public int getId() {
        return id;
    }

    /**
     * <b>avatar_url</b> Getter
     * @return the url of the member's avatar
     */
    public String getAvatar_url() {
        return avatar_url;
    }

    /**
     * <b>web_url</b> Getter
     * @return the url of the member's profile
     */
    public String getWeb_url() {
        return web_url;
    }
    /**
     * Change the <b>Member</b> to a JSON formatted String
     * @return a string that contains the <b>Member</b> with his/her parameters
     */
    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
