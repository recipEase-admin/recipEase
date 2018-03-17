package com.recipease.project;


import java.io.Serializable;
import java.util.ArrayList;

public class DataWrapper implements Serializable{

    private ArrayList<Recipe> recipeList;

    public DataWrapper(ArrayList<Recipe> recipeData){
        this.recipeList = recipeData;
    }

    public ArrayList<Recipe> getRecipes(){
        return this.recipeList;
    }

}
