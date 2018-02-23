package com.recipease.recipease;

/**
 * Created by robert matyjek on 2/19/2018.
 */

public class Measurement {
    private int amount;
    private String unit;


    public int getAmount(){
        return amount;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }

    public String getUnit(){
        return unit;
    }

    public void SetUnit(String unit) {
        this.unit = unit;
    }
}
