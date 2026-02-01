package com.example.foodplanner.service;

import com.example.foodplanner.Entity.Meal;

public interface MealCallback {
    void onSuccess(Meal meal);
    void onError(String error);
}

