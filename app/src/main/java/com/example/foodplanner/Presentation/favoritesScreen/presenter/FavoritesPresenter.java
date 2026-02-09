package com.example.foodplanner.Presentation.favoritesScreen.presenter;

import com.example.foodplanner.Data.meals.model.Meal;

public interface FavoritesPresenter {
    
    void loadFavorites();
    
    void removeFavorite(int mealId);
    
    void onMealClicked(Meal meal);
    
    void onDestroy();
}
