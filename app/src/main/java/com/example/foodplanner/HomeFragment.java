package com.example.foodplanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Entity.Meal;
import com.example.foodplanner.remote.RetrofitClient;
import com.example.foodplanner.wrapper.MealResponse;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private ImageView homeRecipeImage;
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
        shimmerFrameLayout = view.findViewById(R.id.shimmerLayout);
        mealCard = view.findViewById(R.id.homeMealOfTheDayCard);
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
                // Handle error
//                shimmerFrameLayout.stopShimmer();
//                shimmerFrameLayout.setVisibility(View.GONE);
                Log.e("Error", t.getMessage());
            }
        });
    }
}