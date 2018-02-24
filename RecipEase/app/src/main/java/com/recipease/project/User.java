package com.recipease.project;

import java.util.ArrayList;

/**
 * Created by pdiddy on 2/19/2018.
 */

public class User {
//----------------------------------------------------------------------------
    // Fields //
    private String email, displayName, bio;
    private String uid;

//----------------------------------------------------------------------------
    // Public Methods //
    public User( String email, String bio, String displayName, String uid ) {
        this.email = email;
        this.bio = bio;
        this.displayName = displayName;
        this.uid = uid;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid( String uid ) {
        this.uid = uid;
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

}
