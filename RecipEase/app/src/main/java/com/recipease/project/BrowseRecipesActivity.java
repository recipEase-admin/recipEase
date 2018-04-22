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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class BrowseRecipesActivity extends DrawerActivity {

    private FirebaseDatabase database;
    private DatabaseReference database_reference;

    private RecyclerView recyclerView;
    private ArrayList<Recipe> recipeList;
    private ArrayList<Integer> numIngredientsList;
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
        numIngredientsList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(this, recipeList, numIngredientsList);
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

    }

    public void onEditSearch(View view){
        finish();
    }

    //used to sort hashmap in descending order by num of correct ingredients
    private static HashMap<String, Integer> sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((HashMap.Entry) (o2)).getValue())
                        .compareTo(((HashMap.Entry) (o1)).getValue());
            }
        });
        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            HashMap.Entry entry = (HashMap.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    //Returns a list of all recipes
    public void retrieveRecipes(final RecipeAdapter recipeAdapter, final ArrayList<Recipe> recipeList) {
        // Unfortunately you'll get an unsafe cast warning here, but it's safe to use
        Intent intent = getIntent();
        final HashMap<String, Integer> recipe_ids = (HashMap<String, Integer>) intent.getSerializableExtra("recipe_ids");
        HashMap<String, Integer> recipes_id_map = sortByValues(recipe_ids);

        int size = recipes_id_map.size();
        Iterator ite = recipes_id_map.entrySet().iterator();
        while (ite.hasNext()) {

            HashMap.Entry pair = (HashMap.Entry) ite.next();
            final int val = (Integer) pair.getValue();
            database_reference.child("recipes").child((String) pair.getKey()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Recipe recipe = dataSnapshot.getValue(Recipe.class); //cannot get value of class
                    if (recipe == null) {
                        return;
                    }
                    recipeList.add(recipe); //breaks here
                    numIngredientsList.add(val);
                    //Asynchronous so have to use this to notify adapter when finished
                    recipeAdapter.notifyDataSetChanged();
                    //Set results TextView
                    TextView resultText = (TextView) findViewById(R.id.resultText);
                    if (recipeList.size() == 1) {
                        resultText.setText(String.format("%d Result", recipeList.size()));
                    }
                    else {
                        resultText.setText(String.format("%d Results", recipeList.size()));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Encountered error:\n " + databaseError.getDetails() + "\n" + databaseError.getMessage());
                }
            });
            ite.remove(); // avoids a ConcurrentModificationException
        }

    }

    public void goBackToIngredientSelector(View view) {
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
        return;
    }
}