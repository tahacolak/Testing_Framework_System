package com.TestFrameworkSys;

public class AIXTestSuiteFactory implements TestSuiteFactory {
    @Override
    public TestSuite createGUITestSuite() {
        TestSuite suite = new TestSuite("AIX GUI Test Suite");
        suite.addTestCase(new TestCase("AIX GUI Login Test"));
        suite.addTestCase(new TestCase("AIX GUI Navigation Test"));
        suite.addTestCase(new TestCase("AIX GUI Rendering Test"));
        return suite;
    }

    @Override
    public TestSuite createNetworkTestSuite() {
        TestSuite suite = new TestSuite("AIX Network Test Suite");
        suite.addTestCase(new TestCase("AIX Network Connection Test"));
        suite.addTestCase(new TestCase("AIX Network Throughput Test"));
        suite.addTestCase(new TestCase("AIX Network Security Test"));
        return suite;
    }
}
