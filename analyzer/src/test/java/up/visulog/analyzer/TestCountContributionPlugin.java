package up.visulog.analyzer;

import org.junit.Test;

import up.visulog.gitrawdata.Commit;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

public class TestCountContributionPlugin {
	@Test
	public void checkCountContribution() {
		HashMap<String, String>emailToName = new HashMap<String, String>();
		emailToName.put("email@1", "Author 1 email@1");
		emailToName.put("email@2", "Author 2 email@2");
		emailToName.put("email@3", "Author 3 email@3");
		
		HashMap<String, Integer> LinesPerEmail = new HashMap<String, Integer>();
		LinesPerEmail.put("email@1", 50);
		LinesPerEmail.put("email@2", 150);
		LinesPerEmail.put("email@3", 50);
		
		CountContributionPlugin contribution = new CountContributionPlugin(null);
		var result = contribution.processLog(LinesPerEmail, emailToName);
		
		String expected = "{Author 3 email@3=20.0, Author 2 email@2=60.0, Author 1 email@1=20.0}";
		
		assertEquals(expected,result.getResultAsString());
	}
}
