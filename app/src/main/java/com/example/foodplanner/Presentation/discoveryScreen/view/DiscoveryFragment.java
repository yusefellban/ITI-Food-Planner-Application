package com.example.foodplanner.Presentation.discoveryScreen.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.Data.meals.model.wrapper.SendSelectedItem;
import com.example.foodplanner.Presentation.discoveryScreen.presenter.DiscoveryPresenterImp;
import com.example.foodplanner.R;
import com.example.foodplanner.Data.meals.model.Category;
import com.example.foodplanner.Data.meals.model.Country;
import com.example.foodplanner.Data.meals.model.Ingredient;
import com.example.foodplanner.Data.meals.datasource.remote.CategoriesResponseCallback;
import com.example.foodplanner.Data.meals.datasource.remote.IngredientResponseCallback;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.ArrayList;
import java.util.List;

public class DiscoveryFragment extends Fragment implements onClickDiscovery, DiscoveryViewer {

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

    DiscoveryPresenterImp presenter;
    SearchView searchView;

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
        searchView = view.findViewById(R.id.search_view);
        searchView.setupWithSearchBar(searchBar);
        chipGroup = view.findViewById(R.id.chipGroup);
        recyclerView = view.findViewById(R.id.discoveryRecyclerView);
        chipIngredient = view.findViewById(R.id.chipIngredient);
        chipCategory = view.findViewById(R.id.chipCategory);
        chipCountry = view.findViewById(R.id.chipCountry);

        presenter = new DiscoveryPresenterImp(this, getContext());

        discoveryAdapter = new DiscoveryAdapter(this);
        recyclerView.setAdapter(discoveryAdapter);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty())
                return;

            int id = checkedIds.get(0);
            if (id == R.id.chipIngredient) {
                if (ingredientList == null || ingredientList.isEmpty()) {
                    setIngredientsList();
                } else {
                    presenter.setList(ingredientList);
                }
            } else if (id == R.id.chipCountry) {
                if (countryList == null || countryList.isEmpty()) {
                    setCountryLit();
                } else {
                    presenter.setList(countryList);
                }

            } else if (id == R.id.chipCategory) {
                if (categoryList == null || categoryList.isEmpty()) {
                    setCategoriesLit();
                } else {
                    presenter.setList(categoryList);
                }
            }

        });

        setCategoriesLit();
        setupSearch();
    }

    private void setupSearch() {
        com.example.foodplanner.Data.MealRepository repository = new com.example.foodplanner.Data.MealRepository(
                getContext());
        SearchMealAdapter searchAdapter = new SearchMealAdapter(getContext(), meal -> {
            // Handle click
            com.example.foodplanner.Data.meals.model.wrapper.SelectedMeal selectedMeal = new com.example.foodplanner.Data.meals.model.wrapper.SelectedMeal(
                    Integer.parseInt(meal.getId()), meal.getName(), meal.getThumbnailUrl());

            DiscoveryFragmentDirections.ActionDiscoveryFragmentToMealDetailsFragment action = DiscoveryFragmentDirections
                    .actionDiscoveryFragmentToMealDetailsFragment(selectedMeal);
            Navigation.findNavController(getView()).navigate(action);
        });

        RecyclerView searchRecyclerView = getView().findViewById(R.id.search_recycler_view);
        searchRecyclerView.setAdapter(searchAdapter);

        io.reactivex.rxjava3.subjects.PublishSubject<String> subject = io.reactivex.rxjava3.subjects.PublishSubject
                .create();

        searchView.getEditText().addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                subject.onNext(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
            }
        });

        io.reactivex.rxjava3.disposables.Disposable disposable = subject
                .debounce(500, java.util.concurrent.TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMapSingle(query -> {
                    if (query.isEmpty()) {
                        return io.reactivex.rxjava3.core.Single
                                .just(new java.util.ArrayList<com.example.foodplanner.Data.meals.model.Meal>());
                    }
                    return repository.searchMeal(query)
                            .onErrorReturnItem(new java.util.ArrayList<>());
                })
                .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                .observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(searchAdapter::setList, throwable -> Log.e("DiscoveryFragment", "Search error", throwable));
    }

    public void setCategoriesLit() {
        presenter.getAllCategories(new CategoriesResponseCallback() {
            @Override
            public void onSuccess(List<Category> categories) {
                categoryList = (List<ChipSelectedType>) (List<?>) categories;// Ugly Hack
                presenter.setList(categoryList);
            }

            @Override
            public void onError(String error) {
                Log.e("Discovery-Fragment", error);
            }
        });
    }

    public void setIngredientsList() {

        presenter.getAllIngredients(new IngredientResponseCallback() {
            @Override
            public void onSuccess(List<Ingredient> ingredients) {
                ingredientList = (List<ChipSelectedType>) (List<?>) ingredients;// Ugly Hack
                presenter.setList(ingredientList);
            }

            @Override
            public void onError(String error) {
                Log.e("DiscoveryFragment", error);
            }
        });

    }

    public void setCountryLit() {
        List<Country> areas = presenter.getAllCountries();
        if (areas != null) {
            countryList = (List<ChipSelectedType>) (List<?>) areas;
            presenter.setList(countryList);
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

    @Override
    public void goToFilteredScreen(SendSelectedItem selectedMeal) {
        DiscoveryFragmentDirections.ActionDiscoveryFragmentToFilteredMealsFragment action = DiscoveryFragmentDirections
                .actionDiscoveryFragmentToFilteredMealsFragment(selectedMeal);
        Navigation.findNavController(getView()).navigate(action);
    }

    @Override
    public void setList(List<ChipSelectedType> list) {
        discoveryAdapter.setList(list);
    }
}