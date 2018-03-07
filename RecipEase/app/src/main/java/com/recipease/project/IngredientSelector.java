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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ramotion.foldingcell.FoldingCell;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.view.Menu;
import android.content.Intent;

import static android.content.ContentValues.TAG;


public class IngredientSelector extends AppCompatActivity {


    // Set is for storing all the ingredients, two arrays carry the names and ids for autocomplete
    HashSet<String> setofIngredients = new HashSet<String>();
    int[] options = {R.id.spinach, R.id.carrot,R.id.bacon,R.id.chicken,R.id.apple,R.id.banana};
    String[] names = {"spinach", "carrot","bacon","chicken","apple","banana","milk","cheese"};

    private FirebaseDatabase database;
    private DatabaseReference database_reference;
    private ArrayList<Ingredient> ingredientList;
    private RecyclerView ingredientRecyclerView;
    IngredientAdapter ingredientAdapter;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_selector);

        database = FirebaseDatabase.getInstance();
        database_reference = database.getReference();

        ingredientRecyclerView = findViewById(R.id.ingredientRecyclerView);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // setting up autocomplete
        ingredientList = new ArrayList<Ingredient>();
        //ingredientAdapter = new IngredientAdapter(this, 0, ingredientList);
        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.actv);
        //actv.setAdapter(ingredientAdapter);
        actv.setTextColor(Color.WHITE);
        actv.getBackground().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        //getAllIngredients(ingredientAdapter, ingredientList);

        // Fixing the folding animation
        final FoldingCell dairy = (FoldingCell) findViewById(R.id.dairy_cell);
        final FoldingCell veggies = (FoldingCell) findViewById(R.id.veggie_cell);
        final FoldingCell fruits = (FoldingCell) findViewById(R.id.fruit_cell);
        final FoldingCell meats = (FoldingCell) findViewById(R.id.meat_cell);

        dairy.initialize(1000, Color.rgb(245,115,27), 0);
        veggies.initialize(1000, Color.rgb(245,115,27), 0);
        fruits.initialize(1000, Color.rgb(245,115,27), 0);
        meats.initialize(1000, Color.rgb(245,115,27), 0);

        dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dairy.toggle(false);
            }
        });
        veggies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                veggies.toggle(false);
            }
        });
        fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruits.toggle(false);
            }
        });
        meats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meats.toggle(false);
            }
        });
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
        //intent.putExtra("recipes", new DataWrapper(recipeList));
        intent.putExtra("recipes", recipeList);
       // intent.putParcelableArrayListExtra("key", ArrayList<Recipe extends Parcelable> recipeList);

        /* Hey, Look Over Here
         Use (ArrayList<String>) getIntent().getSerializableExtra("recipes"); to open this list in the other class
          */
        startActivity(intent);
    }


    //Adding and removing ingredients from set
    public void addItem(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        for(int i=0; i<options.length; i++){

            if(view.getId()==options[i]){

                if(checked){
                    setofIngredients.add(names[i]);
                }
                else{
                    setofIngredients.remove(names[i]);
                }
                break;
            }

        }
    }


    //Fetches all ingredients from database
    public void getAllIngredients(final ArrayAdapter<Ingredient> ingredientAdapter, final ArrayList<Ingredient> ingredientList) {
        // Read ingredients in from the database and convert them to an ArrayList of Ingredient objects
        database_reference.child("ingredients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot all_ingredients) {
                //Loop through each separate ingredient
                for (DataSnapshot single_ingredient : all_ingredients.getChildren()) {
                    //Create a new recipe object
                    Ingredient ingredient = single_ingredient.getValue(Ingredient.class);
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
}
