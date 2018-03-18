package com.recipease.project;

import java.util.ArrayList;
import java.util.HashSet;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    int[] options = {R.id.spinach, R.id.carrot,R.id.bacon,R.id.chicken,R.id.apple,R.id.banana,R.id.milk,R.id.cheese};
    String[] names = {"spinach", "carrot","bacon","chicken","apple","banana","milk","cheese"};

    private FirebaseDatabase database;
    private DatabaseReference database_reference;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_selector);

        // setting up autocomplete
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,names);
        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.actv);
        actv.setAdapter(adapter);
        actv.setTextColor(Color.WHITE);
        actv.getBackground().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        // Fixing the folding animation
        final FoldingCell veggies = (FoldingCell) findViewById(R.id.veggie_cell);
        final FoldingCell fruits = (FoldingCell) findViewById(R.id.fruit_cell);
        final FoldingCell meats = (FoldingCell) findViewById(R.id.meat_cell);
        final FoldingCell dairy = (FoldingCell) findViewById(R.id.dairy_cell);

        veggies.initialize(2000, Color.rgb(245,115,27), 0);
        fruits.initialize(2000, Color.rgb(245,115,27), 0);
        meats.initialize(2000, Color.rgb(245,115,27), 0);

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
        dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dairy.toggle(false);
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
        getAllRecipes(recipeList);

        Intent intent = new Intent(this, BrowseRecipesActivity.class);

        intent.putExtra("numIngredients", setofIngredients.size());


        //intent.putExtra("recipes", new DataWrapper(recipeList));
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


    //Edited the getAllRecipes method from Home Activity for my use
    public void getAllRecipes(final ArrayList<Recipe> recipeList) {
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
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "onCancelled", databaseError.toException());
            }
        });
        return;
    }
}
