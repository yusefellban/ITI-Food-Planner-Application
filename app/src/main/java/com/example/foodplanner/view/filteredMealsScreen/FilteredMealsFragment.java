package com.example.foodplanner.view.filteredMealsScreen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.R;
import com.example.foodplanner.datasource.remote.MealRemoteDataSource;
import com.example.foodplanner.model.Meal;
import com.example.foodplanner.datasource.remote.MealsListCallback;
import com.example.foodplanner.model.wrapper.SendSelectedItem;

import java.util.List;


public class FilteredMealsFragment extends Fragment {
 private RecyclerView filteredMealsRecyclerView;
 private MealRemoteDataSource mealRemoteDataSource;

    public FilteredMealsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filtered_meals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filteredMealsRecyclerView=view.findViewById(R.id.filteredMealsRecyclerView);
        filteredMealsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mealRemoteDataSource=new MealRemoteDataSource();
        //get selected item
        SendSelectedItem selectedItem = FilteredMealsFragmentArgs.fromBundle(getArguments()).getSendSelectedItem();



        mealRemoteDataSource.getAllFilteredMeals(selectedItem, new MealsListCallback() {
            @Override
            public void onSuccess(List<Meal> meals) {
                FilteredMealsAdapter adapter=new FilteredMealsAdapter(view.getContext(),meals);
                filteredMealsRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(String error) {
                Log.d("TAG", error);
            }
        });


    }
}