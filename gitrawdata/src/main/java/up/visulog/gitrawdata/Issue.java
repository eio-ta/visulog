package up.visulog.gitrawdata;

/**
 * This class represents one issue
 */
public class Issue {
    /**
     * <b>iid</b> is the id of the issue
     */
    private int iid;
    /**
     * <b>author</b> is the author of the issue
     */
    private Author author;

    /**
     * <b>Issue</b> Constructor
     * @param iid the id of the issue
     * @param author its author
     */
    public Issue(int iid,Author author){
        this.iid = iid;
        this.author = author;
    }

    /**
     * <b>author</b> getter
     * @return the author of the issue
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * <b>iid</b> getter
     * @return the id of the issue
     */
    public int getIid() {
        return iid;
    }

    /**
     * Change the <b>Issue</b> to a JSON formatted String
     * @return a string that contains the <b>Issue</b> with its parameters
     */
    @Override
    public String toString() {
        return "Issue{" +
                "iid=" + iid +
                ", author=" + author +
                '}';
    }
}
