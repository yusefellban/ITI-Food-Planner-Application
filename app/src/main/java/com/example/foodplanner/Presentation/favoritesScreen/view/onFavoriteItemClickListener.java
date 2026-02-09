package com.example.foodplanner.Presentation.favoritesScreen.view;

import com.example.foodplanner.Data.meals.model.Meal;

public interface onFavoriteItemClickListener {
    
    void onMealClick(Meal meal);
    
    void onRemoveClick(Meal meal);
}
