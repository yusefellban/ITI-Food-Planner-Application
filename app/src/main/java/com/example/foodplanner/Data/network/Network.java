package com.example.foodplanner.Data.network;

import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.Data.meals.model.wrapper.MealDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {

    private static Network INSTANCE;
    private MealApis mealApis;
    private static Retrofit retrofit;

    public static Network getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Network();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Meal.class, new MealDeserializer())
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                    .addCallAdapterFactory(retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();
        }
        return INSTANCE;
    }

    public MealApis getMealsAPI() {
        mealApis = retrofit.create(MealApis.class);
        return mealApis;
    }

}
