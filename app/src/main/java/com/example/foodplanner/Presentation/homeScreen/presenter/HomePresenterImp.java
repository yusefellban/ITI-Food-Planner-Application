package com.example.foodplanner.Presentation.homeScreen.presenter;

import android.content.Context;

import com.example.foodplanner.Data.Repository;
import com.example.foodplanner.Data.meals.datasource.remote.MealCallback;
import com.example.foodplanner.Data.meals.datasource.remote.MealsListCallback;
import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.Data.meals.model.wrapper.SelectedMeal;
import com.example.foodplanner.Presentation.homeScreen.view.HomeViewer;

import java.util.List;

public class HomePresenterImp implements HomePresenter{

private Repository repository;
    HomeViewer homeViewer;

    public HomePresenterImp( HomeViewer homeViewer) {
        this.homeViewer = homeViewer;
        repository=new Repository();
    }

    @Override
    public void fetchRandomMeal(MealCallback mealCallback) {
        repository.getRandomMeal(mealCallback);
        homeViewer.checkIfAllLoadingFinished();
    }
    @Override
      public void fetchCarouselMeals(MealsListCallback mealsListCallback){
        repository.getRandomMealsList(mealsListCallback);
      }


    @Override
    public String getCountryFlagUrl(String area) {
        return repository.getImageUrl(area);
    }

    @Override
    public void showSelectedMeal(SelectedMeal selectedMeal) {
        homeViewer.showSelectedMeal(selectedMeal);
    }
    @Override
    public void checkIfAllLoadingFinished(){
        homeViewer.checkIfAllLoadingFinished();
    }

    @Override
    public void stopMealListShimmer() {
        homeViewer.stopMealListShimmer();
    }

    @Override
    public void setMealListAdapter(List<Meal> meals) {
        homeViewer.setMealListAdapter(meals);
    }

    @Override
    public void stopMealShimmer() {
        homeViewer.stopMealShimmer();
    }

    @Override
    public void startMealShimmer() {
        homeViewer.startMealShimmer();
    }

    @Override
    public void startMealListShimmer() {
        homeViewer.startMealListShimmer();
    }

    @Override
    public void setRefreshManager() {
        homeViewer.setRefreshManager();
    }

    @Override
    public void showGoToRegistrationDialog() {
        homeViewer.showGoToRegistrationDialog();
    }

    @Override
    public void goToRegistration() {
        homeViewer.goToRegistration();
    }
}
