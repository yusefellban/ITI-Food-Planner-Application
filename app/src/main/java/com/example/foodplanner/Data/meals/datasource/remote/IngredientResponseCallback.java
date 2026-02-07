package com.example.foodplanner.Data.meals.datasource.remote;

import com.example.foodplanner.Data.meals.model.Ingredient;

import java.util.List;

public interface IngredientResponseCallback {
    void onSuccess(List<Ingredient> ingredients);
    void onError(String error);
}
