package com.example.foodplanner.Presentation.homeScreen;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
import com.example.foodplanner.R;
import com.example.foodplanner.Data.meals.datasource.remote.MealRemoteDataSource;
import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.Data.meals.datasource.local.CountryCodeLocalDataSource;
import com.example.foodplanner.Data.meals.datasource.remote.MealCallback;
import com.example.foodplanner.Data.meals.datasource.remote.MealsListCallback;
import com.example.foodplanner.Data.meals.model.wrapper.SelectedMeal;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.MultiBrowseCarouselStrategy;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class HomeFragment extends Fragment {

    private ImageView homeRecipeImage;
    private ImageView homeMealCardArea;
    private TextView homeRecipeTitle;
    private TextView homeTag1;
    private TextView homeTag2;

    private ShimmerFrameLayout shimmerFrameLayout;
    private ShimmerFrameLayout shimmerCarousel;
    private View mealCard;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView recyclerView;
    private Button cookNow;

    /// datasource
    private MealRemoteDataSource remoteDataSource;

    public HomeFragment() { }

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
        cookNow=view.findViewById(R.id.homeCookNow);

        remoteDataSource=new MealRemoteDataSource();




        RefreshManager.setup(swipeLayout, this::loadAllData);


        loadAllData();

        view.findViewById(R.id.ffa).setOnClickListener((e)->{
            showCustomDialog();
        });

    }


    private void loadAllData() {

        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        mealCard.setVisibility(View.GONE);

        shimmerCarousel.setVisibility(View.VISIBLE);
        shimmerCarousel.startShimmer();
        recyclerView.setVisibility(View.GONE);

        // نداء الـ APIs
        fetchRandomMeal();
        fetchCarouselMeals();
    }

    private void fetchRandomMeal() {


        remoteDataSource.getRandomMeal(new MealCallback() {
            @Override
            public void onSuccess(Meal meal) {
//                Log.d("MEAL", meal.toString());
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                mealCard.setVisibility(View.VISIBLE);


                Glide.with(requireContext())
                        .load(meal.getThumbnailUrl())
                        .placeholder(R.drawable.rounded_image)
                        .centerCrop()
                        .into(homeRecipeImage);

                homeRecipeTitle.setText(meal.getName());

                String flagUrl = CountryCodeLocalDataSource.getImageUrl(meal.getArea());
                if (flagUrl != null) {
                    homeMealCardArea.setVisibility(View.VISIBLE);
                    Glide.with(requireContext()).load(flagUrl).circleCrop().into(homeMealCardArea);
                } else {
                    homeMealCardArea.setVisibility(View.GONE);
                }

                if (meal.getTags() != null) {
                    List<String> list = meal.getTagsAsList();
                    if(list.isEmpty()) homeTag1.setText(meal.getTags());
                    else if(list.size() >= 2) {
                        homeTag1.setText(list.get(0));
                        homeTag2.setText(list.get(1));
                    } else homeTag1.setText(list.get(0));
                }

                cookNow.setOnClickListener((v)->{

                    SelectedMeal selectedMeal=new SelectedMeal(Integer.parseInt(meal.getId()),meal.getName(),meal.getThumbnailUrl());

                    HomeFragmentDirections.ActionHomeFragmentToMealDetailsFragment action=
                            HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(selectedMeal);
                    Navigation.findNavController(v).navigate(action);

                });
                checkIfAllLoadingFinished();
            }

            @Override
            public void onError(String error) {
                Log.e("ERROR", error);
                checkIfAllLoadingFinished();
            }
        });

    }

    private void fetchCarouselMeals() {
        remoteDataSource.getRandomMealsList(new MealsListCallback() {
            @Override
            public void onSuccess(List<Meal> meals) {
                if (getContext() == null) return;

                shimmerCarousel.stopShimmer();
                shimmerCarousel.setVisibility(View.GONE);

                recyclerView.setVisibility(View.VISIBLE);


                if (recyclerView.getLayoutManager() == null) {
                    CarouselLayoutManager layoutManager = new CarouselLayoutManager(new MultiBrowseCarouselStrategy());
                    recyclerView.setLayoutManager(layoutManager);

                    CarouselSnapHelper snapHelper = new CarouselSnapHelper();
                    snapHelper.attachToRecyclerView(recyclerView);

                    recyclerView.setNestedScrollingEnabled(false);
                }

                HomeCarouselAdapter adapter = new HomeCarouselAdapter(getContext(), meals);
                recyclerView.setAdapter(adapter);

                checkIfAllLoadingFinished();
            }

            @Override
            public void onFailure(String error) {
                Log.e("Error", error);
                checkIfAllLoadingFinished();
            }
        });
    }

    private void checkIfAllLoadingFinished() {
        RefreshManager.stopRefreshing(swipeLayout);
    }


    public void showCustomDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this.getContext(), R.style.CustomDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.goto_login_dialog_layout, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();

        view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
            dialog.dismiss();
        });
    }
}