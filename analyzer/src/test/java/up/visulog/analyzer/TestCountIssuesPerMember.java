package up.visulog.analyzer;
import org.junit.Test;
import up.visulog.gitrawdata.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
public class TestCountIssuesPerMember {
    @Test
    public void checkCountIssues(){
        String[] authors = {"test1","test2","test3"};
        Collection<Assignees> assignees = new ArrayList<>();
        int entries = 20;
        for(int i=0;i<entries;i++){
            Collection<Assignee> assignee = new ArrayList<>();
            assignee.add(new Assignee(i%3,authors[i%3],authors[i%3]));
            assignees.add(new Assignees(assignee));
        }
        var res = CountIssuesPerMemberPlugin.processLog(assignees);
        assertEquals(authors.length, res.getIssuesPerMember().size());
        var sum = res.getIssuesPerMember().values().stream().reduce(0, Integer::sum);
        assertEquals(entries, sum.longValue());
    }

}
