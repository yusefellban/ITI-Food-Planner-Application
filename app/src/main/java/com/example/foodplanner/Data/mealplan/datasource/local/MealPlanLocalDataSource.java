package com.example.foodplanner.Data.mealplan.datasource.local;

import com.example.foodplanner.Data.mealplan.entity.ScheduledMealEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface MealPlanLocalDataSource {
    
    Completable insertScheduledMeal(ScheduledMealEntity scheduledMeal);
    
    Completable deleteScheduledMeal(String mealId, String date, String userId);
    
    Flowable<List<ScheduledMealEntity>> getScheduledMealsForDate(String date, String userId);
    
    Flowable<List<ScheduledMealEntity>> getScheduledMealsForMonth(String yearMonth, String userId);
    
    Flowable<List<ScheduledMealEntity>> getAllScheduledMeals(String userId);
    
    Completable deleteAllForUser(String userId);
    
    Flowable<List<String>> getDatesWithMeals(String yearMonth, String userId);
}
