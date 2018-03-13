package pt.ist.ap.labs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class TestRunner {
    private int passed = 0;
    private int failed = 0;
    private LinkedHashMap<String,Method> setupMethods = new LinkedHashMap<>();
    public int getPassed() {
        return passed;
    }

    public int getFailed() {
        return failed;
    }

    public void runTests(Class testClass) {
        Class superclass = testClass.getSuperclass();
        if (superclass != null) {
            runTests(superclass);
        }
        collectSetupMethods(testClass);
        for (Method m: testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Test.class)) {
                try {
                    runSetupMethods(m.getAnnotation(Test.class).value());
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

    private void runSetupMethods(String[] value) throws InvocationTargetException, IllegalAccessException {
        if (Arrays.stream(value).anyMatch((s) -> s.equals("*"))) {
            for (Method m: setupMethods.values()) {
                m.setAccessible(true);
                m.invoke(null);
            }
        } else {
            for (String name: value) {
                Method m = setupMethods.get(name);
                m.setAccessible(true);
                m.invoke(null);
            }
        }
    }

    private void collectSetupMethods(Class testClass) {
        for (Method m: testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Setup.class)) {
                setupMethods.put(m.getName(), m);
            }
        }
    }
}
