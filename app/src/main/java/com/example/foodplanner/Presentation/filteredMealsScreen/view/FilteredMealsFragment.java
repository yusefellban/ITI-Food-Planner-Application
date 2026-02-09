package com.example.foodplanner.Presentation.filteredMealsScreen.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.Data.meals.model.wrapper.SelectedMeal;
import com.example.foodplanner.Presentation.filteredMealsScreen.presenter.FilteredMealsPresenterImp;
import com.example.foodplanner.R;
import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.Data.meals.datasource.remote.MealsListCallback;
import com.example.foodplanner.Data.meals.model.wrapper.SendSelectedItem;

import java.util.List;


public class FilteredMealsFragment extends Fragment implements onFilteredItemClickListener{
 private RecyclerView filteredMealsRecyclerView;
    FilteredMealsAdapter adapter;
 private FilteredMealsPresenterImp presenter;


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

        presenter=new FilteredMealsPresenterImp(getContext());
        //get selected item
        SendSelectedItem selectedItem = FilteredMealsFragmentArgs.fromBundle(getArguments()).getSendSelectedItem();

        adapter=new FilteredMealsAdapter(view.getContext(),this);


        presenter.getAllFilteredMeals(selectedItem, new MealsListCallback() {
            @Override
            public void onSuccess(List<Meal> meals) {
                adapter.setMealList(meals);
                filteredMealsRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(String error) {
                Log.d("TAG", error);
            }
        });


    }

    @Override
    public void showSelectedMeal(SelectedMeal selectedMeal) {
        FilteredMealsFragmentDirections.ActionFilteredMealsFragmentToMealDetailsFragment action=
                FilteredMealsFragmentDirections.actionFilteredMealsFragmentToMealDetailsFragment(selectedMeal);

        Navigation.findNavController(getView()).navigate(action);
    }
}