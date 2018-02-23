package com.recipease.project;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by andrewratz on 2/20/18.
 */

public class DatabaseConnection {
    // ----- Public Fields -----


    // ----- Private Fields -----

    private FirebaseDatabase database;
    private DatabaseReference database_reference;


    // ----- Public Methods -----

    //Initiate a connection to the database
    public DatabaseConnection() {
        //Used to connect to the firebase database
        database = FirebaseDatabase.getInstance();
        //References the root of the database
        database_reference = database.getReference();
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

    //Returns a list of recipes that satisfy the query
    /*
    public void queryRecipes(final String query_key, final String query_value, final ArrayAdapter recipeAdapter, final ArrayList<Recipe> recipeList) {
        // Read recipes that fit the search criteria in from the database and convert them to an ArrayList of Recipe objects
        database_reference.child("recipes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot all_recipes) {
                //Loop through each separate recipe
                for (DataSnapshot single_recipe : all_recipes.getChildren()) {
                    //Create a new recipe object
                    Recipe new_recipe = new Recipe();
                    //populateRecipe(new_recipe, single_recipe);
                    //Only add the recipe to the database if it fits the search criteria
                    if (querySuccess(query_key, query_value, new_recipe)) {
                        recipeList.add(new_recipe);
                    }
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
    */

    // ----- Private Methods -----

    private ArrayList<String> createListFromArray(DataSnapshot array, String key) {
        //Creates a new arraylist of strings
        ArrayList<String> list = new ArrayList<>();
        //Loop through the individual elements of the array
        for (DataSnapshot children : array.getChildren()) {
            Object value_object = children.child(key).getValue();
            String value = value_object.toString();
            //Adds the given ingredient to the arraylist
            list.add(value);
        }
        return list;
    }

    /*
    private boolean querySuccess(String query_key, String query_value, Recipe new_recipe) {
        if (query_key.equals("title")) {
            if (new_recipe.getTitle().contains(query_value)) {
                return true;
            }
            else {
                return false;
            }
        }
        else if (query_key.equals("recipeID")) {
            String str_recipeID = Long.toString(new_recipe.getRecipeID());
            if (str_recipeID.contains(query_value)) {
                return true;
            }
            else {
                return false;
            }
        }
        else if (query_key.equals("cookTime")) {
            String str_cookTime = Integer.toString(new_recipe.getCookTime());
            if (str_cookTime.contains(query_value)) {
                return true;
            }
            else {
                return false;
            }
        }
        else if (query_key.equals("imageURL")) {
            if (new_recipe.getImageURL().contains(query_value)) {
                return true;
            }
            else {
                return false;
            }
        }
        /*If the query key is invalid, or the query value is not in the recipe, then
        the current recipe does not match the query criteria, so throw it out*/
        /*
        else {
            return false;
        }
    }
    */
}
