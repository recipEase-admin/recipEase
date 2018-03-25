package com.recipease.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CreateRecipeActivity extends DrawerActivity{

    Filter inputFilter;
    Uri imageURI;

    ArrayList<String> recipeInstructions = new ArrayList<String>();
    ArrayList<String> recipeIngredients = new ArrayList<String>();

    private EditText etIngredient;

    private ImageView ivRecipePicture;
    private String imageURL;

    private TextView theIngredients;
    private EditText etInstruction, etCookTime, etName;
    private TextView theInstructions;
    RadioGroup difficultyGroup;
    int difficulty = 0;
    int cookTime = 0;

    private String recipeTitle;

    private FirebaseAuth firebaseAuth;
    private String userID = "";
    FirebaseUser user;
  
    @Override

    // TODO: 3/16/2018 Check ingredient/image fields are filled

    // TODO: 3/16/2018 Add upload image functionality

    // TODO: 3/16/2018 Add inserting ingredient functionality

    // TODO: 3/16/2018 Add database functionality

    // TODO: Add remove ingredients ability


    
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.activity_create_recipe, null, false);
        mDrawerLayout.addView(contentView, 0);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        userID = user.getUid();

        etInstruction = findViewById(R.id.etInstruction);
        etCookTime = findViewById(R.id.cookTime);
        etName = findViewById(R.id.etName);
        theInstructions = findViewById(R.id.tvInstructions);

        etIngredient = findViewById(R.id.etIngredient);
        theIngredients = findViewById(R.id.tvIngredients);

        ivRecipePicture = findViewById(R.id.ivRecipePicture);

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
        recipeTitle = etName.getText().toString();

        // Check if all fields filled
        if( difficulty == 0 || recipeTitle.equals("") || etCookTime.getText().toString().equals("")) {
            showAlert("Please fill in all fields", "I'm on it");
        }
        else if(recipeInstructions.isEmpty()) {
            showAlert("Please add instructions for the recipe", "I'm on it");
        }
        else if(recipeIngredients.isEmpty()){
            showAlert("Please add ingredients for the recipe", "I'm on it");
        }
        else if(inputFilter.containsProfanity(recipeTitle)) {
            showAlert("Your recipe name contains profanity, please remove the profanity.", "I'll Handle It");
        }
        else {
            try{
                cookTime = Integer.parseInt(etCookTime.getText().toString());
                storeRecipeInDatabase();
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
    private void storeRecipeInDatabase() {
        createImageURL();
    }

    private void createImageURL() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("recipe_pictures" + "/" + userID + "/" + imageURI.getLastPathSegment());
        UploadTask uploadTask = storageRef.putFile(imageURI);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                imageURL = downloadUrl.toString();
                uploadRecipe();
            }
        });

    }

    private void uploadRecipe() {
        final Recipe newRecipe = new Recipe();
        newRecipe.setCookingInstructions( recipeInstructions );
        newRecipe.setCookTime( cookTime );
        newRecipe.setCookingIngredients( recipeIngredients );
        newRecipe.setTitle( recipeTitle );
        newRecipe.setImageURL( imageURL );
        newRecipe.generateRecipeId();
        newRecipe.setNumFavorites(0);
        newRecipe.setOwnerID(userID);

        //Used to connect to the firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //References the root of the database
        DatabaseReference database_reference = database.getReference();
        //Add the new recipe to the database
        database_reference.child("recipes").child(Long.toString(newRecipe.getRecipeID())).setValue(newRecipe);
        //Give the owner (creator) of the new recipe the recipeID
        database_reference.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot the_user) {
                User user = the_user.getValue(User.class);
                ArrayList<Long> recipesOwned;
                if (user.getRecipesOwned() == null) {
                    recipesOwned = new ArrayList<Long>();
                }
                else {
                    recipesOwned = new ArrayList(user.getRecipesOwned());
                }
                recipesOwned.add(newRecipe.getRecipeID());
                user.setRecipesOwned(recipesOwned);
                the_user.getRef().setValue(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "onCancelled", databaseError.toException());
            }
        });
        showAlert("Recipe successfully uploaded!", "Cool, I'm hyped");
    }

    //Called when imageView is clicked
    private static final int READ_REQUEST_CODE = 42;
    public void selectRecipePicture(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Glide.with(this).load(uri).centerCrop().into(ivRecipePicture);
                imageURI = uri;
            }
        }
    }
}
