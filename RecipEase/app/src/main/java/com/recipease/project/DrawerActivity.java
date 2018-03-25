package com.recipease.project;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class DrawerActivity extends AppCompatActivity {

    protected DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch(menuItem.getItemId()){
                            case R.id.profile:
                                Intent intent=new Intent(DrawerActivity.this,ProfileActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.home:
                                Intent intentA=new Intent(DrawerActivity.this,HomeActivity.class);
                                startActivity(intentA);
                                break;
                            case R.id.create_recipe:
                                Intent intentb = new Intent(DrawerActivity.this, CreateRecipeActivity.class);
                                startActivity(intentb);
                                break;
                            case R.id.find_recipe:
                                Intent intentc = new Intent(DrawerActivity.this, IngredientSelector.class);
                                startActivity(intentc);
                                break;
                            case R.id.about:
                               Intent intentd = new Intent(DrawerActivity.this, AboutActivity.class);
                                startActivity(intentd);
                                break;
                            case R.id.logout:
                                FirebaseAuth.getInstance().signOut();
                                Toast.makeText(DrawerActivity.this, "Succesfully logged out", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(DrawerActivity.this, MainActivity.class);
                                startActivity(i);
                                break;


                        }
                        return true;
                }}
        );


}

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}