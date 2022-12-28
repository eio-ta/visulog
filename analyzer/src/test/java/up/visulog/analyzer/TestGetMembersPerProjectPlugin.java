package up.visulog.analyzer;
import org.junit.Test;
import up.visulog.gitrawdata.*;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
public class TestGetMembersPerProjectPlugin {
    @Test
    public void checkMembers(){
        String[] authors = {"test1","test2","test3"};
        Collection<Member> members = new ArrayList<>();
        members.add(new Member(0,"test1","test1",null,null));
        members.add(new Member(1,"test2","test2",null,null));
        members.add(new Member(2,"test3","test3",null,null));
        var res = GetMembersPerProjectPlugin.processLog(members);
        assertEquals(authors.length, res.getMembers().size());
    }
}
