package com.TestFrameworkSys;

public interface TestSuiteFactory {
    TestSuite createGUITestSuite();
    TestSuite createNetworkTestSuite();
}
