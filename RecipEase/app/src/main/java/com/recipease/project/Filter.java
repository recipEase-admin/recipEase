package com.recipease.project;

/**
 * Created by pdiddy on 3/16/2018.
 */

public class Filter {

    public Filter() {

    }

    public boolean containsProfanity( String inputToCheck ) {
        String[] profanityWords = {"fuck","shit","ass","cunt","nigger","bitch", "faggot", "asshole","goddamn","damn"};

        for(String curseWord : profanityWords) {
            if(curseWord.equals( inputToCheck )) {
                return true;
            }
        }
        return false;
    }

}
