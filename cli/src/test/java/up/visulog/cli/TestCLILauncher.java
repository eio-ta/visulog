package up.visulog.cli;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestCLILauncher {
    @Test
    public void testArgumentParser() {
        var config1 = CLILauncher.makeConfigFromCommandLineArgs(new String[]{".", "--addPlugin=countCommits"});
        assertTrue(config1.isPresent());
        var config2 = CLILauncher.makeConfigFromCommandLineArgs(new String[] {
            "--nonExistingOption"
        });
        assertFalse(config2.isPresent());
    }
}

