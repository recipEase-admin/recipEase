package com.recipease.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class RecipeDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        receiveRecipe();
    }

    private void receiveRecipe() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("TITLE");
        long recipeID = intent.getLongExtra("UNIQUE ID", 0);
        int cookTime = intent.getIntExtra("COOK TIME", 0);
        String imageURL = intent.getStringExtra("IMAGE URL");
        ArrayList<String> cookingIngredients = intent.getStringArrayListExtra("INGREDIENTS LIST");
        ArrayList<String> cookingInstructions = intent.getStringArrayListExtra("INSTRUCTIONS LIST");

        ImageView ivImageURL = (ImageView) findViewById(R.id.ivImageURL);
        Picasso
                .with(getBaseContext())
                .load(imageURL)
                .fit()
                .into(ivImageURL);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        TextView tvCookTime = (TextView) findViewById(R.id.tvCookTime);
        tvCookTime.setText(Integer.toString(cookTime));

        TextView tvCookingIngredients = (TextView) findViewById(R.id.tvCookingIngredients);
        tvCookingIngredients.setText(TextUtils.join("\n\n", cookingIngredients));

        TextView tvCookingInstructions = (TextView) findViewById(R.id.tvCookingInstructions);
        tvCookingInstructions.setText(TextUtils.join("\n\n", cookingInstructions));

    }
}


