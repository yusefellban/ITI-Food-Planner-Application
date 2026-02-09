package com.example.foodplanner.Data;

import android.content.Context;
import android.util.Log;

import com.example.foodplanner.Data.db.AppDatabase;
import com.example.foodplanner.Data.mealplan.datasource.local.MealPlanLocalDataSource;
import com.example.foodplanner.Data.mealplan.datasource.local.MealPlanLocalDataSourceImp;
import com.example.foodplanner.Data.mealplan.datasource.remote.MealPlanRemoteDataSource;
import com.example.foodplanner.Data.mealplan.datasource.remote.MealPlanRemoteDataSourceImp;
import com.example.foodplanner.Data.mealplan.entity.ScheduledMealEntity;
import com.example.foodplanner.Data.mealplan.model.ScheduledMeal;
import com.example.foodplanner.Data.meals.datasource.local.CountryCodeLocalData;
import com.example.foodplanner.Data.meals.datasource.remote.CategoriesResponseCallback;
import com.example.foodplanner.Data.meals.datasource.remote.IngredientResponseCallback;
import com.example.foodplanner.Data.meals.datasource.remote.MealCallback;
import com.example.foodplanner.Data.meals.datasource.remote.MealRemoteDataSource;
import com.example.foodplanner.Data.meals.datasource.remote.MealsListCallback;
import com.example.foodplanner.Data.meals.model.Country;
import com.example.foodplanner.Data.meals.model.wrapper.SendSelectedItem;
import com.example.foodplanner.utils.NetworkManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealRepository {
    private final CountryCodeLocalData countryCodeLocalData;
    private final MealRemoteDataSource mealRemoteDataSource;
//    private final MealDetailsRemoteDataSource mealDetailsRemoteDataSource;
    private final MealPlanLocalDataSource localDataSource;
    private final MealPlanRemoteDataSource remoteDataSource;
    private final FirebaseAuth firebaseAuth;
    Context context;
    private static final String TAG = "MealPlanRepository";

    public MealRepository(Context context) {
        countryCodeLocalData = new CountryCodeLocalData();
        mealRemoteDataSource = new MealRemoteDataSource();
//        mealDetailsRemoteDataSource=new MealDetailsRemoteDataSource();
        this.context=context;
        this.localDataSource = new MealPlanLocalDataSourceImp(AppDatabase.getInstance(this.context).scheduledMealDao());
        this.remoteDataSource = new MealPlanRemoteDataSourceImp();
        this.firebaseAuth = FirebaseAuth.getInstance();
    }


    public void getRandomMeal(MealCallback mealCallback) {
        mealRemoteDataSource.getRandomMeal(mealCallback);

    }

    public void getRandomMealsList(MealsListCallback mealsListCallback) {
        mealRemoteDataSource.getRandomMealsList(mealsListCallback);

    }

    public String getImageUrl(String area) {
        return countryCodeLocalData.getImageUrl(area);

    }

    public void getAllCategories(CategoriesResponseCallback categoriesResponseCallback) {
        mealRemoteDataSource.getAllCategories(categoriesResponseCallback);
    }

    public void getAllIngredients(IngredientResponseCallback ingredientResponseCallback) {
        mealRemoteDataSource.getAllIngredients(ingredientResponseCallback);
    }

    public List<Country> getAllCountries() {
        return countryCodeLocalData.getAllCountries();
    }

    public void getMealDetails(int id, MealCallback mealCallback) {
        mealRemoteDataSource.getMealDetails(id, mealCallback);
    }

    public void getAllFilteredMeals(SendSelectedItem selectedItem, MealsListCallback mealsListCallback) {
        mealRemoteDataSource.getAllFilteredMeals(selectedItem,mealsListCallback);
    }

    /**
     * planing meals
     */

    // Schedule a meal
    public Completable scheduleMeal(ScheduledMeal meal) {
        String userId = getCurrentUserId();
        if (userId == null) {
            return Completable.error(new Exception("User not logged in"));
        }

        ScheduledMealEntity entity = convertToEntity(meal, userId);

        if (isOnline()) {
            // Online: Write to Firebase first, then cache in Room
            return remoteDataSource.addScheduledMeal(meal, userId)
                    .subscribeOn(Schedulers.io())
                    .andThen(localDataSource.insertScheduledMeal(entity).subscribeOn(Schedulers.io()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete(() -> Log.d(TAG, "Meal scheduled online and cached"))
                    .doOnError(error -> Log.e(TAG, "Error scheduling meal online", error));
        } else {
            // Offline: Write to Room only
            return localDataSource.insertScheduledMeal(entity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete(() -> Log.d(TAG, "Meal scheduled offline (Room only)"))
                    .doOnError(error -> Log.e(TAG, "Error scheduling meal offline", error));
        }
    }

    // Remove a scheduled meal
    public Completable removeScheduledMeal(String mealId, String date) {
        String userId = getCurrentUserId();
        if (userId == null) {
            return Completable.error(new Exception("User not logged in"));
        }

        if (isOnline()) {
            // Online: Remove from Firebase and Room
            return remoteDataSource.removeScheduledMeal(mealId, date, userId)
                    .subscribeOn(Schedulers.io())
                    .andThen(localDataSource.deleteScheduledMeal(mealId, date, userId).subscribeOn(Schedulers.io()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete(() -> Log.d(TAG, "Meal removed from Firebase and Room"))
                    .doOnError(error -> Log.e(TAG, "Error removing meal", error));
        } else {
            return localDataSource.deleteScheduledMeal(mealId, date, userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete(() -> Log.d(TAG, "Meal removed from Room (offline)"))
                    .doOnError(error -> Log.e(TAG, "Error removing meal offline", error));
        }
    }

    // Get scheduled meals for a specific date
    public Flowable<List<ScheduledMeal>> getScheduledMealsForDate(String date) {
        String userId = getCurrentUserId();
        if (userId == null) {
            return Flowable.error(new Exception("User not logged in"));
        }

        if (isOnline()) {
            // Try Firebase first, cache to Room, then return from Room (for real-time updates)
            return remoteDataSource.getScheduledMealsForDate(date, userId)
                    .subscribeOn(Schedulers.io())
                    .flatMapCompletable(remoteMeals -> {
                        // Cache the Firebase data to Room
                        List<Completable> insertCompletables = new ArrayList<>();
                        for (ScheduledMeal meal : remoteMeals) {
                            insertCompletables.add(localDataSource.insertScheduledMeal(convertToEntity(meal, userId)));
                        }
                        return Completable.merge(insertCompletables);
                    })
                    .andThen(localDataSource.getScheduledMealsForDate(date, userId))
                    .map(this::convertEntitiesToMeals)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(meals -> Log.d(TAG, "Retrieved " + meals.size() + " meals for date from Firebase/Room"))
                    .onErrorResumeNext(error -> {
                        // On error, fallback to Room cache
                        Log.w(TAG, "Error fetching from Firebase, using cache", error);
                        return localDataSource.getScheduledMealsForDate(date, userId)
                                .map(this::convertEntitiesToMeals)
                                .observeOn(AndroidSchedulers.mainThread());
                    });
        } else {
            // Offline: Return from Room cache
            return localDataSource.getScheduledMealsForDate(date, userId)
                    .map(this::convertEntitiesToMeals)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(meals -> Log.d(TAG, "Retrieved " + meals.size() + " meals for date from Room (offline)"));
        }
    }

    // Get scheduled meals for a month
    public Flowable<List<ScheduledMeal>> getScheduledMealsForMonth(String yearMonth) {
        String userId = getCurrentUserId();
        if (userId == null) {
            return Flowable.error(new Exception("User not logged in"));
        }

        if (isOnline()) {
            // Online: Fetch from Firebase and cache
            return remoteDataSource.getScheduledMealsForMonth(yearMonth, userId)
                    .subscribeOn(Schedulers.io())
                    .flatMapCompletable(remoteMeals -> {
                        List<Completable> insertCompletables = new ArrayList<>();
                        for (ScheduledMeal meal : remoteMeals) {
                            insertCompletables.add(localDataSource.insertScheduledMeal(convertToEntity(meal, userId)));
                        }
                        return Completable.merge(insertCompletables);
                    })
                    .andThen(localDataSource.getScheduledMealsForMonth(yearMonth, userId))
                    .map(this::convertEntitiesToMeals)
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(error -> {
                        Log.w(TAG, "Error fetching month from Firebase, using cache", error);
                        return localDataSource.getScheduledMealsForMonth(yearMonth, userId)
                                .map(this::convertEntitiesToMeals)
                                .observeOn(AndroidSchedulers.mainThread());
                    });
        } else {
            // Offline: Return from Room cache
            return localDataSource.getScheduledMealsForMonth(yearMonth, userId)
                    .map(this::convertEntitiesToMeals)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(meals -> Log.d(TAG, "Retrieved " + meals.size() + " meals for month from Room (offline)"));
        }
    }

    // Get dates that have meals scheduled
    public Flowable<List<String>> getDatesWithMeals(String yearMonth) {
        String userId = getCurrentUserId();
        if (userId == null) {
            return Flowable.error(new Exception("User not logged in"));
        }

        if (isOnline()) {
            return remoteDataSource.getDatesWithMeals(yearMonth, userId)
                    .toFlowable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(error -> {
                        Log.w(TAG, "Error fetching dates from Firebase, using cache", error);
                        return localDataSource.getDatesWithMeals(yearMonth, userId)
                                .observeOn(AndroidSchedulers.mainThread());
                    });
        } else {
            return localDataSource.getDatesWithMeals(yearMonth, userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    // Check if online
    public boolean isOnline() {
        return NetworkManager.isNetworkAvailable(context);
    }

    // Get current user ID
    private String getCurrentUserId() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    // Convert ScheduledMeal to Entity
    private ScheduledMealEntity convertToEntity(ScheduledMeal meal, String userId) {
        return new ScheduledMealEntity(
                meal.getMealId(),
                meal.getMealName(),
                meal.getMealImageUrl(),
                meal.getCategory(),
                meal.getScheduledDate(),
                userId,
                System.currentTimeMillis()
        );
    }

    // Convert list of entities to ScheduledMeals
    private List<ScheduledMeal> convertEntitiesToMeals(List<ScheduledMealEntity> entities) {
        List<ScheduledMeal> meals = new ArrayList<>();
        for (ScheduledMealEntity entity : entities) {
            ScheduledMeal meal = new ScheduledMeal();
            meal.setMealId(entity.getMealId());
            meal.setMealName(entity.getMealName());
            meal.setMealImageUrl(entity.getMealImageUrl());
            meal.setCategory(entity.getCategory());
            meal.setScheduledDate(entity.getScheduledDate());
            meals.add(meal);
        }
        return meals;
    }
}
