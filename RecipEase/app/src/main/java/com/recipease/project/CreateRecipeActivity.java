package com.recipease.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class CreateRecipeActivity extends AppCompatActivity {

    Filter inputFilter;
    ArrayList<String> recipeInstructions = new ArrayList<String>();
    ArrayList<String> recipeIngredients = new ArrayList<String>();
    private EditText etInstruction;
    private EditText etIngredient;
    private TextView theInstructions;
    private TextView theIngredients;
    @Override

    // TODO: 3/16/2018 Check all fields filled when recipe submitted

    // TODO: 3/16/2018 Add upload image functionality

    // TODO: 3/16/2018 Add inserting ingredient functionality

    // TODO: 3/16/2018 Add database functionality

    // TODO: Add ability remove instructions / ingredients


    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        etInstruction = findViewById(R.id.etInstruction);
        theInstructions = findViewById(R.id.tvInstructions);
        etIngredient = findViewById(R.id.etIngredient);
        theIngredients = findViewById(R.id.tvIngredients);
        inputFilter = new Filter();
    }

    public void addInstructionToRecipe(View v) {
        // TODO: 3/16/2018 Apply Filtering System
        String newInstruction = etInstruction.getText().toString();
        theInstructions.setText("");
        if(inputFilter.containsProfanity(newInstruction)) {
            showAlert("You're instruction contains profanity, please remove it.", "I'll Handle It");
        }
        else {
            if( newInstruction.equals("Enter Cooking Instruction") ) {
                showAlert("Please enter an instruction first", "I'm On It");
            }
            else {
                // Add instruction to list
                recipeInstructions.add( newInstruction );
                int i = 1;
                for(String s : recipeInstructions) {
                    theInstructions.setText( theInstructions.getText().toString() + i + ") " + s + "\n" );
                    i++;
                }
                etInstruction.setText("Enter Cooking Instruction");
            }
        }

    }


    public void addIngredientToRecipe(View v) {
        // TODO: 3/28/18 Add Database functionality, add search method
        String newIngredient = etIngredient.getText().toString();
        theIngredients.setText("");
            if( newIngredient.equals("Enter Cooking Instruction") ) {
                showAlert("Please enter an ingredient first", "I'm On It");
            }
            else {
                // Add instruction to list
                recipeIngredients.add( newIngredient );
                int i = 1;
                for(String s : recipeIngredients) {
                    theIngredients.setText( theIngredients.getText().toString() + i + ") " + s + "\n" );
                    i++;
                }
                etIngredient.setText("Enter Cooking Ingredient");
            }


    }



    private boolean fieldIsEmpty( String fieldText ) {
        return fieldText.equals("");
    }

    private boolean allFieldsFilled() {
        return false;
    }


    public void showAlert(String message, String actionText) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(actionText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
