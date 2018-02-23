package com.recipease.project;

//Created by Andrew Ratz on 2/20/18
//Demonstrates the conversion process from firebase database to arraylist of recipes

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        //Used to connect to the firebase database
        database = FirebaseDatabase.getInstance();
        //References the root of the database
        database_reference = database.getReference();

        ArrayList<Recipe> recipeList = new ArrayList<>();
        RecipeAdapter recipeAdapter = new RecipeAdapter(this, recipeList);

        ListView listView = (ListView) findViewById(R.id.lvRecipes);
        listView.setAdapter(recipeAdapter);

        getAllRecipes(recipeAdapter, recipeList);

        //Bring data of selected recipe to the RecipeDetailsActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent details = new Intent(BrowseRecipesActivity.this, RecipeDetailsActivity.class);
                Recipe selected_recipe = (Recipe) parent.getItemAtPosition(position);
                sendRecipe(details, selected_recipe);
                startActivity(details);
            }
        });
    }

    private void sendRecipe(Intent intent, Recipe recipe_to_bring) {
        String title = recipe_to_bring.getTitle();
        long recipeID = recipe_to_bring.getRecipeID();
        int cookTime = recipe_to_bring.getCookTime();
        String imageURL = recipe_to_bring.getImageURL();
        List<String> cookingIngredients = recipe_to_bring.getCookingIngredients();
        List<String> cookingInstructions = recipe_to_bring.getCookingInstructions();
        intent.putExtra("TITLE", title);
        intent.putExtra("UNIQUE ID", recipeID);
        intent.putExtra("COOK TIME", cookTime);
        intent.putExtra("IMAGE URL", imageURL);
        intent.putStringArrayListExtra("INGREDIENTS LIST", (ArrayList) cookingIngredients);
        intent.putStringArrayListExtra("INSTRUCTIONS LIST", (ArrayList) cookingInstructions);
    }

    //Returns a list of all recipes
    public void getAllRecipes(final ArrayAdapter recipeAdapter, final ArrayList<Recipe> recipeList) {
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

