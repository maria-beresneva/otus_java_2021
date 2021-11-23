package homework;

import ru.otus.testRunner.TestClassExecutor;

public class Tests {
    public static void main(String[] args) {
        TestClassExecutor.executeTestsInClass(TestsPassAndFailWithGoodBeforeAfter.class);
        System.out.println("____________________________________");
        TestClassExecutor.executeTestsInClass(TestsFailOnAfter.class);
        System.out.println("____________________________________");
        TestClassExecutor.executeTestsInClass(TestsSkippedBeforeFail.class);
        System.out.println("____________________________________");
    }
}
