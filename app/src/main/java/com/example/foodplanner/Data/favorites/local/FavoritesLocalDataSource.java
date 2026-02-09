package com.example.foodplanner.Data.favorites.local;

import com.example.foodplanner.Data.favorites.entity.FavoriteMealEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface FavoritesLocalDataSource {
    
    void insertFavorite(FavoriteMealEntity favoriteMeal);
    
    void deleteFavoriteById(int mealId);
    
    Flowable<List<FavoriteMealEntity>> getAllFavorites();
    
    Single<Boolean> isFavorite(int mealId);
    
    Single<FavoriteMealEntity> getFavoriteById(int mealId);
    
    void deleteAllFavorites();
}
