package test_frame;

import java.util.*;

/**
 * CLIProcess interface provides an Command-Line Interface for planning, executing
 * and managing test cases using a test scheduler and command pattern.
 */

interface CLIProcess {
    Scanner scanner = new Scanner(System.in);
    TestScheduler scheduler = TestScheduler.getInstance();

    default void start() {
        System.out.println("\n\t\t*");
        System.out.println("\t\t  TESTING FRAMEWORK SYSTEM ");//Scheduler will run every Monday at 09:00
        System.out.println("\t\t*");
        boolean running = true;

        //User Input Operations over MAIN MENU
        while (running) {
            System.out.println("\n*----------------- MAIN MENU -----------------*");
            System.out.print("|");System.out.print("  1. Plan a Test Execution");System.out.println("                   |");
            System.out.print("|");System.out.print("  2. List Planned Executions");System.out.println("                 |");
            System.out.print("|");System.out.print("  3. Run Tests (Simulate Monday)");System.out.println("             |");
            System.out.print("|");System.out.print("  4. Simulate Source Code Check-In Command");System.out.println("   |");
            System.out.print("|");System.out.print("  5. Report Results (Optional)");System.out.println("               |");
            System.out.print("|");System.out.print("  6. Clear All Scheduled Tests");System.out.println("               |");
            System.out.print("|");System.out.print("  7. Run AIX Network Unit Test");System.out.println("               |");
            System.out.print("|");System.out.print("  8. Run AIX GUI Unit Test");System.out.println("                   |");
            System.out.print("|");System.out.print("  9. Run macOS Network Unit Test");System.out.println("             |");
            System.out.print("|");System.out.print("  10. Run macOS GUI Unit Test");System.out.println("                |");
            System.out.print("|");System.out.print("  11. View Test Cases in macOS GUI Test Suite");System.out.println("|");
            System.out.print("|");System.out.print("  12. View Test Cases in AIX GUI Test Suite");System.out.println("  |");
            System.out.print("|");System.out.print("  13. View Test Logs");System.out.println("                         |");
            System.out.print("|");System.out.print("  14. Exit");System.out.println("                                   |");
            System.out.println("------------------------------------------------------");



            System.out.print("Choose an option [1-14]: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();// Consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> planTest();
                case 2 -> listPlannedTests();
                case 3 -> runTestsNow();
                case 4 -> new SourceCodeCheckInCommand().execute();
                case 5 -> System.out.println("Reporting is done automatically after source-code check-in.");
                case 6-> clearTests();
                case 7 -> new AIXTestFactory().createNetworkTest().run();
                case 8 -> new AIXTestFactory().createGUITest().run();
                case 9 -> new MacOSTestFactory().createNetworkTest().run();
                case 10-> new MacOSTestFactory().createGUITest().run();
                case 11 -> viewSuite(new MacOSTestSuiteFactory().createGUITestSuite());
                case 12 -> viewSuite(new AIXTestSuiteFactory().createGUITestSuite());
                case 13 -> LogViewer.printLogs();
                case 14 -> {
                    System.out.println("Exiting the Testing Framework System. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid selection. Try again.");
            }
        }
    }

    //User plan a new test execution by selecting OS and test type then suites will be added in queue
    private void planTest() {
        System.out.println("\n--- Plan a Test Execution ---");
        System.out.print("Select Platform (AIX/macOS): ");
        String platformInput = scanner.nextLine().trim().toLowerCase();

        if (!platformInput.equals("aix") && !platformInput.equals("macos")) {
            System.out.println("Invalid platform. Use 'AIX' or 'macOS'.");
            return;
        }

        System.out.print("Select Test Type (GUI/Network/All): ");
        String typeInput = scanner.nextLine().trim().toLowerCase();

        if (!typeInput.equals("gui") && !typeInput.equals("network") && !typeInput.equals("all")) {
            System.out.println("Invalid test type. Use 'GUI', 'Network', or 'All'.");
            return;
        }
        //AIX or macOS selection
        TestSuiteFactory factory = platformInput.equals("aix") ? new AIXTestSuiteFactory() : new MacOSTestSuiteFactory();
        TestSuite suite;
        // Convert platformInput to uppercase for consistency
        platformInput = platformInput.equals("aix") ? "AIX" : "MacOS";
        String description = platformInput + " - " + typeInput + " Test Execution";

        if (typeInput.equals("gui")) {
            suite = factory.createGUITestSuite();
        } else if (typeInput.equals("network")) {
            suite = factory.createNetworkTestSuite();
        } else {
            //GUI and Network Test Suites' Combination
            suite = new TestSuite(platformInput + " All Tests");
            for (TestComponent tc : factory.createGUITestSuite()) suite.add(tc);
            for (TestComponent tc : factory.createNetworkTestSuite()) suite.add(tc);
        }

        TestExecution execution = new TestExecution(description, platformInput, suite);
        if (typeInput.equals("gui")) execution.setExecuteGUITestsOnly(true);
        if (typeInput.equals("network")) execution.setExecuteNetworkTestsOnly(true);

        scheduler.scheduleExecution(execution);
        System.out.println("✔ Test execution successfully planned.");
    }

    private void listPlannedTests() { //List all test executions scheduled in the system till now.
        System.out.println("\n--- Planned Test Executions ---");
        List<TestExecution> executions = scheduler.getPendingExecutions();
        if (executions.isEmpty()) {
            System.out.println("No tests currently scheduled.");
        } else {
            for (int i = 0; i < executions.size(); i++) {
                System.out.println((i + 1) + ". " + executions.get(i).getDescription());
            }
        }
    }

    /**
     * Immediately executes all scheduled tests (simulating a Monday schedule).
     * Each test execution is wrapped in Command objects.
     */
    private void runTestsNow() {
        System.out.println("\n--- Running All Scheduled Tests (Simulated Monday) ---");
        List<TestExecution> copy = new ArrayList<>(scheduler.getPendingExecutions());
        for (TestExecution exec : copy) {
            TestInvoker invoker = new TestInvoker();
            invoker.addCommand(new TestExecutionCommand(exec));
            invoker.addCommand(new ReportingCommand(exec));
            invoker.executeAll();
        }
        scheduler.clearExecutions();
    }

    private void clearTests() { //Clear scheduled tests from scheduler
        scheduler.clearExecutions();
        System.out.println("✔ All scheduled tests have been cleared.");
    }

    //Display test cases inside a given TestSuite object that called suite
    private void viewSuite(TestSuite suite) {
        System.out.println("Test Suite: " + suite.getDescription());
        List<TestComponent> testCases = suite.getTests();
        if (testCases.isEmpty()) {
            System.out.println("No test cases found.");
        } else {
            int i = 1;
            for (TestComponent tc : testCases) {
                System.out.println(i++ + ". Test case:");
                tc.execute(); // Display each test's execution
            }
        }
    }
}

// Start here.
class Main implements CLIProcess {
    public static void main(String[] args) {
        CLIProcess tfs = new Main(); // Create instance of the CLI processor
        tfs.start(); //Start main menu
    }
}