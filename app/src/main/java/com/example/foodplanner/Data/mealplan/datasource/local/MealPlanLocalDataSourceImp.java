package com.example.foodplanner.Data.mealplan.datasource.local;

import com.example.foodplanner.Data.mealplan.entity.ScheduledMealEntity;
import com.example.foodplanner.Data.mealplan.datasource.ScheduledMealDao;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class MealPlanLocalDataSourceImp implements MealPlanLocalDataSource {
    
    private final ScheduledMealDao scheduledMealDao;

    public MealPlanLocalDataSourceImp(ScheduledMealDao scheduledMealDao) {
        this.scheduledMealDao = scheduledMealDao;
    }

    @Override
    public Completable insertScheduledMeal(ScheduledMealEntity scheduledMeal) {
        return scheduledMealDao.insertScheduledMeal(scheduledMeal);
    }

    @Override
    public Completable deleteScheduledMeal(String mealId, String date, String userId) {
        return scheduledMealDao.deleteScheduledMeal(mealId, date, userId);
    }

    @Override
    public Flowable<List<ScheduledMealEntity>> getScheduledMealsForDate(String date, String userId) {
        return scheduledMealDao.getScheduledMealsForDate(date, userId);
    }

    @Override
    public Flowable<List<ScheduledMealEntity>> getScheduledMealsForMonth(String yearMonth, String userId) {
        return scheduledMealDao.getScheduledMealsForMonth(yearMonth, userId);
    }

    @Override
    public Flowable<List<ScheduledMealEntity>> getAllScheduledMeals(String userId) {
        return scheduledMealDao.getAllScheduledMeals(userId);
    }

    @Override
    public Completable deleteAllForUser(String userId) {
        return scheduledMealDao.deleteAllForUser(userId);
    }

    @Override
    public Flowable<List<String>> getDatesWithMeals(String yearMonth, String userId) {
        return scheduledMealDao.getDatesWithMeals(yearMonth, userId);
    }
}
