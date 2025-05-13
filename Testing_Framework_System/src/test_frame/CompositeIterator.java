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

import java.util.*;

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

