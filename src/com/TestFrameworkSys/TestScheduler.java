/**
 * @author:tahacolak
 * @since 2025/04/24
 */
package com.TestFrameworkSys;
import java.util.ArrayList;
import java.util.List;

/**
 * This class created with Singleton Design Pattern
 * for scheduling and managing test executions in the system.
 */
public class TestScheduler {
    //Global Instance
    private static TestScheduler instance;

    private final List<TestExecution> pendingExecutions;

    private TestScheduler() {
        pendingExecutions = new ArrayList<>();
    }
    public static synchronized TestScheduler getInstance() {
        if (instance == null) {
            instance = new TestScheduler();
        }
        return instance;
    }
    public void scheduleExecution(TestExecution execution) {
        pendingExecutions.add(execution);
        System.out.println("Test execution scheduled for next Monday: " + execution.getDescription());
    }

    public List<TestExecution> getPendingExecutions() {
        return pendingExecutions;
    }
    public void clearExecutions() {
        pendingExecutions.clear();
    }
}
