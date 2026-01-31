package com.example.foodplanner.wrapper;

import com.example.foodplanner.Entity.Meal;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MealResponse {
    @SerializedName("meals")
    private List<Meal> meals;

    public List<Meal> getMeals() { return meals; }
}