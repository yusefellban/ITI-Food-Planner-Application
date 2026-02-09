package com.example.foodplanner.Data.user.local;

import android.content.Context;

import com.example.foodplanner.Data.db.AppDatabase;
import com.example.foodplanner.Data.favorites.local.FavoritesLocalDataSource;
import com.example.foodplanner.Data.favorites.entity.FavoriteMealEntity;
import com.example.foodplanner.Data.favorites.local.FavoriteMealDao;
import com.example.foodplanner.Data.user.Entity.UserEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public class UserLocalDataSource implements FavoritesLocalDataSource {
    private final UserDao userDao;
    private final FavoriteMealDao favoriteMealDao;

    public UserLocalDataSource(Context context){
        userDao= AppDatabase.getInstance(context).userDaoDao();
        this.favoriteMealDao = AppDatabase.getInstance(context).favoriteMealDao();

    }
    public Completable updateImage(String uid, String path) {
        return userDao.updateOnlyImagePath(uid,path);
    }

    public Maybe<UserEntity> getUserById(String uid) {
        return userDao.getUserById(uid);
    }
    public Maybe<UserEntity> getUserByEmail(String email) {
        return userDao.getUserByُEmail(email);
    }



    public Flowable<String> getProfileImageStream(String uid) {
        return userDao.getProfileImagePath(uid);
    }


    public Completable insertUser(UserEntity userEntity){
        return userDao.insertOrUpdate(userEntity);
    }

    public Completable clearAllData() {
        return userDao.deleteAllUsers();
    }

    /// favorites

    @Override
    public void insertFavorite(FavoriteMealEntity favoriteMeal) {
        favoriteMealDao.insertFavorite(favoriteMeal);
    }

    @Override
    public void deleteFavoriteById(int mealId) {
        favoriteMealDao.deleteFavoriteById(mealId);
    }

    @Override
    public Flowable<List<FavoriteMealEntity>> getAllFavorites() {
        return favoriteMealDao.getAllFavorites();
    }

    @Override
    public Single<Boolean> isFavorite(int mealId) {
        return favoriteMealDao.isFavorite(mealId);
    }

    @Override
    public Single<FavoriteMealEntity> getFavoriteById(int mealId) {
        return favoriteMealDao.getFavoriteById(mealId);
    }

    @Override
    public void deleteAllFavorites() {
        favoriteMealDao.deleteAllFavorites();
    }
}
