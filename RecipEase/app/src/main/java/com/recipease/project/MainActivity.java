package com.recipease.project;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        TextView tvLogo = (TextView) findViewById(R.id.logoText);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Painter.ttf");
        tvLogo.setTypeface(font);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        firebaseAuth = FirebaseAuth.getInstance();

        etPassword.setTransformationMethod(new PasswordMethod());


        //http://www.recipease.com/recipe/?id=570 is Jesse, for example - not anymore
        //https://f1xgsqmynnpnthilnfi7aq-on.drv.tw/RecipEase/?id=570 now equals Jesse if user has app, else download page
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            final String recipeID = uri.getQueryParameter("id");
            if (firebaseAuth.getCurrentUser() == null) {
                (firebaseAuth.signInAnonymously())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Logging in as guest", Toast.LENGTH_SHORT).show();
                                    viewRecipeFromRedirect(recipeID);                                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                                } else {
                                    Log.e("ERROR", task.getException().toString());
                                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
            else {
                viewRecipeFromRedirect(recipeID);
            }
        }

        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth authData) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if (user.isAnonymous()) {
                        Toast.makeText(MainActivity.this, "Logging in as guest", Toast.LENGTH_SHORT).show();
                    }
                    // user is logged in
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(i);
                } else {
                    // user is not logged in
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        return;
    }

    public void btnLogin_Click(View v) {

        if( etUsername.getText().toString().equals("") || etPassword.getText().toString().equals("") ) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Error, invalid login information");
            alertDialogBuilder.setPositiveButton("Try Again",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        else {
            final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "Please wait...", "Processing...", true);

            (firebaseAuth.signInWithEmailAndPassword(etUsername.getText().toString(), etPassword.getText().toString()))
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if (task.isSuccessful()) {
                                System.out.println("The current user: " + firebaseAuth.getCurrentUser().getUid());
                                Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(i);
                            } else {
                                Log.e("ERROR", task.getException().toString());
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }

    public void btnRegister_Click(View v) {
        Intent i = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(i);
    }

    private void viewRecipeFromRedirect(String recipeID) {
        FirebaseDatabase database;
        DatabaseReference database_reference;
        database = FirebaseDatabase.getInstance();
        database_reference = database.getReference();
        database_reference.child("recipes").child(recipeID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Recipe recipe_to_bring = dataSnapshot.getValue(Recipe.class);
                if (recipe_to_bring == null) {
                    return;
                }
                else {
                    String title = recipe_to_bring.getTitle();
                    String recipeID = recipe_to_bring.getRecipeID();
                    String imageURL = recipe_to_bring.getImageURL();
                    int numFavorites = recipe_to_bring.getNumFavorites();
                    List<String> cookingIngredients = recipe_to_bring.getCookingIngredients();
                    List<String> cookingInstructions = recipe_to_bring.getCookingInstructions();
                    List<String> comments = recipe_to_bring.getComments();
                    Intent intent = new Intent(MainActivity.this, RecipeDetailsActivity.class);
                    intent.putExtra("TITLE", title);
                    intent.putExtra("UNIQUE ID", recipeID);
                    intent.putExtra("IMAGE URL", imageURL);
                    intent.putExtra("NUM FAVORITES", numFavorites);
                    intent.putStringArrayListExtra("INGREDIENTS LIST", (ArrayList) cookingIngredients);
                    intent.putStringArrayListExtra("INSTRUCTIONS LIST", (ArrayList) cookingInstructions);
                    intent.putStringArrayListExtra("COMMENTS", (ArrayList) comments);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}