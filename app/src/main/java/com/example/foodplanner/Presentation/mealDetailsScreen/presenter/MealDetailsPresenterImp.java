package com.example.foodplanner.Presentation.mealDetailsScreen.presenter;

import com.example.foodplanner.Data.Repository;
import com.example.foodplanner.Data.meals.datasource.local.CountryCodeLocalDataSource;
import com.example.foodplanner.Data.meals.datasource.remote.MealCallback;
import com.example.foodplanner.Data.meals.datasource.remote.MealDetailsRemoteDataSource;
import com.example.foodplanner.Presentation.mealDetailsScreen.view.MealDetailsViewer;

public class MealDetailsPresenterImp implements MealDetailsPresenter {
    private final Repository repository;
    private final MealDetailsViewer mealDetailsViewer;

    public MealDetailsPresenterImp(MealDetailsViewer mealDetailsViewer) {
        this.mealDetailsViewer = mealDetailsViewer;
        repository=new Repository();
    }

    @Override
    public void getMealDetails(int id, MealCallback mealCallback) {
        repository.getMealDetails(id, mealCallback);
    }

    @Override
    public String getCountryFlagUrl(String area) {
        return repository.getImageUrl(area);
    }

    @Override
    public void setupYoutubePlayer(String youtubeUrl) {
        mealDetailsViewer.setupYoutubePlayer(getYoutubeVideoId(youtubeUrl));
    }


    @Override
    public String getYoutubeVideoId(String youtubeUrl) {
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

}
