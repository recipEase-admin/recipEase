package com.recipease.recipease;

/**
 * Created by pdiddy on 2/19/2018.
 */

public class Instruction {

    private String content;
    private boolean isRequired;
    private int recipeID, stepNumber;

    public Instruction( Boolean isRequired, String content ) {

    }

    public String getContent() {
        return this.content;
    }

    public boolean isRequired() {
        return this.isRequired;
    }

    public int getRecipeID() {
        return this.recipeID;
    }
    public int getStepNumber() {
        return this.stepNumber;
    }

    public void setStepNumber( int newStepNumber ) {
        this.stepNumber = newStepNumber;
        // Update field in database
    }
}
