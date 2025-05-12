package test_frame;

/**
 * Abstract Factory interface for creating GUI and Network tests.
 * @author tahacolak
 */
interface TestCaseFactory {
    GUITest createGUITest();
    NetworkTest createNetworkTest();
}

//Concrete Factory for AIX-TestCases
class AIXTestFactory implements TestCaseFactory {
    public GUITest createGUITest() {
        return new AIXGUITest();
    }//AIX-GUITest

    public NetworkTest createNetworkTest() {
        return new AIXNetworkTest();
    }//AIX-NetworkTest
}

//Concrete Factory for macOS-TestCases
class MacOSTestFactory implements TestCaseFactory {
    public GUITest createGUITest() {
        return new MacOSGUITest();
    }

    public NetworkTest createNetworkTest() {
        return new MacOSNetworkTest();
    }
}

//Factory Interface: for creating test suites
interface TestSuiteFactory {
    TestSuite createGUITestSuite();
    TestSuite createNetworkTestSuite();
}

//Concrete Factory, creates test suites for AIX
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

//Concrete Factory, creates test suites for MACos
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

interface GUITest { //represent GUITest
    void run();
}

interface NetworkTest { //represent NetworkTest
    void run();
}

//GUITest interface implementation for AIX platform
class AIXGUITest implements GUITest {
    public void run() {
        System.out.println("[GUITest] Running AIX GUI Test");
    }
}
//NetworkTest interface implementation for AIX platform
class AIXNetworkTest implements NetworkTest {
    public void run() {
        System.out.println("[NetworkTest] Running AIX Network Test");
    }
}

//GUITest interface implementation for macOS platform
class MacOSGUITest implements GUITest {
    public void run() {
        System.out.println("[GUITest] Running macOS GUI Test");
    }
}
//NetworkTest interface implementation for macOS platform
class MacOSNetworkTest implements NetworkTest {
    public void run() {
        System.out.println("[NetworkTest] Running macOS Network Test");
    }
}

