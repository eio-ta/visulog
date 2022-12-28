package up.visulog.gitrawdata;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCommit {
    @Test //changed the expected value of date since issue 41 required it.
    public void testParseCommit() throws IOException, URISyntaxException {
        var expected = "Commit{id='6304c1acdc1cbdeb8315528781896abc72a021b8', date='2020-09-01T12:30:53', author='Aldric Degorre <adegorre@irif.fr>', description='More gradle configuration (with subprojects)'}";
        var uri = getClass().getClassLoader().getResource("git.log").toURI();
        try (var reader = Files.newBufferedReader(Paths.get(uri))) {
            var commit = Commit.parseCommit(reader);
            assertTrue(commit.isPresent());
            assertEquals(expected, commit.get().toString());
        }
    }
    
    @Test
    public void testParseLog() throws IOException, URISyntaxException {
        var expectedUri = getClass().getClassLoader().getResource("expected").toURI();
        var logUri = getClass().getClassLoader().getResource("git.log").toURI();
        try (var expectedReader =  Files.newBufferedReader(Paths.get(expectedUri))) {
            try (var logReader = Files.newBufferedReader(Paths.get(logUri))) {
                var log = Commit.parseLog(logReader);
                var expected = expectedReader.lines().reduce("", (cur, acc) -> cur + acc);
                assertEquals(expected, log.toString());
            }
        }
    }
    
    @Test
    public void testGetNumberLines() {
    	//Values collected on gitlab
    	int[][]expectedLines = {{18,0},{30,11},{1,1},{1,3},{2,1},{854,2},{854,2},{10,7}};
    	List<Commit> commits = Commit.parseLogFromCommand(Paths.get("../"), false); 
    	int x = 0;
    	for(int i = commits.size() - 1; i >= commits.size() - 8; i--) {
    		String line = commits.get(i).getLinesToString();
    		String expectedLine = expectedLines[x][0] + " , " + expectedLines[x][1];
    		assertEquals(line, expectedLine);
    		x++;
    	}
    	
    }
    
    @Test
    public void testParseLinesContribution() throws URISyntaxException {
    	String expected = "{<ismail.badaoui@etu.univ-paris-diderot.fr>=13, <etienne.nedjai@laposte.net>=4, <adegorre@irif.fr>=57, <elodytang@hotmail.fr>=35, <not.committed.yet>=12, <julescherion@yahoo.fr>=10, <yoyo031201@orange.fr>=105}";
    	var uri = getClass().getClassLoader().getResource("git.blame").toURI();
    	try {
			var reader = Files.newBufferedReader(Paths.get(uri));
			assertEquals(expected, Commit.parseLinesContribution(reader).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}


