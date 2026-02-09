package com.example.foodplanner.Data.mealplan.datasource;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.Data.mealplan.entity.ScheduledMealEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ScheduledMealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertScheduledMeal(ScheduledMealEntity scheduledMeal);

    @Query("DELETE FROM scheduled_meals WHERE mealId = :mealId AND scheduledDate = :date AND userId = :userId")
    Completable deleteScheduledMeal(String mealId, String date, String userId);

    @Query("SELECT * FROM scheduled_meals WHERE scheduledDate = :date AND userId = :userId ORDER BY timestamp DESC")
    Flowable<List<ScheduledMealEntity>> getScheduledMealsForDate(String date, String userId);

    @Query("SELECT * FROM scheduled_meals WHERE userId = :userId AND scheduledDate LIKE :yearMonth || '%' ORDER BY scheduledDate, timestamp DESC")
    Flowable<List<ScheduledMealEntity>> getScheduledMealsForMonth(String yearMonth, String userId);

    @Query("SELECT * FROM scheduled_meals WHERE userId = :userId")
    Flowable<List<ScheduledMealEntity>> getAllScheduledMeals(String userId);

    @Query("DELETE FROM scheduled_meals WHERE userId = :userId")
    Completable deleteAllForUser(String userId);

    @Query("SELECT DISTINCT scheduledDate FROM scheduled_meals WHERE userId = :userId AND scheduledDate LIKE :yearMonth || '%'")
    Flowable<List<String>> getDatesWithMeals(String yearMonth, String userId);
}
