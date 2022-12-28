package up.visulog.cli;

import up.visulog.analyzer.Analyzer;
import up.visulog.analyzer.AnalyzerResult;
import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.webgen.WebGen;

import java.awt.Desktop;
// a library that allows you to read an inputReader (for example an FileReader or an InputStreamReader)
import java.io.*;
//a library that allows you to read a File, creates an FileReader
//A FileReader alone is useless, we need to use it in a BufferedReader

// a library that allows you to write to files

//When we have a java error when we open a file or write to a file, it creates an IOException
//So we need to catch it, that's why I use the couple try,catch
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;

//libraries that allow you to find files thanks to their path
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.*;

/**
 * Class containing the main section of the program
 * its main goal is to interpret the argument, which we call the <b>Command line </b>
 */
public class CLILauncher {
	/**
	 * <b>pluginWithoutAPI</b> is the list of plugins that doesn't use API
	 */
	private static String[] pluginWithoutAPI = {"countCommits","countCommitsForAllBranches","countMergeCommits","countCommitsPerMonths","countCommitsPerMonthsForAllBranches",
			"countCommitsPerWeeks","countCommitsPerWeeksForAllBranches","countCommitsPerDays","countCommitsPerDaysForAllBranches",
			"countLinesDeleted","countLinesDeletedForAllBranches","countLinesAdded","countLinesAddedForAllBranches","countLinesAddedPerDays",
			"countLinesAddedPerDaysForAllBranches","countLinesAddedPerWeeks","countLinesAddedPerWeeksForAllBranches","countLinesAddedPerMonths",
			"countLinesAddedPerMonthsForAllBranches","countLinesDeletedPerDays","countLinesDeletedPerDaysForAllBranches","countLinesDeletedPerWeeks",
			"countLinesDeletedPerWeeksForAllBranches","countLinesDeletedPerMonths","countLinesDeletedPerMonthsForAllBranches","countContribution",
			"countLinesAddedPerAuthorPerDays","countLinesAddedPerAuthorPerDaysForAllBranches","countLinesAddedPerAuthorPerWeeks",
			"countLinesAddedPerAuthorPerWeeksForAllBranches","countLinesAddedPerAuthorPerMonths","countLinesAddedPerAuthorPerMonthsForAllBranches",
			"countLinesDeletedPerAuthorPerDays","countLinesDeletedPerAuthorPerDaysForAllBranches","countLinesDeletedPerAuthorPerWeeks","countLinesDeletedPerAuthorPerWeeksForAllBranches",
			"countLinesDeletedPerAuthorPerMonths","countLinesDeletedPerAuthorPerMonthsForAllBranches","countCommitsPerAuthorPerDays","countCommitsPerAuthorPerDaysForAllBranches",
			"countCommitsPerAuthorPerMonths","countCommitsPerAuthorPerMonthsForAllBranches","countCommitsPerAuthorPerWeeks","countCommitsPerAuthorPerWeeksForAllBranches"};
	/**
	 * <b>pluginWithAPI</b> is the list of plugins that use API
	 */
	private static String[] pluginWithAPI = {"countComments","getMembers","getExtensions","countIssues"};

	/**
	 * This function checks whether the plugin uses API or not
	 * @param p the name of plugin
	 * @param arr the list of plugins
	 * @return true if p is in arr
	 */
	public static boolean in(String p,String[] arr){
		for(String s:arr){
			if(p.equals(s)) return true;
		}
		return false;
	}

	/** 
	 * Main function of the project it's the function that is executed when you run the program
	 * @param args command line written by user
	 * @throws java.io.IOException when we have a java error when we open a file or write to a file
         * @throws java.net.URISyntaxException when the URI syntax is wrong
	 * */
    public static void main(String[] args) throws IOException, URISyntaxException {
    	if(args.length == 0) {
    		new CLIMenu();
    	}
    	else launch(args);
    }
    
