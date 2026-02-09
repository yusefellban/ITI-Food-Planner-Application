package com.example.foodplanner.Presentation.favoritesScreen.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.R;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {
    
    private final Context context;
    private List<Meal> favoritesList;
    private final onFavoriteItemClickListener clickListener;

    public FavoritesAdapter(Context context, onFavoriteItemClickListener clickListener) {
        this.context = context;
        this.favoritesList = new ArrayList<>();
        this.clickListener = clickListener;
    }

    public void setFavoritesList(List<Meal> favoritesList) {
        this.favoritesList = favoritesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_meal, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Meal meal = favoritesList.get(position);
        
        holder.mealName.setText(meal.getName());
        
        if (meal.getCategory() != null && meal.getArea() != null) {
            holder.mealCategory.setText(meal.getCategory() + " • " + meal.getArea());
        } else if (meal.getCategory() != null) {
            holder.mealCategory.setText(meal.getCategory());
        }
        
        Glide.with(context)
                .load(meal.getThumbnailUrl())
                .placeholder(R.drawable.rounded_image)
                .centerCrop()
                .into(holder.mealImage);
        
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onMealClick(meal);
            }
        });
        
        holder.removeButton.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onRemoveClick(meal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView mealImage;
        TextView mealName;
        TextView mealCategory;
        ImageButton removeButton;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImage = itemView.findViewById(R.id.favoriteMealImage);
            mealName = itemView.findViewById(R.id.favoriteMealName);
            mealCategory = itemView.findViewById(R.id.favoriteMealCategory);
            removeButton = itemView.findViewById(R.id.favoriteRemoveButton);
        }
    }
}
