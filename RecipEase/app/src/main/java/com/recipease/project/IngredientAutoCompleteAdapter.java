package com.recipease.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Archit on 3/20/18.
 */

public class IngredientAutoCompleteAdapter extends ArrayAdapter<Ingredient> {

    private Context context;
    private List<Ingredient> ingredientList, suggestions, tempItems;
    private int resource, textViewResourceId;

    public IngredientAutoCompleteAdapter(Context context, int r, int tvR, List<Ingredient> in){
        super(context,r,tvR,in);
        this.context = context;
        ingredientList = in;
        suggestions = new ArrayList<Ingredient>();
        tempItems = new ArrayList<Ingredient>(ingredientList);
    }



    public View getView(int position, View convertView, ViewGroup parent){

        View view = convertView;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.select_dialog_item,parent,false);
        }

        Ingredient in = ingredientList.get(position);
        if(in!=null){
            TextView lbl = (TextView) view.findViewById((R.id.lbl_name));
            if(lbl!=null){
                lbl.setText(in.getName());
                lbl.setTag(in.getName());
            }
        }
        return view;
    }

    public Filter getFilter(){
        return inFilter;
    }

    Filter inFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Ingredient) resultValue).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Ingredient people : tempItems) {
                    if (people.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Ingredient> filterList = (ArrayList<Ingredient>) results.values;
            if (results != null && results.count > 0) {
                filterList.clear();
                for (Ingredient people : filterList) {
                    filterList.add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };

}