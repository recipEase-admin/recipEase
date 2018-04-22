package com.recipease.project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class SearchRecipesActivity extends DrawerActivity {

    private FirebaseDatabase database;
    private DatabaseReference database_reference;

    private RecyclerView recyclerView;
    private ArrayList<Recipe> recipeList;
    FavoriteRecipeAdapter recipeAdapter;

    private EditText etRecipeSearch;

    private ArrayList<Recipe> allRecipes;

    boolean first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.activity_search_recipe, null, false);
        mDrawerLayout.addView(contentView, 0);

        findViewById(R.id.searchLoadingPanel).setVisibility(View.GONE);


        TextView tvLogo = (TextView) findViewById(R.id.logoText);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Painter.ttf");
        tvLogo.setTypeface(font);

        database = FirebaseDatabase.getInstance();
        database_reference = database.getReference();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeList = new ArrayList<>();

        allRecipes = new ArrayList<>();
        first = true;

        etRecipeSearch = findViewById(R.id.etRecipeSearch);
        etRecipeSearch.getBackground().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        etRecipeSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    retrieveRecipes(recipeAdapter, recipeList);
                }
                return false;
            }
        });
        recipeAdapter = new FavoriteRecipeAdapter(this, recipeList);
        recyclerView.setAdapter(recipeAdapter);

        getAllRecipes(recipeAdapter, recipeList);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }

    public void onEditSearch(View view){
        finish();
    }

    public void cacheRecipes() {
        database_reference.child("recipes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    Recipe recipe = children.getValue(Recipe.class);
                    if (recipe == null) {
                        return;
                    }
                    allRecipes.add(recipe); //breaks here
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void searchForRecipe(View view) {
        retrieveRecipes(recipeAdapter, recipeList);
    }
    //Returns a list of all recipes
    public void retrieveRecipes(final FavoriteRecipeAdapter recipeAdapter, final ArrayList<Recipe> recipeList) {
        recipeList.clear();
        recipeAdapter.notifyDataSetChanged();
        findViewById(R.id.searchLoadingPanel).setVisibility(View.VISIBLE);
        final String search = etRecipeSearch.getText().toString();
        hideKeyboard();
            int i = 0;
            for (int j = 0; j < allRecipes.size(); j++) {
                if (allRecipes.get(j).getTitle().contains(search)) {
                    recipeList.add(allRecipes.get(j)); //breaks here
                    //Asynchronous so have to use this to notify adapter when finished
                    recipeAdapter.notifyItemInserted(i);
                    i++;
                    //Set results TextView
                    TextView resultText = findViewById(R.id.resultText);
                    if (recipeList.size() == 1) {
                        resultText.setText(String.format("%d Result", recipeList.size()));
                    } else {
                        resultText.setText(String.format("%d Results", recipeList.size()));
                    }
                    if (i == 100) {
                        break;
                    }
                }
            }
            findViewById(R.id.searchLoadingPanel).setVisibility(View.GONE);
        }

    //Returns a list of all recipes
    public void getAllRecipes(final FavoriteRecipeAdapter recipeAdapter, final ArrayList<Recipe> recipeList) {
        recipeList.clear();
        hideKeyboard();
        database_reference.child("recipes").limitToFirst(100).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    Recipe recipe = children.getValue(Recipe.class);
                    if (recipe == null) {
                        return;
                    }
                    recipeList.add(recipe); //breaks here
                    //Set results TextView
                    TextView resultText = findViewById(R.id.resultText);
                    if (recipeList.size() == 1) {
                        resultText.setText(String.format("%d Result", recipeList.size()));
                    } else {
                        resultText.setText(String.format("%d Results", recipeList.size()));
                    }
                    //Asynchronous so have to use this to notify adapter when finished
                    recipeAdapter.notifyDataSetChanged();
                }
                cacheRecipes();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void goBackToIngredientSelector(View view) {
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
        return;
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