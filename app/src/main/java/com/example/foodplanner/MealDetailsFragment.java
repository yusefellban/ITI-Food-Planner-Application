package com.example.foodplanner;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import com.example.foodplanner.Entity.Meal;
import com.example.foodplanner.adapter.DetailsScreenIngredientAdapter;
import com.example.foodplanner.adapter.DetailsScreenInstructionAdapter;
import com.example.foodplanner.remote.RetrofitClient;
import com.example.foodplanner.service.CountryCodeService;
import com.example.foodplanner.wrapper.MealResponse;
import com.example.foodplanner.wrapper.SelectedMeal;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
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

    private String mPendingVideoId;
    private YouTubePlayer mYouTubePlayer;

    public MealDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_details, container, false);

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
        youtubePlayerView = view.findViewById(R.id.youtube_player_view);

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
        getLifecycle().addObserver(youtubePlayerView);
        IFramePlayerOptions options = new IFramePlayerOptions.Builder()
                .controls(1)
                .origin("https://www.youtube.com")
                .build();
        youtubePlayerView.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                mYouTubePlayer = youTubePlayer;
                if (mPendingVideoId != null) {
                    mYouTubePlayer.cueVideo(mPendingVideoId, 0);
                }
            }
        }, options); // استخدم المتغير options اللي عرفناه فوق


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
                //loadYoutubeVideo(myMeal.getYoutubeUrl());
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

    // 3. تعديل ميثود loadYoutubeVideo
    private void loadYoutubeVideo(String videoUrl) {
        String videoId = getYoutubeVideoId(videoUrl);

        if (videoId != null && !videoId.isEmpty()) {
            mPendingVideoId = videoId;

            // 1. لو المشغل جاهز، اعرض الفيديو
            if (mYouTubePlayer != null) {
                mYouTubePlayer.cueVideo(videoId, 0);
            }

            // 2. الحل العبقري: لما المستخدم يدوس على المشغل، يفتح الفيديو في تطبيق يوتيوب
            // ده بيضمن إن حتى لو الـ WebView علق، المستخدم هيشوف الفيديو 100%
            youtubePlayerView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // لو مفيش تطبيق يوتيوب، افتحه في المتصفح
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoId)));
                }
            });

        } else {
            youtubePlayerView.setVisibility(View.GONE);
        }
    }
}