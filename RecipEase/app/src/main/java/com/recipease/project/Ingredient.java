package com.recipease.recipease;

/**
 * Created by robert matyjek on 2/19/2018.
 */

public class Ingredient {
    private String name;
    private int ingredientID;

    public Ingredient(int ingredientID){
        this.ingredientID= ingredientID;
    }

    public Ingredient(String name, int ingredientID){
        this.name = name;
        this.ingredientID = ingredientID;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getId(){
        return ingredientID;
    }

}
