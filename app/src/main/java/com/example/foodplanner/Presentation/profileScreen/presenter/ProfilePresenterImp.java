package com.example.foodplanner.Presentation.profileScreen.presenter;

import android.content.Context;
import android.util.Log;

import com.example.foodplanner.Data.AuthRepository;
import com.example.foodplanner.Data.UserRepository;


import com.example.foodplanner.Presentation.profileScreen.view.ProfileView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfilePresenterImp implements ProfilePresenter {
    private UserRepository userRepository;
    private AuthRepository authRepository;
    private ProfileView view;
    private CompositeDisposable disposable = new CompositeDisposable();

    public ProfilePresenterImp(Context context, ProfileView view) {
        this.userRepository = new UserRepository(context);
        authRepository = new AuthRepository(context);
        this.view = view;
    }

    @Override

    public void getCurrentLocalUser() {
        disposable.add(authRepository.getUserInfo().subscribeOn(Schedulers.io()).flatMapMaybe(firebaseUser -> userRepository.getUserById(firebaseUser.getUid()).doOnSuccess(user -> Log.d("ROOM", "User fetched: " + user.getUid()))).observeOn(AndroidSchedulers.mainThread()).subscribe(userEntity -> {
            view.showUserData(userEntity.getName(), userEntity.getEmail(), userEntity.getImagePath());
        }, error -> {
            Log.e("ROOM", "Fetch error", error);
            view.showError("Can not fetch data");
        }, () -> {
            Log.d("ROOM", "No user found in local DB");
            view.showError(" No user found in local DB");
        }));
    }


    @Override

    public void changeProfileImage(String newPath) {
        disposable.add(authRepository.getUserInfo().subscribeOn(Schedulers.io()).flatMapCompletable(firebaseUser -> userRepository.updateUserImage(firebaseUser.getUid(), newPath)).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> view.showSuccessMessage("image uploaded success"), throwable -> view.showError(" fail to upload " + throwable.getMessage())));
    }

    @Override

    public void trackProfileImage() {
        disposable.add(authRepository.getUserInfo()
                .subscribeOn(Schedulers.io()).
                flatMapPublisher(firebaseUser ->
                        userRepository.
                                getProfileImage(firebaseUser.getUid())
        ).observeOn(AndroidSchedulers.mainThread()).subscribe(path -> {
            if (path != null && !path.isEmpty()) {
                view.displayImage(path);
            }
        }, error -> view.showError("Stream error: " + error.getMessage())));
    }

    @Override
    public void dispose() {
        disposable.clear();
    }


    @Override
    public void performLogout() {
        disposable.add(
                authRepository.logout()
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .andThen(userRepository.logout())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    Log.d("LOGOUT", "Firebase and Local DB cleared");
                                    view.onLogoutSuccess();
                                },
                                throwable -> {
                                    Log.e("LOGOUT", "Error during logout", throwable);
                                    view.showError("Logout failed: " + throwable.getMessage());
                                }
                        )
        );
    }

}