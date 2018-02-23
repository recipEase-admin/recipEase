package com.recipease.recipease;

/**
 * Created by pdiddy on 2/19/2018.
 */

public class MeasuredIngredient {

    Measurement measurement;
    Ingredient ingredient;

    public MeasuredIngredient( Measurement measurement, Ingredient ingredient ) {
        this.measurement = measurement;
        this.ingredient = ingredient;
    }

    public void setIngredient( Ingredient newIngredient ) {

    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public void setMeasurement( Measurement newMeasurement ) {
        this.measurement = newMeasurement;
    }

    public Measurement getMeasurement() {
        return this.measurement;
    }
}
