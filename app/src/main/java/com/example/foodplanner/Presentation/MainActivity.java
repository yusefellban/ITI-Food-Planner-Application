package com.example.foodplanner.Presentation;

import android.os.Bundle;
import android.view.View;

import androidx.core.splashscreen.SplashScreen;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;

import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;


import com.example.foodplanner.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        splashScreen.setOnExitAnimationListener(provider -> {
            View icon = provider.getIconView();
            icon.setPivotX(icon.getWidth() / 2f);
            icon.setPivotY(icon.getHeight() / 2f);
            icon.animate()
                    .rotationBy(360f)
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .alpha(0f)
                    .setDuration(600)
                    .withEndAction(provider::remove)
                    .start();
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNav, navController);



/// hide nav bar in specific screens

        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);
        FloatingActionButton fab = findViewById(R.id.fab);

        bottomAppBar.post(() -> {
            MaterialShapeDrawable shapeDrawable = (MaterialShapeDrawable) bottomAppBar.getBackground();
            shapeDrawable.setShapeAppearanceModel(
                    shapeDrawable.getShapeAppearanceModel()
                            .toBuilder()
                            .setAllCorners(CornerFamily.ROUNDED, 120f)
                            .build()
            );
        });

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.loginFragment ||
                    destination.getId() == R.id.registrationFragment ||
                    destination.getId() == R.id.splashFragment ||
                    destination.getId() == R.id.mealDetailsFragment) {

                bottomAppBar.setVisibility(View.GONE);
                fab.hide();
            } else {

                bottomAppBar.setVisibility(View.VISIBLE);
                fab.show();

                bottomAppBar.performShow();

                fab.animate().translationY(0).setDuration(50).start();
            }
        });



    }


}