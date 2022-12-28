package up.visulog.webgen;

import up.visulog.gitrawdata.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.xmlet.htmlapifaster.Body;
import org.xmlet.htmlapifaster.Html;

import htmlflow.HtmlView;
import htmlflow.StaticHtml;

/**
 * The module WebGen is converting result from Plugins in Analyzer into Html and Javascript part
 * @see <a href="file:../../../../../../../analyzer/build/docs/javadoc/index.html">Analyzer</a>
 */

public class WebGen {
	/**
	 * Declared a variable view of Body<Html<HtmlView>> type where is stocked the head of the page
	 * it also stocks the first tag of the body
	 */
	private Body<Html<HtmlView>> view = StaticHtml.view()
			.html()
				.head()
					.meta().attrCharset("utf-8").__()
					.link().addAttr("rel", "icon").addAttr("href", "gengisKhan.png").__()
					.title().text("GenGit Scan").__()
					.link().addAttr("rel", "stylesheet").addAttr("href", "style.css").__()
					.script().attrSrc("chartsMin.js").__()
					.script().attrSrc("chartGen.js").__()
				.__()
				.body();
	/**
	 * Adds to the body the list of the authors linked with an Integer 
	 * @param list A map which contains the name of the authors linked with an Integer
	 * @param title A String of the title of the graph
	 */
	public void addListAuthor(Map<String, Integer>list, String title) {
		var body = view.div().ul().text(title);
		for(var item : list.entrySet()) {
			String content = item.getKey() + ": " + item.getValue();
			
			body.li().text(content).__();
		}
		body.__().__();		
	}

	/**
	 * Adds to the body the list of the authors
	 * @param list An ArrayList of String which contains the list of the authors
	 * @param title A String of the title of the graph
	 */
	public void addListAuthor(ArrayList<String>list, String title){
		var body = view.div().ul().text(title);
		for(String author : list){
			body.li().text(author).__();
		}
		body.__().__();
	}

	/**
	 * Adds to the body the list of the members in the project with their avatar, their full name and their username
	 * @param members A collection of all members of the project
	 * @param title A String of the title of the graph
	 */
	public void addListMembers(Collection<Member> members, String title){
		var body = view
				.div()
					.text(title)
					.table()
						.attrClass("members")
						.tr()
							.th().text("Avatar").__()
							.th().text("Full Name").__()
							.th().text("Username").__()
						.__();
		for(var member:members){
			body.tr()
					.td().img().attrSrc(member.getAvatar_url()).__().__()
					.td().text(member.getName()).__()
					.td().a().attrHref(member.getWeb_url()).text(member.getUsername()).__().__()
					.__();
		}

		body.__() //table
				.__(); //div
	}

	/**
	 * Adds a graph of a certain type with its title
	 * this graph contains the data compared with the labels
	 * @param type A String of the type of the graph
	 * @param title A String of the title of the graph
	 * @param label A String of the title of the list labels 
	 * @param labels An ArrayList of String of labels
	 * @param data An ArrayList of Integer of data measured
	 */
	public void addChart(String type, String title, String label, ArrayList<String> labels, ArrayList<Integer> data){
		String labelsJs = "var labels = [";
		for(String l : labels){
			labelsJs += "'" + l + "',";
		}
		labelsJs = labelsJs.substring(0, labelsJs.length()-1);
		labelsJs += "];";

		String dataJs = "var data = [";
		for(Integer d : data){
			dataJs += d+",";
		}
		dataJs = dataJs.substring(0, dataJs.length()-1);
		dataJs += "];";

		String genChartJs = "genChart('"+type+"','"+title+"','"+label+"', labels, data);";

		String js = labelsJs+"\n"+dataJs+"\n"+genChartJs+"\n";

		view.div()
				.script().text(js).__()
			.__();
	}

	/**
	 * Adds a graph of a certain type with its title
	 * this graph contains the data compared with the labels
	 * @param type A String of the type of the graph
	 * @param title A String of the title of the graph
	 * @param label A String of the title of the list labels 
	 * @param labels An ArrayList of String of labels
	 * @param data An ArrayList of Double of data measured
	 */
	public void addChartDouble(String type, String title, String label, ArrayList<String> labels, ArrayList<Double> data){
		String labelsJs="var labels = [";
		for(String l : labels){
			labelsJs += "'" + l + "',";
		}
		labelsJs = labelsJs.substring(0, labelsJs.length()-1);
		labelsJs += "];";

		String dataJs = "var data = [";
		for(Double d : data){
			dataJs+= d+",";
		}
		dataJs = dataJs.substring(0, dataJs.length()-1);
		dataJs+="];";

		String genChartJs = "genChart('"+type+"','"+title+"','"+label+"', labels, data);";

		String js = labelsJs+"\n"+dataJs+"\n"+genChartJs+"\n";

		view.div()
				.script().text(js).__()
			.__();
	}

	/**
	 * Adds a stacked bar chart with its title
	 * this graph contains the data compared with the labels
	 * @param title A String of the title of the graph
	 * @param labels An ArrayList of String of labels
	 * @param datasets An HashMap of a String linked with an ArrayList of Integer of data we measure
	 */
	public void addChart(String title, ArrayList<String> labels, HashMap<String, ArrayList<Integer>> datasets){
		String labelsJs = "var labels = [";
		for(String l : labels){
			labelsJs += "'" + l + "',";
		}
		labelsJs = labelsJs.substring(0, labelsJs.length()-1);
		labelsJs += "];";

		String datasetsJs = "var datasets = [";
		for(var d : datasets.entrySet()){
			datasetsJs += "['" + d.getKey() + "', [";
			for(int n : d.getValue()){
				datasetsJs += n + ",";
			}
			datasetsJs = datasetsJs.substring(0, datasetsJs.length()-1)+"]],";
		}
		datasetsJs = datasetsJs.substring(0, datasetsJs.length()-1)+"];";

		String genChartJs = "genChart2('"+title+"', labels, datasets);";

		String js = labelsJs+"\n"+datasetsJs+"\n"+genChartJs+"\n";

		view.div()
				.script().text(js).__()
			.__();
	}

	/**
	 * Closes the tag of body and html
	 * @return A String of the html code
	 */
	public String getHtml() {
		return view.__().__().render();
	}	

	/**
	 * Writes in index.html located in the folder htmlResult the result from the differents plugins of Analyzer
	 */
	public void write(){
		try{
			FileWriter file = new FileWriter("../htmlResult/index.html");
			file.write(getHtml());
			file.close();
		}catch(IOException e){
			throw new RuntimeException("Error SaveConfig", e);
		}

	}
	
	
	
	
}
