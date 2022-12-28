package up.visulog.cli;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * A JPanel that will present a convenient way to select the plugin you want to launch
  */
public class CliMenuPlugin extends JPanel {

	private CLIMenu CLIM;
	private String result; //the command line that will be added to CLIM.commande

	//Buttons that represents the "main" plugins
	private JButton countLinesAdded = makeABeautifulButton("countLinesAdded");
	private JButton countLinesDeleted = makeABeautifulButton("countLinesDeleted");
	private JButton countMergeCommits = makeABeautifulButton("countMergeCommits");
	private JButton countCommits = makeABeautifulButton("countCommits");
	private JButton countContribution = makeABeautifulButton("countContribution");
	
	//Between the lines of "-" are the plugins that use the API, need to insert project Id and Token
	//-------------------------------------------------------------------------------------------------------------
	private JButton getMembers = makeABeautifulButton("getMembers");
	private JButton getExtensions = makeABeautifulButton("getExtensions");
	private JButton countIssues = makeABeautifulButton("countIssues");
	private JButton countComments = makeABeautifulButton("countComments");
	//-------------------------------------------------------------------------------------------------------------

	/**
	 * Function that create your MenuPlugin window
	 * @param CLIM is the GUI that contains <b>CliMenuPath</b> and the <b>Command line</b>
	 */

	public CliMenuPlugin(CLIMenu CLIM) {
		this.CLIM = CLIM;
		this.setSize(1200,500);
		this.setLayout(null);
		this.setBackground(new Color(180, 211, 212));
		
		JPanel panelMain = new JPanel(new GridLayout(4,1)); //The main Panel were you add everything
		panelMain.setBackground(Color.white);
		panelMain.setBorder(BorderFactory.createEmptyBorder(10, 50, 50, 50));
		panelMain.setBounds(50, 30, 1100, 400);
		
		this.add(panelMain);
		
		JLabel title = new JLabel("Plugins without API", 0);
		title.setFont(new Font("Monica", Font.PLAIN, 20));
		panelMain.add(title);
		
		GridLayout g1 = new GridLayout(1,5);
		g1.setHgap(20);
		JPanel buttonNoAPI = new JPanel(g1);//Panel that cointains non-API plugin button
		buttonNoAPI.setBackground(Color.white);
		panelMain.add(buttonNoAPI);
				
		buttonNoAPI.add(countLinesAdded);
		buttonNoAPI.add(countLinesDeleted);
		buttonNoAPI.add(countMergeCommits);
		buttonNoAPI.add(countCommits);
		buttonNoAPI.add(countContribution);
		
		JLabel title2 = new JLabel("Plugins with API", 0);
		title2.setFont(new Font("Monica", Font.PLAIN, 20));
		panelMain.add(title2);
		
		GridLayout g2 = new GridLayout(1,4);
		g2.setHgap(20);
		JPanel buttonAPI = new JPanel(g2);//Panel that cointains API plugin button
		buttonAPI.setBackground(Color.white);
		panelMain.add(buttonAPI);
		
		buttonAPI.add(getMembers);
		buttonAPI.add(getExtensions);
		buttonAPI.add(countIssues);
		buttonAPI.add(countComments);
	}
	
	/**
	 * Create buttons with the same properties
	 * @param name is the name of the button
	 * @return a JButton
	 */
	public JButton makeABeautifulButton(String name) {
		JButton b = new JButton(name);
		b.setBackground(new Color(180, 211, 212));
		b.addActionListener((event) -> {
			eventButton(name);
		});
		return b;
	}
	
	
	/**What happen when a button is pressed
	 * you have three possibilities:
	 *  -an non-API plugin with parameter
	 *  -an API plugin (it always has parameter
	 *  -an non-API plugin without parameter)
	 * @param name Of the Button
	   */
	public void eventButton(String name) {
		result = name;
		
		if(name.equals("countLinesAdded") || name.equals("countLinesDeleted") || name.equals("countCommits")) {
			CLIM.changeToCliPara(result,true);
		} else if(name.equals("getMembers") || name.equals("getExtensions") || name.equals("countIssues") || name.equals("countComments")) {
			CLIM.changeToCliPara(result,false);
		} else {
			CLIM.addPlugin(result);
			CLIM.changeToMenuLast();
		}
		
	}
	
	
}
