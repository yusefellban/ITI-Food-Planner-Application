package com.example.foodplanner.Presentation.filteredMealsScreen.presenter;

import com.example.foodplanner.Data.MealRepository;
import com.example.foodplanner.Data.meals.datasource.remote.MealsListCallback;
import com.example.foodplanner.Data.meals.model.wrapper.SendSelectedItem;

public class FilteredMealsPresenterImp implements FilteredMealsPresenter {
    private MealRepository mealRepository;

    public FilteredMealsPresenterImp() {
        mealRepository = new MealRepository();

    }

    @Override
    public void getAllFilteredMeals(SendSelectedItem selectedItem, MealsListCallback mealsListCallback) {
        mealRepository.getAllFilteredMeals(selectedItem, mealsListCallback);
    }
}
