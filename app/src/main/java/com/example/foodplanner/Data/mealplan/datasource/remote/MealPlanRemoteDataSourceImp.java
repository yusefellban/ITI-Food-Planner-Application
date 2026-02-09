package com.example.foodplanner.Data.mealplan.datasource.remote;

import android.util.Log;

import com.example.foodplanner.Data.mealplan.model.ScheduledMeal;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class MealPlanRemoteDataSourceImp implements MealPlanRemoteDataSource {
    
    private static final String TAG = "MealPlanRemoteDS";
    private static final String COLLECTION_USERS = "users";
    private static final String COLLECTION_SCHEDULED_MEALS = "scheduled_meals";
    
    private final FirebaseFirestore firestore;

    public MealPlanRemoteDataSourceImp() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public Completable addScheduledMeal(ScheduledMeal meal, String userId) {
        return Completable.create(emitter -> {
            String documentId = meal.getMealId() + "_" + meal.getScheduledDate();
            
            Map<String, Object> mealData = new HashMap<>();
            mealData.put("mealId", meal.getMealId());
            mealData.put("mealName", meal.getMealName());
            mealData.put("mealImageUrl", meal.getMealImageUrl());
            mealData.put("category", meal.getCategory());
            mealData.put("scheduledDate", meal.getScheduledDate());
            mealData.put("timestamp", System.currentTimeMillis());
            
            firestore.collection(COLLECTION_USERS)
                    .document(userId)
                    .collection(COLLECTION_SCHEDULED_MEALS)
                    .document(documentId)
                    .set(mealData)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Meal scheduled successfully: " + documentId);
                        emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error scheduling meal", e);
                        emitter.onError(e);
                    });
        });
    }

    @Override
    public Completable removeScheduledMeal(String mealId, String date, String userId) {
        return Completable.create(emitter -> {
            String documentId = mealId + "_" + date;
            
            firestore.collection(COLLECTION_USERS)
                    .document(userId)
                    .collection(COLLECTION_SCHEDULED_MEALS)
                    .document(documentId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Scheduled meal removed: " + documentId);
                        emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error removing scheduled meal", e);
                        emitter.onError(e);
                    });
        });
    }

    @Override
    public Single<List<ScheduledMeal>> getScheduledMealsForMonth(String yearMonth, String userId) {
        return Single.create(emitter -> {
            firestore.collection(COLLECTION_USERS)
                    .document(userId)
                    .collection(COLLECTION_SCHEDULED_MEALS)
                    .whereGreaterThanOrEqualTo("scheduledDate", yearMonth + "-01")
                    .whereLessThan("scheduledDate", getNextMonth(yearMonth))
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<ScheduledMeal> meals = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            ScheduledMeal meal = documentToScheduledMeal(document);
                            if (meal != null) {
                                meals.add(meal);
                            }
                        }
                        Log.d(TAG, "Retrieved " + meals.size() + " meals for month: " + yearMonth);
                        emitter.onSuccess(meals);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error retrieving meals for month", e);
                        emitter.onError(e);
                    });
        });
    }

    @Override
    public Single<List<ScheduledMeal>> getScheduledMealsForDate(String date, String userId) {
        return Single.create(emitter -> {
            firestore.collection(COLLECTION_USERS)
                    .document(userId)
                    .collection(COLLECTION_SCHEDULED_MEALS)
                    .whereEqualTo("scheduledDate", date)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<ScheduledMeal> meals = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            ScheduledMeal meal = documentToScheduledMeal(document);
                            if (meal != null) {
                                meals.add(meal);
                            }
                        }
                        Log.d(TAG, "Retrieved " + meals.size() + " meals for date: " + date);
                        emitter.onSuccess(meals);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error retrieving meals for date", e);
                        emitter.onError(e);
                    });
        });
    }

    @Override
    public Single<List<String>> getDatesWithMeals(String yearMonth, String userId) {
        return Single.create(emitter -> {
            firestore.collection(COLLECTION_USERS)
                    .document(userId)
                    .collection(COLLECTION_SCHEDULED_MEALS)
                    .whereGreaterThanOrEqualTo("scheduledDate", yearMonth + "-01")
                    .whereLessThan("scheduledDate", getNextMonth(yearMonth))
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<String> dates = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String date = document.getString("scheduledDate");
                            if (date != null && !dates.contains(date)) {
                                dates.add(date);
                            }
                        }
                        emitter.onSuccess(dates);
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }

    private ScheduledMeal documentToScheduledMeal(QueryDocumentSnapshot document) {
        try {
            ScheduledMeal meal = new ScheduledMeal();
            meal.setMealId(document.getString("mealId"));
            meal.setMealName(document.getString("mealName"));
            meal.setMealImageUrl(document.getString("mealImageUrl"));
            meal.setCategory(document.getString("category"));
            meal.setScheduledDate(document.getString("scheduledDate"));
            return meal;
        } catch (Exception e) {
            Log.e(TAG, "Error converting document to ScheduledMeal", e);
            return null;
        }
    }

    private String getNextMonth(String yearMonth) {
        // yearMonth format: "2026-02"
        String[] parts = yearMonth.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        
        month++;
        if (month > 12) {
            month = 1;
            year++;
        }
        
        return String.format("%04d-%02d", year, month) + "-01";
    }
}
