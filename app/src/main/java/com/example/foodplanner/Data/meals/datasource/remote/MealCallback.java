package com.example.foodplanner.Data.meals.datasource.remote;

import com.example.foodplanner.Data.meals.model.Meal;

public interface MealCallback {
    void onSuccess(Meal meal);
    void onError(String error);
}

