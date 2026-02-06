package com.example.foodplanner.network;

import com.example.foodplanner.model.CategoriesResponse;
import com.example.foodplanner.model.IngredientResponse;
import com.example.foodplanner.model.wrapper.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface MealApis {
    @GET("random.php")
    Call<MealResponse> getRandomMeal();

    @GET("lookup.php")
    Call<MealResponse> getMealByID(@Query("i") String mealId);

    @GET("categories.php")
    Call<CategoriesResponse> getAllCategories();

    @GET("list.php?i=list")
    Call<IngredientResponse> getAllIngredients();

    @GET
    Call<MealResponse> getFilteredMeals(@Url String url);
}