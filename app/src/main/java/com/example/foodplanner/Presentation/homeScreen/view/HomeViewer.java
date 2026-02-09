package com.example.foodplanner.Presentation.homeScreen.view;

import android.net.Uri;

import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.Data.meals.model.wrapper.SelectedMeal;

import java.util.List;

public interface HomeViewer {

    void startMealShimmer();
    void stopMealShimmer();
    void startMealListShimmer();
    void stopMealListShimmer();
    void setMealListAdapter(List<Meal> meals);
    void checkIfAllLoadingFinished();
    void showGoToRegistrationDialog();
    void showSelectedMeal(SelectedMeal selectedMeal);

    void setRefreshManager();
     void goToRegistration();

    void setUserData(String displayName, Uri photoUrl);
}
