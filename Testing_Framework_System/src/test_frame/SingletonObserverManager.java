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
import java.util.*;
import java.io.*;
import java.nio.file.*;

/**
 * Singleton class responsible for managing the test lifecycle.
 * It ensures source code is checked in before execution and schedules automatic weekly test runs.
 */
class TestManager {
    private static TestManager instance;
    private final Timer timer = new Timer(); // Timer to schedule test executions every Monday
    private static boolean isCheckedIn = false; // Flag indicating whether the source code has been checked in
    private static TestExecutionState testExecutionState; // Subject for observer pattern (state updates)

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes observer pattern and sets up scheduled testing.
     */
    private TestManager() {
        scheduleMondayTests();
        testExecutionState = new TestExecutionState();
        testExecutionState.attach(new TestObserver("Project Manager"));
        testExecutionState.attach(new TestObserver("Test Lead"));
        testExecutionState.attach(new TestObserver("QA Team"));
    }

    public static TestManager getInstance() {
        if (instance == null) instance = new TestManager();
        return instance;
    }

    public static void setIsCheckedIn(boolean isCheckedIn) {
        TestManager.isCheckedIn = isCheckedIn;
    }

    public void startTestingCycle(TestExecution execution) {
        // Check if the source code is checked in. No testing can be done without the code being checked in.
        if (!isCheckedIn) {
            System.out.println("[Manager] Source code not checked in. Cannot start testing cycle.");
            return;
        }
        System.out.println("[Manager] Starting testing cycle...");
        execution.executeTests();
        saveResultToJson(execution);
        System.out.println("[Manager] Testing cycle completed.");
        testExecutionState.setState("Test cycle completed.");
        // Reset the check-in status for the next cycle
        isCheckedIn = false;
    }

    //Uses Java's built-in Timer and TimerTask; like serializable Calendar instance methods
    private void scheduleMondayTests() {
        /* Schedule the tests to run every Monday at 09:00.
        *  Causes a side effect where the code will consider itself late when starting the program and execute all tests
        *  simultaneously with the first time the run all tests command is given, resulting in the first test run happening twice.
         */
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date firstTime = calendar.getTime();
        if (firstTime.before(new Date())) {
            calendar.add(Calendar.DATE, 7);
            // This reassignment is a workaround to fix that.
            firstTime = calendar.getTime();
        }

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println("[Scheduler] Monday test run triggered.");
                List<TestExecution> copy = new ArrayList<>(TestScheduler.getInstance().getPendingExecutions());
                for (TestExecution e : copy) {
                    startTestingCycle(e);
                }
                TestScheduler.getInstance().clearExecutions();
            }
        }, firstTime, 7 * 24 * 60 * 60 * 1000);// Repeat weekly
    }

    //Saves execution results as JSON entries in a local file.

    private void saveResultToJson(TestExecution execution) {//The execution whose result should be logged.
        try {
            Date date=new Date();
            String filename = "test_log.json";
            String logEntry = String.format(
                    "{ \"description\": \"%s\", \"platform\": \"%s\", \"timestamp\": \"%s\" }",
                    escapeJson(execution.getDescription()),
                    escapeJson(execution.platform),
                    date
            );

            List<String> lines = new ArrayList<>();
            if (Files.exists(Paths.get(filename))) {
                lines = Files.readAllLines(Paths.get(filename));
            }

            if (lines.isEmpty()) {
                lines.add("[");
            } else if (lines.size() > 1) {
                String last = lines.get(lines.size() - 1).trim();
                if (last.equals("]")) lines.remove(lines.size() - 1);
                else if (last.endsWith("]")) lines.set(lines.size() - 1, last.substring(0, last.length() - 1));
                lines.set(lines.size() - 1, lines.get(lines.size() - 1) + ",");
            }

            lines.add("  " + logEntry);
            lines.add("]");

            Files.write(Paths.get(filename), lines);
        } catch (IOException e) {
            System.err.println("⚠ Error writing log: " + e.getMessage());
        }
    }

    /**
     * Escapes special characters for safe JSON formatting.
     * @param input The input string.
     * @return Escaped string for JSON.
     */
    private String escapeJson(String input) {
        return input.replace("\"", "\\\"");
    }
}

interface Observer { //Called when the subject's state changes.
    void update();
}

class TestObserver implements Observer {
    // Implementing observers with only a name for the sake of simplicity. This could be extended to have more functionality.
    private final String name;

    public TestObserver(String name) {
        this.name = name;
    }

    public void update() {
        System.out.println("[Observer] " + name + " has been notified.");
    }
}

abstract class StateSubject {
    private final List<Observer> observers = new ArrayList<>();

    public void attach(Observer observer) {
        observers.add(observer);
    }//Attaches a new observer to the subject.

    // detach(), despite not being used here, should be implemented for completeness.
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    //Notifies all observers of a state change.
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
//Concrete subject class representing the state of a test execution cycle.
class TestExecutionState extends StateSubject {
    private String state;

    public void setState(String state) {
        this.state = state;
        notifyObservers(); //Notifies observers when state is changed.
    }

    // A getter for the state, in case we need to check the state later.
    public String getState() {
        return state;
    }
}


class TestScheduler {
    private static TestScheduler instance;
    private final List<TestExecution> pendingExecutions = new ArrayList<>();

    private TestScheduler() {}

    //return the Singleton instance of TestScheduler.
    public static TestScheduler getInstance() {
        if (instance == null) instance = new TestScheduler();
        return instance;
    }

    /**
     * Schedules a new test execution to be run on the next cycle.
     * @param execution The test to be scheduled.
     */
    public void scheduleExecution(TestExecution execution) {
        pendingExecutions.add(execution);
    }

    /**
     * Gets the list of all currently scheduled test executions.
     * @return A list of TestExecution objects.
     */
    public List<TestExecution> getPendingExecutions() {
        return pendingExecutions;
    }

    //Clears all scheduled test executions.
    public void clearExecutions() {
        pendingExecutions.clear();
    }
}