package test_frame;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

class TestExecution {
    private final String description;
    String platform;
    private final TestSuite suite;
    public boolean executeGUITestsOnly = false;
    public boolean executeNetworkTestsOnly = false;

    public TestExecution(String description, String platform, TestSuite suite) {
        this.description = description;
        this.platform = platform;
        this.suite = suite;
    }

    public void executeTests() {
        System.out.println("[Execution] Executing tests for " + platform + "...");
        suite.execute();
    }

    public void reportResults() {
        System.out.println("[Execution] Reporting results for: " + description);
    }

    public void setExecuteGUITestsOnly(boolean value) {
        this.executeGUITestsOnly = value;
    }

    public void setExecuteNetworkTestsOnly(boolean value) {
        this.executeNetworkTestsOnly = value;
    }

    public String getDescription() {
        return description;
    }
}

class LogViewer {
    public static void printLogs() {
        String filename = "test_log.json";
        try {
            if (!Files.exists(Paths.get(filename))) {
                System.out.println("No logs found.");
                return;
            }

            List<String> lines = Files.readAllLines(Paths.get(filename));
            System.out.println("\n--- Test Execution Log ---");
            for (String line : lines) {
                System.out.println(line);
            }
            System.out.println("---------------------------");
        } catch (IOException e) {
            System.err.println("!!! Error reading log file: " + e.getMessage());
        }
    }
}
