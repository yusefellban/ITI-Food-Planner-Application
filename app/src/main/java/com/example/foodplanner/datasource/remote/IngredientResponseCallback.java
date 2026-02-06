package com.example.foodplanner.datasource.remote;

import com.example.foodplanner.model.Ingredient;

import java.util.List;

public interface IngredientResponseCallback {
    void onSuccess(List<Ingredient> ingredients);
    void onError(String error);
}
