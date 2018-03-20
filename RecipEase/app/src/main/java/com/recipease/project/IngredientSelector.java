package com.recipease.project;

import java.util.ArrayList;
import java.util.HashSet;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import static android.content.ContentValues.TAG;
import java.util.HashMap;


public class IngredientSelector extends AppCompatActivity {


    // Set is for storing all the ingredients, two arrays carry the names and ids for autocomplete
    HashSet<String> setofIngredients = new HashSet<String>();
    //int[] options = {R.id.spinach, R.id.carrot,R.id.bacon,R.id.chicken,R.id.apple,R.id.banana};
    String[] names = {"spinach", "carrot","bacon","chicken","apple","banana","milk","cheese"};

    private FirebaseDatabase database;
    private DatabaseReference database_reference;

    private RecyclerView recyclerView;
    private ArrayList<Ingredient> ingredientList;
    IngredientAdapter ingredientAdapter;
    IngredientAutoCompleteAdapter ia;

    private ArrayList<Ingredient> checked_ingredients;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_selector);

        database = FirebaseDatabase.getInstance();
        database_reference = database.getReference();

//        checked_ingredients = new ArrayList<>();
//
        ingredientAdapter = new IngredientAdapter(this, ingredientList);
//        recyclerView.setAdapter(ingredientAdapter);
//        getAllIngredients(ingredientAdapter,ingredientList);


//
//        HashMap<String,Ingredient> map = new HashMap<String,Ingredient>();
//        System.out.println(ingredientList.size());
//        for(Ingredient x: ingredientList){
//            map.put(x.getName(),x);
//        }

        ingredientList = new ArrayList<Ingredient>();
        ia = new IngredientAutoCompleteAdapter(this,R.layout.activity_ingredient_selector,R.id.lbl_name,ingredientList);
        getAllIngredients(ia, ingredientList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,names);
        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.actv);
        actv.setAdapter(ia);
        actv.setTextColor(Color.WHITE);
        actv.getBackground().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);











//
//        // setting up autocomplete
//        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.actv);
//
//        actv.setThreshold(1);
//        actv.setAdapter(ia);
//        actv.setTextColor(Color.WHITE);
//        actv.getBackground().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);


//        actv.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
//                String i = (String)parent.getItemAtPosition(position);
//                Log.d("LOL", i);
//            }
//        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_item_ingredient, menu);
        return true;
    }


    //Sending intent to BrowseRecipes Activity
    public void sendRecipes(View view){


        ArrayList<Recipe> recipeList = new ArrayList<>();
        //RecipeAdapter recipeAdapter = new RecipeAdapter(this, recipeList);
        database = FirebaseDatabase.getInstance();
        database_reference = database.getReference();

        Intent intent = new Intent(this, BrowseRecipesActivity.class);
        intent.putExtra("recipes", recipeList);

        /* Hey, Look Over Here
         Use (ArrayList<String>) getIntent().getSerializableExtra("recipes"); to open this list in the other class
          */
        startActivity(intent);
    }


    //Adding and removing ingredients from set
    public void addItem(View view) {
//        boolean checked = ((CheckBox) view).isChecked();
//
//        for(int i=0; i<options.length; i++){
//
//            if(view.getId()==options[i]){
//
//                if(checked){
//                    setofIngredients.add(names[i]);
//                }
//                else{
//                    setofIngredients.remove(names[i]);
//                }
//                break;
//            }
//
//        }
    }


    //Returns a list of all ingredients
    public void getAllIngredients(final IngredientAutoCompleteAdapter ingredientAdapter, final ArrayList<Ingredient> ingredientList) {
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
                System.out.println(ingredientList.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "onCancelled", databaseError.toException());
            }
        });
        return;

    }
}
