package com.example.foodplanner.Presentation.registrationScreen.presenter;

import com.example.foodplanner.Data.AuthRepository;
import com.example.foodplanner.Data.auth.datasource.AuthRemoteDataSourceImp;
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
    private final CompositeDisposable disposable = new CompositeDisposable();

    public RegistrationPresenterImp(RegistrationViewer view) {
        this.view = view;
        this.repo = new AuthRepository();
    }

    @Override
    public void register(String name, String email, String password) {
        view.showLoading();
        disposable.add(
                repo.registerWithEmail(name, email, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result -> {
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
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result -> {
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
    public void onGoogleBtnClicked() { view.openGoogleSignInMap(); }

    @Override
    public void dispose() { disposable.clear(); }
}