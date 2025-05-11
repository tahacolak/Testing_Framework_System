package test_frame;
import java.util.*;
import java.io.*;
import java.nio.file.*;

class TestManager {
    private static TestManager instance;
    private final Timer timer = new Timer();
    private static boolean isCheckedIn = false;
    private static TestExecutionState testExecutionState;

    private TestManager() {
        scheduleMondayTests();
        testExecutionState = new TestExecutionState();
        testExecutionState.attach(new TestObserver("Project Manager"));
        testExecutionState.attach(new TestObserver("Test Lead"));
        testExecutionState.attach(new TestObserver("QA Team"));

        testExecutionState.setState("Test cycle started.");
    }

    public static TestManager getInstance() {
        if (instance == null) instance = new TestManager();
        return instance;
    }

    public static void setIsCheckedIn(boolean isCheckedIn) {
        TestManager.isCheckedIn = isCheckedIn;
    }

    public void startTestingCycle(TestExecution execution) {
        if (!isCheckedIn) {
            System.out.println("[Manager] Source code not checked in. Cannot start testing cycle.");
            return;
        }
        System.out.println("[Manager] Starting testing cycle...");
        execution.executeTests();
        execution.reportResults();
        saveResultToJson(execution);
        System.out.println("[Manager] Testing cycle completed.");
        testExecutionState.setState("Test cycle completed.");
        // Reset the check-in status for the next cycle
        isCheckedIn = false;
    }

    private void scheduleMondayTests() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date firstTime = calendar.getTime();
        if (firstTime.before(new Date())) calendar.add(Calendar.DATE, 7);

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println("[Scheduler] Monday test run triggered.");
                List<TestExecution> copy = new ArrayList<>(TestScheduler.getInstance().getPendingExecutions());
                for (TestExecution e : copy) {
                    startTestingCycle(e);
                }
                TestScheduler.getInstance().clearExecutions();
            }
        }, firstTime, 7 * 24 * 60 * 60 * 1000);
    }

    private void saveResultToJson(TestExecution execution) {
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

    private String escapeJson(String input) {
        return input.replace("\"", "\\\"");
    }
}

interface Observer {
    void update();
}

class TestObserver implements Observer {
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
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}

class TestExecutionState extends StateSubject {
    private String state;

    public void setState(String state) {
        this.state = state;
        notifyObservers();
    }

    public String getState() {
        return state;
    }
}


class TestScheduler {
    private static TestScheduler instance;
    private final List<TestExecution> pendingExecutions = new ArrayList<>();

    private TestScheduler() {}

    public static TestScheduler getInstance() {
        if (instance == null) instance = new TestScheduler();
        return instance;
    }

    public void scheduleExecution(TestExecution execution) {
        pendingExecutions.add(execution);
    }

    public List<TestExecution> getPendingExecutions() {
        return pendingExecutions;
    }

    public void clearExecutions() {
        pendingExecutions.clear();
    }
}