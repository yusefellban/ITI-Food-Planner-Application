package com.example.foodplanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.foodplanner.Entity.Meal;
import com.example.foodplanner.adapter.HomeCarouselAdapter;
import com.example.foodplanner.adapter.RefreshManager;
import com.example.foodplanner.service.CountryCodeService;
import com.example.foodplanner.service.GetMealRandom;
import com.example.foodplanner.service.GetRandomMealList;
import com.example.foodplanner.service.MealCallback;
import com.example.foodplanner.service.OnMealsLoadedListener;
import com.example.foodplanner.wrapper.MealResponse;
import com.example.foodplanner.wrapper.SelectedMeal;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.MultiBrowseCarouselStrategy;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        RefreshManager.setup(swipeLayout, this::loadAllData);


        loadAllData();
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


        GetMealRandom.getMeal(new MealCallback() {
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

                String code = CountryCodeService.getCountryCode(meal.getArea());
                if (code != null) {
                    homeMealCardArea.setVisibility(View.VISIBLE);
                    String flagUrl = "https://flagcdn.com/w160/" + code.toLowerCase() + ".png";
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
        GetRandomMealList.getMealsList(new OnMealsLoadedListener() {
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
}