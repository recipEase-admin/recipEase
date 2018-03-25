package com.recipease.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDrawerList = (ListView)findViewById(R.id.navList);

        String[] osArray = { "Profile", "Create A Recipe", "Personal Recipes", "About", "Logout" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.bringToFront();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent goToProfile = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(goToProfile);
                        break;
                    case 1:
                        Intent goToCreateRecipe = new Intent(HomeActivity.this, CreateRecipeActivity.class);
                        startActivity(goToCreateRecipe);
                        break;
                    case 2:
                        Intent goToPersonalRecipes = new Intent(HomeActivity.this, PersonalRecipesActivity.class);
                        startActivity(goToPersonalRecipes);
                        break;
                    case 3:
                        Intent goToAbout = new Intent ( HomeActivity.this, AboutActivity.class);
                        startActivity(goToAbout);
                        break;
                    case 4: //Logout
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(HomeActivity.this, "Successfully logged out", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(i);
                        break;
                }
            }
        });
    }

    public void goToFindRecipesPage( View v ) {
        Intent i = new Intent(HomeActivity.this, IngredientSelector.class);
        startActivity(i);
    }

    public void goToIngredientSelector(View v){
        Intent i = new Intent(HomeActivity.this, IngredientSelector.class);
        startActivity(i);
    }

}
