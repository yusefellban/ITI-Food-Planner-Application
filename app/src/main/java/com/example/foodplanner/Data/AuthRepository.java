package com.example.foodplanner.Data;

import com.example.foodplanner.Data.auth.datasource.AuthRemoteDataSource;
import com.example.foodplanner.Data.auth.datasource.AuthRemoteDataSourceImp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Single;

public class AuthRepository {
    private final AuthRemoteDataSource remoteDataSource;

    public AuthRepository() {
        this.remoteDataSource = new AuthRemoteDataSourceImp();
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
}
