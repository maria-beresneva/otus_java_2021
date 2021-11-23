package ru.otus.testRunner;

import ru.otus.testRunner.enums.TestStatus;

import java.util.List;
import java.util.stream.Collectors;

class ReportGenerator {

    static void printTestReport(Class testingClass, List<TestResult> testResults) {
        List<TestResult> failedTests = testResults.stream()
                .filter(testResult -> testResult.getTestStatus().equals(TestStatus.FAILED))
                .collect(Collectors.toList());
        List<TestResult> skippedTests = testResults.stream()
                .filter(testResult -> testResult.getTestStatus().equals(TestStatus.SKIPPED))
                .collect(Collectors.toList());
        List<TestResult> failedOnCleanUpTests = testResults.stream()
                .filter(testResult -> testResult.getTestStatus().equals(TestStatus.FAILED_ON_CLEANUP))
                .collect(Collectors.toList());
        List<TestResult> passedTests = testResults.stream()
                .filter(testResult -> testResult.getTestStatus().equals(TestStatus.SUCCESS))
                .collect(Collectors.toList());

        StringBuilder testReport =
                new StringBuilder(String.format("\n###\nTests were run for the class %s\n", testingClass.getName()));

        testReport.append(String.format(
                "Total: %d\nPassed: %d\nFailed: %d\nSkipped: %d\n",
                testResults.size(),
                passedTests.size(),
                (failedTests.size() + failedOnCleanUpTests.size()),
                skippedTests.size()));

        if(passedTests.size() > 0) {
            testReport.append(generateSuccessTestReport(passedTests));
        }

        if (failedOnCleanUpTests.size() > 0) {
            testReport.append(generateIssuedTestReport(
                    "Tests passed but failed during After hook:\n",
                    failedOnCleanUpTests));
        }

        if (skippedTests.size() > 0) {
            testReport.append(generateIssuedTestReport(
                    "Tests skipped, because of failed during Before hook:\n",
                    skippedTests));
        }

        if(failedTests.size() > 0) {
            testReport.append(generateIssuedTestReport(
                    "Failed test names and failure reasons:\n",
                    failedTests));
        }

        System.out.println(testReport);
    }

    private static String generateSuccessTestReport(List<TestResult> passedTests) {
        StringBuilder successTestsSection = new StringBuilder("Successful test names:\n");
        List<String> passedTestsNames = passedTests.stream()
                .map(TestResult::getTestMethodName)
                .collect(Collectors.toList());

        successTestsSection.append(String.join(", ", passedTestsNames));
        successTestsSection.append("\n");
        return successTestsSection.toString();
    }

    private static String generateIssuedTestReport(String reportSectionHeader, List<TestResult> failedTests) {
        StringBuilder failedTestsSection = new StringBuilder(reportSectionHeader);
        failedTests.forEach(failedTest ->
                failedTestsSection.append(String.format(
                        "Test name: %s\nIssue:\n%s\n",
                        failedTest.getTestMethodName(),
                        failedTest.getErrorMessage()))
        );

        return failedTestsSection.toString();
    }
}
