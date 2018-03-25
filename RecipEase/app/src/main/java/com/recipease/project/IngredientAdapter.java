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

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private ArrayList<Ingredient> checkedIngredientList;
    private Context context;

    IngredientAdapter(Context context, ArrayList<Ingredient> checkedIngredientList) {
        this.checkedIngredientList = checkedIngredientList;
        this.context = context;
    }

    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_selected_ingredient, parent, false));
    }

    @Override
    public void onBindViewHolder(IngredientAdapter.ViewHolder holder, int position) {
        Ingredient currentIngredient = checkedIngredientList.get(position);
        holder.bindTo(currentIngredient);
    }

    @Override
    public int getItemCount() {
        return checkedIngredientList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nameText;
        private ImageView trash;

        ViewHolder(View itemView) {
            super(itemView);

            nameText = (TextView) itemView.findViewById(R.id.name);
            trash = itemView.findViewById(R.id.trash);

            itemView.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    checkedIngredientList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }

        void bindTo(Ingredient currentIngredient) {
            //Populate the textviews with data
            nameText.setText(currentIngredient.getName());
            trash.setImageResource(R.drawable.ic_trash);
        }

    }

}