package com.example.foodplanner.remote;

import com.example.foodplanner.Entity.CategoriesResponse;
import com.example.foodplanner.Entity.IngredientResponse;
import com.example.foodplanner.wrapper.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApiService {
    @GET("random.php")
    Call<MealResponse> getRandomMeal();

    @GET("lookup.php")
    Call<MealResponse> getMealByID(@Query("i") String mealId);

    @GET("categories.php")
    Call<CategoriesResponse> getAllCategories();

    @GET("list.php?i=list")
    Call<IngredientResponse> getAllIngredients();
}