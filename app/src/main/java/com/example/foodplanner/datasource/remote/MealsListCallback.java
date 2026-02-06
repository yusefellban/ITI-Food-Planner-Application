package com.example.foodplanner.datasource.remote;

import com.example.foodplanner.model.Meal;

import java.util.List;

public interface MealsListCallback {
    void onSuccess(List<Meal> meals);
    void onFailure(String error);
}
