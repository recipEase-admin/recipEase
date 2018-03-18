package com.recipease.project;

/**
 * Created by pdiddy on 3/16/2018.
 */

public class Filter {

    public Filter() {

    }

    public boolean containsProfanity( String inputToCheck ) {
        String[] profanityWords = {"fuck","shit","ass","cunt","nigger","bitch", "faggot", "asshole","goddamn","damn"};
        String[] splitInput = inputToCheck.split(" ");
        for(String curseWord : profanityWords) {
            for(String wordToCheck : splitInput) {
                if(curseWord.equals( wordToCheck )) {
                    return true;
                }
            }
        }
        return false;
    }

}
