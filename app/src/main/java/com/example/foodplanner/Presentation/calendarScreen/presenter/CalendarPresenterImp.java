package com.example.foodplanner.Presentation.calendarScreen.presenter;

import android.content.Context;
import android.util.Log;

import com.example.foodplanner.Data.MealRepository;
import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.Data.mealplan.model.ScheduledMeal;
import com.example.foodplanner.Data.meals.model.wrapper.SelectedMeal;
import com.example.foodplanner.Presentation.calendarScreen.view.CalendarViewer;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class CalendarPresenterImp implements CalendarPresenter {
    
    private static final String TAG = "CalendarPresenterImp";
    private final CalendarViewer viewer;
    private final MealRepository repository;
    private final CompositeDisposable disposables;
    private String currentYearMonth;

    public CalendarPresenterImp(CalendarViewer viewer, Context context) {
        this.viewer = viewer;
        this.repository = new MealRepository(context);
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void loadMonth(int year, int month) {
        currentYearMonth = String.format("%04d-%02d", year, month);
        
        // Check connectivity and show/hide banner
        checkConnectivity();
        
        // Load dates with scheduled meals to highlight them
        Disposable disposable = repository.getDatesWithMeals(currentYearMonth)
                .subscribe(
                        viewer::highlightDatesWithMeals,
                        error -> {
                            Log.e(TAG, "Error loading dates with meals", error);
                            viewer.showError("Error loading calendar data");
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void onDaySelected(String date) {
        viewer.showLoading();
        
        Disposable disposable = repository.getScheduledMealsForDate(date)
                .subscribe(
                        meals -> {
                            viewer.hideLoading();
                            if (meals.isEmpty()) {
                                viewer.showEmptyState(date);
                            } else {
                                viewer.showScheduledMeals(meals, date);
                            }
                        },
                        error -> {
                            viewer.hideLoading();
                            Log.e(TAG, "Error loading meals for date: " + date, error);
                            viewer.showError("Error loading meals for selected date");
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void addMealToDate(Meal meal, String date) {
        ScheduledMeal scheduledMeal = new ScheduledMeal(
                meal.getId(),
                meal.getName(),
                meal.getThumbnailUrl(),
                meal.getCategory(),
                date
        );
        
        Disposable disposable = repository.scheduleMeal(scheduledMeal)
                .subscribe(
                        () -> {
                            Log.d(TAG, "Meal added successfully: " + meal.getName());
                            viewer.showMealAddedSuccess();
                            // Reload the day to show updated list
                            onDaySelected(date);
                            // Reload month to update highlighted dates
                            if (currentYearMonth != null) {
                                String[] parts = currentYearMonth.split("-");
                                loadMonth(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                            }
                        },
                        error -> {
                            Log.e(TAG, "Error adding meal to calendar", error);
                            viewer.showError("Failed to add meal to calendar");
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void removeMeal(String mealId, String date) {
        Disposable disposable = repository.removeScheduledMeal(mealId, date)
                .subscribe(
                        () -> {
                            Log.d(TAG, "Meal removed successfully");
                            viewer.showMealRemovedSuccess();
                            // Reload the day to show updated list
                            onDaySelected(date);
                            // Reload month to update highlighted dates
                            if (currentYearMonth != null) {
                                String[] parts = currentYearMonth.split("-");
                                loadMonth(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                            }
                        },
                        error -> {
                            Log.e(TAG, "Error removing meal from calendar", error);
                            viewer.showError("Failed to remove meal");
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void checkConnectivity() {
        if (repository.isOnline()) {
            viewer.hideOfflineBanner();
        } else {
            viewer.showOfflineBanner();
        }
    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }

    @Override
    public void onMealClicked(ScheduledMeal meal) {
        viewer.navigateToMealDetails(new SelectedMeal(Integer.parseInt(meal.getMealId()),meal.getMealName(),meal.getMealImageUrl()));

    }

}
