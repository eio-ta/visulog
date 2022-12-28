package up.visulog.analyzer;

import org.junit.Test;

import up.visulog.analyzer.CommitsPerDatePlugin.Result;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.CommitBuilder;
import static org.junit.Assert.assertEquals;
import java.time.LocalDateTime;

import java.util.ArrayList;

public class TestCommitsPerDatePlugin {
	
	@Test
	public void checkCommitsPerDate() {
		Commit c1 = new Commit("1", "Author 1", LocalDateTime.of(2020, 1, 20, 11, 16, 50), null, null);
		Commit c2 = new Commit("2", "Author 1", LocalDateTime.of(2023, 12, 26, 20, 16, 50), null, null);
		Commit c3 = new Commit("3", "Author 2", LocalDateTime.of(2020, 1, 30, 20, 54, 50), null, null);
		Commit c4 = new Commit("4", "Author 2", LocalDateTime.of(2020, 8, 20, 11, 16, 50), null, null);
		Commit c5 = new Commit("2", "Author 1", LocalDateTime.of(1999, 8, 21, 11, 16, 50), null, null);
		Commit c6 = new Commit("2", "Author 3", LocalDateTime.of(2020, 9, 20, 11, 16, 50), null, null);
		
		ArrayList<Commit> L = new ArrayList<Commit>();
		L.add(c1);
		L.add(c2);
		L.add(c3);
		L.add(c4);
		L.add(c5);
		L.add(c6);
		
		var plugin = new CommitsPerDatePlugin(null, "months", false);
		Result result = plugin.processLog(L);
		
		String expected = "{1999-08-01=1, 2020-01-01=2, 2020-08-01=1, 2020-09-01=1, 2023-12-01=1}";
		assertEquals(expected, result.getResultAsString());	
	}
}
