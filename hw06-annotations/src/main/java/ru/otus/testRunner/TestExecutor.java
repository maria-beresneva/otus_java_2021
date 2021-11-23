package ru.otus.testRunner;

import ru.otus.testRunner.enums.TestStatus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

class TestExecutor {
    public static TestResult execute(
            List<Method> beforeMethods,
            List<Method> afterMethods,
            Method testMethod,
            Object testingClassObject) {
        TestResult testResult = beforeMethodResult(beforeMethods, testMethod, testingClassObject);

        testResult = testingMethodResult(testResult, testMethod, testingClassObject);

        testResult = afterMethodResult(testResult, afterMethods, testMethod, testingClassObject);

        return testResult;
    }

    private static String getExceptionAsString(Throwable exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    private static TestResult beforeMethodResult(
            List<Method> beforeMethods,
            Method testMethod,
            Object testingClassObject) {
        TestResult testResult = new TestResult(TestStatus.SUCCESS, testMethod.getName(), null);
        try {
            for (Method beforeMethod : beforeMethods) {
                beforeMethod.invoke(testingClassObject);
            }
        } catch (InvocationTargetException e) {
            testResult = new TestResult(
                    TestStatus.SKIPPED,
                    testMethod.getName(),
                    getExceptionAsString(e.getTargetException()));
        } catch (Exception e) {
            testResult = new TestResult(
                    TestStatus.SKIPPED,
                    testMethod.getName(),
                    getExceptionAsString(e));
        }
        return testResult;
    }

    private static TestResult testingMethodResult(
            TestResult testResult,
            Method testMethod,
            Object testingClassObject) {
        if (!testResult.getTestStatus().equals(TestStatus.SKIPPED)) {
            try {
                testMethod.invoke(testingClassObject);
            } catch (InvocationTargetException e) {
                testResult = new TestResult(
                        TestStatus.FAILED,
                        testMethod.getName(),
                        getExceptionAsString(e.getTargetException()));
            } catch (Exception e) {
                testResult = new TestResult(
                        TestStatus.FAILED,
                        testMethod.getName(),
                        getExceptionAsString(e));
            }
        }
    return testResult;
    }

    private static TestResult afterMethodResult(
            TestResult testResult,
            List<Method> afterMethods,
            Method testMethod,
            Object testingClassObject) {
        try {
            for (Method afterMethod : afterMethods) {
                afterMethod.invoke(testingClassObject);
            }
        } catch (InvocationTargetException e) {
            if (testResult.getTestStatus().equals(TestStatus.SUCCESS))
                testResult = new TestResult(
                        TestStatus.FAILED_ON_CLEANUP,
                        testMethod.getName(),
                        getExceptionAsString(e.getTargetException()));
        } catch (Exception e) {
            if (testResult.getTestStatus().equals(TestStatus.SUCCESS))
                testResult = new TestResult(
                        TestStatus.FAILED_ON_CLEANUP,
                        testMethod.getName(),
                        getExceptionAsString(e));
        }
        return testResult;
    }
}
