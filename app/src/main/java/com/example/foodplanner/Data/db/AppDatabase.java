package com.example.foodplanner.Data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.foodplanner.Data.favorites.entity.FavoriteMealEntity;
import com.example.foodplanner.Data.favorites.local.FavoriteMealDao;
import com.example.foodplanner.Data.user.Entity.UserEntity;
import com.example.foodplanner.Data.user.local.UserDao;


@Database(entities = {UserEntity.class, FavoriteMealEntity.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDaoDao();
    public abstract FavoriteMealDao favoriteMealDao();
    private static volatile AppDatabase instance;


    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "user_db"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}