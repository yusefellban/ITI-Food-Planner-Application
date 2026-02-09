package com.example.foodplanner.Presentation.profileScreen.view;

import com.example.foodplanner.Data.user.Entity.UserEntity;

public interface ProfileView {
    void displayImage(String path);

    void showSuccessMessage(String message);

    void showError(String error);
    void showUserData(String name, String email, String photoUrl);
}
