package com.example.foodplanner.datasource.remote;

import com.example.foodplanner.model.Meal;

public interface RandomMealCallback {
    void onSuccess(Meal meal);
    void onError(String error);
}

