package test_frame;
import java.util.List;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

interface Command {
    void execute();
}

class SourceCodeCheckInCommand implements Command {
    public void execute() {
        try {
            System.out.println("[Command] Committing source code to the repository...");
            sleep (2000); // Simulate time taken to check in source code
            System.out.println("[Command] Source code check-in completed. Testing team may proceed.");
            TestManager.setIsCheckedIn(true);
        }
        catch (Exception e) {
            System.out.println("[Command] Error during source code check-in: " + e.getMessage());
        }
    }
}

class TestExecutionCommand implements Command {
    private final TestExecution execution;

    public TestExecutionCommand(TestExecution execution) {
        this.execution = execution;
    }

    public void execute() {
        TestManager.getInstance().startTestingCycle(execution);
    }
}

class ReportingCommand implements Command {
    private final TestExecution execution;

    public ReportingCommand(TestExecution execution) {
        this.execution = execution;
    }

    public void execute() {
        execution.reportResults();
    }
}

class TestInvoker {
    private final List<Command> commands = new ArrayList<>();

    public void addCommand(Command c) {
        commands.add(c);
    }

    public void executeAll() {
        for (Command c : commands) c.execute();
    }

    public void clear() {
        commands.clear();
    }
}

