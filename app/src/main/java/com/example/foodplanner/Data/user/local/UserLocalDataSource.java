package com.example.foodplanner.Data.user.local;

import android.content.Context;

import com.example.foodplanner.Data.db.AppDatabase;
import com.example.foodplanner.Data.user.Entity.UserEntity;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

public class UserLocalDataSource {
    private final UserDao userDao;
    public UserLocalDataSource(Context context){
        userDao= AppDatabase.getInstance(context).userDaoDao();
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
//        return Completable.fromAction(() -> userDao.insertOrUpdate(userEntity));
        return userDao.insertOrUpdate(userEntity);
    }


}
