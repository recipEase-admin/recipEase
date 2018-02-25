package com.recipease.project;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class BrowseRecipesActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference database_reference;

    private RecyclerView recyclerView;
    private ArrayList<Recipe> recipeList;
    RecipeAdapter recipeAdapter;

    //private ArrayList<String> serialRecipeList = (ArrayList<String>) getIntent().getSerializableExtra("recipes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        database = FirebaseDatabase.getInstance();
        database_reference = database.getReference();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeList = new ArrayList<>();

        //DataWrapper recipeDataWrapper = (DataWrapper) getIntent().getSerializableExtra("recipes");
        //recipeList = getIntent().getParcelableArrayListExtra("recipes");//recipeDataWrapper.getRecipes();

        recipeAdapter = new RecipeAdapter(this, recipeList);
        recyclerView.setAdapter(recipeAdapter);

        getAllRecipes(recipeAdapter, recipeList);

        //Set results TextView
        TextView resultText = findViewById(R.id.resultText);
        resultText.setText(String.format("%d Results", 5)); //hardcoded for now since size() doesn't seem to work

    }

    //Returns a list of all recipes
    public void getAllRecipes(final RecipeAdapter recipeAdapter, final ArrayList<Recipe> recipeList) {
        // Read recipes in from the database and convert them to an ArrayList of Recipe objects
        database_reference.child("recipes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot all_recipes) {
                //Loop through each separate recipe
                for (DataSnapshot single_recipe : all_recipes.getChildren()) {
                    //Create a new recipe object
                    Recipe recipe = single_recipe.getValue(Recipe.class);
                    //populateRecipe(new_recipe, single_recipe);
                    //Adds this new recipe to the recipe arraylist
                    recipeList.add(recipe);
                }
                //Asynchronous so have to use this to notify adapter when finished
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "onCancelled", databaseError.toException());
            }
        });
        return;

    }



}