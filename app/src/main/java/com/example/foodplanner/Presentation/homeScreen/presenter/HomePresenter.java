package com.example.foodplanner.Presentation.homeScreen.presenter;

import com.example.foodplanner.Data.meals.datasource.remote.MealCallback;
import com.example.foodplanner.Data.meals.datasource.remote.MealsListCallback;
import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.Data.meals.model.wrapper.SelectedMeal;

import java.util.List;

public interface HomePresenter {
     void fetchRandomMeal(MealCallback mealCallback) ;
    void fetchCarouselMeals(MealsListCallback mealsListCallback);
     String getCountryFlagUrl(String area) ;
     void showSelectedMeal(SelectedMeal selectedMeal);
     void checkIfAllLoadingFinished();
     void stopMealListShimmer() ;
     void setMealListAdapter(List<Meal> meals) ;
     void stopMealShimmer() ;
     void startMealShimmer() ;
     void startMealListShimmer() ;
     void setRefreshManager() ;
     void showGoToRegistrationDialog() ;
     void goToRegistration() ;
}
