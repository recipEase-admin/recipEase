package com.recipease.project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TrendingRecipes extends DrawerActivity {

    private FirebaseDatabase database;
    private DatabaseReference database_reference;

    private RecyclerView recyclerView;
    private ArrayList<Recipe> recipeList;
    FavoriteRecipeAdapter recipeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.activity_trending_recipes, null, false);
        mDrawerLayout.addView(contentView, 0);

        TextView tvLogo = (TextView) findViewById(R.id.logoText);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Painter.ttf");
        tvLogo.setTypeface(font);


        database = FirebaseDatabase.getInstance();
        database_reference = database.getReference();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeList = new ArrayList<>();

        recipeAdapter = new FavoriteRecipeAdapter(this, recipeList);
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
    public void retrieveRecipes(final FavoriteRecipeAdapter recipeAdapter, final ArrayList<Recipe> recipeList) {

        database_reference.child("recipes").orderByChild("numFavorites").limitToLast(30).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot topThirty) {
                ArrayList<Recipe> temp = new ArrayList<>();
                for (DataSnapshot eachRecipe : topThirty.getChildren()) {
                    Recipe r = eachRecipe.getValue(Recipe.class);
                    if (r != null) {
                        temp.add(r);
                    }
                }
                //Firebase sorts in ascending order only, because it's dumb
                int tempSize = temp.size();
                for (int i = tempSize-1; i >= 0; i--) {
                    recipeList.add(temp.get(i));
                }
                recipeAdapter.notifyDataSetChanged();

                if (recipeList.size() >= 1) {
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}