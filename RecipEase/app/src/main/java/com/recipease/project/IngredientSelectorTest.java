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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static android.content.ContentValues.TAG;

public class IngredientSelectorTest extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference database_reference;

    private RecyclerView recyclerView;
    private ArrayList<Ingredient> ingredientList;
    IngredientAdapter ingredientAdapter;

    private ArrayList<Ingredient> checked_ingredients;

    //private ArrayList<String> serialIngredientList = (ArrayList<String>) getIntent().getSerializableExtra("ingredients");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_list);

        database = FirebaseDatabase.getInstance();
        database_reference = database.getReference();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientList = new ArrayList<>();

        checked_ingredients = new ArrayList<>();

        //DataWrapper ingredientDataWrapper = (DataWrapper) getIntent().getSerializableExtra("ingredients");
        //ingredientList = getIntent().getParcelableArrayListExtra("ingredients");//ingredientDataWrapper.getIngredients();

        ingredientAdapter = new IngredientAdapter(this, ingredientList);
        recyclerView.setAdapter(ingredientAdapter);

        getAllIngredients(ingredientAdapter, ingredientList);

    }

    //Returns a list of all ingredients
    public void getAllIngredients(final IngredientAdapter ingredientAdapter, final ArrayList<Ingredient> ingredientList) {
        // Read ingredients in from the database and convert them to an ArrayList of Ingredient objects
        database_reference.child("ingredients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot all_ingredients) {
                //Loop through each separate ingredient
                for (DataSnapshot single_ingredient : all_ingredients.getChildren()) {
                    //Create a new ingredient object
                    Ingredient ingredient = single_ingredient.getValue(Ingredient.class);
                    //populateIngredient(new_ingredient, single_ingredient);
                    //Adds this new ingredient to the ingredient arraylist
                    ingredientList.add(ingredient);
                }
                //Asynchronous so have to use this to notify adapter when finished
                ingredientAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "onCancelled", databaseError.toException());
            }
        });
        return;

    }


    //Adding and removing ingredients from set
    public void addItem(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        CharSequence text = ((CheckBox) view).getText();
        for(int i=0; i<ingredientList.size(); i++) {
            if (text.equals(ingredientList.get(i).getName())) {
                if (checked) {
                    checked_ingredients.add(ingredientList.get(i));
                }
                else {
                    checked_ingredients.remove(ingredientList.get(i));
                }
                break;
            }
        }
    }

    //Sending intent to BrowseRecipes Activity
    public void sendRecipes(View view){
        if (checked_ingredients.isEmpty()) {
            Toast.makeText(IngredientSelectorTest.this, "Select at least one ingredient", Toast.LENGTH_LONG).show();
        }
        else {
            ArrayList<Long> recipe_ids_intersection = convertToRecipeIDs();
            Intent intent = new Intent(this, BrowseRecipesActivity.class);
            // Put as Serializable
            intent.putExtra("recipe_ids", recipe_ids_intersection);
            startActivity(intent);
        }
    }

    public ArrayList<Long> convertToRecipeIDs() {
        ArrayList<Collection<Long>> recipe_ids = new ArrayList<>();
        for (int i = 0; i < checked_ingredients.size(); i+=2) {
            //If odd number of ingredients selected and are on the last loop, add last one to the list
            if ((checked_ingredients.size() - i) == 1) {
                Collection<Long> collection = checked_ingredients.get(i).getRecipesUsing();
                recipe_ids.add(collection);
                break;
            }
            else {
                Collection<Long> collection1 = checked_ingredients.get(i).getRecipesUsing();
                Collection<Long> collection2 = checked_ingredients.get(i + 1).getRecipesUsing();
                Collection<Long> intersection = CollectionUtils.intersection(collection1, collection2);
                recipe_ids.add(intersection);
            }
        }
        //Now we have an Arraylist of Collection of recipe_ids
        while (recipe_ids.size() > 1) {
            Collection<Long> collection1 = recipe_ids.get(0);
            System.out.println(collection1);
            Collection<Long> collection2 = recipe_ids.get(1);
            System.out.println(collection2);
            Collection<Long> intersection = CollectionUtils.intersection(collection1, collection2);
            System.out.println(intersection);
            //Add this intersection
            recipe_ids.add(0, intersection);
            //Remove the former two collections
            recipe_ids.remove(1);
            recipe_ids.remove(1);
        }
        //Final intersection has been formed
        return (ArrayList<Long>) recipe_ids.get(0);
    }
}