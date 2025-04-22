package com.TestFrameworkSys;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestSuite implements TestComponent {
    private final String name;
    private final List<TestComponent> testComponents;

    public TestSuite(String name) {
        this.name = name;
        this.testComponents = new ArrayList<>();
    }

    public void addTestCase(TestComponent component) {
        testComponents.add(component);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void execute() {
        System.out.println("Executing Test Suite: " + name);
        for (TestComponent component : testComponents) {
            component.execute();
        }
    }

    @Override
    public TestResult getResult() {
        boolean allPassed = true;
        StringBuilder details = new StringBuilder();
        for (TestComponent component : testComponents) {
            TestResult result = component.getResult();
            if (result != null) {
                if (!result.isPassed()) allPassed = false;
                details.append(component.getName()).append(": ")
                        .append(result.isPassed() ? "PASSED" : "FAILED")
                        .append(" - ").append(result.getDetails()).append("\n");
            }
        }
        return new TestResult(allPassed, LocalDateTime.now(), details.toString());
    }

    public Iterator<TestComponent> getIterator() {
        return testComponents.iterator();
    }

    public Iterator<TestComponent> getGUITestIterator() {
        return new FilteringIterator<>(testComponents.iterator(),
                component -> component.getName().contains("GUI"));
    }

    public Iterator<TestComponent> getNetworkTestIterator() {
        return new FilteringIterator<>(testComponents.iterator(),
                component -> component.getName().contains("Network"));
    }
}
