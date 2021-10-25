package homework;

import ru.otus.testRunner.annotations.After;
import ru.otus.testRunner.annotations.Before;
import ru.otus.testRunner.annotations.Test;

public class TestsFailOnAfter {
    @Before
    public void setUp() {
        System.out.println("Before test 1 section");
    }

    @After
    public void tearDawn1() {
        System.out.println("Tear dawn 1 section that fails");
        throw new IllegalArgumentException("Incorrect argument somewhere");
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
}
