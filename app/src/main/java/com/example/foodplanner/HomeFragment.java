package com.example.foodplanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Entity.Meal;
import com.example.foodplanner.adapter.HomeCarouselAdapter;
import com.example.foodplanner.remote.RetrofitClient;
import com.example.foodplanner.service.CountryCodeService;
import com.example.foodplanner.service.GetRandomMealList;
import com.example.foodplanner.service.OnMealsLoadedListener;
import com.example.foodplanner.wrapper.MealResponse;
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
    private View mealCard;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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


        //start animation
        shimmerFrameLayout.startShimmer();


        RetrofitClient.getApiService().getRandomMeal().enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    mealCard.setVisibility(View.VISIBLE);

                    Meal myMeal = response.body().getMeals().get(0);
                    Log.d("MealDetails", "Ingredient: " + myMeal.toString());

                    Glide.with(view)
                            .load(myMeal.getThumbnailUrl())
                            .placeholder(R.drawable.rounded_image)
                            .centerCrop()
                            .into(homeRecipeImage);
                    homeRecipeTitle.setText(myMeal.getName());

                    String code = CountryCodeService.getCountryCode(myMeal.getArea());

                    if (code != null) {
                        String flagUrl = "https://flagcdn.com/w160/" + code.toLowerCase() + ".png";
                        Glide.with(view)
                                .load(flagUrl)
                                .circleCrop()
                                .into(homeMealCardArea);
                    } else {
                        homeMealCardArea.setVisibility(View.GONE);
                    }

                    if (myMeal.getTags() != null) {
                        List<String> list=myMeal.getTagsAsList();
                        if(list.isEmpty()){
                            homeTag1.setText(myMeal.getTags());
                        }else if(list.size()>=2){
                        homeTag1.setText(list.get(0));
                            homeTag2.setText(list.get(1));
                        }
                        else{
                            homeTag1.setText(list.get(0));

                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                // Handle error//shimmerFrameLayout.stopShimmer();
              // shimmerFrameLayout.setVisibility(View.GONE);
                Log.e("Error", t.getMessage());
            }
        });

        ShimmerFrameLayout shimmerCarousel = view.findViewById(R.id.homeProductShimmerLayout);
        shimmerCarousel.startShimmer();

        GetRandomMealList.getMealsList(new OnMealsLoadedListener() {
            @Override
            public void onSuccess(List<Meal> meals) {
                if (getContext() == null) return;

                RecyclerView recyclerView = view.findViewById(R.id.homeProductsRecyclerView);

                shimmerCarousel.stopShimmer();
                shimmerCarousel.setVisibility(View.GONE);

                recyclerView.setAlpha(0f);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.animate().alpha(1f).setDuration(500);

                recyclerView.setHasFixedSize(true);
                recyclerView.setItemViewCacheSize(20);


                if (recyclerView.getLayoutManager() == null) {
                    CarouselLayoutManager layoutManager = new CarouselLayoutManager(new MultiBrowseCarouselStrategy());
                    recyclerView.setLayoutManager(layoutManager);

                 /// only once to not throws any exceptions
                    CarouselSnapHelper snapHelper = new CarouselSnapHelper();
                    snapHelper.attachToRecyclerView(recyclerView);
                }

                HomeCarouselAdapter adapter = new HomeCarouselAdapter(getContext(), meals);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(String error) {
                shimmerCarousel.stopShimmer();
                Log.e("Error", error);
            }
        });

    }
}