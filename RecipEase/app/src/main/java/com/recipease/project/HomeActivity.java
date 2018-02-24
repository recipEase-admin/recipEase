package com.recipease.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void goToFindRecipesPage( View v ) {
        Intent i = new Intent(HomeActivity.this, BrowseRecipesActivity.class);
        startActivity(i);
    }

    public void goToIngredientSelector(View v){
        Intent i = new Intent(HomeActivity.this, IngredientSelector.class);
        startActivity(i);
    }

}