    /**
     * Launch is a method that allow to "run" the program when using the GUI
     * @param args should be the command line
     * @throws java.io.IOException when we have a java error when we open a file or write to a file
     * @throws java.net.URISyntaxException when the URI syntax is wrong
     * */
    public static void launch(String [] args) throws IOException, URISyntaxException {
	    var config = makeConfigFromCommandLineArgs(args);
	    //create config from args
	    if (config.isPresent()) {
	        var analyzer = new Analyzer(config.get());
	        // run the plugins of the config
			var results = analyzer.computeResults();
			// get the result
			var wg = new WebGen();
			results.toHTML(wg);
			wg.write();
			//creation of a new blank html page and then fill it with the results
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(new File("../htmlResult/index.html"));
				//Opening of the file if it's possible
			}
	    } else displayHelpAndExit(args);
	    // if the Opening of the file was not possible show the "Help" in the terminal
	}

    /**
     * makeConfigFromCommandLineArgs is a fonction that transforms the command Line and it's arguments into a configuration :
     * -it cut the arguments to find the plugins that will be launched
     * -transform the Strings into configuration, used in analyzer
     * @param args Should be the <b>Command line</b>
     * @return The configuration build from the <b>Command line</b>
     */
    static Optional<Configuration> makeConfigFromCommandLineArgs(String[] args) {
    	if(args.length==0){
    		return Optional.empty();
		}
        var gitPath = FileSystems.getDefault().getPath("../");
        var plugins = new ArrayList<String>();
        String pPrivateToken = "";
        int pProjectId = -1;
        boolean API = false;
        for (var arg : args) {
            if (arg.startsWith("--")) {
                String[] parts = arg.split("=");
                if (parts.length != 2) {
                	if(parts.length == 1 && arg.equals("--help")) {
                		displayHelpAndExit(args);
                	}
                	else return Optional.empty();
                }
                else {
                    String pName = parts[0];
                    String pValue = parts[1];
                    switch (pName) {
                    //Here looking is you want to launch a plugin (with or without API), save or even load a configuration
                        case "--addPlugin":
                            // Here you are looking for the plugin the user gave in the command line
							if(in(pValue,pluginWithoutAPI)){
								plugins.add(pValue);
								break;
							}
							else if(in(pValue,pluginWithAPI)){
								plugins.add(pValue);
								API = true;
								break;
							}
							else{
								return Optional.empty();
							}
                        case "--load":
                        	//Format command: --load=name of the config
                        	//Example: ./gradlew run --args='--load=test'
                        	//this command loads the command "test" that is in ../config.txt
                        	return makeConfigFromCommandLineArgs(loadConfig(pValue));
                        	
                        case "--save":
                        	//Format command: --save=name of the config
                        	//Example: ./gradlew run --args='--addPlugin=countCommits --save=test'
                        	//this command saves "--addPlugin=countCommits" in ../config.txt
                        	String command = saveConfig(args,pValue);
                        	System.out.println("Command: " + command + ", is saved");
                        	System.exit(0);	
                        case "--privateToken":
                            pPrivateToken = pValue;
                            break;
                        case "--projectId":
                            pProjectId = Integer.parseInt(pValue);
                            break;
                        default:
                            return Optional.empty();
                    }
                }
            } else {
            	// Case of a directory being written
            	String path = arg;
            	if(!arg.startsWith("/")) path = "../" + path;
            	if(!isGitDirectory(path)) displayHelpAndExit(args);
                gitPath = FileSystems.getDefault().getPath(path);
            }
        }
        if(API && (pProjectId==-1 || pPrivateToken.equals(""))){
            return Optional.empty();
        }
        return Optional.of(new Configuration(gitPath,pPrivateToken,plugins,pProjectId));
    }
    
    
    /**
     * Method that check if the Directory given is a GitDirectory or not
     * @return true or false 
     */
    private static boolean isGitDirectory(String path) {
    	File file = new File(path);
    	if(!file.exists()) {
    		if(!path.startsWith("/")) path = path.substring(3);
    		System.out.println("Error to find the file " + path);
    		return false;
    	}
    	
    	if(!file.isDirectory()) {
    		if(!path.startsWith("/")) path = path.substring(3);
    		System.out.println("the file : " + path + " is not a directory !");
    		return false;
    	}
    	
    	File[] files = file.listFiles();
    	for(File f : files) {
    		if(f.getName().equals(".git")) {
    			return true;
    		}
    	}
    	if(!path.startsWith("/")) path = path.substring(3);
		System.out.println("the file : " + path + " is not a git directory !");
		return false;
     }
    
    /**
     * this function save the command in the file whose path is "path"
     * @param args <b>Command line</b>
     * @param name Name of the configuration where the <b>Command line</b> is saved
     * @return the name of the commnd name
     */
    private static String saveConfig(String[] args, String name) {
    	String content = "";
    	for(int i = 0; i < args.length; i++) {
    		if(!args[i].equals("--save=" + name)) content += args[i] + " ";
    	}
    	if(content.length() != 0) {
    		content = content.substring(0, content.length() - 1);
    	}
    	try {
    		BufferedReader reader = new BufferedReader(new FileReader("../config.txt"));
    		String line;
    		String oldContent = "";
    		while((line = reader.readLine()) != null) {
    			Scanner sc = new Scanner(line);
    			if(sc.hasNext()) {
    				String n = sc.next();
    				if(!n.equals(name)) {
    					oldContent += line + "\n";
    				}
    				else System.out.println("Warning! old configuation :" + name + " overwitted!");
    			}
    			sc.close();
    		}
    		FileWriter file = new FileWriter("../config.txt");
    		file.write(oldContent + name + " " + content + "\n");
    		file.close();
    		return content;
    	} catch(IOException e) {
    		throw new RuntimeException("Error SaveConfig", e);
    	}
    }
    
    /**LoadConfig launch a config that had been saved before
     * @param name This is the name of the saved configuration
     * @return The <b>Command line</b> saved in form of a <i>String[]</i>
    */
    private static String[] loadConfig(String name) {
    	try {
    		BufferedReader reader = new BufferedReader(new FileReader(Paths.get("../config.txt").toFile()));
    		String line;
    		while((line = reader.readLine()) != null) {
    			Scanner sc = new Scanner(line);
    			if(sc.hasNext() && sc.next().equals(name)) {
    				sc.close();
    				break;
    			}
    			sc.close();
    		}
    		if(line == null) {
    			//error message
    			System.out.println("Config not found, you should save it first using: \n./gradlew run --args=' Your command --save="+name+"'");
    			String [] fill= {"--"};
    			return fill;
    		}
    		line = line.substring(name.length() + 1);
    		String[]args = line.split(" ");
    		return args;
    	}catch(IOException e) {
    		throw new RuntimeException("Error loadConfig",e);
    	}
    }
    

    /**
     * This method is used when something went wrong with the command line filled by the user
     * it print all the existing plugins and some advices to use the program properly in the Terminal
     * for more details @see printAllPossiblePlugins
     * @param args should be the <b>Command line</b>
     * */
    private static void displayHelpAndExit(String[] args) {
    	if(args.length != 0) {
    		if(!args[0].equals("--help")) System.out.println("Wrong command...");
    	}
    	System.out.println("You can use a Graphic Interface by running the program without arguments");
    	System.out.println("You juste have to writte \"./gradlew run\" to launch it \n");
	    System.out.println("Here is a list of what we can do : ");
	    System.out.println("--args=\"[path to your git project] \"");
	    System.out.println("/!\\ if no path is selected the directory will be GenGit scan directory, not the current directory !");
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
     * print in the terminal all the puglins that exist in the project
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
