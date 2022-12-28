package up.visulog.cli;

import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * A JPanel that will present a convenient way to select the plugin you want to launch
  */
public class CliMenuParameter extends JPanel {
	private CLIMenu CLIM;
	private String result;
	private boolean version;
	
	private JButton back = makeABeautifulButton("BACK");
	private JButton submit = makeABeautifulButton("SUBMIT");

	//CheckBox and RadioButton that will appear when the selected plugin have more parameter
	private ButtonGroup perDate = new ButtonGroup();
	private JRadioButton perDays = makeBeautifulRadioButton("PerDays");
	private JRadioButton perWeeks = makeBeautifulRadioButton("PerWeeks");
	private JRadioButton perMonths = makeBeautifulRadioButton("PerMonths");
	private JRadioButton none = new JRadioButton("None");
	
	private JCheckBox perAuthor = new JCheckBox("perAuthor");
	private JCheckBox forAllBranches = new JCheckBox("forAllBranches");
	
	//Texte Area needed to be fill For plugins using API
	private JTextField projectId = new JTextField();
	private JTextField privateToken = new JTextField();

	/**
	 * 	 * Function that create your MenuPlarameter window
	 * @param CLIM is the GUI that contains <b>CliMenuPath</b> and the <b>Command line</b>
	 * @param pluginName is the name of the plugin selected in the <b>MenuPlugin</b>
	 * @param version <p> two possibilities :<br>
	 * -True means the non-API with parameters plugin (Lines added/deleted + count commits)<br>
	 * -False means the API plugins</p>
	 */


	public CliMenuParameter(CLIMenu CLIM, String pluginName,boolean version) {
		this.version = version;
		this.CLIM = CLIM;
		result = pluginName;
		
		none.setFont(new Font("Monica", Font.PLAIN, 15));
		none.setHorizontalAlignment(0);
		none.setOpaque(false);
		none.addActionListener((event) -> NoDate());
		none.setSelected(true);
		perDate.add(none);
		
		perAuthor.setFont(new Font("Monica", Font.ITALIC, 15));
		perAuthor.setEnabled(false);
		perAuthor.setSelected(true);
		perAuthor.setHorizontalAlignment(0);
		perAuthor.setOpaque(false);
		
		
		
		
		forAllBranches.setFont(new Font("Monica", Font.PLAIN, 15));
		forAllBranches.setHorizontalAlignment(0);
		forAllBranches.setOpaque(false);

		this.setSize(800,500);
		this.CLIM.setSize(800,500);
		this.setLayout(null);
		this.setBackground(new Color(180, 211, 212));
		
		JPanel panelMain = new JPanel(new GridLayout(4,1));
		panelMain.setBackground(Color.white);
		panelMain.setBorder(BorderFactory.createEmptyBorder(10, 50, 50, 50));
		panelMain.setBounds(50, 30, 700, 400);
		this.add(panelMain);
		
		JLabel title = new JLabel("Parameters of "+pluginName+ ":", 0);
		title.setFont(new Font("Monica", Font.PLAIN, 20));
		panelMain.add(title);

		if(version) {// The first version (true one) of the window, the version for the non-API plugins
			
			GridLayout g1 = new GridLayout(1,4);
			g1.setHgap(20);
			JPanel date = new JPanel(g1); //Panel with all the date related Radio button
			date.setBackground(Color.white);
			panelMain.add(date);
			
			date.add(none);
			date.add(perDays);
			date.add(perWeeks);
			date.add(perMonths);
			
			GridLayout g2 = new GridLayout(1,2);
			g1.setHgap(60);
			JPanel options = new JPanel(g2); //Panel with the two check box button
			options.setBackground(Color.white);
			panelMain.add(options);
			options.add(perAuthor);
			options.add(forAllBranches);
		} else {// Second version (false one) of the window, the version for the API plugins
			
			GridLayout g = new GridLayout(4,1);
			g.setVgap(30);
			panelMain.setLayout(g);
			
			GridLayout g1 = new GridLayout(1,2);
			g1.setHgap(60);
			JPanel panelID = new JPanel(g1);//Panel containing the text filed for the project ID
			
			panelID.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
			panelID.setBackground(Color.white);
			
			JLabel subtitle1 = makeBeautifulLabel("Project ID :");
			panelID.add(subtitle1);
			panelID.add(projectId);
			panelMain.add(panelID);
			
			JPanel panelToken = new JPanel(g1);//Panel containing the text field for the private token
			panelToken.setBackground(Color.white);
			
			JLabel subtitle2 = makeBeautifulLabel("Private Token :");
			panelToken.add(subtitle2);
			panelToken.add(privateToken);
			panelMain.add(panelToken);
		}
		
		GridLayout g3 = new GridLayout(1,2); //Panel containing the submit and back button
		g3.setHgap(50);
		JPanel button = new JPanel(g3);
		button.setBackground(Color.white);
		button.add(back);
		button.add(submit);
		panelMain.add(button);
		
		back.addActionListener((event) -> { //Button that allows you to go back to the Menu plugin
			CLIM.changeToCliPlugin();
		});
		
		submit.addActionListener((event) -> {
			try {
				submitMethode();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
		}});
	}
	
