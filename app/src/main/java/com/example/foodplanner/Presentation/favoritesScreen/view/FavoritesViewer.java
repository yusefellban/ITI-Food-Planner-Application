package com.example.foodplanner.Presentation.favoritesScreen.view;

import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.Data.meals.model.wrapper.SelectedMeal;

import java.util.List;

public interface FavoritesViewer {
    
    void showFavorites(List<Meal> favorites);
    
    void showEmptyState();
    
    void showError(String message);
    
    void navigateToMealDetails(SelectedMeal selectedMeal);
    
    void showLoading();
    
    void hideLoading();
}
