package com.example.foodplanner.Presentation.mealDetailsScreen.presenter;

import android.content.Context;
import android.util.Log;

import com.example.foodplanner.Data.AuthRepository;
import com.example.foodplanner.Data.MealRepository;
import com.example.foodplanner.Data.UserRepository;
import com.example.foodplanner.Data.meals.datasource.remote.MealCallback;
import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.Data.mealplan.model.ScheduledMeal;
import com.example.foodplanner.Presentation.mealDetailsScreen.view.MealDetailsViewer;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class MealDetailsPresenterImp implements MealDetailsPresenter {
    private static final String TAG = "MealDetailsPresenter";
    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    private final MealDetailsViewer mealDetailsViewer;
    private final CompositeDisposable compositeDisposable;
    private final AuthRepository authRepository;
    private boolean isFavorite = false;

    public MealDetailsPresenterImp(MealDetailsViewer mealDetailsViewer, Context context) {
        this.mealDetailsViewer = mealDetailsViewer;
        this.mealRepository = new MealRepository(context);
        this.compositeDisposable = new CompositeDisposable();
        userRepository=new UserRepository(context);
        authRepository=new AuthRepository(context);
    }

    @Override
    public void getMealDetails(int id, MealCallback mealCallback) {
        mealRepository.getMealDetails(id, mealCallback);
    }

    @Override
    public String getCountryFlagUrl(String area) {
        return mealRepository.getImageUrl(area);
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

    @Override
    public void checkIfFavorite(int mealId) {
        Disposable disposable = userRepository.isFavorite(mealId)
                .subscribe(
                        isFav -> {
                            this.isFavorite = isFav;
                            mealDetailsViewer.updateFavoriteButton(isFav);
                        },
                        error -> Log.e(TAG, "Error checking favorite status", error)
                );
        compositeDisposable.add(disposable);
    }

    @Override
    public void toggleFavorite(Meal meal) {
        if(!authRepository.isSharedLoggedIn()){
            mealDetailsViewer.showGoToRegistrationDialog();
            return;
        }
        if (isFavorite) {
            // Remove from favorites
            Disposable disposable = userRepository.removeFromFavorites(Integer.parseInt(meal.getId()))
                    .subscribe(
                            () -> {
                                isFavorite = false;
                                mealDetailsViewer.updateFavoriteButton(false);
                                Log.d(TAG, "Meal removed from favorites");
                            },
                            error -> Log.e(TAG, "Error removing from favorites", error)
                    );
            compositeDisposable.add(disposable);
        } else {
            // Add to favorites
            Disposable disposable = userRepository.addToFavorites(meal)
                    .subscribe(
                            () -> {
                                isFavorite = true;
                                mealDetailsViewer.updateFavoriteButton(true);
                                Log.d(TAG, "Meal added to favorites");
                            },
                            error -> Log.e(TAG, "Error adding to favorites", error)
                    );
            compositeDisposable.add(disposable);
        }
    }

    @Override
    public void goToRegistration() {
        mealDetailsViewer.goRegistration();
    }

    @Override
    public void addToCalendar(Meal meal, String date) {
        ScheduledMeal scheduledMeal = new ScheduledMeal(
                meal.getId(),
                meal.getName(),
                meal.getThumbnailUrl(),
                meal.getCategory(),
                date
        );
        
        Disposable disposable = mealRepository.scheduleMeal(scheduledMeal)
                .subscribe(
                        () -> {
                            Log.d(TAG, "Meal added to calendar successfully");
                            mealDetailsViewer.showMealAddedToCalendar(date);
                        },
                        error -> {
                            Log.e(TAG, "Error adding meal to calendar", error);
                        }
                );
        compositeDisposable.add(disposable);
    }

    public void onDestroy() {
        compositeDisposable.clear();
    }
}
