package com.example.foodplanner.Presentation.calendarScreen.presenter;

import com.example.foodplanner.Data.mealplan.model.ScheduledMeal;
import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.Data.meals.model.wrapper.SelectedMeal;

public interface CalendarPresenter {
    
    void loadMonth(int year, int month);
    
    void onDaySelected(String date);
    
    void addMealToDate(Meal meal, String date);
    
    void removeMeal(String mealId, String date);
    
    void checkConnectivity();
    
    void onDestroy();

    void onMealClicked(ScheduledMeal meal);

}
