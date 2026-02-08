package com.example.foodplanner.Presentation.filteredMealsScreen.presenter;

import com.example.foodplanner.Data.Repository;
import com.example.foodplanner.Data.meals.datasource.remote.MealRemoteDataSource;
import com.example.foodplanner.Data.meals.datasource.remote.MealsListCallback;
import com.example.foodplanner.Data.meals.model.wrapper.SendSelectedItem;

public class FilteredMealsPresenterImp implements FilteredMealsPresenter {
    private Repository repository;

    public FilteredMealsPresenterImp() {
        repository = new Repository();

    }

    @Override
    public void getAllFilteredMeals(SendSelectedItem selectedItem, MealsListCallback mealsListCallback) {
        repository.getAllFilteredMeals(selectedItem, mealsListCallback);
    }
}