	/**Our plugins names were a bit awkward, by default Lines added/deleted and countCommits are perAuthors. 
   	 * So plugins like LinesAddedAerAuthor without date doesn't exist by default.
	 * perAuthor button can be used when a date button is selected
	 */
	public void DateIsSelected() {
		if(!perAuthor.isEnabled()) {
			perAuthor.setEnabled(true);
			perAuthor.setSelected(false);
			perAuthor.setFont(new Font("Monica", Font.PLAIN, 15));
		}
	}
	/**
	 * If the non date button is selected you can't use the perAuthor button
	 */
	public void NoDate() {
		perAuthor.setSelected(true);
		perAuthor.setEnabled(false);
		perAuthor.setFont(new Font("Monica", Font.ITALIC, 15));
	}
	
	/**
	 * Create Radio buttons with the same properties
	 * @param name is the name of the Radio button
	 * @return A JRadioButton
	 */
	public JRadioButton makeBeautifulRadioButton(String name) {
		JRadioButton res = new JRadioButton(name);
		res.setFont(new Font("Monica", Font.PLAIN, 15));
		res.setHorizontalAlignment(0);
		res.setOpaque(false);
		res.addActionListener((event) -> DateIsSelected());
		perDate.add(res);
		return res;
	}
	
	/**
	 * Create buttons with the same properties
	 * @param name is the name of the button
	 * @return a JButton
	 */
	public JButton makeABeautifulButton(String name) {
		JButton b = new JButton(name);
		b.setBackground(new Color(180, 211, 212));
		return b;
	}
	
	/**
	 * Create Label with the same properties
	 * @param name is the Text of the Label
	 * @return a JLabel
	 */
	public JLabel makeBeautifulLabel(String name) {
		JLabel res = new JLabel(name, 0);
		res.setFont(new Font("Monica", Font.PLAIN, 15));
		return res;
	}
	
	/**
	 * Method executed when <b>Submit</b> button is pressed
	 * given the version of the window it will get the value of the buttons
	 * and add it to the <b>command</b> of <b>CLIM</b>.
	 * It will then swap the window to the Last menu
	 * @throws java.io.IOException when we have a java error when we open a file or write to a file
         * @throws java.net.URISyntaxException when the URI syntax is wrong
	 */
	
	public void submitMethode() throws IOException, URISyntaxException{
		if(version) {
			if(perAuthor.isEnabled() && perAuthor.isSelected()) result += "PerAuthor";
			
			if(perDays.isSelected()) result += "PerDays";
			else if (perWeeks.isSelected()) result += "PerWeeks";
			else if(perMonths.isSelected()) result += "PerMonths";
			
			
			if(forAllBranches.isSelected()) result += "ForAllBranches";
			CLIM.addPlugin(result);
		}
		
		else {
			CLIM.addPlugin(result);
			CLIM.addPrivateToken(privateToken.getText());
			CLIM.addProjectID(projectId.getText());

		}
		
		CLIM.changeToMenuLast();

	}

	
}
