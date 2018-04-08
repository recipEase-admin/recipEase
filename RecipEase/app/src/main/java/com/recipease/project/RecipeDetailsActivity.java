package com.recipease.project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.Map;


import static android.content.ContentValues.TAG;


public class RecipeDetailsActivity extends DrawerActivity {

    private FirebaseDatabase database;
    private DatabaseReference database_reference;
    private DatabaseReference favorites_reference;
    private DatabaseReference comments_reference;
    private TextView tvNumFavorites;
    private int numFavorites;
    private String rID;

    private ArrayList<String> rComments;

    //variables for recipesFavorited in user object
    private FirebaseAuth firebaseAuth;
    private String userID = "";
    public  FirebaseUser user;
    private DatabaseReference user_reference;
    ArrayList<String> recipesFavorited = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.activity_recipe_details, null, false);
        mDrawerLayout.addView(contentView, 0);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        receiveRecipe();

    }

    public void onClickFavorites(View view){

        database = FirebaseDatabase.getInstance();
        database_reference = database.getReference();

        //update numFavorites in recipe object
        ++numFavorites;
        favorites_reference = database_reference.child("recipes").child(rID);
        Map<String, Object>  favorites_update = new HashMap<>();
        favorites_update.put("numFavorites", numFavorites);
        favorites_reference.updateChildren(favorites_update);
        tvNumFavorites.setText(String.format("%d", numFavorites));

        //update recipesFavorited in user object
        database_reference.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot the_user) {
                User user = the_user.getValue(User.class);
                ArrayList<String> recipesFavorited;
                if (user.getRecipesFavorited() == null) {
                    recipesFavorited = new ArrayList<String>();
                }
                else {
                    recipesFavorited = new ArrayList(user.getRecipesFavorited());
                }
                recipesFavorited.add(rID);
                user.setRecipesFavorited(recipesFavorited);
                the_user.getRef().setValue(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "Could not add recipe to favorites", databaseError.toException());
            }
        });

        //disable button
        ImageView heart_button = (ImageView) findViewById(R.id.favoriteButton);
        heart_button.setEnabled(false);

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
            Glide.with(RecipeDetailsActivity.this).load(R.drawable.no_image).into(ivImageURL);
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
}


