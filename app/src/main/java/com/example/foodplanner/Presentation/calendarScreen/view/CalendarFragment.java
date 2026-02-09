package com.example.foodplanner.Presentation.calendarScreen.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.Data.mealplan.model.ScheduledMeal;
import com.example.foodplanner.Presentation.calendarScreen.presenter.CalendarPresenter;
import com.example.foodplanner.Presentation.calendarScreen.presenter.CalendarPresenterImp;
import com.example.foodplanner.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment implements CalendarViewer, onScheduledMealClickListener {
    
    private CalendarView calendarView;
    private RecyclerView dayMealsRecyclerView;
    private LinearLayout emptyStateLayout;
    private TextView emptyStateText;
    private ProgressBar progressBar;
    private MaterialCardView offlineBanner;
    private FloatingActionButton addMealFab;
    private TextView selectedDateLabel;
    
    private CalendarPresenter presenter;
    private DayMealsAdapter adapter;
    private String selectedDate;
    private SimpleDateFormat dateFormat;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CalendarPresenterImp(this, requireContext());
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        calendarView = view.findViewById(R.id.calendarView);
        dayMealsRecyclerView = view.findViewById(R.id.dayMealsRecyclerView);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        emptyStateText = view.findViewById(R.id.emptyStateText);
        progressBar = view.findViewById(R.id.progressBar);
        offlineBanner = view.findViewById(R.id.offlineBanner);
        addMealFab = view.findViewById(R.id.addMealFab);
        selectedDateLabel = view.findViewById(R.id.selectedDateLabel);
        
        // Setup RecyclerView
        adapter = new DayMealsAdapter(this);
        dayMealsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        dayMealsRecyclerView.setAdapter(adapter);
        
        // Get current date
        Calendar calendar = Calendar.getInstance();
        selectedDate = dateFormat.format(calendar.getTime());
        
        // Restrict calendar to current month only
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        long minDate = calendar.getTimeInMillis();
        
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        long maxDate = calendar.getTimeInMillis();
        
        calendarView.setMinDate(minDate);
        calendarView.setMaxDate(maxDate);
        
        // Calendar date selection listener
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            presenter.onDaySelected(selectedDate);
        });
        
        // FAB click listener - navigate to discovery to select a meal
        addMealFab.setOnClickListener(v -> {
            // Navigate to discovery fragment
            Navigation.findNavController(v).navigate(R.id.discoveryFragment);
            Toast.makeText(requireContext(), "Select a meal to add to " + selectedDate, Toast.LENGTH_SHORT).show();
        });
        
        // Load current month and selected day
        Calendar now = Calendar.getInstance();
        presenter.loadMonth(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);
        presenter.onDaySelected(selectedDate);
        presenter.checkConnectivity();
    }

    @Override
    public void showScheduledMeals(List<ScheduledMeal> meals, String date) {
        dayMealsRecyclerView.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        adapter.setMeals(meals);
        updateSelectedDateLabel(date, meals.size());
    }

    @Override
    public void showEmptyState(String date) {
        dayMealsRecyclerView.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.VISIBLE);
        emptyStateText.setText(getString(R.string.no_meals_scheduled));
        updateSelectedDateLabel(date, 0);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showOfflineBanner() {
        offlineBanner.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideOfflineBanner() {
        offlineBanner.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        dayMealsRecyclerView.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void highlightDatesWithMeals(List<String> dates) {
        // Note: CalendarView doesn't support custom date decorators in XML
        // For more advanced highlighting, consider using MaterialCalendarView library
        // For now, this is a placeholder
    }

    @Override
    public void showMealAddedSuccess() {
        Toast.makeText(requireContext(), R.string.meal_added_to_calendar, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMealRemovedSuccess() {
        Toast.makeText(requireContext(), R.string.meal_removed_from_calendar, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMealClick(ScheduledMeal meal) {
        // Navigate to meal details
        // Note: You'll need to convert ScheduledMeal to SelectedMeal for navigation
        Toast.makeText(requireContext(), "Viewing: " + meal.getMealName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemoveClick(ScheduledMeal meal) {
        presenter.removeMeal(meal.getMealId(), meal.getScheduledDate());
    }

    private void updateSelectedDateLabel(String date, int mealCount) {
        String label = "Meals for " + date + " (" + mealCount + ")";
        selectedDateLabel.setText(label);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}
