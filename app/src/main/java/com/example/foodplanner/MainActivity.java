package com.example.foodplanner;

import android.os.Bundle;
import android.view.View;

import androidx.core.splashscreen.SplashScreen;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      SplashScreen splashScreen= SplashScreen.installSplashScreen(this);

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






    }
}