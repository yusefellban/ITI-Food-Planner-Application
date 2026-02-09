package com.example.foodplanner.Presentation.homeScreen.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Presentation.homeScreen.presenter.HomePresenterImp;
import com.example.foodplanner.R;
import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.Data.meals.datasource.remote.MealCallback;
import com.example.foodplanner.Data.meals.datasource.remote.MealsListCallback;
import com.example.foodplanner.Data.meals.model.wrapper.SelectedMeal;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.MultiBrowseCarouselStrategy;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class HomeFragment extends Fragment implements onItemClickListener, HomeViewer {

    private ImageView homeRecipeImage;
    private ImageView homeMealCardArea;
    private TextView homeRecipeTitle;
    private TextView homeTag1;
    private TextView homeTag2;
    private TextView homeUserName;
    private ImageView homeUserImage;

    private ShimmerFrameLayout shimmerFrameLayout;
    private ShimmerFrameLayout shimmerCarousel;
    private View mealCard;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView recyclerView;
    private Button cookNow;

    private HomeCarouselAdapter adapter;
    /// presenter
    HomePresenterImp presenter;


    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeRecipeImage = view.findViewById(R.id.homeRecipeImage);
        homeRecipeTitle = view.findViewById(R.id.homeRecipeTitle);
        homeTag1 = view.findViewById(R.id.homeTag1);
        homeTag2 = view.findViewById(R.id.homeTag2);
        shimmerFrameLayout = view.findViewById(R.id.homeMealOfTheDayShimmerLayout);
        mealCard = view.findViewById(R.id.homeMealOfTheDayCard);
        homeMealCardArea = view.findViewById(R.id.homeMealCardArea);
        swipeLayout = view.findViewById(R.id.swipeRefreshLayout);
        shimmerCarousel = view.findViewById(R.id.homeProductShimmerLayout);
        recyclerView = view.findViewById(R.id.homeProductsRecyclerView);
        cookNow = view.findViewById(R.id.homeCookNow);
        homeUserName = view.findViewById(R.id.homeUserName);
        homeUserImage = view.findViewById(R.id.homeUserImage);

        presenter = new HomePresenterImp(this, getContext());

        adapter = new HomeCarouselAdapter(getContext(), this);


        presenter.setRefreshManager();
        presenter.getUserData();
        loadAllData();

        view.findViewById(R.id.ffa).setOnClickListener((e) -> {
            presenter.showGoToRegistrationDialog();
        });

    }


    private void loadAllData() {

        presenter.startMealShimmer();
        presenter.startMealListShimmer();

        fetchRandomMeal();
        fetchCarouselMeals();
    }

    private void fetchRandomMeal() {
        presenter.fetchRandomMeal(new MealCallback() {
            @Override
            public void onSuccess(Meal meal) {
                presenter.stopMealShimmer();

                Glide.with(requireContext())
                        .load(meal.getThumbnailUrl())
                        .placeholder(R.drawable.rounded_image)
                        .centerCrop()
                        .into(homeRecipeImage);

                homeRecipeTitle.setText(meal.getName());
                String flagUrl = presenter.getCountryFlagUrl(meal.getArea());
                Glide.with(requireContext()).load(flagUrl).circleCrop().into(homeMealCardArea);

                cookNow.setOnClickListener((v) -> {
                    SelectedMeal selectedMeal = new SelectedMeal(Integer.parseInt(meal.getId()), meal.getName(), meal.getThumbnailUrl());
                    presenter.showSelectedMeal(selectedMeal);

                });
                presenter.checkIfAllLoadingFinished();

            }

            @Override
            public void onError(String error) {
                Log.e("ERROR", error);
                presenter.checkIfAllLoadingFinished();
            }
        });
    }

    private void fetchCarouselMeals() {
        presenter.fetchCarouselMeals(new MealsListCallback() {
            @Override
            public void onSuccess(List<Meal> meals) {
                if (getContext() == null) return;

                presenter.stopMealListShimmer();

                if (recyclerView.getLayoutManager() == null) {
                    CarouselLayoutManager layoutManager = new CarouselLayoutManager(new MultiBrowseCarouselStrategy());
                    recyclerView.setLayoutManager(layoutManager);

                    CarouselSnapHelper snapHelper = new CarouselSnapHelper();
                    snapHelper.attachToRecyclerView(recyclerView);

                    recyclerView.setNestedScrollingEnabled(false);
                }

                recyclerView.setAdapter(adapter);
                //set List
                presenter.setMealListAdapter(meals);

                presenter.checkIfAllLoadingFinished();
            }

            @Override
            public void onFailure(String error) {
                Log.e("Error", error);
                presenter.checkIfAllLoadingFinished();
            }
        });
    }

    @Override
    public void startMealShimmer() {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        mealCard.setVisibility(View.GONE);
    }

    @Override
    public void stopMealShimmer() {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        mealCard.setVisibility(View.VISIBLE);

    }

    @Override
    public void startMealListShimmer() {
        shimmerCarousel.setVisibility(View.VISIBLE);
        shimmerCarousel.startShimmer();
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void stopMealListShimmer() {
        shimmerCarousel.stopShimmer();
        shimmerCarousel.setVisibility(View.GONE);

        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setMealListAdapter(List<Meal> meals) {
        adapter.setMealList(meals);
    }

    @Override
    public void checkIfAllLoadingFinished() {
        RefreshManager.stopRefreshing(swipeLayout);
    }

    @Override
    public void showGoToRegistrationDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.CustomDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.goto_login_dialog_layout, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();

        view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
            dialog.dismiss();
            presenter.goToRegistration();
        });
    }


    @Override
    public void setRefreshManager() {
        RefreshManager.setup(swipeLayout, this::loadAllData);

    }

    @Override
    public void showSelectedMeal(SelectedMeal selectedMeal) {
        HomeFragmentDirections.ActionHomeFragmentToMealDetailsFragment action =
                HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(selectedMeal);
        Navigation.findNavController(getView()).navigate(action);
    }

    @Override
    public void goToRegistration() {
        NavHostFragment.findNavController(HomeFragment.this)
                .navigate(R.id.action_homeFragment_to_registrationFragment);
    }

    @Override
    public void setUserData(String displayName, Uri photoUrl) {

        homeUserName.setText(displayName);

        Glide.with(requireContext())
                .load(photoUrl)
                .placeholder(R.drawable.ic_user_guest)
                .centerCrop()
                .into(homeUserImage);
    }
}