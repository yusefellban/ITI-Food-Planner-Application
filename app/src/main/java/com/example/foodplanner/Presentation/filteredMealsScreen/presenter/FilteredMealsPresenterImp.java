package com.example.foodplanner.Presentation.filteredMealsScreen.presenter;

import android.content.Context;

import com.example.foodplanner.Data.MealRepository;
import com.example.foodplanner.Data.meals.datasource.remote.MealsListCallback;
import com.example.foodplanner.Data.meals.model.wrapper.SendSelectedItem;

public class FilteredMealsPresenterImp implements FilteredMealsPresenter {
    private MealRepository mealRepository;

    public FilteredMealsPresenterImp(Context context) {
        mealRepository = new MealRepository(context);

    }

    @Override
    public void getAllFilteredMeals(SendSelectedItem selectedItem, MealsListCallback mealsListCallback) {
        mealRepository.getAllFilteredMeals(selectedItem, mealsListCallback);
    }
}
