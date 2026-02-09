package com.example.foodplanner.Data.auth.datasource.remote;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface AuthRemoteDataSource{
    Single<AuthResult> loginWithEmail(String email, String password);
    Single<AuthResult> loginWithGoogle(String idToken);
     Single<AuthResult> registerWithEmail(String fullName, String email, String password) ;
    Single<FirebaseUser> getCurrentUser();
     Completable logout() ;
}
