package com.example.foodplanner.Data.favorites.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.Data.favorites.entity.FavoriteMealEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface FavoriteMealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(FavoriteMealEntity favoriteMeal);

    @Query("DELETE FROM favorite_meals WHERE mealId = :mealId")
    void deleteFavoriteById(int mealId);

    @Query("SELECT * FROM favorite_meals ORDER BY timestamp DESC")
    Flowable<List<FavoriteMealEntity>> getAllFavorites();

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_meals WHERE mealId = :mealId)")
    Single<Boolean> isFavorite(int mealId);

    @Query("SELECT * FROM favorite_meals WHERE mealId = :mealId")
    Single<FavoriteMealEntity> getFavoriteById(int mealId);

    @Query("DELETE FROM favorite_meals")
    void deleteAllFavorites();
}
