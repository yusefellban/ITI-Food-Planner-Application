package com.example.foodplanner.Presentation.discoveryScreen.presenter;

import com.example.foodplanner.Data.MealRepository;
import com.example.foodplanner.Data.meals.datasource.remote.CategoriesResponseCallback;
import com.example.foodplanner.Data.meals.datasource.remote.IngredientResponseCallback;
import com.example.foodplanner.Data.meals.model.Country;
import com.example.foodplanner.Presentation.discoveryScreen.view.ChipSelectedType;
import com.example.foodplanner.Presentation.discoveryScreen.view.DiscoveryViewer;

import java.util.List;

public class DiscoveryPresenterImp implements DiscoveryPresenter{
    private MealRepository mealRepository;
    private DiscoveryViewer discoveryViewer;

    public DiscoveryPresenterImp(DiscoveryViewer discoveryViewer) {
        mealRepository = new MealRepository();
        this.discoveryViewer = discoveryViewer;
    }

    @Override
    public void getAllCategories(CategoriesResponseCallback categoriesResponseCallback) {
        mealRepository.getAllCategories(categoriesResponseCallback);
    }

    @Override
    public void getAllIngredients(IngredientResponseCallback ingredientResponseCallback) {
        mealRepository.getAllIngredients(ingredientResponseCallback);
    }

    @Override
    public List<Country> getAllCountries() {
        return mealRepository.getAllCountries();
    }

    @Override
    public void setList(List<ChipSelectedType> viewList) {
        discoveryViewer.setList(viewList);
    }

}
