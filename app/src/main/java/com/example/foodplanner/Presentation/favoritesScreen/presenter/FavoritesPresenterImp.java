package com.example.foodplanner.Presentation.favoritesScreen.presenter;

import android.content.Context;
import android.util.Log;

import com.example.foodplanner.Data.UserRepository;
import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.Data.meals.model.wrapper.SelectedMeal;
import com.example.foodplanner.Presentation.favoritesScreen.view.FavoritesViewer;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class FavoritesPresenterImp implements FavoritesPresenter {
    
    private static final String TAG = "FavoritesPresenter";
    private final FavoritesViewer viewer;
    private final UserRepository userRepository;
    private final CompositeDisposable compositeDisposable;

    public FavoritesPresenterImp(FavoritesViewer viewer, Context context) {
        this.viewer = viewer;
        this.compositeDisposable = new CompositeDisposable();
        userRepository=new UserRepository(context);
    }

    @Override
    public void loadFavorites() {
        viewer.showLoading();
        
        Disposable disposable = userRepository.getAllFavorites()
                .subscribe(
                        favorites -> {
                            viewer.hideLoading();
                            if (favorites.isEmpty()) {
                                viewer.showEmptyState();
                            } else {
                                viewer.showFavorites(favorites);
                            }
                        },
                        error -> {
                            viewer.hideLoading();
                            Log.e(TAG, "Error loading favorites", error);
                            viewer.showError("Failed to load favorites");
                        }
                );
        
        compositeDisposable.add(disposable);
    }

    @Override
    public void removeFavorite(int mealId) {
        Disposable disposable = userRepository.removeFromFavorites(mealId)
                .subscribe(
                        () -> {
                            Log.d(TAG, "Favorite removed successfully");
                            // The favorites list will auto-update via Flowable
                        },
                        error -> {
                            Log.e(TAG, "Error removing favorite", error);
                            viewer.showError("Failed to remove favorite");
                        }
                );
        
        compositeDisposable.add(disposable);
    }

    @Override
    public void onMealClicked(Meal meal) {
        SelectedMeal selectedMeal = new SelectedMeal(
                Integer.parseInt(meal.getId()),
                meal.getName(),
                meal.getThumbnailUrl()
        );
        viewer.navigateToMealDetails(selectedMeal);
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
    }
}
