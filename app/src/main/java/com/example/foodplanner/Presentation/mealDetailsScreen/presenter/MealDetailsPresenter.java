package com.example.foodplanner.Presentation.mealDetailsScreen.presenter;

import com.example.foodplanner.Data.meals.datasource.remote.MealCallback;

public interface MealDetailsPresenter {

     void getMealDetails(int id, MealCallback mealCallback) ;
     String getMealImageUrl(String area) ;
     void setupYoutubePlayer(String youtubeUrl) ;
     String getYoutubeVideoId(String youtubeUrl) ;
}
