package com.thoughtworks.bbs.util;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class ViolationCheckerTest {
    private ViolationChecker checker;

    @Before
    public void setup(){
        checker = ViolationChecker.getInstance();
    }

    @Test
    public void shouldReturnTrueWhenContainsNonViolationWords(){
        String content = "Hello, world.我们是中国人。";
        boolean result = checker.contentLegal(content);
        assertThat(result, is(true));
    }

    @Test
    public void shouldReturnFalseWhenContainsWordsNotPermitted(){
        String content = "I love 法轮功";
        boolean result = checker.contentLegal(content);
        assertFalse(result);
    }
}
