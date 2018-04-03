package com.recipease.project;

/**
 * Created by pdiddy on 3/16/2018.
 */

public class Filter {

    String[] profanityWords = {
            "fuck","shit","ass","cunt","nigger","bitch", "faggot", "asshole","goddamn",
            "damn", "pussy"
    };

    public Filter() {}

    public boolean containsProfanity( String inputToCheck ) {

        String[] splitInput = inputToCheck.split(" ");
        for(String singleWord : splitInput) {
            singleWord = singleWord.toLowerCase();
            if(hasProfanityWordContained(singleWord)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasProfanityWordContained(String input) {
        for(String curseWord : profanityWords) {
            curseWord = curseWord.toLowerCase();
            if(input.contains(curseWord))
                return true;
        }
        return false;
    }
}

