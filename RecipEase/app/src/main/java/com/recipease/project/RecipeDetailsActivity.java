package com.recipease.project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static android.content.ContentValues.TAG;


public class RecipeDetailsActivity extends DrawerActivity {

    private FirebaseDatabase database;
    private DatabaseReference database_reference;
    private DatabaseReference recipeFavoritesReference;
    private DatabaseReference userFavoritesReference;
    private DatabaseReference favorites_reference;
    private DatabaseReference comments_reference;
    private TextView tvNumFavorites;
    private int numFavorites;
    private String rID;
    private ArrayList<String> recipesFavorited  = new ArrayList<String>();
    private boolean favoritesExecute;

    private ArrayList<String> rComments;

    //variables for recipesFavorited in user object
    private FirebaseAuth firebaseAuth;
    private String userID = "";
    public  FirebaseUser user;
    private DatabaseReference user_reference;
    //ArrayList<String> recipesFavorited = new ArrayList<String>();

    ImageView trash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.activity_recipe_details, null, false);
        mDrawerLayout.addView(contentView, 0);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();
        trash = findViewById(R.id.btDeleteTrash);
        trash.setClickable(false);

        receiveRecipe();

    }

    public void onClickFavorites(View view){

        database = FirebaseDatabase.getInstance();
        database_reference = database.getReference();

        database_reference.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot the_user) {
                User user = the_user.getValue(User.class);

                    if (user.getRecipesFavorited() == null) {
                        recipesFavorited = new ArrayList<String>();
                        //call function to favorite
                        //favoriteRecipe();

                        recipesFavorited.add(rID);
                        user.setRecipesFavorited(recipesFavorited);
                        the_user.getRef().setValue(user);

                        ++numFavorites;
                        favorites_reference = database_reference.child("recipes").child(rID);
                        Map<String, Object> favorites_update = new HashMap<>();
                        favorites_update.put("numFavorites", numFavorites);
                        favorites_reference.updateChildren(favorites_update);
                        tvNumFavorites.setText(String.format("%d", numFavorites));


                    }else{
                        recipesFavorited = new ArrayList(user.getRecipesFavorited());
                        //unfavoriteRecipe();

                        if(recipesFavorited.contains(rID)){

                            recipesFavorited.remove(rID);
                            user.setRecipesFavorited(recipesFavorited);
                            the_user.getRef().setValue(user);

                            --numFavorites;
                            favorites_reference = database_reference.child("recipes").child(rID);
                            Map<String, Object> favorites_update = new HashMap<>();
                            favorites_update.put("numFavorites", numFavorites);
                            favorites_reference.updateChildren(favorites_update);
                            tvNumFavorites.setText(String.format("%d", numFavorites));


                        }else{

                            recipesFavorited.add(rID);
                            user.setRecipesFavorited(recipesFavorited);
                            the_user.getRef().setValue(user);

                            ++numFavorites;
                            favorites_reference = database_reference.child("recipes").child(rID);
                            Map<String, Object> favorites_update = new HashMap<>();
                            favorites_update.put("numFavorites", numFavorites);
                            favorites_reference.updateChildren(favorites_update);
                            tvNumFavorites.setText(String.format("%d", numFavorites));

                        }

                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "Could not add recipe to favorites", databaseError.toException());
            }

        });

    }

    private void receiveRecipe() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("TITLE");
        String recipeID = intent.getStringExtra("UNIQUE ID");

        numFavorites = intent.getIntExtra("NUM FAVORITES", 0);
        String imageURL = intent.getStringExtra("IMAGE URL");
        ArrayList<String> cookingIngredients = intent.getStringArrayListExtra("INGREDIENTS LIST");
        ArrayList<String> cookingInstructions = intent.getStringArrayListExtra("INSTRUCTIONS LIST");
        ArrayList<String> comments = intent.getStringArrayListExtra("COMMENTS");

        ImageView ivImageURL = (ImageView) findViewById(R.id.ivImageURL);

        if (imageURL.equals("")) {
            Glide.with(RecipeDetailsActivity.this).load(R.drawable.no_image).centerCrop().into(ivImageURL);
        }
        else {
            Glide.with(RecipeDetailsActivity.this).load(imageURL).centerCrop().into(ivImageURL);
        }

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(title);
      
        TextView tvCookingIngredients = (TextView) findViewById(R.id.tvCookingIngredients);
        tvCookingIngredients.setText(TextUtils.join("\n\n", cookingIngredients));

        TextView tvCookingInstructions = (TextView) findViewById(R.id.tvCookingInstructions);
        tvCookingInstructions.setText(TextUtils.join("\n\n", cookingInstructions));

        TextView tvComments = (TextView) findViewById(R.id.tvComments);
        if (comments == null) {
            tvComments.setText("There are no comments - be the first!");
        }
        else {
            tvComments.setText(TextUtils.join("\n\n", comments));
        }

        tvNumFavorites = (TextView) findViewById(R.id.tvNumFavorites);
        tvNumFavorites.setText(String.format("%d", numFavorites));

        rID = recipeID;
        rComments = comments;

        database = FirebaseDatabase.getInstance();
        database_reference = database.getReference();

        database_reference.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot the_user) {
                User user = the_user.getValue(User.class);
                if (user.getRecipesOwned() != null) {
                    if (user.getRecipesOwned().contains(rID)) {
                        trash.setClickable(true);
                        trash.setImageResource(R.drawable.ic_trash);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

    }

    public void addComment(View view) {
        Filter inputFilter = new Filter();
        EditText etNewComment = (EditText) findViewById(R.id.etNewComment);
        String comment = etNewComment.getText().toString();

        if(inputFilter.containsProfanity(comment)) {
            showAlert("Your comment contains profanity, please remove the profanity.", "I'll Handle It");
        }
        else if(comment.equals("")) {
            showAlert("Please enter a comment first", "I'm On It");
        }
        else {
            if (rComments == null) {
                rComments = new ArrayList<String>();
            }
            rComments.add(comment);
            database = FirebaseDatabase.getInstance();
            database_reference = database.getReference();

            comments_reference = database_reference.child("recipes").child(rID);
            Map<String, Object>  comments_update = new HashMap<>();
            comments_update.put("comments", rComments);
            comments_reference.updateChildren(comments_update);

            Toast.makeText(RecipeDetailsActivity.this, "Comment posted!", Toast.LENGTH_SHORT).show();

            TextView tvComments = (TextView) findViewById(R.id.tvComments);
            if (rComments == null || rComments.isEmpty()) {
                tvComments.setText("There are no comments - be the first!");
            }
            else {
                tvComments.setText(TextUtils.join("\n\n", rComments));
            }

            etNewComment.setText("");
        }
    }

    public void deleteRecipe(View view) {
        showAlertRequireResponse("Are you sure you want to delete your recipe?", "Yes", "No");
    }

    public void showAlert(String message, String actionText) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(actionText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showAlertRequireResponse(String message, String actionText, String cancelText) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(actionText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        database = FirebaseDatabase.getInstance();
                        database_reference = database.getReference();

                        database_reference.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(DataSnapshot the_user) {
                                 User user = the_user.getValue(User.class);
                                 List<String> recipesOwned = user.getRecipesOwned();
                                 for (int i = 0; i < recipesOwned.size(); i++) {
                                     if (recipesOwned.get(i).equals(rID)) {
                                         database_reference.child("users").child(userID).child("recipesOwned").child(Integer.toString(i)).removeValue();
                                         break;
                                     }
                                 }
                             }
                             @Override
                            public void onCancelled(DatabaseError error) {

                             }
                         });
                        database_reference.child("recipes").child(rID).removeValue();
                        Intent i = new Intent(RecipeDetailsActivity.this, HomeActivity.class);
                        Toast.makeText(RecipeDetailsActivity.this, "Recipe deleted", Toast.LENGTH_LONG).show();
                        startActivity(i);
                    }
                });
        alertDialogBuilder.setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}


