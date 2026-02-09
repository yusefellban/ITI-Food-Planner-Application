package com.example.foodplanner.Data;

import android.content.Context;

import com.example.foodplanner.Data.auth.datasource.local.AuthSharedPrefsLocalDataSource;
import com.example.foodplanner.Data.auth.datasource.remote.AuthRemoteDataSource;
import com.example.foodplanner.Data.auth.datasource.remote.AuthRemoteDataSourceImp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AuthRepository {
    private final AuthRemoteDataSource remoteDataSource;
    private final AuthSharedPrefsLocalDataSource authSharedPrefsLocalDataSource;

    public AuthRepository(Context context) {
        this.remoteDataSource = new AuthRemoteDataSourceImp();
        authSharedPrefsLocalDataSource=new AuthSharedPrefsLocalDataSource(context);
    }

    public Single<AuthResult> loginWithEmail(String email, String password) {
        return remoteDataSource.loginWithEmail(email, password);
    }

    public Single<AuthResult> loginWithGoogle(String idToken) {
        return remoteDataSource.loginWithGoogle(idToken);
    }

    public Single<AuthResult> registerWithEmail(String fullName, String email, String password) {
        return remoteDataSource.registerWithEmail(fullName, email, password);
    }

    public Single<FirebaseUser> getUserInfo() {
        return remoteDataSource.getCurrentUser();
    }
    public Completable logout() {
        authSharedPrefsLocalDataSource.setLoggedIn(false);
        return remoteDataSource.logout()
                .subscribeOn(Schedulers.io());
    }

    /// local
    public void setSharedLoggedIn(boolean isLoggedIn) {
        authSharedPrefsLocalDataSource.setLoggedIn(isLoggedIn);
    }

    public boolean isSharedLoggedIn() {
        return authSharedPrefsLocalDataSource.isLoggedIn();
    }

    public void clearShared() {
        authSharedPrefsLocalDataSource.clear();
    }

}
