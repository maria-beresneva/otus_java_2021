package homework;

import ru.otus.testRunner.annotations.After;
import ru.otus.testRunner.annotations.Before;
import ru.otus.testRunner.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestsPassAndFailWithGoodBeforeAfter {
    @Before
    public void setUp1() {
        System.out.println("Set up 1 section");
    }

    @Before
    public void setUp2() {
        System.out.println("Set up 2 section");
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
    public void passedTest1() {
        System.out.println("Everything is good. Test1 passed.");
    }

    @Test
    public void passedTest2() {
        System.out.println("Everything is good. Test2 passed.");
    }

    @Test
    public void failedTest3() {
        System.out.println("Something went wrong. Test3 failed.");
        throw new IllegalArgumentException("test3 has incorrect argument somewhere");
    }

    @Test
    public void failedTest4() {
        System.out.println("Something went wrong. Test4 failed.");
        assertThat(1)
                .as("This will always fail")
                .isZero();
    }
}
