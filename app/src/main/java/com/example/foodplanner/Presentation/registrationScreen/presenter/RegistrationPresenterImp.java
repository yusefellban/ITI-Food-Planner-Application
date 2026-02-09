package com.example.foodplanner.Presentation.registrationScreen.presenter;

import android.content.Context;
import android.util.Log;

import com.example.foodplanner.Data.AuthRepository;
import com.example.foodplanner.Data.UserRepository;
import com.example.foodplanner.Data.auth.datasource.AuthRemoteDataSourceImp;
import com.example.foodplanner.Data.user.Entity.UserEntity;
import com.example.foodplanner.Presentation.registrationScreen.view.RegistrationViewer;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegistrationPresenterImp implements RegistrationPresenter {
    private final RegistrationViewer view;
    private final AuthRepository repo;
    private final UserRepository userRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public RegistrationPresenterImp(Context context, RegistrationViewer view) {
        this.view = view;
        this.repo = new AuthRepository();
        userRepository = new UserRepository(context)
        ;
    }

    @Override
    public void register(String name, String email, String password) {
        view.showLoading();
        disposable.add(
                repo.registerWithEmail(name, email, password)
                        .subscribeOn(Schedulers.io())
                        .flatMapCompletable(result -> {
                            UserEntity userEntity = new UserEntity(
                                    result.getUser().getUid(),
                                    name,
                                    email
                            );
                            Log.d("register add to local", userEntity.toString());

                            return userRepository.addUser(userEntity)
                                    .doOnComplete(() -> Log.d("ROOM", "User inserted"))
                                    .doOnError(e -> Log.e("ROOM", "Insert error", e));
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    view.hideLoading();
                                    view.onRegistrationSuccess();
                                },
                                throwable -> {
                                    view.hideLoading();
                                    view.onRegistrationError(throwable.getMessage());
                                }
                        )
        );
    }

    @Override
    public void registerWithGoogle(String idToken) {
        view.showLoading();

        disposable.add(
                repo.loginWithGoogle(idToken)
                        .subscribeOn(Schedulers.io())
                        .flatMapCompletable(result -> {

                            String uid = result.getUser().getUid();
                            String name = result.getUser().getDisplayName();
                            String email = result.getUser().getEmail();
                            String photoUrl = (result.getUser().getPhotoUrl() != null) ? result.getUser().getPhotoUrl().toString() : "";


                            UserEntity userEntity = new UserEntity(
                                    uid,
                                    name,
                                    email,
                                    photoUrl
                            );

                            Log.d("google add to local", userEntity.toString());

                            return userRepository.addUser(userEntity)
                                    .doOnComplete(() -> Log.d("ROOM", "User inserted"))
                                    .doOnError(e -> Log.e("ROOM", "Insert error", e));
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    view.hideLoading();
                                    view.onRegistrationSuccess();
                                },
                                throwable -> {
                                    view.hideLoading();
                                    view.onRegistrationError(throwable.getMessage());
                                }
                        )
        );
    }

    @Override
    public void onGoogleBtnClicked() {
        view.openGoogleSignInMap();
    }

    @Override
    public void dispose() {
        disposable.clear();
    }
}