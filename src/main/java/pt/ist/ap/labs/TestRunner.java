package pt.ist.ap.labs;

import java.lang.reflect.Method;

public class TestRunner {
    private int passed = 0;
    private int failed = 0;

    public int getPassed() {
        return passed;
    }

    public int getFailed() {
        return failed;
    }

    public void runTests(Class testClass) {
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
    }
}
