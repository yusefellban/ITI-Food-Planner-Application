package com.example.foodplanner.Data.meals.datasource.remote;

import com.example.foodplanner.Data.meals.model.Meal;

import java.util.List;

public interface MealsListCallback {
    void onSuccess(List<Meal> meals);
    void onFailure(String error);
}
