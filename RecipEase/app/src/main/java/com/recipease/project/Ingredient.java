package com.recipease.project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert matyjek on 2/19/2018.
 * Last updated by andrew ratz on 2/25/2018.
 */

public class Ingredient {
    private String name;
    private Long ingredientID;
    private List<Long> recipesUsing;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(long ingredientID) {
        this.ingredientID = ingredientID;
    }

    public List<Long> getRecipesUsing() {
        return recipesUsing;
    }

    public void setRecipesUsing(ArrayList<Long> recipesUsing) {
        this.recipesUsing = recipesUsing;
    }

}
