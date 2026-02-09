package com.example.foodplanner.Presentation.registrationScreen.view;

public interface RegistrationViewer {
    void showLoading();
    void hideLoading();
    void onRegistrationSuccess();
    void onRegistrationError(String message);
    void setupGoogleClient();
    void openGoogleSignInMap();
}
