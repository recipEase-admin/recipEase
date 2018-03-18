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
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private ArrayList<Recipe> recipeList;
    private Context context;
    private int numIngredients;

    RecipeAdapter(Context context, ArrayList<Recipe> recipeList, int numIngredients) {
        this.recipeList = recipeList;
        this.context = context;
        this.numIngredients = numIngredients;
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {
        Recipe currentRecipe = recipeList.get(position);
        holder.bindTo(currentRecipe);
        Glide.with(context).load(currentRecipe.getImageURL()).into(holder.recipeImage);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView titleText;
        private TextView cookTimeText;
        private ImageView recipeImage;
        private TextView missingIngredientsText;

        ViewHolder(View itemView) {
            super(itemView);

            titleText = (TextView) itemView.findViewById(R.id.title);
            cookTimeText = (TextView) itemView.findViewById(R.id.cooktime);
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

        void bindTo(Recipe currentRecipe) {
            //Populate the textviews with data
            titleText.setText(currentRecipe.getTitle());
            cookTimeText.setText(String.format("Cook Time: %d minutes", currentRecipe.getCookTime()));

            int missingIngredients = currentRecipe.getCookingIngredients().size() - numIngredients;

            missingIngredientsText.setText(String.format("Number of Missing Ingredients: %d", missingIngredients));
        }

    }

    private void sendRecipe(Intent intent, Recipe recipe_to_bring) {
        String title = recipe_to_bring.getTitle();
        long recipeID = recipe_to_bring.getRecipeID();
        int cookTime = recipe_to_bring.getCookTime();
        String imageURL = recipe_to_bring.getImageURL();
        List<String> cookingIngredients = recipe_to_bring.getCookingIngredients();
        List<String> cookingInstructions = recipe_to_bring.getCookingInstructions();
        intent.putExtra("TITLE", title);
        intent.putExtra("UNIQUE ID", recipeID);
        intent.putExtra("COOK TIME", cookTime);
        intent.putExtra("IMAGE URL", imageURL);
        intent.putStringArrayListExtra("INGREDIENTS LIST", (ArrayList) cookingIngredients);
        intent.putStringArrayListExtra("INSTRUCTIONS LIST", (ArrayList) cookingInstructions);
    }

}