package com.example.foodplanner.Presentation.favoritesScreen.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.Data.meals.model.wrapper.SelectedMeal;
import com.example.foodplanner.Presentation.favoritesScreen.presenter.FavoritesPresenter;
import com.example.foodplanner.Presentation.favoritesScreen.presenter.FavoritesPresenterImp;
import com.example.foodplanner.R;

import java.util.List;

public class FavoritesFragment extends Fragment implements FavoritesViewer, onFavoriteItemClickListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View emptyStateView;
    private LottieAnimationView emptyAnimation;
    private TextView emptyText;

    private FavoritesAdapter adapter;
    private FavoritesPresenter presenter;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        recyclerView = view.findViewById(R.id.favoritesRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.favoritesSwipeRefresh);
        emptyStateView = view.findViewById(R.id.emptyStateView);
        emptyAnimation = view.findViewById(R.id.emptyAnimation);
        emptyText = view.findViewById(R.id.emptyText);

        // Setup RecyclerView
        adapter = new FavoritesAdapter(requireContext(), this);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(adapter);

        // Setup presenter
        presenter = new FavoritesPresenterImp(this, requireContext());

        // Setup swipe refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.loadFavorites();
        });

        // Load favorites
        presenter.loadFavorites();
    }

    @Override
    public void showFavorites(List<Meal> favorites) {
        recyclerView.setVisibility(View.VISIBLE);
        emptyStateView.setVisibility(View.GONE);
        adapter.setFavoritesList(favorites);
    }

    @Override
    public void showEmptyState() {
        recyclerView.setVisibility(View.GONE);
        emptyStateView.setVisibility(View.VISIBLE);
        emptyAnimation.playAnimation();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToMealDetails(SelectedMeal selectedMeal) {
        FavoritesFragmentDirections.ActionFavoritesFragmentToMealDetailsFragment action = FavoritesFragmentDirections
                .actionFavoritesFragmentToMealDetailsFragment(selectedMeal);
        Navigation.findNavController(getView()).navigate(action);
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onMealClick(Meal meal) {
        presenter.onMealClicked(meal);
    }

    @Override
    public void onRemoveClick(Meal meal) {
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                .setTitle("Remove from Favorites")
                .setMessage("Are you sure you want to remove this meal from your favorites?")
                .setPositiveButton("Remove", (dialog, which) -> {
                    presenter.removeFavorite(Integer.parseInt(meal.getId()));
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
