package com.example.foodplanner.service;

import com.example.foodplanner.model.Ingredient;

import java.util.List;

public interface IngredientGetResponse {
    void onSuccess(List<Ingredient> ingredients);
    void onError(String error);
}
