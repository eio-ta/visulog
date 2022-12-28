package up.visulog.gitrawdata;

import java.time.LocalDateTime;

/**
 * This class represents a builder of the commits
 */
public class CommitBuilder {
	/**
	 * <b>id</b> the id of the commit
	 */
    private final String id;
	/**
	 * <b>author</b> the author of the commit
	 */
    private String author;
	/**
	 * <b>date</b> the date of the commit
	 */
    private LocalDateTime date;
	/**
	 * <b>description</b> the description of the commit
	 */
    private String description;
	/**
	 * <b>mergedFrom</b> where was the commit merged from
	 */
    private String mergedFrom;

	/**
	 * <b>CommitBuilder</b> Constructor
	 * @param id the id of the commit
	 */
    public CommitBuilder(String id) {
        this.id = id;
    }

	/**
	 * <b>author</b> setter
	 * @param author author of the commit
	 * @return <b>CommitBuilder</b> after setting a new <b>author</b>
	 */
    public CommitBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

	/**
	 * <b>date</b> Setter
	 * @param date date of the commit
	 * @return <b>CommitBuilder</b> after setting a new <b>date</b>
	 */
    public CommitBuilder setDate(String date) {
    	/* date is a String with expected value : "Mon Aug 31 11:28:28 2020 +0200"
    	 using java.time.LocalDateTime we can store a date with time in an object LocalDateTime*/
    	String[] dateList =date.split("\\s");
    	int day = Integer.parseInt(dateList[2]);
    	int month = stringToMonth(dateList[1]);
    	int year = Integer.parseInt(dateList[4]);
    	String[] time = dateList[3].split(":");
    	int hour = Integer.parseInt(time[0]);
    	int minute = Integer.parseInt(time[1]);
    	int seconds = Integer.parseInt(time[2]);
    	
    	LocalDateTime newDate = LocalDateTime.of(year, month, day, hour, minute, seconds);
    	
        this.date = newDate;
        return this;
    }

	/**
	 * Method that return the number of the month based on a string
	 * @param month the String of a month
	 * @return the number of the Month from 1 to 12
	 */
	int stringToMonth(String month){ //method that return the number of the month based on a string "Jan" -> January -> 1
		switch (month) {
			case "Jan":
				return 1;
			case "Feb":
				return 2;
			case "Mar":
				return 3;
			case "Apr":
				return 4;
			case "May":
				return 5;
			case "Jun":
				return 6;
			case "Jul":
				return 7;
			case "Aug":
				return 8;
			case "Sep":
				return 9;
			case "Oct":
				return 10;
			case "Nov":
				return 11;
			case "Dec":
				return 12;
			default :
				return 0;
		}
	}

	/**
	 * <b>description</b> Setter
	 * @param description description of the commit
	 * @return <b>CommitBuilder</b> after setting a new <b>description</b>
	 */
    public CommitBuilder setDescription(String description) {		
 

        this.description = description;
        return this;
    }

	/**
	 * <b>mergedFrom</b> Setter
	 * @param mergedFrom where was the commit merged from
	 * @return <b>CommitBuilder</b> after setting a new <b>mergedFrom</b>
	 */
    public CommitBuilder setMergedFrom(String mergedFrom) {
        this.mergedFrom = mergedFrom;
        return this;
    }

	/**
	 * Creation of <b>Commit</b>
	 * @return a new <b>Commit</b> created from the <b>CommitBuilder</b> parameters
	 */
    public Commit createCommit() {
        return new Commit(id, author, date, description, mergedFrom);
    }
}