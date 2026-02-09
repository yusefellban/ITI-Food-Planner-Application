package com.example.foodplanner.Data.mealplan.datasource.remote;

import com.example.foodplanner.Data.mealplan.model.ScheduledMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface MealPlanRemoteDataSource {
    
    Completable addScheduledMeal(ScheduledMeal meal, String userId);
    
    Completable removeScheduledMeal(String mealId, String date, String userId);
    
    Single<List<ScheduledMeal>> getScheduledMealsForMonth(String yearMonth, String userId);
    
    Single<List<ScheduledMeal>> getScheduledMealsForDate(String date, String userId);
    
    Single<List<String>> getDatesWithMeals(String yearMonth, String userId);
}
