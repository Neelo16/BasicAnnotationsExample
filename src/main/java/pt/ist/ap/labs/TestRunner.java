package pt.ist.ap.labs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;

public class TestRunner {
    private int passed = 0;
    private int failed = 0;
    private HashMap<String,Method> setupMethods = new HashMap<>();
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
            if (isTestMethod(m)) {
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
            if (isSetupMethod(m)) {
                setupMethods.put(m.getAnnotation(Setup.class).value(), m);
            }
        }
    }

    private boolean isStaticMethodNoArguments(Method m) {
        int mod = m.getModifiers();
        return Modifier.isStatic(mod) && m.getParameterCount() == 0;
    }

    private boolean isSetupMethod(Method m) {
        return m.isAnnotationPresent(Setup.class) && isStaticMethodNoArguments(m);
    }

    private boolean isTestMethod(Method m) {
        return m.isAnnotationPresent(Test.class) && isStaticMethodNoArguments(m);
    }

}
