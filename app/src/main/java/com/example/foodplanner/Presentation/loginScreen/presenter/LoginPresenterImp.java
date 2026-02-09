package com.example.foodplanner.Presentation.loginScreen.presenter;


import com.example.foodplanner.Data.AuthRepository;
import com.example.foodplanner.Presentation.loginScreen.view.LoginViewer;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginPresenterImp implements LoginPresenter {

    private final LoginViewer loginViewer;
    private final AuthRepository authRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LoginPresenterImp(LoginViewer loginViewer) {
        this.loginViewer = loginViewer;
        this.authRepository = new AuthRepository();
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
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result -> {
                                    loginViewer.hideLoading();
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
}