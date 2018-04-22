package com.recipease.project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private FirebaseDatabase database;
    private ArrayList<Recipe> recipeList;
    private ArrayList<Integer> numIngredientsList;
    private Context context;
    private DatabaseReference database_reference;
    int nFavorites;

    RecipeAdapter(Context context, ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
        this.context = context;
    }
    RecipeAdapter(Context context, ArrayList<Recipe> recipeList, ArrayList<Integer> numIngredientsList) {
        this.recipeList = recipeList;
        this.context = context;
        this.numIngredientsList = numIngredientsList;
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {
        Recipe currentRecipe = recipeList.get(position);
        Integer currentNumIngredientsPresent = numIngredientsList.get(position);
        holder.bindTo(currentRecipe, currentNumIngredientsPresent);
        if (currentRecipe.getImageURL().equals("")) {
            Glide.with(context).load(R.drawable.no_image).into(holder.recipeImage);
        }
        else {
            Glide.with(context).load(currentRecipe.getImageURL()).into(holder.recipeImage);
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView titleText;
        private TextView numFavoritesText;
        private ImageView recipeImage;
        private TextView missingIngredientsText;

        ViewHolder(View itemView) {
            super(itemView);

            titleText = (TextView) itemView.findViewById(R.id.title);
            numFavoritesText = (TextView) itemView.findViewById(R.id.favorites);
            missingIngredientsText = (TextView) itemView.findViewById(R.id.missing_ingredients);
            recipeImage = itemView.findViewById(R.id.recipeImage);

            itemView.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Recipe currentRecipe = recipeList.get(getAdapterPosition());
                    Intent details = new Intent(context, RecipeDetailsActivity.class);
                    sendRecipe(details, currentRecipe);
                    startActivity(context, details, null);

                }
            });
        }

        void bindTo(Recipe currentRecipe, Integer currentNumIngredientsPresent) {
            //Populate the textviews with data
            titleText.setText(currentRecipe.getTitle());
            numFavoritesText.setText(String.format("%d", currentRecipe.getNumFavorites()));

            if(currentNumIngredientsPresent > 0) {
                int missingIngredients = Math.abs(currentRecipe.getCookingIngredients().size()-currentNumIngredientsPresent);
                missingIngredientsText.setText("Number of Matched Ingredients: " + currentNumIngredientsPresent + "/" + currentRecipe.getCookingIngredients().size());
            }
        }

    }

    private void sendRecipe(Intent intent, Recipe recipe_to_bring) {
        String title = recipe_to_bring.getTitle();
        String recipeID = recipe_to_bring.getRecipeID();
        String imageURL = recipe_to_bring.getImageURL();
        int numFavorites = recipe_to_bring.getNumFavorites();
        List<String> cookingIngredients = recipe_to_bring.getCookingIngredients();
        List<String> cookingInstructions = recipe_to_bring.getCookingInstructions();
        List<String> comments = recipe_to_bring.getComments();
        intent.putExtra("TITLE", title);
        intent.putExtra("UNIQUE ID", recipeID);
        intent.putExtra("IMAGE URL", imageURL);
        intent.putExtra("NUM FAVORITES", numFavorites);
        intent.putStringArrayListExtra("INGREDIENTS LIST", (ArrayList) cookingIngredients);
        intent.putStringArrayListExtra("INSTRUCTIONS LIST", (ArrayList) cookingInstructions);
        intent.putStringArrayListExtra("COMMENTS", (ArrayList) comments);
    }

}