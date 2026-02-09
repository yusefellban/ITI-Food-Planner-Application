package com.example.foodplanner.Presentation.loginScreen.presenter;


import android.content.Context;
import android.util.Log;

import com.example.foodplanner.Data.AuthRepository;
import com.example.foodplanner.Data.UserRepository;
import com.example.foodplanner.Data.user.Entity.UserEntity;
import com.example.foodplanner.Presentation.loginScreen.view.LoginViewer;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginPresenterImp implements LoginPresenter {

    private final LoginViewer loginViewer;
    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LoginPresenterImp(LoginViewer loginViewer, Context context) {
        this.loginViewer = loginViewer;
        this.authRepository = new AuthRepository(context);
        this.userRepository = new UserRepository(context);

    }

    @Override
    public void loginWithEmail(String email, String password) {
        loginViewer.showLoading();
        compositeDisposable.add(
                authRepository.loginWithEmail(email, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result -> {
                                    loginViewer.hideLoading();
                                    authRepository.setSharedLoggedIn(true);
                                    loginViewer.onLoginSuccess();
                                },
                                throwable -> {
                                    loginViewer.hideLoading();
                                    loginViewer.onLoginError(throwable.getMessage());
                                }
                        )
        );
    }

    @Override
    public void loginWithGoogle(String idToken) {
        loginViewer.showLoading();
        compositeDisposable.add(
                authRepository.loginWithGoogle(idToken)
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
                                    loginViewer.hideLoading();
                                    authRepository.setSharedLoggedIn(true);
                                    loginViewer.onLoginSuccess();
                                },
                                throwable -> {
                                    loginViewer.hideLoading();
                                    loginViewer.onLoginError(throwable.getMessage());
                                }
                        )
        );
    }


    @Override
    public void openGoogleSignInMap() {

        loginViewer.openGoogleSignInMap();
    }

    @Override
    public void setupGoogle() {
        loginViewer.setupGoogleClient();
    }

    @Override
    public void dispose() {
        compositeDisposable.clear();
    }

    public void cheekIfUserExist(){
        if(authRepository.isSharedLoggedIn()){
            loginViewer.onLoginSuccess();
        }
    }
}