package com.example.foodplanner.Presentation.homeScreen.presenter;

import android.content.Context;
import android.util.Log;

import com.example.foodplanner.Data.AuthRepository;
import com.example.foodplanner.Data.MealRepository;
import com.example.foodplanner.Data.UserRepository;
import com.example.foodplanner.Data.meals.datasource.remote.MealCallback;
import com.example.foodplanner.Data.meals.datasource.remote.MealsListCallback;
import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.Data.meals.model.wrapper.SelectedMeal;
import com.example.foodplanner.Presentation.homeScreen.view.HomeViewer;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenterImp implements HomePresenter{

private MealRepository mealRepository;
   private HomeViewer homeViewer;
private AuthRepository authRepository;
    public HomePresenterImp(HomeViewer homeViewer, Context context) {
        this.homeViewer = homeViewer;
        mealRepository =new MealRepository(context);
        authRepository=new AuthRepository(context);
    }

    @Override
    public void fetchRandomMeal(MealCallback mealCallback) {
        mealRepository.getRandomMeal(mealCallback);
        homeViewer.checkIfAllLoadingFinished();
    }
    @Override
      public void fetchCarouselMeals(MealsListCallback mealsListCallback){
        mealRepository.getRandomMealsList(mealsListCallback);
      }


    @Override
    public String getCountryFlagUrl(String area) {
        return mealRepository.getImageUrl(area);
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
    public void getUserData(){
        authRepository.getUserInfo()
                .subscribeOn(Schedulers.io()).
                  observeOn(AndroidSchedulers.mainThread()).subscribe(firebaseUser -> {
                    homeViewer.setUserData(firebaseUser.getDisplayName(),firebaseUser.getPhotoUrl());
                }, error -> Log.d("TAG", "getUserData: "));
    }

}
