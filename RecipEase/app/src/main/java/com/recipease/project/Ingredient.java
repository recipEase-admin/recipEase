package com.recipease.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by robert matyjek on 2/19/2018.
 * Last updated by andrew ratz on 2/25/2018.
 */

public class Ingredient {
    private String name;
    private Long ingredientID;
    private List<String> recipesUsing;

    public void generateIngredientId() {
        this.ingredientID = new Date().getTime();
    }

    public Long getIngredientID() {
        return ingredientID;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRecipesUsing() {
        return recipesUsing;
    }

    public void setRecipesUsing(ArrayList<String> recipesUsing) {
        this.recipesUsing = recipesUsing;
    }

}