package com.example.foodplanner.Data;

import com.example.foodplanner.Data.meals.datasource.local.CountryCodeLocalDataSource;
import com.example.foodplanner.Data.meals.datasource.remote.CategoriesResponseCallback;
import com.example.foodplanner.Data.meals.datasource.remote.IngredientResponseCallback;
import com.example.foodplanner.Data.meals.datasource.remote.MealCallback;
import com.example.foodplanner.Data.meals.datasource.remote.MealRemoteDataSource;
import com.example.foodplanner.Data.meals.datasource.remote.MealsListCallback;
import com.example.foodplanner.Data.meals.model.Country;

import java.util.List;

public class Repository {
    private CountryCodeLocalDataSource countryCodeLocalDataSource;
    private MealRemoteDataSource mealRemoteDataSource;

    public Repository() {
        countryCodeLocalDataSource = new CountryCodeLocalDataSource();
        mealRemoteDataSource = new MealRemoteDataSource();
    }


    public void getRandomMeal(MealCallback mealCallback) {
        mealRemoteDataSource.getRandomMeal(mealCallback);

    }

    public void getRandomMealsList(MealsListCallback mealsListCallback) {
        mealRemoteDataSource.getRandomMealsList(mealsListCallback);

    }

    public String getImageUrl(String area) {
        return countryCodeLocalDataSource.getImageUrl(area);

    }

    public void getAllCategories(CategoriesResponseCallback categoriesResponseCallback) {
        mealRemoteDataSource.getAllCategories(categoriesResponseCallback);
    }

    public void getAllIngredients(IngredientResponseCallback ingredientResponseCallback) {
        mealRemoteDataSource.getAllIngredients(ingredientResponseCallback);
    }

    public List<Country> getAllCountries() {
        return countryCodeLocalDataSource.getAllCountries();
    }
}
