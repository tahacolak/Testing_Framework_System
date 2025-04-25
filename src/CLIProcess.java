/**
 * @author: tahacolak
 * @since: 2025/04/17
 */

import java.util.*;

public interface CLIProcess {
    Scanner scanner = new Scanner(System.in);
    TestScheduler scheduler = TestScheduler.getInstance();
    TestExecutionCaretaker caretaker = new TestExecutionCaretaker();
    TestInvoker invoker = new TestInvoker();



    default void start() {
        System.out.println("***********************");
        TestManager.getInstance().startTestingCycle();
        System.out.println("***********************");
        boolean running = true;

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Plan a Test Execution");
            System.out.println("2. List Planned Executions");
            System.out.println("3. Run Tests (Monday Schedule)");
            System.out.println("4. Report Results");
            System.out.println("5. Restore Execution State");
            System.out.println("6. View Saved States");
            System.out.println("7. Clear All Scheduled Tests");
            System.out.println("8. Run AIX Network Unit Test");
            //RUN macOS Network Unit Test
            //RUN AIX GUI Unit Test will be added-taha
            System.out.println("9. Run macOS GUI Unit Test");
            System.out.println("10. Simulate Source Code Check-in Command");
            System.out.println("11. View Test Cases in macOS GUI Test Suite");//LogIn & Navigation Test in macOS
            System.out.println("12. View Test Cases in AIX GUI Test Suite");//LogIn & Navigation Test in AIX
            System.out.println("13. Exit");


            System.out.print("Choose a valid option from above [1-11]: ");

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
                case 3 -> runTests();
                case 4 -> reportResults();
                case 5 -> restoreTest();
                case 6 -> viewSavedStates();
                case 7 -> clearTests();
                case 8 -> {
                    NetworkTest test = new AIXTestFactory().createNetworkTest();
                    test.run();
                }
                case 9 -> {
                    GUITest test = new MacOSTestFactory().createGUITest();
                    test.run();
                }
                case 10 -> {
                    Command checkInCommand = new SourceCodeCheckInCommand();
                    checkInCommand.execute();
                }

                case 11 -> {
                    TestSuite suite = new MacOSTestSuiteFactory().createGUITestSuite();  // veya AIX de olabilir
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
                case 12 -> {
                    TestSuite suite = new AIXTestSuiteFactory().createGUITestSuite();
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
                case 13 -> {
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
            if (platformInput.equalsIgnoreCase("aix") || platformInput.equalsIgnoreCase("macos")) {
                System.out.println("Wrong writing style for platform. Use exact case: 'AIX' or 'macOS'.");
            } else {
                System.out.println("Unsupported platform. Please choose AIX or macOS.");
            }
            return;
        }

        System.out.print("Select Test Type (GUI/Network/All): ");
        String typeInput = scanner.nextLine().trim();

        if (!typeInput.equals("GUI") && !typeInput.equals("Network") && !typeInput.equals("All")) {
            if (typeInput.equalsIgnoreCase("gui") || typeInput.equalsIgnoreCase("network") || typeInput.equalsIgnoreCase("all")) {
                System.out.println("Wrong writing style for test type. Use exact case: 'GUI', 'Network', or 'All'.");
            } else {
                System.out.println("Invalid test type. Please enter GUI, Network, or All.");
            }
            return;
        }

        TestSuiteFactory factory = platformInput.equals("AIX") ? new AIXTestSuiteFactory() : new MacOSTestSuiteFactory();
        String platform = platformInput;

        TestSuite suite;
        String description = platform + " - " + typeInput + " Test Execution";
        if (typeInput.equals("GUI")) {
            suite = factory.createGUITestSuite();
        } else if (typeInput.equals("Network")) {
            suite = factory.createNetworkTestSuite();
        } else {
            suite = new TestSuite(platform + " All Tests");
            for (TestComponent tc : factory.createGUITestSuite()) {
                suite.add(tc);
            }
            for (TestComponent tc : factory.createNetworkTestSuite()) {
                suite.add(tc);
            }
        }

        TestExecution execution = new TestExecution(description, platform, suite);
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

    private void runTests() {
        System.out.println("\n--- Running All Scheduled Tests (Monday Schedule) ---");
        invoker.clear();
        for (TestExecution exec : scheduler.getPendingExecutions()) {
            invoker.addCommand(new TestExecutionCommand(exec));
            caretaker.saveState(exec.getDescription(), exec);
        }
        invoker.executeAll();
    }

    private void reportResults() {
        System.out.println("\n--- Reporting Test Results ---");
        invoker.clear();
        for (TestExecution exec : scheduler.getPendingExecutions()) {
            invoker.addCommand(new ReportingCommand(exec));
        }
        invoker.executeAll();
    }

    private void restoreTest() {
        System.out.print("Enter exact execution description to restore (as shown in list): ");
        String desc = scanner.nextLine();
        TestExecution restored = caretaker.getState(desc);
        if (restored != null) {
            System.out.println("✔ Test state successfully restored:");
            System.out.println("  Description: " + restored.getDescription());
            System.out.println("  Platform: " + restored.platform);
        } else {
            System.out.println("❌ No saved state found for: " + desc);
        }
    }

    private void viewSavedStates() {
        System.out.println("\n--- Saved Test Execution States ---");
        Map<String, TestExecution> states = caretaker.getAllStates();
        if (states.isEmpty()) {
            System.out.println("❌ No saved states found.");
        } else {
            int i = 1;
            for (Map.Entry<String, TestExecution> entry : states.entrySet()) {
                System.out.println(i++ + ". " + entry.getKey());
            }
        }
    }

    private void clearTests() {
        scheduler.clearExecutions();
        System.out.println("✔ All scheduled tests have been cleared.");
    }
}

class Main implements CLIProcess {
    public static void main(String[] args) {
    }
}
