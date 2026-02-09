package com.example.foodplanner.Data;

import com.example.foodplanner.Data.meals.datasource.local.CountryCodeLocalDataSource;
import com.example.foodplanner.Data.meals.datasource.remote.CategoriesResponseCallback;
import com.example.foodplanner.Data.meals.datasource.remote.IngredientResponseCallback;
import com.example.foodplanner.Data.meals.datasource.remote.MealCallback;
import com.example.foodplanner.Data.meals.datasource.remote.MealDetailsRemoteDataSource;
import com.example.foodplanner.Data.meals.datasource.remote.MealRemoteDataSource;
import com.example.foodplanner.Data.meals.datasource.remote.MealsListCallback;
import com.example.foodplanner.Data.meals.model.Country;
import com.example.foodplanner.Data.meals.model.wrapper.SendSelectedItem;

import java.util.List;

public class MealRepository {
    private final CountryCodeLocalDataSource countryCodeLocalDataSource;
    private final MealRemoteDataSource mealRemoteDataSource;
    private final MealDetailsRemoteDataSource mealDetailsRemoteDataSource;

    public MealRepository() {
        countryCodeLocalDataSource = new CountryCodeLocalDataSource();
        mealRemoteDataSource = new MealRemoteDataSource();
        mealDetailsRemoteDataSource=new MealDetailsRemoteDataSource();
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

    public void getMealDetails(int id, MealCallback mealCallback) {
        mealDetailsRemoteDataSource.getMealDetails(id, mealCallback);
    }

    public void getAllFilteredMeals(SendSelectedItem selectedItem, MealsListCallback mealsListCallback) {
        mealRemoteDataSource.getAllFilteredMeals(selectedItem,mealsListCallback);
    }
}
