package com.recipease.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.AdapterView;
import android.view.Menu;
import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.collections4.CollectionUtils;

import static android.content.ContentValues.TAG;
import java.util.HashMap;


public class IngredientSelector extends DrawerActivity {

    String[] ingredientNames;
    String[] checkedIngredientNames;

    private FirebaseDatabase database;
    private DatabaseReference database_reference;

    private RecyclerView recyclerView;
    private ArrayList<Ingredient> ingredientList;
    IngredientAutoCompleteAdapter ingredientAutoCompleteAdapter;

    private ArrayList<Ingredient> checked_ingredients = new ArrayList<Ingredient>();

    private AutoCompleteTextView actv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.activity_ingredient_selector, null, false);
        mDrawerLayout.addView(contentView, 0);



        database = FirebaseDatabase.getInstance();
        database_reference = database.getReference();

        ingredientList = new ArrayList<Ingredient>();
        ingredientAutoCompleteAdapter = new IngredientAutoCompleteAdapter(this,R.layout.activity_ingredient_selector,R.id.lbl_name,ingredientList);
        getAllIngredients(ingredientAutoCompleteAdapter, ingredientList);

        IngredientAdapter ingredientAdapter = new IngredientAdapter(IngredientSelector.this, checked_ingredients);
        recyclerView = findViewById(R.id.ingredientRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(IngredientSelector.this));
        recyclerView.setAdapter(ingredientAdapter);

        actv = (AutoCompleteTextView) findViewById(R.id.actv);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_item_ingredient, menu);
        return true;
    }

    //Adding and removing ingredients from set
    public void addItem(View view) {
        TextView lbl = (TextView) view;
        String selection = lbl.getText().toString();
        System.out.println(selection);
        for (int i = 0; i < ingredientList.size(); i++) {
            if (ingredientList.get(i).getName().equals(selection)) {
                checked_ingredients.add(ingredientList.get(i));
                actv.dismissDropDown();
                hideKeyboard();
                return;
            }
        }
    }

    //Returns a list of all ingredients
    public void getAllIngredients(final IngredientAutoCompleteAdapter ingredientAdapter, final ArrayList<Ingredient> ingredientList) {
        // Read ingredients in from the database and convert them to an ArrayList of Ingredient objects
        database_reference.child("ingredientRecipes").addValueEventListener(new ValueEventListener() {
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
                ingredientNames = new String[ingredientList.size()];
                for (int i = 0; i < ingredientList.size(); i++) {
                    ingredientNames[i] = ingredientList.get(i).getName();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(IngredientSelector.this,R.layout.item_ingredient,ingredientNames);
                AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.actv);
                actv.setAdapter(adapter);
                actv.setTextColor(Color.WHITE);
                actv.getBackground().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "onCancelled", databaseError.toException());
            }
        });
        return;

    }

    //Sending intent to BrowseRecipes Activity
    public void sendRecipes(View view){
        if (checked_ingredients.isEmpty()) {
            Toast.makeText(IngredientSelector.this, "Select at least one ingredient", Toast.LENGTH_LONG).show();
        }
        else {
            ArrayList<String> recipe_ids_intersection = convertToRecipeIDs();
            int numIngredients = checked_ingredients.size();
            Intent intent = new Intent(this, BrowseRecipesActivity.class);
            // Put as Serializable
            intent.putExtra("recipe_ids", recipe_ids_intersection);
            intent.putExtra("numIngredients", numIngredients);
            startActivity(intent);
        }
    }

    //Narrows down the recipe ids to send by using intersect operations
    public ArrayList<String> convertToRecipeIDs() {
        ArrayList<Collection<String>> recipe_ids = new ArrayList<>();
        for (int i = 0; i < checked_ingredients.size(); i+=2) {
            //If odd number of ingredients selected and are on the last loop, add last one to the list
            if ((checked_ingredients.size() - i) == 1) {
                Collection<String> collection = checked_ingredients.get(i).getRecipesUsing();
                recipe_ids.add(collection);
                break;
            }
            else {
                Collection<String> collection1 = checked_ingredients.get(i).getRecipesUsing();
                Collection<String> collection2 = checked_ingredients.get(i + 1).getRecipesUsing();
                Collection<String> intersection = CollectionUtils.intersection(collection1, collection2);
                recipe_ids.add(intersection);
            }
        }
        //Now we have an Arraylist of Collection of recipe_ids
        while (recipe_ids.size() > 1) {
            Collection<String> collection1 = recipe_ids.get(0);
            System.out.println(collection1);
            Collection<String> collection2 = recipe_ids.get(1);
            System.out.println(collection2);
            Collection<String> intersection = CollectionUtils.intersection(collection1, collection2);
            System.out.println(intersection);
            //Add this intersection
            recipe_ids.add(0, intersection);
            //Remove the former two collections
            recipe_ids.remove(1);
            recipe_ids.remove(1);
        }
        //Final intersection has been formed
        return (ArrayList<String>) recipe_ids.get(0);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}