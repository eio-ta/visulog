package up.visulog.gitrawdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * This class represents one Commit
 */
public class Commit {
    /**
     * <b>id</b> the id of the commit
     */
    public final String id;
    /**
     * <b>date</b> the date of the commit
     */
    public final LocalDateTime date;
    /**
     * <b>author</b> the author of the commit
     */
    public final String author;
    /**
     * <b>description</b> the description of the commit
     */
    public final String description;
    /**
     * <b>mergedFrom</b> where was the commit merged from
     */
    public final String mergedFrom;
    /**
     * <b>linesAdded</b> number of lines added in the commit
     */
    public int linesAdded = 0;
    /**
     * <b>linesDeleted</b> number of lines deleted in the commit
     */
    public int linesDeleted = 0;

    /**
     * <b>Commit</b> Constructor
     * @param id the id of the commit
     * @param author the author of the commit
     * @param date the date of the commit
     * @param description the description of the commit
     * @param mergedFrom where was the commit merged from
     */
    public Commit(String id, String author, LocalDateTime date, String description, String mergedFrom) {
        this.id = id;
        this.author = author;
        this.date = date;
        this.description = description;
        this.mergedFrom = mergedFrom;
    }

    /**
     * This function counts the number of lines per Author
     * @param path the path of the git repository in the local machine
     * @return a HashMap that contains the <b>author</b> and the number of lines changed by him/her
     */
    public static HashMap<String, Integer> countLinesContribution(Path path) {
    	HashMap<String, Integer> tot = new HashMap<String, Integer>();
    	BufferedReader b = command(path, "git", "ls-files", "--exclude-standard");
    	String line;
    	try {
			while((line = b.readLine()) != null) {
				BufferedReader reader = command(path, "git", "blame", "-e", line);
				var result = parseLinesContribution(reader);
				for(var ass : result.entrySet()) {
					String author = ass.getKey();
					Integer nb = ass.getValue();
					Integer oldNb = tot.getOrDefault(author, 0);
					tot.put(author, nb + oldNb);
				}
			}
    		
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return tot;
    }

    /**
     * This function parses the results of the command git blame
     * @param b a <b>BufferedReader</b> representing the results from the git command
     * @return a <b>HashMap</b> containing the email and the number of times seen
     */
    public static HashMap<String, Integer> parseLinesContribution(BufferedReader b) {
    	HashMap<String, Integer> hm = new HashMap<String, Integer>();
    	String line;
    	try {
			while((line = b.readLine()) != null) {
				Scanner sc = new Scanner(line);
				if(sc.hasNext()) {
					sc.next();
					if(sc.hasNext()) {
						String email = sc.next();
						if(!(email.startsWith("(<") && email.endsWith(">"))) return hm;
						email = email.substring(1);
						
						Integer nb = hm.getOrDefault(email, 0);
						hm.put(email, nb + 1);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return hm;
    }

    /**
     * this function execute the command args from directory whose path is "path"
     * @param path the path of the git repository in the local machine
     * @param args the command that are going to be executed
     * @return a <b>BufferedReader</b> representing the results from the git command
     */
    public static BufferedReader command(Path path, String... args) {
    	ProcessBuilder builder = new ProcessBuilder();
    	builder.directory(path.toFile());
    	builder.command(args);
    	
    	Process process;
    	try {
    		process = builder.start();
    	}catch(IOException e) {
    		String arg = "";
    		for(int i = 0; i < args.length; i++) {
    			arg += args[i] + " ";
    		}
    		throw new RuntimeException("Error running \"" + arg + "\"", e);
    	}
    	InputStream is = process.getInputStream();
    	return new BufferedReader(new InputStreamReader(is));
    }

    /**
     * This function collects the number of line added and deleted for each commits
     * @param path the path of the git repository in the local machine
     * @param commits a list containing multiple <b>Commit</b>
     * @return the <b>List</b> of <b>Commit</b> after collecting the number of lines added and deleted
     */
    public static List<Commit> getNumberLines(Path path, List<Commit> commits) {
    	for(Commit commit : commits) {
    		int[] lines = parseLine(command(path, "git", "show", "--format=oneline",commit.id,"--numstat"));
    		commit.linesAdded = lines[0];
    		commit.linesDeleted = lines[1];
    	}
    	
    	//If you want to test the function, remove the '/* */'
    	//It shows the number of line added and deleted for each commits from the most recent to the oldest
    	//You can compare the results with gitlab
    	/*for(Commit commit : commits) {
    		System.out.println(commit.linesAdded + " added , " + commit.linesDeleted + " deleted");
    	}*/
    	
    	return commits;
    	
    }

    /**
     * This function parses the output of the command 'git show', to collect the number of line added and deleted
     * @param reader representing the results from the git command
     * @return an <b>int</b> array containing the number of lines added and deleted
     */
    public static int[] parseLine(BufferedReader reader) {
    	try {
    		String line;
    		reader.readLine();
    		int added = 0;
    		int deleted = 0;
    		while((line = reader.readLine()) != null) {
    			if(!line.equals("")) {
	    			Scanner sc = new Scanner(line);
					String s = sc.next();
					if(s.equals("-")) added += 0;
					else added += Integer.parseInt(s);
					s = sc.next();
					if(s.equals("-")) deleted += 0;
					else deleted += Integer.parseInt(s);  		
    			}
    		}
    		int[] res = {added, deleted};
    		return res;
    	}catch(IOException e) {
    		throw new RuntimeException("Error parseLine", e);
    	}
    }

    /**
     * This function collects the commits in the branches of the git repository
     * @param gitPath the path of the git repository in the local machine
     * @param allBranches it's a boolean to check for all branches or just a specific branch
     * @return <b>List</b> of <b>Commit</b> repesenting every commit in a specific branch or all the branches
     */
    public static List<Commit> parseLogFromCommand(Path gitPath, boolean allBranches) {
    	BufferedReader reader;
    	if(allBranches) {
    		reader = command(gitPath, "git", "log", "--all");
    	} else {
    		reader = command(gitPath, "git", "log");
    	}
        return getNumberLines(gitPath,parseLog(reader));
    }

    /**
     * This function parses the output of the command 'git log' and collect the commits in the branches
     * @param reader representing the results from the git command
     * @return a <b>List</b> of <b>Commit</b> repesenting every commit in a specific branch or all the branches
     */
    public static List<Commit> parseLog(BufferedReader reader) {
        var result = new ArrayList<Commit>();
        Optional<Commit> commit = parseCommit(reader);
        while (commit.isPresent()) {
            result.add(commit.get());
            commit = parseCommit(reader);
        }
        return result;
    }

    /**
     * Parses a log item and outputs a commit object. Exceptions will be thrown in case the input does not have the proper format.
     * @param input representing the results from the git command
     * @return a commit object or an empty optional if there is nothing to parse anymore.
     */
    public static Optional<Commit> parseCommit(BufferedReader input) {
        try {

            var line = input.readLine();
            if (line == null) return Optional.empty(); // if no line can be read, we are done reading the buffer
            var idChunks = line.split(" ");
            if (!idChunks[0].equals("commit")) parseError();
            var builder = new CommitBuilder(idChunks[1]);

            line = input.readLine();
            while (!line.isEmpty()) {
                var colonPos = line.indexOf(":");
                var fieldName = line.substring(0, colonPos);
                var fieldContent = line.substring(colonPos + 1).trim();
                switch (fieldName) {
                    case "Author":
                        builder.setAuthor(fieldContent);
                        break;
                    case "Merge":
                        builder.setMergedFrom(fieldContent);
                        break;
                    case "Date":
                        builder.setDate(fieldContent);
                        break;
                    default:
                        System.out.println("The field '"+fieldName+"' was ignored while parsing the commit");
                }
                line = input.readLine(); //prepare next iteration
                if (line == null) parseError(); // end of stream is not supposed to happen now (commit data incomplete)
            }

            // now read the commit message per se
            var description = input
                    .lines() // get a stream of lines to work with
                    .takeWhile(currentLine -> !currentLine.isEmpty()) // take all lines until the first empty one (commits are separated by empty lines). Remark: commit messages are indented with spaces, so any blank line in the message contains at least a couple of spaces.
                    .map(String::trim) // remove indentation
                    .reduce("", (accumulator, currentLine) -> accumulator + currentLine); // concatenate everything
            builder.setDescription(description);
            return Optional.of(builder.createCommit());
        } catch (IOException e) {
            parseError();
        }
        return Optional.empty(); // this is supposed to be unreachable, as parseError should never return
    }

    /**
     * Helper function for generating parsing exceptions. This function *always* quits on an exception. It *never* returns.
     */
    private static void parseError() {
        throw new RuntimeException("Wrong commit format.");
    }

    /**
     * Change the <b>Commit</b> to a JSON formatted String
     * @return  a string that contains the <b>Commit</b> with its parameters
     */
    @Override
    public String toString() {
        return "Commit{" +
                //these 3 fields are optional
                (id != null ? ("id='" + id + '\'') : "") +
                (mergedFrom != null ? ("mergedFrom...='" + mergedFrom + '\'') : "") +
                (date != null ? (", date='" + date + '\'') : "") +
                (author != null ? (", author='" + author + '\'') : "") +
                //field of description is not optional
                ", description='" + description + '\'' +
                '}';
    }
    
    public String getLinesToString() {
    	int[]t = {this.linesAdded, this.linesDeleted};
    	return t[0] + " , " + t[1];
    }
}
