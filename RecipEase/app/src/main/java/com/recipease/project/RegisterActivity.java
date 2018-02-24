package com.recipease.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etBio, etDisplayName;
    private String email, password, bio, displayName;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etBio = (EditText) findViewById(R.id.etBio);
        etDisplayName = (EditText) findViewById(R.id.etDisplayName);
    }

    public void registerUser( View v ) {

        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        bio = etBio.getText().toString();
        displayName = etDisplayName.getText().toString();

        if( allFieldsFilled() ) {
            createNewUser();
            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(i);
        }
        else {
            showAlert( "Please fill in all fields", "Try again" );
        }
    }

    private void showAlert( String messageToSay, String buttonText ) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage( messageToSay );
        alertDialogBuilder.setPositiveButton( buttonText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private boolean allFieldsFilled() {

        // Store values to array list
        ArrayList<String> tfValues = new ArrayList<String>();
        tfValues.add(email);
        tfValues.add(password);
        tfValues.add(bio);
        tfValues.add(displayName);

        // Check for empty values
        for( String tfValue : tfValues ) {
            if( tfValue.equals( "" ) ) return false;
        }
        return true;
    }

    private void createNewUser() {
        // Create a new user in firebase system
        createFirebaseUser();
        createUserInDatabase();
    }

    private void createFirebaseUser() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createUserInDatabase() {

    }
}
