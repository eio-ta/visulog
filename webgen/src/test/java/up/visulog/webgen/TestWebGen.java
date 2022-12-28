package up.visulog.webgen;

import up.visulog.webgen.WebGen;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


public class TestWebGen {

	@Test
	public void checkWebGen() throws URISyntaxException, IOException {
		//initializing Map and String for addListAuthor
		Map<String, Integer>list = new HashMap<String, Integer>();
		list.put("Author 1",1);
		list.put("Author 2", 2);
		String title ="title";
		//initializing variables for addChart
		String type = "type";
		ArrayList<String> labels = new ArrayList<String>();
		labels.add("Label1");
		labels.add("Label2");
		ArrayList<Integer> data = new ArrayList<Integer>();
		data.add(1);
		data.add(2);
		//initializing variables for addChart(String title, ArrayList<String> labels, HashMap<String, ArrayList<Integer>> datasets)
		HashMap<String, ArrayList<Integer>> datasets = new HashMap<String, ArrayList<Integer>>();
		datasets.put("data1", data);
		
		//using these variables we will run the different functions of WebGen and test if it gives the expected result
	
		WebGen webG = new WebGen();
		webG.addListAuthor(list, title);
		webG.addChart(title, labels, datasets);
		
		String result = webG.getHtml() + "\n";
		var uri = getClass().getClassLoader().getResource("expected.html").toURI();
		var expected = new File(uri);
		String expec = "";
		BufferedReader reader = new BufferedReader(new FileReader(expected));
		String line = "";
		while((line = reader.readLine()) != null) {
			expec += line + "\n";
		}
		
		//checking if we get expected result
		assertEquals(result,expec);
	}	

}
