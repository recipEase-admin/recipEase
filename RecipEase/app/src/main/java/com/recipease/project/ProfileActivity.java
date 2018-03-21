package com.recipease.project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    // TODO: 3/20/2018 Get user's bio, profile picture, and display name from database

    ArrayList<Recipe> favoriteRecipes, personalRecipes;

    String displayName, bio;
    String userID;
    EditText etBio, etDisplayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etDisplayName = findViewById(R.id.etDisplayName);
        etBio = findViewById(R.id.etBio);

        favoriteRecipes = new ArrayList<>();
        personalRecipes = new ArrayList<>();


        // Get current user's information
        bio = "balling out";
        displayName = "snoop_dawg_420";
        userID = "1234";

        // Set GUI fields to current user's information
        etDisplayName.setHint(displayName);
        etBio.setHint(bio);


    }



}