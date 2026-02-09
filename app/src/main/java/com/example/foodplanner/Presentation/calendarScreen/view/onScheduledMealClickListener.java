package com.example.foodplanner.Presentation.calendarScreen.view;

import com.example.foodplanner.Data.mealplan.model.ScheduledMeal;

public interface onScheduledMealClickListener {
    
    void onMealClick(ScheduledMeal meal);
    
    void onRemoveClick(ScheduledMeal meal);
}
