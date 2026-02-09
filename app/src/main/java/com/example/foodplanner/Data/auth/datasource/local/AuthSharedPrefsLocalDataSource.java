package com.example.foodplanner.Data.auth.datasource.local;

import android.content.Context;
import android.content.SharedPreferences;

import io.reactivex.rxjava3.core.Completable;

public class AuthSharedPrefsLocalDataSource {
    private static final String PREF_NAME = "user_prefs";
    private final SharedPreferences sharedPreferences;

    public AuthSharedPrefsLocalDataSource(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setLoggedIn(boolean isLoggedIn) {
        sharedPreferences.edit().putBoolean("is_logged_in", isLoggedIn).apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean("is_logged_in", false);
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }


    public void logout() {
        this.setLoggedIn(false);
    }
}
