package com.recipease.project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private ArrayList<Ingredient> ingredientList;
    private Context context;

    IngredientAdapter(Context context, ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
        this.context = context;
    }

    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false));
    }

    @Override
    public void onBindViewHolder(IngredientAdapter.ViewHolder holder, int position) {
        Ingredient currentIngredient = ingredientList.get(position);
        holder.bindTo(currentIngredient);
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox nameCheckBox;

        ViewHolder(View itemView) {
            super(itemView);

            nameCheckBox = (CheckBox) itemView.findViewById(R.id.name);

        }

        void bindTo(Ingredient currentIngredient) {
            //Populate the checkboxes with data
            nameCheckBox.setText(currentIngredient.getName());
        }

    }
}