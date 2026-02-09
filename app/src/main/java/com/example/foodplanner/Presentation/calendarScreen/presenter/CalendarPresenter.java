package com.example.foodplanner.Presentation.calendarScreen.presenter;

import com.example.foodplanner.Data.meals.model.Meal;

public interface CalendarPresenter {
    
    void loadMonth(int year, int month);
    
    void onDaySelected(String date);
    
    void addMealToDate(Meal meal, String date);
    
    void removeMeal(String mealId, String date);
    
    void checkConnectivity();
    
    void onDestroy();
}
