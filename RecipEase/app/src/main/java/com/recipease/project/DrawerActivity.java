package com.recipease.project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class BaseActivity extends FragmentActivity {

    protected DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.base_layout);

        mDrawerList = (ListView)findViewById(R.id.navList);

        String[] osArray = { "Profile", "Create A Recipe", "About", "Logout" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.bringToFront();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent goToProfile = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(goToProfile);
                        break;
                    case 1:
                        Intent goToCreateRecipe = new Intent(HomeActivity.this, CreateRecipeActivity.class);
                        startActivity(goToCreateRecipe);
                        break;
                    case 2:
                        Intent goToAbout = new Intent ( HomeActivity.this, AboutActivity.class);
                        startActivity(goToAbout);
                        break;
                    case 3: //Logout
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(HomeActivity.this, "Succesfully logged out", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(i);
                        break;
                }
            }
        });
    }

}