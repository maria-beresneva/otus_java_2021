package homework;

import ru.otus.testRunner.annotations.After;
import ru.otus.testRunner.annotations.Before;
import ru.otus.testRunner.annotations.Test;

public class TestsSkippedBeforeFail {
    @Before
    public void setUp() {
        System.out.println("Set up section");
        throw new IllegalArgumentException("Incorrect argument somewhere");
    }

    @After
    public void tearDawn1() {
        System.out.println("Tear dawn 1 section");
    }

    @After
    public void tearDawn2() {
        System.out.println("Tear dawn 2 section");
    }

    @Test
    public void skippedTest() {
        System.out.println("Test skipped.");
    }
}
