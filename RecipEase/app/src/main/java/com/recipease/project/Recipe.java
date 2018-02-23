package com.recipease.project;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by robert matyjek on 2/19/2018.
 * Updated 2/20/2018 by andrew ratz.
 */

public class Recipe {
    /*
    private int cookTime;
    private String imageURL;
    private ArrayList<Integer> ingredientMeasurements;


    public void setCookTime(int cookTime){
        this.cookTime = cookTime;
    }

    public int getCookTime(){
        return cookTime;
    }

    public String getImageURL(){
        return imageURL;
    }

    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }

    public ArrayList<Integer> getIngredientMeasurements() {
        return ingredientMeasurements;
    }

    public void setIngredientMeasurements(ArrayList<Integer> ingredientMeasurements) {
        this.ingredientMeasurements = ingredientMeasurements;
    }

    public void addCookingInstruction(Instruction newInstruction){
        //Needs to still be implemented. This essentially adds an instruction object to the ArrayList called cookingInstruction.
        //Conversion between object and ID also needs to occur
        //Relies on the instruction class
    }

    public void removeCookingInstruction(int instructionID){
        //Needs to still be implemented.
        //Uses some time of search to find specified element and then remove it from the list.
    }

    public void addIngredientMeasurement(MeasuredIngredient newMeasuredIngredient){
        //Needs to still be implemented,
        //Relies on the MeasuredIngredient class
    }

    public void removeIngredientMeasurement(){
        //Needs to still be implemented
    }
    */

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

        /*public String getRecipeURL() {
            return recipeURL;
        }

        public void setRecipeURL(String recipeURL) {
            this.recipeURL = recipeURL;
        }
        */

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
