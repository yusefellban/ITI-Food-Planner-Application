package com.example.foodplanner.datasource.remote;

import com.example.foodplanner.model.Meal;
import com.example.foodplanner.network.MealApis;
import com.example.foodplanner.network.Network;
import com.example.foodplanner.model.wrapper.MealResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealRemoteDataSource {
    private final MealApis mealApis;

    public MealRemoteDataSource() {
        mealApis = Network.getInstance().getMealsAPI();
    }

    public void getRandomMeal(RandomMealCallback callback) {

        mealApis.getRandomMeal().enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.body() != null && response.body().getMeals() != null) {
                    Meal meal = response.body().getMeals().get(0);
                    callback.onSuccess(meal);
                } else {
                    callback.onError("No meal found");
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getRandomMealsList(RandomMealsListCallback listener) {
        List<Meal> mealslist = new ArrayList<>();

        final int totalRequests = 8;
        final int[] completedRequests = {0};

        for (int i = 0; i < totalRequests; i++) {
            mealApis.getRandomMeal().enqueue(new Callback<MealResponse>() {
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
}
