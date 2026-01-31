package com.example.foodplanner.remote;

import com.example.foodplanner.wrapper.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MealApiService {
    @GET("random.php")
    Call<MealResponse> getRandomMeal();
}