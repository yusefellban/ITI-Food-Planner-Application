package com.example.foodplanner.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {

    private static Network INSTANCE;
    private MealApis mealApis;
    private static Retrofit retrofit;

    public static Network getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Network();
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return INSTANCE;
    }


    public MealApis getMealsAPI() {
        mealApis = retrofit.create(MealApis.class);
        return mealApis;
    }

}
