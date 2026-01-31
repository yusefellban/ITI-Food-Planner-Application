package com.example.foodplanner.service;

import com.example.foodplanner.Entity.Meal;

import java.util.List;

public interface OnMealsLoadedListener {
    void onSuccess(List<Meal> meals);
    void onFailure(String error);
}
