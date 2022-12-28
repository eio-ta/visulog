package up.visulog.cli;

import java.awt.CardLayout;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This Class is the core of the GUI, it's the JFrame where all the other JPanel will be showed
 * */

public class CLIMenu extends JFrame {
	private String projectID;
	private String privateToken;

	private String commande = ""; 	//the commande line that is written by the GUI
	
	private JPanel mainPanel;
	

	private CliMenuPath menuPath; 	//All the Jpanel that can be showed when using the GUI
	private CliMenuPlugin menuPlugin;
	private CliMenuParameter menuParameter;
	private CliMenuLast menuLast;

	private CardLayout cardLayout;
	

	/**
	 * The Master Frame that will contains all the windows
	 */
	public CLIMenu() {
		this.setSize(700, 500);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("GenGit Scan");
		
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		//The mainPanel will contain all the other JPanel defined in the Class
		
		//We add all the JPanel that might be of use
		menuPath = new CliMenuPath(this);
		mainPanel.add("menuPath", menuPath);
		menuPlugin = new CliMenuPlugin(this);
		mainPanel.add("menuPlugin" ,menuPlugin);
		menuLast = new CliMenuLast(this);
		mainPanel.add("menuLast",menuLast);
		
		
		this.getContentPane().add(mainPanel);
		cardLayout.show(mainPanel, "menuPath");
	
		
		this.setVisible(true);
	}
	
	
	
	/*
	 All the method which names start by "add" literally add a String properly in the command line
	 It's the way we build the command line when the GUI is being used */
	/**
	 * add the path parameter to the <b>Command line</b>
	 * @param path of the project
	 */
	public void addPath(String path) {
		commande += path;
	}
	/**
	 * add the plugin with parameters to the <b>Command line</b>
	 * @param R the String of the plugin
	 */
	public void addPlugin(String R) {
		if(commande.length() == 0) commande = "--addPlugin="+R;
		else commande = commande + " --addPlugin=" + R;
	}
	/**
	 * add the private Token parameter to the <b>Command line</b>
	 * @param token String of the token
	 */
	public void addPrivateToken(String token) {
		this.privateToken = token;
		commande = commande + " --privateToken=" + token;
	}
	/**
	 * add the project ID parameter to the <b>Command line</b>
	 * @param ID String of the project ID
	 */
	public void addProjectID(String ID) {
		this.projectID = ID;
		commande = commande + " --projectId=" + ID;
	}
	
	
	/**
	 * All the method that start with "changeTo" will swap the JPanel showed to the designed JPanel
	 * By doing so they will resize the window and so on to have a nice and tidy GUI
	 * */
	public void changeToCliPlugin() {
		this.setSize(1200,500);
		cardLayout.show(mainPanel, "menuPlugin");
	}
	
	
	/**
	 * This method change to the panel CliMenuParameter
	 * @param result is the name of the plugin
	 * @param version define what page should be showed (there are 2 versions)
	  */
	public void changeToCliPara(String result,  boolean version) {
		if( !version && privateToken == null && projectID == null) {		
			menuParameter = new CliMenuParameter(this, result, version);
			mainPanel.add("menuParameter", menuParameter);
			cardLayout.show(mainPanel, "menuParameter");

		}
		else if(version) {
			menuParameter = new CliMenuParameter(this, result, version);
			mainPanel.add("menuParameter", menuParameter);
			cardLayout.show(mainPanel, "menuParameter");
		}
		else {
			changeToMenuLast();
			addPlugin(result);
		}
		
	}
	
	/**
	 * Change the window to menu last
	 */
	public void changeToMenuLast() {
		this.setSize(700,500);
		menuLast.setCommande(commande);
		cardLayout.show(mainPanel, "menuLast");
	}
	
	
	/**
	 * A simple method that run the program when the use of the GUI is over
	 * @throws java.io.IOException when we have a java error when we open a file or write to a file
         * @throws java.net.URISyntaxException when the URI syntax is wrong
	 */
	
	public void launch() throws IOException, URISyntaxException {
		this.dispose();
		CLILauncher.launch(commande.split(" "));

	}
}

