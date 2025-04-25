import java.util.*;

// Singleton Pattern
public class TestManager {
    private static TestManager instance;
    private TestManager() {}
    public static TestManager getInstance() {
        if (instance == null) instance = new TestManager();
        return instance;
    }
    public void startTestingCycle() {
        System.out.println("[Manager] Starting testing cycle...");
    }
}

// Command Pattern
interface Command {
    void execute();
}

class SourceCodeCheckInCommand implements Command {
    public void execute() {
        System.out.println("[Command] Source code checked in.");
    }
}

class TestExecutionCommand implements Command {
    private final TestExecution execution;
    public TestExecutionCommand(TestExecution execution) {
        this.execution = execution;
    }
    public void execute() {
        execution.checkInSourceCode();
        execution.executeTests();
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

// Abstract Factory Pattern
interface TestCaseFactory {
    GUITest createGUITest();
    NetworkTest createNetworkTest();
}

class AIXTestFactory implements TestCaseFactory {
    public GUITest createGUITest() {
        return new AIXGUITest();
    }
    public NetworkTest createNetworkTest() {
        return new AIXNetworkTest();
    }
}

class MacOSTestFactory implements TestCaseFactory {
    public GUITest createGUITest() {
        return new MacOSGUITest();
    }
    public NetworkTest createNetworkTest() {
        return new MacOSNetworkTest();
    }
}

// Composite + Iterator Pattern
abstract class TestComponent {
    public void add(TestComponent test) {}
    public abstract void execute();
}

class TestCase extends TestComponent {
    private final String name;
    public TestCase(String name) {
        this.name = name;
    }
    public void execute() {
        System.out.println("[TestCase] Executing: " + name);
    }
}

class TestSuite extends TestComponent implements Iterable<TestComponent> {
    private final String description;
    private final List<TestComponent> tests = new ArrayList<>();
    public TestSuite(String description) {
        this.description = description;
    }
    public void add(TestComponent test) {
        tests.add(test);
    }
    public void execute() {
        System.out.println("[TestSuite] Executing: " + description);
        for (TestComponent test : tests) test.execute();
    }
    public Iterator<TestComponent> iterator() {
        return tests.iterator();
    }
    public String getDescription() {
        return description;
    }
    public List<TestComponent> getTests() {
        return tests;
    }
}

// Test Types
interface GUITest {
    void run();
}

interface NetworkTest {
    void run();
}



class AIXGUITest implements GUITest {
    public void run() {
        System.out.println("[GUITest] Running AIX GUI Test");
    }
}

class AIXNetworkTest implements NetworkTest {
    public void run() {
        System.out.println("[NetworkTest] Running AIX Network Test");
    }
}

class MacOSGUITest implements GUITest {
    public void run() {
        System.out.println("[GUITest] Running macOS GUI Test");
    }
}

class MacOSNetworkTest implements NetworkTest {
    public void run() {
        System.out.println("[NetworkTest] Running macOS Network Test");
    }
}


class TestExecution {
    private final String description;
    String platform;
    private final TestSuite suite;
    public boolean executeGUITestsOnly = false;
    public boolean executeNetworkTestsOnly = false;

    public TestExecution(String description, String platform, TestSuite suite) {
        this.description = description;
        this.platform = platform;
        this.suite = suite;
    }

    public void checkInSourceCode() {
        System.out.println("[Execution] Checking in source code...");
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
    }

    public void setExecuteNetworkTestsOnly(boolean value) {
        this.executeNetworkTestsOnly = value;
    }

    public String getDescription() {
        return description;
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

class TestExecutionCaretaker {
    private final Map<String, TestExecution> savedStates = new HashMap<>();

    public void saveState(String name, TestExecution state) {
        savedStates.put(name, state);
    }

    public TestExecution getState(String name) {
        return savedStates.get(name);
    }

    public Map<String, TestExecution> getAllStates() {
        return Collections.unmodifiableMap(savedStates);
    }
}

interface TestSuiteFactory {
    TestSuite createGUITestSuite();
    TestSuite createNetworkTestSuite();
}

class AIXTestSuiteFactory implements TestSuiteFactory {
    public TestSuite createGUITestSuite() {
        TestSuite suite = new TestSuite("AIX GUI Test Suite");
        suite.add(new TestCase("AIX GUI Login Test"));
        suite.add(new TestCase("AIX GUI Navigation Test"));
        return suite;
    }

    public TestSuite createNetworkTestSuite() {
        TestSuite suite = new TestSuite("AIX Network Test Suite");
        suite.add(new TestCase("AIX Network Connectivity Test"));
        suite.add(new TestCase("AIX Network Latency Test"));
        return suite;
    }
}

class MacOSTestSuiteFactory implements TestSuiteFactory {
    public TestSuite createGUITestSuite() {
        TestSuite suite = new TestSuite("macOS GUI Test Suite");
        suite.add(new TestCase("macOS GUI Login Test"));
        suite.add(new TestCase("macOS GUI Navigation Test"));
        return suite;
    }

    public TestSuite createNetworkTestSuite() {
        TestSuite suite = new TestSuite("macOS Network Test Suite");
        suite.add(new TestCase("macOS Network Speed Test"));
        suite.add(new TestCase("macOS Network Security Test"));
        return suite;
    }
}
