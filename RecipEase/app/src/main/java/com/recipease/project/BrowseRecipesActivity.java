package com.recipease.project;

//Created by Andrew Ratz on 2/20/18
//Demonstrates the conversion process from firebase database to arraylist of recipes

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BrowseRecipesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        DatabaseConnection databaseConnection = new DatabaseConnection();

        ArrayList<Recipe> recipeList = new ArrayList<>();
        RecipeAdapter recipeAdapter = new RecipeAdapter(this, recipeList);

        ListView listView = (ListView) findViewById(R.id.lvRecipes);
        listView.setAdapter(recipeAdapter);

        databaseConnection.getAllRecipes(recipeAdapter, recipeList);
        //Query example
        //databaseConnection.queryRecipes("title", "Best", recipeAdapter, recipeList);

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
}

