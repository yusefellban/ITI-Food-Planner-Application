package com.example.foodplanner.service;

import android.util.Log;

import com.example.foodplanner.datasource.remote.CategoriesResponseCallback;
import com.example.foodplanner.datasource.remote.IngredientResponseCallback;
import com.example.foodplanner.datasource.remote.RandomMealsListCallback;
import com.example.foodplanner.model.CategoriesResponse;
import com.example.foodplanner.model.IngredientResponse;
import com.example.foodplanner.network.MealApis;
import com.example.foodplanner.model.wrapper.MealResponse;
import com.example.foodplanner.model.wrapper.SendSelectedItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetApiService {
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static Retrofit retrofit = null;

    private static final MealApis apiService = getRetrofit().create(MealApis.class);

    public static void getAllFilteredMeals(SendSelectedItem selectedItem, RandomMealsListCallback randomMealsListCallback) {
        String selectedItemUrl = getSelectedItemUrl(selectedItem);
        apiService.getFilteredMeals(selectedItemUrl).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                Log.d("API-DEBUG", "URL: " + call.request().url().toString());
                if (response.body() != null && response.body().getMeals() != null) {
                    randomMealsListCallback.onSuccess(response.body().getMeals());
                } else {
                    randomMealsListCallback.onFailure("Server Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                randomMealsListCallback.onFailure(t.getMessage());
                Log.d("API-SERVICES", " can not get any Filtered Item");
            }
        });

    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static String getSelectedItemUrl(SendSelectedItem selectedItem) {
        String res = null;
        if (selectedItem.getType() == 1) {//category
            res = "filter.php?c=" + selectedItem.getName().toLowerCase().replace(" ", "_");
        } else if(selectedItem.getType() == 2) {//Ingreadient
            res = "filter.php?i=" + selectedItem.getName().toLowerCase().replace(" ", "_");
        } else if(selectedItem.getType() == 3) {//country
            res = "filter.php?a=" + selectedItem.getName().toLowerCase().replace(" ", "_");

        }

        return res;
    }
}
