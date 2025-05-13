/**
 *Project: Testing Framework System
 *
 *@author Taha Yasir Colak
 *@author Aykan Berk Ayvazoglu
 *@author Cankat Caglar Acarer
 *@author Bartu Nurgun
 *
 */
package test_frame;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

//Represents a test execution context for a specific platform and test suite.
class TestExecution {
    private final String description;
    String platform; // Target platform for test execution
    private final TestSuite suite; // Associated test suite to execute
    public boolean executeGUITestsOnly = false; // Flag to indicate GUI-only testing
    public boolean executeNetworkTestsOnly = false;  // Flag to indicate network-only testing

    /**
     * Constructs a TestExecution with the given description, platform, and test suite.
     * @param description A label or note about the test run.
     * @param platform The name of the platform under test (e.g., Windows, Linux).
     * @param suite The suite of test cases to be executed.
     */
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
    }//value true to execute only GUI tests, false to disable the filter.

    public void setExecuteNetworkTestsOnly(boolean value) {
        this.executeNetworkTestsOnly = value;
    }
    //value true to execute only Network tests, false to disable the filter.

    public String getDescription() {
        return description;
    } //return Test exec. description
}

class LogViewer {
    /**
     * Reads the test_log.json file and prints its contents line-by-line to the console.
     * Handles the case where the file does not exist or an I/O error occurs.
     */
    public static void printLogs() {
        String filename = "test_log.json";
        try {
            if (!Files.exists(Paths.get(filename))) {
                System.out.println("No logs found."); // File not found
                return;
            }

            List<String> lines = Files.readAllLines(Paths.get(filename));
            System.out.println("\n--- Test Execution Log ---");
            for (String line : lines) {
                System.out.println(line); // Prints each line of the log file
            }
            System.out.println("---------------------------");
        } catch (IOException e) { //Catching error and report
            System.err.println("!!! Error reading log file: " + e.getMessage());
        }
    }
}
