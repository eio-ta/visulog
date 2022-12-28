package up.visulog.gitrawdata;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is the core for all the API responses
 * Create JSON files that contains information that we got from the website
 */
public class APIresponse {
    /**
     * <b>APIresponse</b> constructor
     */
    public APIresponse(){
    }

    /**
     * This is a function that create and save a log file for one commit
     * @deprecated
     */
    @Deprecated
    public void createLogFileForCommits(){
        Process process;
        try {
            //getting date and time of the API request
            String out = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
            //creating a json file
            File jsonFile = new File("../GitLog/resultsCommit"+out+".json");
            FileWriter writeJsonFile = new FileWriter("../GitLog/resultsCommit"+out+".json");
            //Sending to GitLab API a request
            process = Runtime.getRuntime().exec("curl --header \"PRIVATE-TOKEN: 1m1pdKszBNnTtCHS9KtS\" \"https://gaufre.informatique.univ-paris-diderot.fr/api/v4/projects/1618/repository/commits\"");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            //Writing in JSON file
            while ((line = reader.readLine()) != null) {
                if(line.equals("{\"message\":\"404 Project Not Found\"}")){
                    System.out.println("Project not found, please check the projectId");
                    displayHelpAndExit();
                }
                else if(line.equals("{\"message\":\"401 Unauthorized\"}")){
                    System.out.println("Please check the Private Token or you don't have access to this project");
                    displayHelpAndExit();
                }
                writeJsonFile.write(line);
            }
            //closing the file
            writeJsonFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * This is a function that create and save a log file for one commit given the id of commit
     * @param id is a String that describes the id of the commit
     * @deprecated
     */
    @Deprecated
    public void createLogFileForOneCommit(String id){
        Process process;
        try {
            //creating a json file
            File jsonFile = new File("../GitLog/resultsCommit"+id+".json");
            FileWriter writeJsonFile = new FileWriter("../GitLog/resultsCommit"+id+".json");
            //Sending to GitLab API a request
            process = Runtime.getRuntime().exec("curl --header \"PRIVATE-TOKEN: 1m1pdKszBNnTtCHS9KtS\" \"https://gaufre.informatique.univ-paris-diderot.fr/api/v4/projects/1618/repository/commits/"+id+"\"");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            //Writing in JSON file
            while ((line = reader.readLine()) != null) {
                if(line.equals("{\"message\":\"404 Project Not Found\"}")){
                    System.out.println("Project not found, please check the projectId");
                    displayHelpAndExit();
                }
                else if(line.equals("{\"message\":\"401 Unauthorized\"}")){
                    System.out.println("Please check the Private Token or you don't have access to this project");
                    displayHelpAndExit();
                }
                writeJsonFile.write(line);
            }
            //closing the file
            writeJsonFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This is a function that create and save a log file for issues
     * @param privateToken is a String that describes your private token to auth in your project
     * @param idProject is an int that describes the id of your project
     */
    public void createLogFileForIssues(String privateToken,int idProject){
        Process process;
        try {

            //creating a json file
            File jsonFile = new File("../GitLog/resultsIssues.json");
            FileWriter writeJsonFile = new FileWriter("../GitLog/resultsIssues.json");
            //Command to terminal
            String[] command = {"curl",
                    "--header",
                    "PRIVATE-TOKEN:"+privateToken,
                    "https://gaufre.informatique.univ-paris-diderot.fr/api/v4/projects/"+idProject+"/issues?per_page=1000"};
            //Sending to GitLab API a request
            process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            //Writing in JSON file
            while ((line = reader.readLine()) != null) {
                if(line.equals("{\"message\":\"404 Project Not Found\"}")){
                    System.out.println("Project not found, please check the projectId");
                    displayHelpAndExit();
                }
                else if(line.equals("{\"message\":\"401 Unauthorized\"}")){
                    System.out.println("Please check the Private Token or you don't have access to this project");
                    displayHelpAndExit();
                }
                writeJsonFile.write(line);
            }
            //closing the file
            writeJsonFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This is a function that create and save a log file for issues of our project
     * @param idProject is an int that describes the id of your project
     * @deprecated
     */
    @Deprecated
    public void createLogFileForIssues(int idProject) {
        createLogFileForIssues("1m1pdKszBNnTtCHS9KtS", idProject);
    }

    /**
     * This function create a log file for only one issue in your project
     * @param privateToken is a String that describes your private token to auth in your project
     * @param idProject is an int that describes the id of your project
     * @param id is an int describing the id of the issue
     */
    public void createLogFileForOneIssue(String privateToken,int idProject ,int id){
        Process process;
        try {
            //creating a json file
            File jsonFile = new File("../GitLog/resultsIssue"+id+".json");
            FileWriter writeJsonFile = new FileWriter("../GitLog/resultsIssue"+id+".json");
            //Command to terminal
            String[] command = {"curl",
                    "--header",
                    "PRIVATE-TOKEN:"+privateToken,
                    "https://gaufre.informatique.univ-paris-diderot.fr/api/v4/projects/"+idProject+"/issues/"+id+"/discussions?per_page=1000"};
            //Sending to GitLab API a request
            process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            //Writing in JSON file
            while ((line = reader.readLine()) != null) {
                if(line.equals("{\"message\":\"404 Project Not Found\"}")){
                    System.out.println("Project not found, please check the projectId");
                    displayHelpAndExit();
                }
                else if(line.equals("{\"message\":\"401 Unauthorized\"}")){
                    System.out.println("Please check the Private Token or you don't have access to this project");
                    displayHelpAndExit();
                }
                writeJsonFile.write(line);
            }

            //closing the file
            writeJsonFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * This is a function that create and save a log file for one issue of our project
     * @param idProject is an int that describes the id of your project
     * @param id is an int describing the id of the issue
     * @deprecated
     */
    @Deprecated
    public void createLogFileForOneIssue(int idProject ,int id){
        createLogFileForOneIssue("1m1pdKszBNnTtCHS9KtS",idProject,id);
    }

    /**
     * This is a function that creates a log file for the members in your project
     * @param privateToken is a String that describes your private token to auth in your project
     * @param idProject is an int that describes the id of your project
     */
    public void createLogFileForMembers(String privateToken,int idProject){
        Process process;
        try {

            //creating a json file
            File jsonFile = new File("../GitLog/resultsMembers.json");
            FileWriter writeJsonFile = new FileWriter("../GitLog/resultsMembers.json");
            //Command to terminal
            String[] command = {"curl",
                    "--header",
                    "PRIVATE-TOKEN:"+privateToken,
                    "https://gaufre.informatique.univ-paris-diderot.fr/api/v4/projects/"+idProject+"/users?per_page=1000"};
            //Sending to GitLab API a request
            process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            //Writing in JSON file
            while ((line = reader.readLine()) != null) {
                if(line.equals("{\"message\":\"404 Project Not Found\"}")){
                        System.out.println("Project not found, please check the projectId");
                        displayHelpAndExit();
                }
                else if(line.equals("{\"message\":\"401 Unauthorized\"}")){
                        System.out.println("Please check the Private Token or you don't have access to this project");
                        displayHelpAndExit();
                }
                writeJsonFile.write(line);
            }
            //closing the file
            writeJsonFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function creates a log file containing the percentage of each extension
     * @param privateToken is a String that describes your private token to auth in your project
     * @param idProject is an int that describes the id of your project
     */
    public void createLogFileForExtensions(String privateToken,int idProject){
        Process process;
        try {

            //creating a json file
            File jsonFile = new File("../GitLog/resultsExtensions.json");
            FileWriter writeJsonFile = new FileWriter("../GitLog/resultsExtensions.json");
            //Command to terminal
            String[] command = {"curl",
                    "--header",
                    "PRIVATE-TOKEN:"+privateToken,
                    "https://gaufre.informatique.univ-paris-diderot.fr/api/v4/projects/"+idProject+"/languages?per_page=1000"};
            //Sending to GitLab API a request
            process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            //Writing in JSON file
            while ((line = reader.readLine()) != null) {
                if(line.equals("{\"message\":\"404 Project Not Found\"}")){
                    System.out.println("Project not found, please check the projectId");
                    displayHelpAndExit();
                }
                else if(line.equals("{\"message\":\"401 Unauthorized\"}")){
                    System.out.println("Please check the Private Token or you don't have access to this project");
                    displayHelpAndExit();
                }
                writeJsonFile.write(line);
            }
            //closing the file
            writeJsonFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function is used to Display help if a wrong command is used
     */
    private static void displayHelpAndExit() {
        System.out.println("Here is a list of what we can do : ");
        printAllPossiblePlugins();
        System.out.println("--load= (Loads the selected commands (test in the example) into a file ../config.txt)");
        System.out.println("(Format command: --load=name of the config)");
        System.out.println("Example : ./gradlew run --args='--load=test'\n");

        System.out.println("--save= (Save the commande \"--addPlugin=(name of command)\" in ../config.txt)");
        System.out.println("(Format command: --save=name of the config)");
        System.out.println("Example : ./gradlew run --args='--addPlugin=countCommits --save=test'\n");

        System.out.println("--privateToken= (to use the API)");
        System.out.println("(For our project: 1m1pdKszBNnTtCHS9KtS)\n");
        System.out.println("--projectId= (this is a ID of the project)");
        System.out.println("(For our project: 1618)\n");
        System.exit(0);
    }

    /**
     * this function displays all the possible plugins in our project
     */
    private static void printAllPossiblePlugins() {
        String space = "          ";
        String prohibition = " (X)";
        String api = " (API)";
        System.out.println("--addPlugin= (allows you to add new plugins)");
        System.out.println("If you want to see the results on all branches, just add \"ForAllBranches\" at the end of the command.\n");

        System.out.println("\"(X)\" means you can't have this command on all branches.\n");
        System.out.println("\"(API)\" means that the plugin in question uses an API. You will have to add as an option \"privateToken\" and projectId\".\n");

        System.out.println("GENERAL STATISTICS :");
        System.out.println("getMembers" + space + "Get members" + prohibition + api);
        System.out.println();

        System.out.println("ACTIVITY :");
        System.out.println("countCommitsPerDays" + space + "Commits per days");
        System.out.println("countCommitsPerWeeks" + space + "Commit per weeks");
        System.out.println("countCommitsPerMonths" + space + "Commits per months");
        System.out.println("countLinesAddedPerDays"+ space + "Lines added per days");
        System.out.println("countLinesDeletedPerDays"+ space + "Lines deleted per days");
        System.out.println("countLinesAddedPerWeeks"+ space + "Lines added per weeks");
        System.out.println("countLinesDeletedPerWeeks"+ space + "Lines deleted per weeks");
        System.out.println("countLinesAddedPerMonths"+ space + "Lines added per months");
        System.out.println("countLinesDeletedPerMonths"+ space + "Lines deleted per months");
        System.out.println();

        System.out.println("AUTHOR :");
        System.out.println("countCommits" + space + "Commits by author");
        System.out.println("countMergeCommits" + space + "Merge Commits by author" + prohibition);
        System.out.println("countLinesAdded" + space + "Lines added by author");
        System.out.println("countLinesDeleted" + space + "Lines deleted by author");
        System.out.println("countContribution"+ space + "Authors' contribution" + prohibition);
        System.out.println("countComments" + space + "Comments by author" + prohibition + api);
        System.out.println("countIssues" + space + "Issues by author" + prohibition + api);
        System.out.println();

        System.out.println("ACTIVITY BY AUTHOR :");
        System.out.println("countLinesAddedPerAuthorPerDays"+ space + "Lines added by author and per days");
        System.out.println("countLinesDeletedPerAuthorPerDays" + space + "Lines deleted by author and per days");
        System.out.println("countLinesAddedPerAuthorPerWeeks" + space + "Lines added by author and per weeks");
        System.out.println("countLinesDeletedPerAuthorPerWeeks" + space + "Lines deleted by author and per weeks");
        System.out.println("countLinesAddedPerAuthorPerMonths" + space + "Lines added by author and per months");
        System.out.println("countLinesDeletedPerAuthorPerMonths" + space + "Lines deleted by author and per months");
        System.out.println("countCommitsPerAuthorPerDays"+ space + "Commits by author and per days");
        System.out.println("countCommitsPerAuthorPerWeeks" + space + "Commits by author and per weeks");
        System.out.println("countCommitsPerAuthorPerMonths" + space + "Commits by author and per months");
        System.out.println();

        System.out.println("FILES AND EXTENSION :");
        System.out.println("getExtensions" + space + "Get Extensions" + prohibition + api);
        System.out.println();
    }
}
