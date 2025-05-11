package test_frame;

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

