package com.example.foodplanner.Presentation.registrationScreen.presenter;

public interface RegistrationPresenter {
     void register(String name, String email, String password) ;
     void registerWithGoogle(String idToken) ;
     void onGoogleBtnClicked() ;
    void dispose();
}
