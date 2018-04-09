package com.recipease.project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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


public class BrowseRecipesActivity extends DrawerActivity {

    private FirebaseDatabase database;
    private DatabaseReference database_reference;

    private RecyclerView recyclerView;
    private ArrayList<Recipe> recipeList;
    RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.activity_recipe_list, null, false);
        mDrawerLayout.addView(contentView, 0);

        TextView tvLogo = (TextView) findViewById(R.id.logoText);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Painter.ttf");
        tvLogo.setTypeface(font);

        database = FirebaseDatabase.getInstance();
        database_reference = database.getReference();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeList = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        int numIngredients = (int) extras.getInt("numIngredients");
        recipeAdapter = new RecipeAdapter(this, recipeList, numIngredients);
        recyclerView.setAdapter(recipeAdapter);

        retrieveRecipes(recipeAdapter, recipeList);

    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onRestart(){
        super.onRestart();

        recipeList.clear();
        retrieveRecipes(recipeAdapter, recipeList);

    }

    public void onEditSearch(View view){
        finish();
    }

    //Returns a list of all recipes
    public void retrieveRecipes(final RecipeAdapter recipeAdapter, final ArrayList<Recipe> recipeList) {
        // Unfortunately you'll get an unsafe cast warning here, but it's safe to use
        Intent intent = getIntent();
        final ArrayList<String> recipe_ids = (ArrayList<String>) intent.getSerializableExtra("recipe_ids");
        int size = recipe_ids.size();
        for (int i = 0; i < size; i++) {
            database_reference.child("recipes").child(recipe_ids.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Recipe recipe = dataSnapshot.getValue(Recipe.class); //cannot get value of class
                    recipeList.add(recipe); //breaks here
                    //Asynchronous so have to use this to notify adapter when finished
                    recipeAdapter.notifyDataSetChanged();

                    //Set results TextView
                    TextView resultText = findViewById(R.id.resultText);
                    if (recipeList.size() == 1) {
                        resultText.setText(String.format("%d Result", recipeList.size()));
                    }
                    else {
                        resultText.setText(String.format("%d Results", recipeList.size()));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void goBackToIngredientSelector(View view) {
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
        return;
    }
}