package com.example.foodplanner.Presentation.mealDetailsScreen.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    private YouTubePlayerView youtubePlayerView;

    private SelectedMeal selectedMeal;

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


        selectedMeal = MealDetailsFragmentArgs.fromBundle(getArguments()).getSelectedMeal();
        presenter=new MealDetailsPresenterImp(this);

        detailsMealName.setText(selectedMeal.getName());

        Glide.with(requireContext())
                .load(selectedMeal.getImageURL())
                .placeholder(R.drawable.rounded_image)
                .centerCrop()
                .into(detailsMealImage);

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

                String flagUrl = presenter.getMealImageUrl(myMeal.getArea());
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


}