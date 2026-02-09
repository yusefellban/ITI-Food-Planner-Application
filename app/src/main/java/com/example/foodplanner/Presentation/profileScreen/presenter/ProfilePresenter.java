package com.example.foodplanner.Presentation.profileScreen.presenter;

public interface ProfilePresenter {
     void getCurrentLocalUser() ;
     void changeProfileImage(String newPath);
     void trackProfileImage() ;
     void dispose() ;
     void performLogout();
    }
