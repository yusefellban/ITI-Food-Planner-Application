package com.example.foodplanner.Data.user.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.Data.user.Entity.UserEntity;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;


@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertOrUpdate(UserEntity user);
    @Query("SELECT * FROM user_profile WHERE uid = :userId LIMIT 1")
    Maybe<UserEntity> getUserById(String userId);
    @Query("SELECT * FROM user_profile WHERE email = :email LIMIT 1")
    Maybe<UserEntity> getUserByُEmail(String email);

    @Query("SELECT imagePath FROM user_profile WHERE uid = :userId")
    Flowable<String> getProfileImagePath(String userId);

    @Query("UPDATE user_profile SET imagePath = :newPath WHERE uid = :userId")
    Completable updateOnlyImagePath(String userId, String newPath);
    @Query("DELETE FROM user_profile")
    Completable deleteAllUsers();

}
