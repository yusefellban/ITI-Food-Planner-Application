package com.example.foodplanner.Presentation.loginScreen.presenter;

public interface LoginPresenter {
    void loginWithEmail(String email, String password);
    void loginWithGoogle(String idToken);
     void openGoogleSignInMap() ;
     void setupGoogle() ;
    void dispose();
}
