package com.example.foodplanner.datasource.remote;

import android.util.Log;

import com.example.foodplanner.model.CategoriesResponse;
import com.example.foodplanner.model.IngredientResponse;
import com.example.foodplanner.model.Meal;
import com.example.foodplanner.model.wrapper.SendSelectedItem;
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

    /// home screen
    public void getRandomMeal(MealCallback callback) {

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

    public void getRandomMealsList(MealsListCallback listener) {
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

    /// Discovery screen

    public void getAllCategories(CategoriesResponseCallback callback) {
        mealApis.getAllCategories().enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                if (response.body() != null && response.body().getCategories() != null) {
                    callback.onSuccess(response.body().getCategories());
                } else {
                    callback.onError("No Categories found");
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                callback.onError(t.getMessage());
                Log.d("API-SERVICES", " can not get any categories");
            }
        });
    }

    public void getAllIngredients(IngredientResponseCallback ingredientResponseCallback) {
        mealApis.getAllIngredients().enqueue(new Callback<IngredientResponse>() {
            @Override
            public void onResponse(Call<IngredientResponse> call, Response<IngredientResponse> response) {
                if (response.body() != null && response.body().getIngredients() != null) {
                    ingredientResponseCallback.onSuccess(response.body().getIngredients());
                } else {
                    ingredientResponseCallback.onError("No Ingredients found");
                }
            }

            @Override
            public void onFailure(Call<IngredientResponse> call, Throwable t) {
                ingredientResponseCallback.onError(t.getMessage());
                Log.d("API-SERVICES", " can not get any Ingredients");
            }
        });
    }

    /// Filtered screen
    public  void getAllFilteredMeals(SendSelectedItem selectedItem, MealsListCallback mealsListCallback) {
        String selectedItemUrl = getSelectedItemUrl(selectedItem);
        mealApis.getFilteredMeals(selectedItemUrl).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                Log.d("API-DEBUG", "URL: " + call.request().url().toString());
                if (response.body() != null && response.body().getMeals() != null) {
                    mealsListCallback.onSuccess(response.body().getMeals());
                } else {
                    mealsListCallback.onFailure("Server Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                mealsListCallback.onFailure(t.getMessage());
                Log.d("API-SERVICES", " can not get any Filtered Item");
            }
        });

    }
    private String getSelectedItemUrl(SendSelectedItem selectedItem) {
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
