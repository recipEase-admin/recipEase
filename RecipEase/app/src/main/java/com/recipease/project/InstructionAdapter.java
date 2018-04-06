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

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.ViewHolder> {

    private ArrayList<String> instructions;
    private Context context;

    InstructionAdapter(Context context, ArrayList<String> instructions) {
        this.instructions = instructions;
        this.context = context;
    }

    @Override
    public InstructionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_selected_ingredient, parent, false));
    }

    @Override
    public void onBindViewHolder(InstructionAdapter.ViewHolder holder, int position) {
        String currentInstruction = instructions.get(position);
        holder.bindTo(currentInstruction);
    }

    @Override
    public int getItemCount() {
        return instructions.size();
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
                    instructions.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }

        void bindTo(String currentInstruction) {
            //Populate the textviews with data
            nameText.setText(currentInstruction);
            trash.setImageResource(R.drawable.ic_trash);
        }

    }

}
