package com.example.foodplanner.Presentation.filteredMealsScreen.presenter;

import com.example.foodplanner.Data.meals.datasource.remote.MealsListCallback;
import com.example.foodplanner.Data.meals.model.wrapper.SendSelectedItem;

public interface FilteredMealsPresenter {
     void getAllFilteredMeals(SendSelectedItem selectedItem, MealsListCallback mealsListCallback) ;
}
