package com.example.foodplanner.Presentation.loginScreen.view;

public interface LoginViewer {
    void showLoading();
    void hideLoading();
    void onLoginSuccess();
    void openGoogleSignInMap();
   void setupGoogleClient();
    void onLoginError(String message);
}
