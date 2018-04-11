package com.recipease.project;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class BrowseFavoritesActivity extends DrawerActivity {

    //NEED TO BE ABLE TO ACCESS MENU

    private FirebaseDatabase database;
    private DatabaseReference database_reference;
    private ArrayList<String> recipesFavorited = new ArrayList<String>();
    private ArrayList<Recipe> favoriteRecipeList;
    private boolean noRecipes;
    FavoriteRecipeAdapter recipeAdapter;
    private RecyclerView recyclerView;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browse_favorites);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_browse_favorites, null, false);

        mDrawerLayout.addView(contentView, 0);

        TextView tvLogo = (TextView) findViewById(R.id.logoText);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Painter.ttf");
        tvLogo.setTypeface(font);

        database = FirebaseDatabase.getInstance();
        database_reference = database.getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteRecipeList = new ArrayList<>();

        recipeAdapter = new FavoriteRecipeAdapter(this, favoriteRecipeList);
        recyclerView.setAdapter(recipeAdapter);

        retrieveFavorites();

    }

    @Override
    protected void onRestart(){
        super.onRestart();

        favoriteRecipeList.clear();
        retrieveFavorites();

    }

    public void retrieveFavorites() {

        database_reference.child("users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot the_user) {
                User user = the_user.getValue(User.class);
                if (user.getRecipesFavorited() == null) {
                    TextView resultText = findViewById(R.id.favoritesPageResults);
                    resultText.setText(String.format("No favorites yet!"));


                } else {
                    recipesFavorited = new ArrayList(user.getRecipesFavorited());
                    noRecipes = false;
                    int size = recipesFavorited.size();

                    for (int i = 0; i < size; i++) {
                        database_reference.child("recipes").child(recipesFavorited.get(i)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Recipe recipe = dataSnapshot.getValue(Recipe.class); //cannot get value of class
                                if (recipe == null) {
                                    return;
                                }
                                favoriteRecipeList.add(recipe); //breaks here
                                //Asynchronous so have to use this to notify adapter when finished
                                recipeAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    //Set results TextView

                    TextView resultText = findViewById(R.id.favoritesPageResults);
                    if (favoriteRecipeList.size() == 1) {
                        resultText.setText(String.format("%d Favorite", favoriteRecipeList.size()));
                    } else {
                        resultText.setText(String.format("%d Favorites", favoriteRecipeList.size()));
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }
}


