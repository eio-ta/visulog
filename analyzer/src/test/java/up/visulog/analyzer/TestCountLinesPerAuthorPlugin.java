package up.visulog.analyzer;

import org.junit.Test;

import up.visulog.analyzer.CountLinesPerAuthorPlugin.Result;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.CommitBuilder;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

public class TestCountLinesPerAuthorPlugin {
	@Test
	public void checkCountLineAdded() {
		Commit c1 = new Commit("1", "Author 1", null, null, null);
		c1.linesAdded = 5;
		Commit c2 = new Commit("2", "Author 1", null, null, null);
		c2.linesAdded = 10;
		Commit c3 = new Commit("3", "Author 2", null, null, null);
		c3.linesAdded = 2;
		Commit c4 = new Commit("4", "Author 2", null, null, null);
		c4.linesAdded = 8;
		Commit c5 = new Commit("2", "Author 1", null, null, null);
		c5.linesAdded = 2;
		Commit c6 = new Commit("2", "Author 3", null, null, null);
		c6.linesAdded = 6;
		
		ArrayList<Commit> L = new ArrayList<Commit>();
		L.add(c1);
		L.add(c2);
		L.add(c3);
		L.add(c4);
		L.add(c5);
		L.add(c6);
		
		var plugin = new CountLinesPerAuthorPlugin(null, true, false);
		Result result = plugin.processLog(L);
		
		String expected = "{Author 2=10, Author 1=17, Author 3=6}";
		assertEquals(expected, result.getResultAsString());
		
	}
}
