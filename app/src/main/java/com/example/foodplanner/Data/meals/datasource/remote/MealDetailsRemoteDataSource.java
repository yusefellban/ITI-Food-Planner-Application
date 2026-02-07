package com.example.foodplanner.Data.meals.datasource.remote;

import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.Data.meals.model.wrapper.MealDeserializer;
import com.example.foodplanner.Data.meals.model.MealResponse;
import com.example.foodplanner.Data.network.MealApis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**This class acts as the Network Engine for fetching meal data. Its main job is to:
 Handle API Connections: Uses Retrofit to connect to TheMealDB API.
 Smart Data Parsing: It doesn't just download data; it uses a custom MealDeserializer to clean up the API response.
 It specifically transforms the messy "Ingredient1, Ingredient2..." fields into a clean, usable HashMap.
 Encapsulation: Keeps the networking logic (like Logging Interceptors and OkHttp configuration) hidden and private,
 providing a clean interface for the rest of the app to get data.
 Efficiency: Implements a Singleton pattern for the Retrofit instance to save system resources by avoiding repeated initializations.
 */
public class MealDetailsRemoteDataSource {

    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static Retrofit retrofit = null;

    /// no one can git this
    private MealApis getApiService() {
        if (retrofit == null) {

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Meal.class, new MealDeserializer())
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit.create(MealApis.class);
    }

    public void getMealDetails(int id,MealCallback mealCallback) {
        getApiService().getMealByID(String.valueOf(id)).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.body() != null && response.body().getMeals() != null) {
                    Meal meal = response.body().getMeals().get(0);
                    mealCallback.onSuccess(meal);
                } else {
                    mealCallback.onError("No meal found");
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                mealCallback.onError(t.getMessage());

            }
        });

    }



}
