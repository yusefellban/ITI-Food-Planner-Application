package com.example.foodplanner;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Entity.Meal;
import com.example.foodplanner.adapter.DetailsScreenIngredientAdapter;
import com.example.foodplanner.adapter.DetailsScreenInstructionAdapter;
import com.example.foodplanner.remote.RetrofitClient;
import com.example.foodplanner.service.CountryCodeService;
import com.example.foodplanner.wrapper.MealResponse;
import com.example.foodplanner.wrapper.SelectedMeal;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;


import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

        String apiUrl = "https://www.youtube.com/watch?v=IhwPQL9dFYc";
        String videoId = getYoutubeVideoId(apiUrl);
        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youtubePlayer) {
                if (videoId != null) {
                    youtubePlayer.cueVideo(videoId, 0);
                }
            }
        });
    }


    private void fetchSelectedMeal() {

        RetrofitClient.getApiService().getMealByID(String.valueOf(selectedMeal.getId())).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                Meal myMeal = response.body().getMeals().get(0);

                String code = CountryCodeService.getCountryCode(myMeal.getArea());
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
        if (youtubeUrl == null || youtubeUrl.trim().isEmpty()) {
            return null;
        }

        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youtubeUrl);

        if (matcher.find()) {
            String id = matcher.group();
            android.util.Log.d("YOUTUBE_ID", "Extracted ID: " + id);
            return id;
        }

        return null;
    }
/// /////////////
private String getEmbedUrl(String url) {
    // اللينك: https://www.youtube.com/watch?v=K0ipnz4fwJI
    if (url.contains("v=")) {
        // بنقص النص من بعد "v="
        String videoId = url.substring(url.indexOf("v=") + 2);

        // لو اللينك فيه علامات تانية بعد الـ ID (زي &feature=...) بنشيلها
        if (videoId.contains("&")) {
            videoId = videoId.substring(0, videoId.indexOf("&"));
        }
        return "https://www.youtube.com/embed/" + videoId;
    }
    return url;
}

}