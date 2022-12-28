package up.visulog.analyzer;

import org.junit.Test;
import up.visulog.gitrawdata.Comments;
import up.visulog.gitrawdata.Comment;
import up.visulog.gitrawdata.Author;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestCountCommentsPerAuthorPlugin {
    @Test
    public void checkCountComments(){
            Author a1 = new Author(1,"test1","test1");
            Author a2 = new Author(2,"test2","test2");
            Author a3 = new Author(3,"test3","test3");
            Author[] authors = {a1,a2,a3};
            Collection<Comments> comments = new ArrayList<>();
            int entries = 20;
            for(int i=0;i<entries;i++){
                List<Comment> commentList = new ArrayList<>();
                commentList.add(new Comment(String.valueOf(i),authors[i%3],"testing",null));
                comments.add(new Comments(commentList,String.valueOf(i)));
        }
            var res = CountCommentsPerAuthorPlugin.processLog(comments);
            assertEquals(authors.length, res.getCommentsPerAuthor().size());
            var sum = res.getCommentsPerAuthor().values().stream().reduce(0, Integer::sum);
            assertEquals(entries, sum.longValue());
    }

}
