package com.example.foodplanner.view.mealDetailsScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailsScreenIngredientAdapter extends RecyclerView.Adapter<DetailsScreenIngredientAdapter.ViewHolder>{
    private Context context;
    private HashMap<String,String> ingredientCard;
    List<String> ingredientList;
    List<String> measureList;

    public DetailsScreenIngredientAdapter(Context context, HashMap<String, String> ingredientCard) {
        this.context = context;
        this.ingredientCard = ingredientCard;
        ingredientList=new ArrayList<>(this.ingredientCard.keySet());
        measureList=new ArrayList<>(this.ingredientCard.values());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.details_ingredient_column, parent, false);
        return new DetailsScreenIngredientAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     String ingredientValue=ingredientList.get(position);
     String measureValue=measureList.get(position);
     holder.name.setText(ingredientValue);
     holder.measure.setText(measureValue);

        Glide.with(context).load("https://www.themealdb.com/images/ingredients/"+ingredientValue+"-small.png").into(holder.image);

    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name,measure;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.IngredientsImage);
            measure = itemView.findViewById(R.id.IngredientsMeasure);
            name = itemView.findViewById(R.id.IngredientsName);
        }
    }
}
