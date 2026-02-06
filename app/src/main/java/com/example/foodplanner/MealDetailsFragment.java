package com.example.foodplanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.model.Meal;
import com.example.foodplanner.adapter.DetailsScreenIngredientAdapter;
import com.example.foodplanner.adapter.DetailsScreenInstructionAdapter;
import com.example.foodplanner.MealDetailsRemote.RetrofitClient;
import com.example.foodplanner.datasource.local.CountryCodeLocalDataSource;
import com.example.foodplanner.model.wrapper.MealResponse;
import com.example.foodplanner.model.wrapper.SelectedMeal;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MealDetailsFragment extends Fragment {

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


    public MealDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_meal_details, container, false);
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

        RetrofitClient.getApiService().getMealByID(String.valueOf(selectedMeal.getId())).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (!isAdded() || getContext() == null) return;
                Meal myMeal = response.body().getMeals().get(0);

                String code = CountryCodeLocalDataSource.getCountryCode(myMeal.getArea());
                if (code != null) {
                    detailsAreaIcon.setVisibility(View.VISIBLE);
                    String flagUrl = "https://flagcdn.com/w160/" + code.toLowerCase() + ".png";
                    Glide.with(requireContext()).load(flagUrl).circleCrop().into(detailsAreaIcon);
                } else {
                    detailsAreaIcon.setVisibility(View.GONE);
                }

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
                setupYoutubePlayer(myMeal.getYoutubeUrl());

            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {

            }
        });
    }

    private List<String> divideInstructions(String instructionsString) {
        return Arrays.stream(instructionsString.split("\\."))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private String getYoutubeVideoId(String youtubeUrl) {
        if (youtubeUrl == null || youtubeUrl.isEmpty()) return null;

        String videoId = null;
        if (youtubeUrl.contains("v=")) {
            String[] parts = youtubeUrl.split("v=");
            videoId = parts[1];

            int ampersandPosition = videoId.indexOf("&");
            if (ampersandPosition != -1) {
                videoId = videoId.substring(0, ampersandPosition);
            }
        }

        //  youtu.be
        else if (youtubeUrl.contains("youtu.be/")) {
            String[] parts = youtubeUrl.split("youtu.be/");
            videoId = parts[1];
            int questionMarkPosition = videoId.indexOf("?");
            if (questionMarkPosition != -1) {
                videoId = videoId.substring(0, questionMarkPosition);
            }
        }

        return videoId;
    }



    private void setupYoutubePlayer(String videoUrl) {
        if (videoUrl == null || videoUrl.isEmpty()) return;

        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = getYoutubeVideoId(videoUrl);
                if (videoId != null) {
                    youTubePlayer.cueVideo(videoId, 0);
                }
            }
        });
    }


}