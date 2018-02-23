package com.recipease.project;

import java.util.ArrayList;

/**
 * Created by pdiddy on 2/19/2018.
 */

public class User {
//----------------------------------------------------------------------------
    // Fields //
    private String email, displayName, bio;
    private int userID;

//----------------------------------------------------------------------------
    // Public Methods //
    public User( String emailAddress ) {
        System.out.println("Hello Android Class");
    }

    public User( int userID ) {
        if( userExists( userID ) ) {
            this.userID = userID;
        }
        else {
            // Throw error, user does no exist
        }

    }

    private boolean userExists( int userID ) {
        // Check if user is in database
        boolean userInDatabase = true;
        return userInDatabase;
    }

    public String getBio() {
        return this.bio;
    }

    public void setBio( String newBio ) {
        this.bio = newBio;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName( String newName ) {
        this.displayName = newName;
    }

    public String getEmail() {
        return this.email;
    }

    public void deleteAccount() {

    }

    public static void main(String [] args) {
        User user = new User("robillard123@gmail.com");
    }

//----------------------------------------------------------------------------
    // Private Methods //
}
