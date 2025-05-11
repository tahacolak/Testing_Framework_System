package test_frame;

import java.util.*;

interface CLIProcess {
    Scanner scanner = new Scanner(System.in);
    TestScheduler scheduler = TestScheduler.getInstance();

    default void start() {
        System.out.println("***********************");
        System.out.println("TESTING FRAMEWORK SYSTEM: Scheduler will run every Monday at 09:00");
        System.out.println("***********************");
        boolean running = true;

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Plan a Test Execution");
            System.out.println("2. List Planned Executions");
            System.out.println("3. Run Tests (Simulate Monday)");
            System.out.println("4. Report Results (No longer required)");
            System.out.println("5. Clear All Scheduled Tests");
            System.out.println("6. Run AIX Network Unit Test");
            System.out.println("7. Run macOS GUI Unit Test");
            System.out.println("8. Simulate Source Code Check-in Command");
            System.out.println("9. View Test Cases in macOS GUI Test Suite");
            System.out.println("10. View Test Cases in AIX GUI Test Suite");
            System.out.println("11. View Test Logs");
            System.out.println("12. Exit");

            System.out.print("Choose an option [1-11]: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> planTest();
                case 2 -> listPlannedTests();
                case 3 -> runTestsNow();
                case 4 -> System.out.println("Reporting is done automatically after tests now.");
                case 5 -> clearTests();
                case 6 -> new AIXTestFactory().createNetworkTest().run();
                case 7 -> new MacOSTestFactory().createGUITest().run();
                case 8 -> new SourceCodeCheckInCommand().execute();
                case 9 -> viewSuite(new MacOSTestSuiteFactory().createGUITestSuite());
                case 10 -> viewSuite(new AIXTestSuiteFactory().createGUITestSuite());
                case 11 -> LogViewer.printLogs();
                case 12 -> {
                    System.out.println("Exiting the system. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid selection. Try again.");
            }
        }
    }

    private void planTest() {
        System.out.println("\n--- Plan a Test Execution ---");
        System.out.print("Select Platform (AIX/macOS): ");
        String platformInput = scanner.nextLine().trim();

        if (!platformInput.equals("AIX") && !platformInput.equals("macOS")) {
            System.out.println("Invalid platform. Use 'AIX' or 'macOS'.");
            return;
        }

        System.out.print("Select Test Type (GUI/Network/All): ");
        String typeInput = scanner.nextLine().trim();

        if (!typeInput.equals("GUI") && !typeInput.equals("Network") && !typeInput.equals("All")) {
            System.out.println("Invalid test type. Use 'GUI', 'Network', or 'All'.");
            return;
        }

        TestSuiteFactory factory = platformInput.equals("AIX") ? new AIXTestSuiteFactory() : new MacOSTestSuiteFactory();
        TestSuite suite;
        String description = platformInput + " - " + typeInput + " Test Execution";

        if (typeInput.equals("GUI")) {
            suite = factory.createGUITestSuite();
        } else if (typeInput.equals("Network")) {
            suite = factory.createNetworkTestSuite();
        } else {
            suite = new TestSuite(platformInput + " All Tests");
            for (TestComponent tc : factory.createGUITestSuite()) suite.add(tc);
            for (TestComponent tc : factory.createNetworkTestSuite()) suite.add(tc);
        }

        TestExecution execution = new TestExecution(description, platformInput, suite);
        if (typeInput.equals("GUI")) execution.setExecuteGUITestsOnly(true);
        if (typeInput.equals("Network")) execution.setExecuteNetworkTestsOnly(true);

        scheduler.scheduleExecution(execution);
        System.out.println("✔ Test execution successfully planned.");
    }

    private void listPlannedTests() {
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

    private void runTestsNow() {
        System.out.println("\n--- Running All Scheduled Tests (Simulated Monday) ---");
        List<TestExecution> copy = new ArrayList<>(scheduler.getPendingExecutions());
        for (TestExecution exec : copy) {
            TestInvoker invoker = new TestInvoker();
            invoker.addCommand(new SourceCodeCheckInCommand());
            invoker.addCommand(new TestExecutionCommand(exec));
            invoker.addCommand(new ReportingCommand(exec));
            invoker.executeAll();
        }
        scheduler.clearExecutions();
    }

    private void clearTests() {
        scheduler.clearExecutions();
        System.out.println("✔ All scheduled tests have been cleared.");
    }

    private void viewSuite(TestSuite suite) {
        System.out.println("Test Suite: " + suite.getDescription());
        List<TestComponent> testCases = suite.getTests();
        if (testCases.isEmpty()) {
            System.out.println("No test cases found.");
        } else {
            int i = 1;
            for (TestComponent tc : testCases) {
                System.out.println(i++ + ". Test case:");
                tc.execute();
            }
        }
    }
}

class Main implements CLIProcess {
    public static void main(String[] args) {
        CLIProcess tfs = new Main();
        tfs.start();
    }
}