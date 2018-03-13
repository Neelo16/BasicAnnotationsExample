package pt.ist.ap.labs;

import java.lang.reflect.Method;

public class RunTests {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.printf("Usage: java %s <testSuiteClassName>%n", RunTests.class.getName());
        } else {
            try {
                Class testClass = Class.forName(args[0]);
                TestRunner testRunner = new TestRunner();
                testRunner.runTests(testClass);
                int passed = testRunner.getPassed();
                int failed = testRunner.getFailed();
                System.out.printf("Passed: %d, Failed %d%n", passed, failed);
            } catch (ClassNotFoundException e) {
                System.err.printf("Class %s not found%n", args[1]);
            }
        }
    }
}
