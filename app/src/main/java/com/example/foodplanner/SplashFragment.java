package com.example.foodplanner;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.airbnb.lottie.LottieAnimationView;


public class SplashFragment extends Fragment {
   private FrameLayout splashScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LottieAnimationView animationView = view.findViewById(R.id.lottieAnimationView);
        splashScreen=view.findViewById(R.id.splashScreen);

        animationView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("onEndSplashFragment", "onAnimationEnd: ");

                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.splashFragment, true)
                        .build();

                NavHostFragment.findNavController(SplashFragment.this)
                        .navigate(R.id.action_splashFragment_to_loginFragment, null, navOptions);
            }
        });


        splashScreen.setOnClickListener((v)->{
            Log.d("onClickSplashFragment", "onAnimationEnd: ");

            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.splashFragment, true)
                    .build();

            NavHostFragment.findNavController(SplashFragment.this)
                    .navigate(R.id.action_splashFragment_to_loginFragment, null, navOptions);
        });

    }
}