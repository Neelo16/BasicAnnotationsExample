package pt.ist.ap.labs;

import java.lang.reflect.Method;

public class RunTests {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.printf("Usage: java %s <testSuiteClassName>%n", RunTests.class.getName());
        } else {
            try {
                Class testClass = Class.forName(args[0]);
                int passed = 0;
                int failed = 0;
                for (Method m: testClass.getDeclaredMethods()) {
                    if (m.isAnnotationPresent(Test.class)) {
                        try {
                            m.setAccessible(true);
                            m.invoke(null);
                            ++passed;
                            System.out.printf("Test %s OK!%n", m.toGenericString());
                        } catch (Throwable e) {
                            System.out.printf("Test %s failed%n", m.toGenericString());
                            ++failed;
                        }
                    }
                }
                System.out.printf("Passed: %d, Failed %d%n", passed, failed);
            } catch (ClassNotFoundException e) {
                System.err.printf("Class %s not found%n", args[1]);
            }
        }
    }
}
