package com.example.foodplanner.Data;

import android.content.Context;

import com.example.foodplanner.Data.user.Entity.UserEntity;
import com.example.foodplanner.Data.user.local.UserLocalDataSource;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
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

}
