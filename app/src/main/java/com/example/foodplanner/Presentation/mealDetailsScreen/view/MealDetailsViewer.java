package com.example.foodplanner.Presentation.mealDetailsScreen.view;

public interface MealDetailsViewer {

     void setupYoutubePlayer(String videoId) ;
     void updateFavoriteButton(boolean isFavorite);
     void showGoToRegistrationDialog();

     void goRegistration();
     
     void showCalendarDatePicker();
     
     void showMealAddedToCalendar(String date);
}
