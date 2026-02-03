package com.example.foodplanner.service;

import android.util.Log;

import com.example.foodplanner.Entity.CategoriesResponse;
import com.example.foodplanner.Entity.IngredientResponse;
import com.example.foodplanner.remote.MealApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetApiService {
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static Retrofit retrofit = null;

   private static final MealApiService apiService = getRetrofit().create(MealApiService.class);

   public static void getAllCategories(CategoriesGetResponse categoriesGetResponse){
      apiService.getAllCategories().enqueue(new Callback<CategoriesResponse>() {
          @Override
          public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
              if (response.body() != null && response.body().getCategories() != null) {
                  categoriesGetResponse.onSuccess(response.body().getCategories());
              }else {
                  categoriesGetResponse.onError("No Categories found");
              }
          }

          @Override
          public void onFailure(Call<CategoriesResponse> call, Throwable t) {
              categoriesGetResponse.onError(t.getMessage());
              Log.d("API-SERVICES", " can not get any categories");
          }
      });
   }

   public static void getAllIngredients(IngredientGetResponse ingredientGetResponse){
       apiService.getAllIngredients().enqueue(new Callback<IngredientResponse>() {
           @Override
           public void onResponse(Call<IngredientResponse> call, Response<IngredientResponse> response) {
               if (response.body() != null && response.body().getIngredients() != null) {
                   ingredientGetResponse.onSuccess(response.body().getIngredients());
               }else {
                   ingredientGetResponse.onError("No Ingredients found");
               }
           }

           @Override
           public void onFailure(Call<IngredientResponse> call, Throwable t) {
               ingredientGetResponse.onError(t.getMessage());
               Log.d("API-SERVICES", " can not get any Ingredients");
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


}
