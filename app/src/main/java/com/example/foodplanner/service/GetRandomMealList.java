package com.example.foodplanner.service;

import com.example.foodplanner.Entity.Meal;
import com.example.foodplanner.remote.MealApiService;
import com.example.foodplanner.wrapper.MealResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetRandomMealList {

    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static Retrofit retrofit = null;

    ///

    public static void getMealsList(OnMealsLoadedListener listener) {
        List<Meal> mealslist = new ArrayList<>();
        MealApiService mealApiService = getRetrofit().create(MealApiService.class);

        final int totalRequests = 8;
        final int[] completedRequests = {0};

        for (int i = 0; i < totalRequests; i++) {
            mealApiService.getRandomMeal().enqueue(new Callback<MealResponse>() {
                @Override
                public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        mealslist.add(response.body().getMeals().get(0));
                    }

                    completedRequests[0]++;
                    if (completedRequests[0] == totalRequests) {
                        listener.onSuccess(mealslist);
                    }
                }

                @Override
                public void onFailure(Call<MealResponse> call, Throwable t) {
                    completedRequests[0]++;
                    if (completedRequests[0] == totalRequests) {
                        listener.onFailure("Can't get products List to home screen");
                    }
                }
            });
        }
    }

    public static Retrofit getRetrofit() {

        if (retrofit == null) {
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        } else {
            return retrofit;
        }

    }
}
