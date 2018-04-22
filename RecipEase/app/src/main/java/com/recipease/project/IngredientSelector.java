package com.recipease.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class IngredientSelector extends DrawerActivity {

    String[] ingredientNames;
    String[] checkedIngredientNames;

    private FirebaseDatabase database;
    private DatabaseReference database_reference;

    private RecyclerView recyclerView;
    private ArrayList<Ingredient> ingredientList;
    IngredientAutoCompleteAdapter ingredientAutoCompleteAdapter;

    private AutoCompleteTextView actv;

    private IngredientAdapter ingredientAdapter;

    private ArrayList<Ingredient> checked_ingredients = new ArrayList<Ingredient>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.activity_ingredient_selector, null, false);
        mDrawerLayout.addView(contentView, 0);

        ingredientNames = new String[1];

        database = FirebaseDatabase.getInstance();
        database_reference = database.getReference();

        actv = (AutoCompleteTextView) findViewById(R.id.actv);
        actv.setTextColor(Color.WHITE);
        actv.getBackground().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        ingredientList = new ArrayList<Ingredient>();
        ingredientAutoCompleteAdapter = new IngredientAutoCompleteAdapter(this,R.layout.activity_ingredient_selector,R.id.lbl_name,ingredientList);
        getAllIngredients(ingredientAutoCompleteAdapter, ingredientList);

        ingredientAdapter = new IngredientAdapter(IngredientSelector.this, checked_ingredients);
        recyclerView = findViewById(R.id.ingredientRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(IngredientSelector.this));
        recyclerView.setAdapter(ingredientAdapter);

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

        for (int i = 0; i < ingredientList.size(); i++){
            if (ingredientList.get(i).getName().equals(selection)) {
                checked_ingredients.add(ingredientList.get(i));
                ingredientAdapter.notifyItemInserted(checked_ingredients.size() - 1);
                actv.dismissDropDown();
                hideKeyboard();
                actv.setText("");
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

                ingredientNames = new String[ingredientList.size()];
                for (int i = 0; i < ingredientList.size(); i++) {
                    ingredientNames[i] = ingredientList.get(i).getName();
                }
                //Asynchronous so have to use this to notify adapter when finished
                ingredientAdapter.notifyDataSetChanged();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(IngredientSelector.this,R.layout.item_ingredient,ingredientNames);
                actv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "Could not populate ingredientList", databaseError.toException());
            }
        });

    }

    //Sending intent to BrowseRecipes Activity
    public void sendRecipes(View view){
        if (checked_ingredients.isEmpty()) {
            Toast.makeText(IngredientSelector.this, "Select at least one ingredient", Toast.LENGTH_LONG).show();
        }
        else {
            HashMap<String, Integer> recipe_ids_map = convertToRecipeIDs();
            Intent intent = new Intent(this, BrowseRecipesActivity.class);
            // Put as Serializable
            intent.putExtra("recipe_ids", recipe_ids_map);
            startActivity(intent);
        }
    }

    public HashMap<String, Integer> convertToRecipeIDs() {
        HashMap<String, Integer> recipe_map = new HashMap<String, Integer>();
        for(int i = 0; i < checked_ingredients.size(); i++){
            List<String> collection = checked_ingredients.get(i).getRecipesUsing();
            for(int j = 0; j < collection.size(); j++){
                if(recipe_map.containsKey(collection.get(j)))
                {
                    int val = recipe_map.get(collection.get(j));
                    recipe_map.put(collection.get(j),val+1);
                }
                else
                {
                    recipe_map.put(collection.get(j),1);
                }
            }
        }
        return recipe_map;
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