package com.recipease.project;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by pdiddy on 4/2/2018.
 */



public class FilterTests {

    Filter f = new Filter();

    String[] inputToFail = {
            "bitchMuffins",
            "asswaffles",
            "fuck_boi_brownies",
    };

    String[] inputToSucceed = {
            "Waffles n Fries",
            "ballin johns",
    };

    @Test
    public void doesContainProfanityChecks() throws Exception {
        for(String testString : inputToFail)
            assertEquals(f.containsProfanity(testString), true);
    }

    @Test
    public void noProfanityChecks() throws Exception {
        for(String testString : inputToSucceed)
            assertEquals(f.containsProfanity(testString), false);
    }
}
