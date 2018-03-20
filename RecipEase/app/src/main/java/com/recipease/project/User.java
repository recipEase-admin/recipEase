package com.recipease.project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pdiddy on 2/19/2018.
 */

public class User {
//----------------------------------------------------------------------------
    // Fields //
    private String email, displayName, bio;
    private String uid;
    private List<Long> recipesOwned;

//----------------------------------------------------------------------------
    // Public Methods //


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

    public void setEmail( String newEmail ) {
        this.email = newEmail;
    }

    public List<Long> getRecipesOwned () {
        return this.recipesOwned;
    }

    public void setRecipesOwned (ArrayList<Long> recipesOwned) {
        this.recipesOwned = recipesOwned;
    }

}
