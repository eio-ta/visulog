package up.visulog.analyzer;

import org.junit.Test;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.CommitBuilder;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestCountMergeCommitsPerAuthorPlugin {
    @Test
    public void checkMergeCommitSum() {
        var log = new ArrayList<Commit>();
        String[] authors = {"foo", "bar", "bcd"};
        var entries = 10;
        for (int i = 0; i < entries; i++) {
            log.add(new Commit(String.valueOf(i%3),authors[i%3],null,"Test","Test"+(i)));
        }
        var res = CountMergeCommitsPerAuthorPlugin.processLog(log);
        assertEquals(authors.length, res.getMergeCommitsPerAuthor().size());
        var sum = res.getMergeCommitsPerAuthor().values().stream().reduce(0, Integer::sum);
        assertEquals(entries, sum.longValue());
    }
}