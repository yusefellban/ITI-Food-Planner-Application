package com.example.foodplanner.Presentation.mealDetailsScreen.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodplanner.Presentation.homeScreen.view.HomeFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Presentation.mealDetailsScreen.presenter.MealDetailsPresenterImp;
import com.example.foodplanner.R;
import com.example.foodplanner.Data.meals.datasource.remote.MealCallback;
import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.Data.meals.model.wrapper.SelectedMeal;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class MealDetailsFragment extends Fragment implements MealDetailsViewer {

    private ImageView detailsMealImage;
    private ImageView detailsAreaIcon;
    private TextView detailsIngredientsItems;
    private TextView detailsMealName;
    private TextView detailsAreaName;
    private TextView detailsCategory;
    private RecyclerView detailsIngredientsRecyclerView;
    private RecyclerView detailsInstructionRecyclerView;
    private FloatingActionButton favoriteFab;

    private YouTubePlayerView youtubePlayerView;

    private SelectedMeal selectedMeal;
    private Meal currentMeal;

    private MealDetailsPresenterImp presenter;


    public MealDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detailsMealImage = view.findViewById(R.id.detailsMealImage);
        detailsMealName = view.findViewById(R.id.detailsMealName);
        detailsAreaIcon = view.findViewById(R.id.detailsAreaIcon);
        detailsAreaName = view.findViewById(R.id.detailsAreaName);
        detailsCategory = view.findViewById(R.id.detailsCategory);
        detailsIngredientsRecyclerView = view.findViewById(R.id.detailsIngredientsRecyclerView);
        detailsInstructionRecyclerView = view.findViewById(R.id.detailsInstructionRecyclerView);
        detailsIngredientsItems = view.findViewById(R.id.detailsIngredientsItems);
        favoriteFab = view.findViewById(R.id.favoriteFab);


        selectedMeal = MealDetailsFragmentArgs.fromBundle(getArguments()).getSelectedMeal();
        presenter=new MealDetailsPresenterImp(this, requireContext());

        detailsMealName.setText(selectedMeal.getName());

        Glide.with(requireContext())
                .load(selectedMeal.getImageURL())
                .placeholder(R.drawable.rounded_image)
                .centerCrop()
                .into(detailsMealImage);

        // Setup FAB click listener
        favoriteFab.setOnClickListener(v -> {
            if (currentMeal != null) {
                presenter.toggleFavorite(currentMeal);
            }
        });

        // Check if meal is already in favorites
        presenter.checkIfFavorite(selectedMeal.getId());

        fetchSelectedMeal();

        ///  show youtube video
        ///  force the video end with screen lifecycle
        youtubePlayerView = view.findViewById(R.id.player_view);
        getLifecycle().addObserver(youtubePlayerView);
//


    }

    private void fetchSelectedMeal() {
        presenter.getMealDetails(selectedMeal.getId(), new MealCallback() {
            @Override
            public void onSuccess(Meal myMeal) {
                 currentMeal = myMeal;
                 String flagUrl=presenter.getCountryFlagUrl(myMeal.getArea());
                Glide.with(requireContext()).load(flagUrl).circleCrop().into(detailsAreaIcon);

                detailsAreaName.setText(myMeal.getArea());
                detailsCategory.setText(myMeal.getCategory());
                detailsIngredientsItems.setText(myMeal.getIngredients().size() + " items");
                /// set screen Adapter
                DetailsScreenIngredientAdapter ingredientAdapter = new DetailsScreenIngredientAdapter(getContext(), myMeal.getIngredients());
                detailsIngredientsRecyclerView.setAdapter(ingredientAdapter);

                DetailsScreenInstructionAdapter instructionAdapter = new DetailsScreenInstructionAdapter(getContext()
                        ,
                        divideInstructions(myMeal.getInstructions())
                );
                detailsInstructionRecyclerView.setAdapter(instructionAdapter);


                /// load youtube video
                presenter.setupYoutubePlayer(myMeal.getYoutubeUrl());
            }

            @Override
            public void onError(String error) {
                Log.d("onError-Selected Meal: ", error);
            }
        });
    }

    private List<String> divideInstructions(String instructionsString) {
        return Arrays.stream(instructionsString.split("\\."))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }



    @Override
    public void setupYoutubePlayer(String videoId) {
        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                if (videoId != null) {
                    youTubePlayer.cueVideo(videoId, 0);
                }
            }
        });
    }

    @Override
    public void updateFavoriteButton(boolean isFavorite) {
        if (isFavorite) {
            favoriteFab.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            favoriteFab.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    @Override
    public void showGoToRegistrationDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.CustomDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.goto_login_dialog_layout, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();

        view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
            dialog.dismiss();
            presenter.goToRegistration();
        });
    }

    @Override
    public void goRegistration() {
        NavHostFragment.findNavController(MealDetailsFragment.this)
                .navigate(R.id.action_mealDetailsFragment_to_registrationFragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}