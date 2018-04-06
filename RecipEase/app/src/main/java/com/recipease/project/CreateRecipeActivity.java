package com.recipease.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CreateRecipeActivity extends DrawerActivity{

    Filter inputFilter;
    Uri imageURI;

    ArrayList<String> recipeInstructions = new ArrayList<String>();
    ArrayList<String> recipeIngredients = new ArrayList<String>();


    private ImageView ivRecipePicture;
    private String imageURL;

    private EditText etInstruction, etName;
    private Button btCreateRecipe;
    private ImageButton btCreateIngredient;

    private String recipeTitle;

    private FirebaseAuth firebaseAuth;
    private String userID = "";
    FirebaseUser user;

    private IngredientAdapter ingredientAdapter;

    private InstructionAdapter instructionAdapter;

    private RecyclerView instructionRecyclerView;

    String[] ingredientNames;

    private FirebaseDatabase database;
    private DatabaseReference database_reference;

    private RecyclerView ingredientRecyclerView;
    private ArrayList<Ingredient> ingredientList;
    IngredientAutoCompleteAdapter ingredientAutoCompleteAdapter;

    private ArrayList<Ingredient> checked_ingredients;

    private AutoCompleteTextView actv;


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

        //Used to connect to the firebase database
        database = FirebaseDatabase.getInstance();
        //References the root of the database
        database_reference = database.getReference();

        ingredientList = new ArrayList<Ingredient>();
        ingredientAutoCompleteAdapter = new IngredientAutoCompleteAdapter(this,R.layout.activity_create_recipe,R.id.lbl_name,ingredientList);
        getAllIngredients(ingredientAutoCompleteAdapter, ingredientList);


        checked_ingredients = new ArrayList<Ingredient>();
        ingredientAdapter = new IngredientAdapter(CreateRecipeActivity.this, checked_ingredients);
        ingredientRecyclerView = findViewById(R.id.createIngredientRecyclerView);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(CreateRecipeActivity.this));
        ingredientRecyclerView.setAdapter(ingredientAdapter);

        actv = (AutoCompleteTextView) findViewById(R.id.createActv);
        actv.setTextColor(Color.BLACK);
        actv.getBackground().mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);

        instructionAdapter = new InstructionAdapter(CreateRecipeActivity.this, recipeInstructions);
        instructionRecyclerView = findViewById(R.id.createInstructionRecyclerView);
        instructionRecyclerView.setLayoutManager(new LinearLayoutManager(CreateRecipeActivity.this));
        instructionRecyclerView.setAdapter(instructionAdapter);




        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        userID = user.getUid();

        etInstruction = findViewById(R.id.etInstruction);
        etName = findViewById(R.id.etName);
        btCreateRecipe = findViewById(R.id.btCreateRecipe);
        btCreateIngredient = findViewById(R.id.btCreateIngredient);


        ivRecipePicture = findViewById(R.id.ivRecipePicture);

        inputFilter = new Filter();

        imageURL = "";

        etInstruction.setTextColor(Color.BLACK);
        etInstruction.getBackground().mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_item_ingredient, menu);
        return true;
    }

    //Adding and removing ingredients from set
    public void addItem(View view) {
        TextView lbl = (TextView) view;
        String selection = lbl.getText().toString();
        for (int i = 0; i < ingredientList.size(); i++) {
            if (ingredientList.get(i).getName().equals(selection)) {
                checked_ingredients.add(ingredientList.get(i));
                actv.dismissDropDown();
                hideKeyboard();
                actv.setText("");
                return;
            }
        }
    }

    //Returns a list of all ingredients
    public void getAllIngredients(final IngredientAutoCompleteAdapter ingredientAdapter, final ArrayList<Ingredient> ingredientList) {
        // Read ingredients in from the database and convert them to an ArrayList of Ingredient objects
        database_reference.child("ingredientRecipes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot all_ingredients) {
                //Loop through each separate ingredient
                for (DataSnapshot single_ingredient : all_ingredients.getChildren()) {
                    //Create a new ingredient object
                    Ingredient ingredient = single_ingredient.getValue(Ingredient.class);
                    //populateIngredient(new_ingredient, single_ingredient);
                    //Adds this new ingredient to the ingredient arraylist
                    ingredientList.add(ingredient);
                }
                ingredientNames = new String[ingredientList.size()];
                for (int i = 0; i < ingredientList.size(); i++) {
                    ingredientNames[i] = ingredientList.get(i).getName();
                }
                //Asynchronous so have to use this to notify adapter when finished
                ingredientAdapter.notifyDataSetChanged();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateRecipeActivity.this,R.layout.item_ingredient,ingredientNames);
                actv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "onCancelled", databaseError.toException());
            }
        });
        return;

    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //Runs when Create Recipe button is clicked
    public void createRecipe(View v) {
        //Lock button until done
        btCreateRecipe.setEnabled(false);
        // Get input from fields
        recipeTitle = etName.getText().toString();
        for (int i = 0; i < checked_ingredients.size(); i++) {
            recipeIngredients.add(checked_ingredients.get(i).getName());
        }

        // Check if all fields filled
        if(recipeTitle.equals("")) {
            showAlert("Please fill in all fields", "I'm on it");
            //Unlock button
            btCreateRecipe.setEnabled(true);
        }
        else if(recipeInstructions.isEmpty()) {
            showAlert("Please add instructions for the recipe", "I'm on it");
            //Unlock button
            btCreateRecipe.setEnabled(true);
        }
        else if(recipeIngredients.isEmpty()){
            showAlert("Please add ingredients for the recipe", "I'm on it");
            //Unlock button
            btCreateRecipe.setEnabled(true);
        }
        else if(inputFilter.containsProfanity(recipeTitle)) {
            showAlert("Your recipe name contains profanity, please remove the profanity.", "I'll Handle It");
            //Unlock button
            btCreateRecipe.setEnabled(true);
        }
        else {
            try {
                storeRecipeInDatabase();
            }
            catch(Exception e) {
                Recipe uploaded = uploadRecipe();
                addRecipeToIngredient(uploaded);
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
                Recipe uploaded = uploadRecipe();
                addRecipeToIngredient(uploaded);
            }
        });

    }

    private Recipe uploadRecipe() {
        final Recipe newRecipe = new Recipe();
        newRecipe.setCookingInstructions( recipeInstructions );
        newRecipe.setCookingIngredients( recipeIngredients );
        newRecipe.setTitle( recipeTitle );
        newRecipe.setImageURL( imageURL );
        newRecipe.generateRecipeID();
        newRecipe.setNumFavorites(0);
        newRecipe.setOwnerID(userID);

        //Used to connect to the firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //References the root of the database
        DatabaseReference database_reference = database.getReference();
        //Add the new recipe to the database
        database_reference.child("recipes").child(newRecipe.getRecipeID()).setValue(newRecipe);
        //Give the owner (creator) of the new recipe the recipeID
        database_reference.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot the_user) {
                User user = the_user.getValue(User.class);
                ArrayList<String> recipesOwned;
                if (user.getRecipesOwned() == null) {
                    recipesOwned = new ArrayList<String>();
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
        return newRecipe;
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

    //Returns a list of all ingredients
    public void addRecipeToIngredient(final Recipe uploadedRecipe) {
        // Read ingredients in from the database and convert them to an ArrayList of Ingredient objects
        for (int i = 0; i < checked_ingredients.size(); i++) {
            System.out.println(checked_ingredients.get(i).getIngredientID());
            if (checked_ingredients.get(i).getIngredientID() == null) {
                checked_ingredients.get(i).generateIngredientId();
                ArrayList<String> recipesUsing = new ArrayList<String>();
                recipesUsing.add(uploadedRecipe.getRecipeID());
                checked_ingredients.get(i).setRecipesUsing(recipesUsing);
                database_reference.child("ingredientRecipes").child(Long.toString(checked_ingredients.get(i).getIngredientID())).setValue(checked_ingredients.get(i));
            }
            else {
                database_reference.child("ingredientRecipes").child(Long.toString(checked_ingredients.get(i).getIngredientID())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot the_ingredient) {
                        //Create a new ingredient object
                        Ingredient ingredient = the_ingredient.getValue(Ingredient.class);
                        ArrayList<String> recipesUsing;
                        if (ingredient.getRecipesUsing() == null) {
                            recipesUsing = new ArrayList<String>();
                        } else {
                            recipesUsing = new ArrayList(ingredient.getRecipesUsing());
                        }
                        recipesUsing.add(uploadedRecipe.getRecipeID());
                        ingredient.setRecipesUsing(recipesUsing);
                        the_ingredient.getRef().setValue(ingredient);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i(TAG, "onCancelled", databaseError.toException());
                    }
                });
            }
        }
        Toast.makeText(this, "Recipe successfully uploaded!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(CreateRecipeActivity.this, HomeActivity.class);
        startActivity(intent);
        return;
    }

    public void createIngredient(View view) {
        String ingredient_name = actv.getText().toString();
        final Ingredient newIngredient = new Ingredient();
        newIngredient.setName(ingredient_name);

        checked_ingredients.add(newIngredient);
        ingredientAdapter.notifyItemInserted(checked_ingredients.size()-1);
        actv.dismissDropDown();
        actv.setText("");
        actv.setHint("Enter Ingredient Name");
        hideKeyboard();
    }

    public void createInstruction(View view) {
        String instruction = etInstruction.getText().toString();

        if(inputFilter.containsProfanity(instruction)) {
            showAlert("Your instruction contains profanity, please remove the profanity.", "I'll Handle It");
        }
        else if(instruction.equals("")) {
            showAlert("Please enter an instruction first", "I'm On It");
        }
        else {
            recipeInstructions.add(instruction);
            instructionAdapter.notifyItemInserted(recipeInstructions.size() - 1);
            hideKeyboard();
            etInstruction.setText("");
            etInstruction.setHint("Enter Cooking Instruction");
        }
    }
}