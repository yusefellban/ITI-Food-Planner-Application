package com.example.foodplanner.service;

import com.example.foodplanner.Entity.Category;
import com.example.foodplanner.Entity.Ingredient;

import java.util.List;

public interface IngredientGetResponse {
    void onSuccess(List<Ingredient> ingredients);
    void onError(String error);
}
