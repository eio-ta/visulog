package up.visulog.cli;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *<p> A JPanel that will be the last window: <br>
 * show the actual <b>command line</b>, it can be selected and copied.<br>
 * ask if the user want to execute the program or add other plugins</p>
  */
public class CliMenuLast extends JPanel{
	private JTextPane JCommande = new JTextPane(); //contains the actual command line
	private CLIMenu CLIM;
	private JPanel panelMain;
	private JPanel panelCommande;
	
	/**
	 * Method to set the text inside the JTextPane
	 * @param line should be the <b>Command line</b>
	 */
	public void setCommande(String line) {
		this.JCommande.setText(line);
	}
	
	/**
	 * Function that create your MenuPlugin window
	 * @param CLIM is the GUI that contains <b>CliMenuPath</b> and the <b>Command line</b>
	 */

	public CliMenuLast(CLIMenu CLIM) {
		this.CLIM = CLIM;
		
		this.setSize(700,500);
		this.setBackground(new Color(180, 211, 212));
		this.setLayout(null);
		
		GridLayout g = new GridLayout(4,1);
		g.setHgap(10);
		panelMain = new JPanel(g);// Panel that will be the window showed
		panelMain.setBackground(Color.white);
		//panelMain.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		panelMain.setBounds(50, 30, 600, 400);
		
		
		JButton execute = new JButton("Execute");
		execute.setBackground(new Color(180, 211, 212));
		JButton addNewPlugin = new JButton("Add Other Plugin");
		addNewPlugin.setBackground(new Color(180, 211, 212));
		execute.addActionListener((event) -> { // when the button execute is pressed launch the program
			try {
				CLIM.launch();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		});
		addNewPlugin.addActionListener((event) -> CLIM.changeToCliPlugin()); //when button addNewPlugin pressed go to Menu plugin
		
		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));//Panel containing execute button
		panel1.setBackground(Color.white);
		execute.setPreferredSize(new Dimension(240,50));
		execute.setFont(new Font("Monica", Font.PLAIN, 20));
		panel1.add(execute);
		
		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));//Panel containing addNewPlugin button
		panel2.setBackground(Color.white);
		addNewPlugin.setPreferredSize(new Dimension(240,50));
		addNewPlugin.setFont(new Font("Monica", Font.PLAIN, 20));
		panel2.add(addNewPlugin);
		
		JLabel subTitle = new JLabel("your command :");
		JPanel panelTitle = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));//Panel containing the Label
		panelTitle.setSize(30, 10);
		panelTitle.setBackground(Color.white);
		panelTitle.add(subTitle);
	
		JCommande.setEditable(false);
		SimpleAttributeSet attribs = new SimpleAttributeSet();
		StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);
		JCommande.setParagraphAttributes(attribs, true);

		
		
		panelCommande = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); //Panel containing the command line
		panelCommande.setLayout(new BorderLayout());
		panelCommande.setBackground(Color.white);
		panelCommande.add(JCommande, BorderLayout.NORTH);
		
		
		panelMain.add(panelTitle);
		panelMain.add(panelCommande);
		
		
		panelMain.add(panel1);
		panelMain.add(panel2);

		
		this.add(panelMain);
	}
	
}
