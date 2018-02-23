package com.recipease.project;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by robert matyjek on 2/19/2018.
 * Updated 2/20/2018 by andrew ratz.
 */

public class Recipe {

        private String title;
        private long recipeID;
        private int cookTime;
        private String imageURL;
        private List<String> cookingIngredients;
        private List<String> cookingInstructions;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getRecipeID() {
            return recipeID;
        }

        public void setRecipeID(long recipeID) {
            this.recipeID = recipeID;
        }

        public int getCookTime() { return cookTime; }

        public void setCookTime(int cookTime) { this.cookTime = cookTime; }

        public String getImageURL() { return imageURL; }

        public void setImageURL(String imageURL) { this.imageURL = imageURL; }

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


}
