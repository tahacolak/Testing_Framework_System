package com.TestFrameworkSys;


import java.time.LocalDateTime;
import java.util.Random;

public class TestCase implements TestComponent {
    private final String name;
    private TestResult result;

    public TestCase(String name) {
        this.name = name;
        this.result = null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void execute() {
        Random random = new Random();
        boolean passed = random.nextBoolean();
        result = new TestResult(passed, LocalDateTime.now(),
                passed ? "Test passed successfully" : "Test failed with errors");

        System.out.println("Executing test case: " + name + " - " +
                (passed ? "PASSED" : "FAILED"));
    }

    @Override
    public TestResult getResult() {
        return result;
    }
}

