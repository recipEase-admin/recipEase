package com.recipease.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by andrewratz on 2/21/18.
 */

public class RecipeAdapter extends ArrayAdapter<Recipe> {
    public RecipeAdapter(Context context, ArrayList<Recipe> recipeList) {
        super(context, 0, recipeList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Recipe recipe = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_recipe, parent, false);
        }
        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvCookTime = (TextView) convertView.findViewById(R.id.tvCookTime);
        ImageView ivImageURL = (ImageView) convertView.findViewById(R.id.ivImage);
        // Populate the data into the template view using the data object
        tvTitle.setText(recipe.getTitle());
        tvCookTime.setText(Integer.toString(recipe.getCookTime()));
        Picasso
                .with(getContext())
                .load(recipe.getImageURL())
                .fit()
                .into(ivImageURL);
        // Return the completed view to render on screen
        return convertView;
    }
}