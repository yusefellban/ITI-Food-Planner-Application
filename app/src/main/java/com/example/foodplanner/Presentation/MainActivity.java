package com.example.foodplanner.Presentation;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.splashscreen.SplashScreen;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;

import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;


import com.example.foodplanner.Data.AuthRepository;
import com.example.foodplanner.Presentation.homeScreen.view.HomeFragment;
import com.example.foodplanner.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;

public class MainActivity extends AppCompatActivity {
 private BottomNavigationView bottomNavigationView;
    NavController navController;
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

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
         navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);



/// hide nav bar in specific screens

        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);

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
            } else {

                bottomAppBar.setVisibility(View.VISIBLE);

                bottomAppBar.performShow();

            }


        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            AuthRepository authRepository=new AuthRepository(getApplicationContext());

            int id = item.getItemId();

            if (id == R.id.profileFragment || id==R.id.calendarFragment) {
                if (!authRepository.isSharedLoggedIn()) {
                    showGoToRegistrationDialog();
                    return false;
                }
            }
            return NavigationUI.onNavDestinationSelected(item, navController)
                    || super.onOptionsItemSelected(item);

        });




    }
    public void showGoToRegistrationDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.CustomDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.goto_login_dialog_layout, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();

        view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
            dialog.dismiss();
            navController.navigate(R.id.registrationFragment);
        });

    }

}