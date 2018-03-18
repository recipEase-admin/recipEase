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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CreateRecipeActivity extends AppCompatActivity {

    Filter inputFilter;
    ArrayList<String> recipeInstructions = new ArrayList<String>();
    private EditText etInstruction, etCookTime, etName;
    private TextView theInstructions;
    RadioGroup difficultyGroup;
    int difficulty = 0;

    @Override

    // TODO: 3/16/2018 Check ingredient/image fields are filled

    // TODO: 3/16/2018 Add upload image functionality

    // TODO: 3/16/2018 Add inserting ingredient functionality

    // TODO: 3/16/2018 Add database functionality

    // TODO: Add remove ingredients ability


    
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        etInstruction = findViewById(R.id.etInstruction);
        etCookTime = findViewById(R.id.cookTime);
        etName = findViewById(R.id.etName);
        theInstructions = findViewById(R.id.tvInstructions);
        difficultyGroup = (RadioGroup) findViewById(R.id.difficultyGroup);
        inputFilter = new Filter();

        difficultyGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.diff_1) {
                    //some code
                    difficulty = 1;
                } else if (checkedId == R.id.diff_2) {
                    //some code
                    difficulty = 2;
                }
                else if (checkedId == R.id.diff_3) {
                    //some code
                    difficulty = 3;
                }

            }

        });
    }

    public void addInstructionToRecipe(View v) {
        String newInstruction = etInstruction.getText().toString();

        if(inputFilter.containsProfanity(newInstruction)) {
            showAlert("Your instruction contains profanity, please remove the profanity.", "I'll Handle It");
        }
        else if(newInstruction.equals("")) {
            showAlert("Please enter an instruction first", "I'm On It");
        }
        else {
            // Add instruction to list
            recipeInstructions.add( newInstruction );
            setInstructionText();
        }

    }

    public void setInstructionText() {
        int i = 1;
        String textFieldText = "";
        for(String s : recipeInstructions) {
            textFieldText += i + ") " + s + "\n";
            i++;
        }
        theInstructions.setText(textFieldText);
        etInstruction.setHint("Enter Cooking Instruction");
    }

    public void removeInstruction(View v) {
        if( recipeInstructions.isEmpty() ) {
            showAlert("There are no instructions to delete", "Okay, I'll Add One");
        }
        else {
            recipeInstructions.remove(recipeInstructions.size() - 1);
            if(recipeInstructions.isEmpty()) {
                setInstructionText();
                etInstruction.setText("");
                etInstruction.setHint("Enter Cooking Instruction");
                theInstructions.setText("No Instructions Yet");
            }
            else {
                setInstructionText();
            }
        }
    }

    public void createRecipe(View v) {
        // Get input from fields
        String recipeName = etName.getText().toString();
        int cookTime = 0;

        // Check if all fields filled
        if( difficulty == 0 || recipeName.equals("") || etCookTime.getText().toString().equals("")) {
            showAlert("Please fill in all fields", "I'm on it");
        }
        else if(recipeInstructions.isEmpty()) {
            showAlert("Please add instructions for the recipe", "I'm on it");
        }
        else if(inputFilter.containsProfanity(recipeName)) {
            showAlert("Your recipe name contains profanity, please remove the profanity.", "I'll Handle It");
        }
        else {
            try{
                cookTime = Integer.parseInt(etCookTime.getText().toString());
            }
            catch(Exception e) {
                showAlert("Please enter a number for the time to prepare", "I'm on it");
            }
        }
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
