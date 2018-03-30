package com.recipease.project;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by robert matyjek on 2/19/2018.
 * Updated 2/20/2018 by andrew ratz.
 */

public class Recipe {

        private String title;
        private String recipeID;
        private String ownerID;
        private int difficulty;
        private String imageURL;
        private String sourceURL;
        private int numFavorites;
        private List<String> cookingIngredients;
        private List<String> cookingInstructions;


        /*public void generateRecipeId() {
            this.recipeID = new Date().getTime();
        }
        */

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getRecipeID() {
        return recipeID;
    }

        public void generateRecipeID() {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference database_reference = database.getReference();
            String key = database_reference.child("recipes").push().getKey();
            this.recipeID = key;
        }

        public String getImageURL() { return imageURL; }

        public void setImageURL(String imageURL) { this.imageURL = imageURL; }

        public String getSourceURL() { return sourceURL; }

        public void setSourceURL(String sourceURL) { this.sourceURL = sourceURL; }

        public List<String> getCookingIngredients() {
            return cookingIngredients;
        }

        public void setCookingIngredients(ArrayList<String> cookingIngredients) {
            this.cookingIngredients = cookingIngredients;
        }

        public List<String> getCookingInstructions() {
            return cookingInstructions;
        }

        public void setCookingInstructions(ArrayList<String> cookingInstructions) {
            this.cookingInstructions = cookingInstructions;
        }

        public int getNumFavorites() {
            return this.numFavorites;
        }

        public void setNumFavorites( int newNumFavorites ) {
            this.numFavorites = newNumFavorites;
        }

        public String getOwnerID() { return this.ownerID; }

        public void setOwnerID(String ownerID) { this.ownerID = ownerID; }


}
