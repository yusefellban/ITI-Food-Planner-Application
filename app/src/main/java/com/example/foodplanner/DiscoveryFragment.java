package com.example.foodplanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.model.Category;
import com.example.foodplanner.model.Country;
import com.example.foodplanner.model.Ingredient;
import com.example.foodplanner.adapter.ChipSelectedType;
import com.example.foodplanner.adapter.DiscoveryAdapter;
import com.example.foodplanner.service.CategoriesGetResponse;
import com.example.foodplanner.datasource.local.CountryCodeLocalDataSource;
import com.example.foodplanner.service.GetApiService;
import com.example.foodplanner.service.IngredientGetResponse;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.ArrayList;
import java.util.List;


public class DiscoveryFragment extends Fragment {

    private ChipGroup chipGroup;

    /// to cashing the list
    private List<ChipSelectedType> categoryList;
    private List<ChipSelectedType> ingredientList;
    private List<ChipSelectedType> countryList;

    private DiscoveryAdapter discoveryAdapter;
    private RecyclerView recyclerView;
    private Chip chipIngredient;
    private Chip chipCategory;
    private Chip chipCountry;


    public DiscoveryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discovery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Java
        SearchBar searchBar = view.findViewById(R.id.search_bar);
        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setupWithSearchBar(searchBar);
        chipGroup = view.findViewById(R.id.chipGroup);
        recyclerView = view.findViewById(R.id.discoveryRecyclerView);
        chipIngredient = view.findViewById(R.id.chipIngredient);
        chipCategory = view.findViewById(R.id.chipCategory);
        chipCountry = view.findViewById(R.id.chipCountry);


        discoveryAdapter = new DiscoveryAdapter();
        recyclerView.setAdapter(discoveryAdapter);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;

            int id = checkedIds.get(0);
            if (id == R.id.chipIngredient) {
                if (ingredientList == null || ingredientList.isEmpty()) {
                    setIngredientsList();
                } else {
                    discoveryAdapter.setList(ingredientList);
                }
            } else if (id == R.id.chipCountry) {
                if (countryList == null || countryList.isEmpty()) {
                    setCountryLit();
                } else {
                    discoveryAdapter.setList(countryList);
                }

            } else if(id == R.id.chipCategory){
                if (categoryList == null || categoryList.isEmpty()) {
                    setCategoriesLit();
                } else {
                    discoveryAdapter.setList(categoryList);
                }
            }


        });


        setCategoriesLit();
    }


    public void setCategoriesLit() {
        GetApiService.getAllCategories(new CategoriesGetResponse() {
            @Override
            public void onSuccess(List<Category> categories) {
                categoryList = (List<ChipSelectedType>) (List<?>) categories;// Ugly Hack
                discoveryAdapter.setList(categoryList);
            }

            @Override
            public void onError(String error) {
                Log.e("DiscoveryFragment", error);
            }
        });
    }

    public void setIngredientsList() {

        GetApiService.getAllIngredients(new IngredientGetResponse() {
            @Override
            public void onSuccess(List<Ingredient> ingredients) {
                ingredientList = (List<ChipSelectedType>) (List<?>) ingredients;// Ugly Hack
                discoveryAdapter.setList(ingredientList);
            }

            @Override
            public void onError(String error) {
                Log.e("DiscoveryFragment", error);
            }
        });

    }

    public void setCountryLit() {
        List<Country> areas = CountryCodeLocalDataSource.getAllCountries();
        if (areas != null) {
            countryList = (List<ChipSelectedType>) (List<?>) areas;
            discoveryAdapter.setList(countryList);
        } else {
            countryList = new ArrayList<>();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        chipCountry.setChecked(false);
        chipIngredient.setChecked(false);
        chipCategory.setChecked(true);
    }
}