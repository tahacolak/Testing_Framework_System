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
import java.util.List;
import java.util.ArrayList;

import static java.lang.Thread.sleep; // instead of Thread.sleep( ), this static method is used due to readibility and short-style

/**
 * Command interface for executing encapsulated actions.
 * Classes implementing this interface should define the specific behavior in execute().
 */
interface Command {
    void execute();//Executes the Command
}

//Once executed, it simulates committing code and notifies the TestManager.
class SourceCodeCheckInCommand implements Command {
    public void execute() {
        try {
            System.out.println("[Command] Committing source code to the repository...");
            sleep (2000); // Simulate time taken -> check in source code
            System.out.println("[Command] Source code check-in completed. Testing team may proceed.");
            TestManager.setIsCheckedIn(true); // Flag check-in completion
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

    //Executes the test by triggering the TestManager.
    public void execute() {
        TestManager.getInstance().startTestingCycle(execution);
    }
}

//Represents a command to generate a test execution report.
class ReportingCommand implements Command {
    private final TestExecution execution;

    /**
     * Constructs a ReportingCommand for a specific test execution.
     * @param execution The test execution whose results will be reported.
     */
    public ReportingCommand(TestExecution execution) {
        this.execution = execution;
    }

    //Executes the reporting process.
    public void execute() {
        execution.reportResults();
    }
}

//The invoker class responsible for storing and executing a list of commands.
class TestInvoker {
    private final List<Command> commands = new ArrayList<>();

    /**
     * Adds a command to the command queue.
     * @param c The command to be added.
     */    public void addCommand(Command c) {
        commands.add(c);
    }

    public void executeAll() {
        for (Command c : commands) c.execute();
    } //Executes each command in the queue


}

