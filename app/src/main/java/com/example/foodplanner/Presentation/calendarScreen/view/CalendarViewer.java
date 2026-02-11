package com.example.foodplanner.Presentation.calendarScreen.view;

import com.example.foodplanner.Data.mealplan.model.ScheduledMeal;
import com.example.foodplanner.Data.meals.model.wrapper.SelectedMeal;

import java.util.List;

public interface CalendarViewer {
    
    void showScheduledMeals(List<ScheduledMeal> meals, String date);
    
    void showEmptyState(String date);
    
    void showError(String message);
    
    void showOfflineBanner();
    
    void hideOfflineBanner();
    
    void showLoading();
    
    void hideLoading();
    
    void highlightDatesWithMeals(List<String> dates);
    
    void showMealAddedSuccess();
    
    void showMealRemovedSuccess();

    void navigateToMealDetails(SelectedMeal meal);
}
