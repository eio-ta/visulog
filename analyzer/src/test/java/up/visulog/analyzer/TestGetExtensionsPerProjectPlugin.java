package up.visulog.analyzer;
import org.junit.Test;
import up.visulog.gitrawdata.*;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
public class TestGetExtensionsPerProjectPlugin {
    @Test
    public void checkCountIssues(){
        HashMap<String,Double> extensions = new HashMap<>();
        extensions.put("Java",90.6);
        extensions.put("Python",9.0);
        extensions.put("Kotlin",0.4);
        var res = GetExtensionsPerProjectPlugin.processLog(extensions);
        assertEquals(extensions.size(), res.getExtensionsResult().size());
        var sum = res.getExtensionsResult().values().stream().reduce(0.0, Double::sum);
        assertEquals(100, (int)sum.doubleValue());
    }
}
