/**
 * @author:tahacolak
 * This class represents a single test execution process for a specific platfors and test suite,
 * support execution of all test types, GUI-only, Network-only tests
 * @since 2025/04/24
 */
package com.TestFrameworkSys;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
public class TestExecution {

    private String description;
    private String platform;
    private TestSuite testSuite;
    private boolean executeGUITestsOnly;
    private boolean executeNetworkTestsOnly;
    private boolean sourceCodeCheckedIn;
    private boolean testExecutionCompleted;
    private boolean resultsReported;

    public TestExecution(String description, String platform, TestSuite testSuite) {
        this.description = description;
        this.platform = platform;
        this.testSuite = testSuite;
        this.executeGUITestsOnly = false;
        this.executeNetworkTestsOnly = false;
        this.sourceCodeCheckedIn = false;
        this.testExecutionCompleted = false;
        this.resultsReported = false;
    };

    //Return the test description
    public String getDescription() {
        return description;
    }

    public void setExecuteGUITestsOnly(boolean executeGUITestsOnly) {
        this.executeGUITestsOnly = executeGUITestsOnly;
        this.executeNetworkTestsOnly = !executeGUITestsOnly && this.executeNetworkTestsOnly;
    }

    public void setExecuteNetworkTestsOnly(boolean executeNetworkTestsOnly) {
        this.executeNetworkTestsOnly = executeNetworkTestsOnly;
        this.executeGUITestsOnly = !executeNetworkTestsOnly && this.executeGUITestsOnly;
    }

    //Source code checking in related platform
    public void checkInSourceCode() {
        System.out.println("Source code checked in for " + platform);
        sourceCodeCheckedIn = true;
    }

    protected void executeTests(){
        if (!sourceCodeCheckedIn) {
            throw new IllegalStateException("Source code must be checked in before executing tests.");
        }
        System.out.println("****************************");
        System.out.println("Executing tests for " + platform);
        System.out.println("****************************");

        if (executeGUITestsOnly) {
            System.out.println("Executing only GUI tests");
            Iterator<TestComponent> guiTests = testSuite.getGUITestIterator();
            while (guiTests.hasNext()) {
                guiTests.next().execute();
            }
        } else if (executeNetworkTestsOnly) {
            System.out.println("Executing only Network tests");
            Iterator<TestComponent> networkTests = testSuite.getNetworkTestIterator();
            while (networkTests.hasNext()) {
                networkTests.next().execute();
            }
        } else {
            System.out.println("Executing all tests");
            testSuite.execute();
        }

        testExecutionCompleted = true;
    }
    public void reportResults() {
        if (!testExecutionCompleted){
            throw new IllegalStateException("Tests must be executed before reporting results.");
        }
        System.out.println("****************************");
        TestResult result = testSuite.getResult();
        System.out.println("\n===== TEST RESULTS REPORT =====");
        System.out.println("Platform: " + platform);
        System.out.println("Test Suite: " + testSuite.getName());
        System.out.println("Overall Result: " + (result.isPassed() ? "PASSED" : "FAILED"));
        System.out.println("Timestamp: " +
                result.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("Details:\n" + result.getDetails());
        System.out.println("****************************");

        notifyStakeholders(result);
        resultsReported = true;

    }
    private void notifyStakeholders(TestResult result) {
        List<String> stakeholders = Arrays.asList(
                "Project Manager", "Development Team Lead", "Quality Assurance Lead",
                "Product Owner", "Release Manager"
        );

        System.out.println("\nNotifying stakeholders about test results:");
        for (String stakeholder : stakeholders) {
            System.out.println("- Sending notification to " + stakeholder);
        }
    }

    //Creates a memento object to capturing the current state of execution.
    public TestExecutionMemento createMemento() {
        return new TestExecutionMemento(sourceCodeCheckedIn, testExecutionCompleted, resultsReported);
    }

    public void restoreFromMemento(TestExecutionMemento memento) {
        this.sourceCodeCheckedIn = memento.isSourceCodeCheckedIn();
        this.testExecutionCompleted = memento.isTestExecutionCompleted();
        this.resultsReported = memento.isResultsReported();
    }

    //Implements a static TestExecutionMemento Class:
    //->Keeps the flags=object's conditions, unique memento object provides TestExecution object's  condition.
    //Just keeps state of Test Executions
    static class TestExecutionMemento {
        private final boolean sourceCodeCheckedIn;
        private final boolean testExecutionCompleted;
        private final boolean resultsReported;

        private TestExecutionMemento(boolean sourceCodeCheckedIn, boolean testExecutionCompleted,
                                     boolean resultsReported) {
            this.sourceCodeCheckedIn = sourceCodeCheckedIn;
            this.testExecutionCompleted = testExecutionCompleted;
            this.resultsReported = resultsReported;
        }

        public boolean isSourceCodeCheckedIn() {
            return sourceCodeCheckedIn;
        }

        public boolean isTestExecutionCompleted() {
            return testExecutionCompleted;
        }

        public boolean isResultsReported() {
            return resultsReported;
        }
    }



}
