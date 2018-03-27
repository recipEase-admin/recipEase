package com.recipease.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etBio, etDisplayName;
    private ImageView ivProfilePicture;
    private String email, password, bio, displayName, imageURL;
    private FirebaseAuth mAuth;
    Filter inputFilter;
    Uri imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView tvLogo = (TextView) findViewById(R.id.logoText);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Painter.ttf");
        tvLogo.setTypeface(font);

        mAuth = FirebaseAuth.getInstance();
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etBio = (EditText) findViewById(R.id.etBio);
        etDisplayName = (EditText) findViewById(R.id.etDisplayName);
        ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
        inputFilter = new Filter();
        imageURI = Uri.parse("android.resource://" + getPackageName() + "/drawable/ic_default_profile");
    }

    public void registerUser(View v) {
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        bio = etBio.getText().toString();
        displayName = etDisplayName.getText().toString();
        String[] input = {email, password, bio, displayName};

        if (allFieldsFilled()) {
            for (String s : input) {
                if (inputFilter.containsProfanity(s)) {
                    showAlert("Your input contains profanity, please remove it before continuing", "I'm on it");
                    return;
                }
            }
            createFirebaseUser();
        } else {
            showAlert("Please fill in all fields", "Try again");
        }
    }

    private void showAlert(String messageToSay, String buttonText) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(messageToSay);
        alertDialogBuilder.setPositiveButton(buttonText,
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
        for (String tfValue : tfValues) {
            if (tfValue.equals("")) return false;
        }
        if (imageURI == null) {
            return false;
        }
        return true;
    }

    private void createFirebaseUser() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            createUserInDatabase();
                            Intent i = new Intent(RegisterActivity.this, AnimationActivity.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createUserInDatabase() {
        createImageURL();
    }

    private void createImageURL() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("profile_pictures" + "/" + mAuth.getCurrentUser().getUid() + "/" + imageURI.getLastPathSegment());
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
                uploadUser();
            }
        });

    }

    private void uploadUser() {
        //Used to connect to the firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //References the root of the database
        DatabaseReference database_reference = database.getReference().child("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid;
        if (user != null) {
            uid = user.getUid();
            User new_user = new User();
            new_user.setEmail(email);
            new_user.setBio(bio);
            new_user.setDisplayName(displayName);
            new_user.setUid(uid);
            new_user.setImageURL(imageURL);
            database_reference.child(uid).setValue(new_user);
        }
    }

    //Called when imageView is clicked
    private static final int READ_REQUEST_CODE = 42;
    public void selectProfilePicture(View v) {
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
                Glide.with(this).load(uri).centerCrop().into(ivProfilePicture);
                imageURI = uri;
            }
        }
    }
}
