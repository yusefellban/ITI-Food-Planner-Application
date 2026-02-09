package com.example.foodplanner.Data;

import android.content.Context;
import android.util.Log;

import com.example.foodplanner.Data.favorites.entity.FavoriteMealEntity;
import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.Data.user.Entity.UserEntity;
import com.example.foodplanner.Data.user.local.UserLocalDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserRepository {
    private UserLocalDataSource userLocalDataSource;

    public UserRepository(Context context){
        userLocalDataSource=new UserLocalDataSource(context);

    }


    public Completable addUser(UserEntity userEntity) {
        return userLocalDataSource
                .insertUser(userEntity)
                .subscribeOn(Schedulers.io());
    }
    public Completable updateUserImage(String uid ,String newPath){
        return userLocalDataSource.updateImage(uid,newPath)
                .subscribeOn(Schedulers.io());
    }
    public Flowable<String> getProfileImage(String uid) {
        return userLocalDataSource.getProfileImageStream(uid);
    }

    public Maybe<UserEntity> getUserById(String uid) {
        return userLocalDataSource.getUserById(uid)
                .subscribeOn(Schedulers.io());
    }

    public Completable logout() {
        return userLocalDataSource.clearAllData()
                .subscribeOn(Schedulers.io());
    }

    /**
     *  favorites
     */
    public Completable addToFavorites(Meal meal) {
        return Completable.fromAction(() -> {
                    FavoriteMealEntity entity = new FavoriteMealEntity(
                            Integer.parseInt(meal.getId()),
                            meal.getName(),
                            meal.getThumbnailUrl(),
                            meal.getCategory(),
                            meal.getArea(),
                            System.currentTimeMillis()
                    );
                    userLocalDataSource.insertFavorite(entity);
                    Log.d("FavoritesRepository", "Added to favorites: " + meal.getName());
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Completable removeFromFavorites(int mealId) {
        return Completable.fromAction(() -> {
                    userLocalDataSource.deleteFavoriteById(mealId);
                    Log.d("FavoritesRepository", "Removed from favorites: " + mealId);
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Flowable<List<Meal>> getAllFavorites() {
        return userLocalDataSource.getAllFavorites()
                .map(this::convertEntitiesToMeals)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Single<Boolean> isFavorite(int mealId) {
        return userLocalDataSource.isFavorite(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Completable clearAllFavorites() {
        return Completable.fromAction(() -> {
                    userLocalDataSource.deleteAllFavorites();
                    Log.d("FavoritesRepository", "Cleared all favorites");
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private List<Meal> convertEntitiesToMeals(List<FavoriteMealEntity> entities) {
        List<Meal> meals = new ArrayList<>();
        for (FavoriteMealEntity entity : entities) {
            Meal meal = new Meal();
            meal.setId(String.valueOf(entity.getMealId()));
            meal.setName(entity.getName());
            meal.setThumbnailUrl(entity.getThumbnailUrl());
            meal.setCategory(entity.getCategory());
            meal.setArea(entity.getArea());
            meals.add(meal);
        }
        return meals;
    }
}
