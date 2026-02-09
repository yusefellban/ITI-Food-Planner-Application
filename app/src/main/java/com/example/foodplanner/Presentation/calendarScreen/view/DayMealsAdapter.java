package com.example.foodplanner.Presentation.calendarScreen.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Data.mealplan.model.ScheduledMeal;
import com.example.foodplanner.R;

import java.util.ArrayList;
import java.util.List;

public class DayMealsAdapter extends RecyclerView.Adapter<DayMealsAdapter.ViewHolder> {
    
    private List<ScheduledMeal> meals;
    private final onScheduledMealClickListener listener;

    public DayMealsAdapter(onScheduledMealClickListener listener) {
        this.meals = new ArrayList<>();
        this.listener = listener;
    }

    public void setMeals(List<ScheduledMeal> meals) {
        this.meals = meals != null ? meals : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScheduledMeal meal = meals.get(position);
        
        holder.mealName.setText(meal.getMealName());
        holder.mealCategory.setText(meal.getCategory());
        
        Glide.with(holder.itemView.getContext())
                .load(meal.getMealImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .circleCrop()
                .into(holder.mealImage);
        
        holder.itemView.setOnClickListener(v -> listener.onMealClick(meal));
        holder.removeButton.setOnClickListener(v -> listener.onRemoveClick(meal));
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mealImage;
        TextView mealName;
        TextView mealCategory;
        ImageButton removeButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImage = itemView.findViewById(R.id.dayMealImage);
            mealName = itemView.findViewById(R.id.dayMealName);
            mealCategory = itemView.findViewById(R.id.dayMealCategory);
            removeButton = itemView.findViewById(R.id.removeDayMealButton);
        }
    }
}
