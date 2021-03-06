package ru.otus.testRunner;

import ru.otus.testRunner.annotations.After;
import ru.otus.testRunner.annotations.Before;
import ru.otus.testRunner.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestClassExecutor {
    public static void executeTestsInClass(Class testingClass) {
        Object testClassObject;
        List<Method> beforeMethods = extractAnnotatedMethods(testingClass.getDeclaredMethods(), Before.class);
        List<Method> afterMethods = extractAnnotatedMethods(testingClass.getDeclaredMethods(), After.class);
        List<Method> testMethods = extractAnnotatedMethods(testingClass.getDeclaredMethods(), Test.class);
        List<TestResult> testResults = new ArrayList<>();

        testClassObject = createObject(testingClass);

        testMethods.forEach(testMethod -> {
           final TestResult testResult =
                   TestExecutor.execute(beforeMethods, afterMethods, testMethod, testClassObject);
           testResults.add(testResult);
        });

        ReportGenerator.printTestReport(testingClass, testResults);
    }

    private static List<Method> extractAnnotatedMethods(
            Method[] classMethods,
            Class<? extends Annotation> annotationClass) {
        List<Method> annotatedMethods = new ArrayList<>();
        for (Method method : classMethods) {
            if (method.isAnnotationPresent(annotationClass)) {
                if (method.getParameterCount() != 0)
                    throw new IllegalArgumentException(String.format(
                            "Method %s can not be annotated with %s",
                            method,
                            annotationClass));

                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }

    private static Object createObject(Class testingClass) {
        try {
            return testingClass.getConstructor().newInstance();
        } catch (NoSuchMethodException
                | SecurityException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException e) {
            throw new IllegalArgumentException(String.format("Failed to initialize class %s. It should have default " +
                    "public constructor without arguments", testingClass));
        }
    }
}
