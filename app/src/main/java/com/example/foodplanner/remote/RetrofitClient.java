package com.example.foodplanner.remote;

import com.example.foodplanner.Entity.Meal;
import com.example.foodplanner.wrapper.MealDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static Retrofit retrofit = null;

    public static MealApiService getApiService() {
        if (retrofit == null) {
            // Deserializer
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Meal.class, new MealDeserializer())
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(MealApiService.class);
    }
}