package com.example.foodplanner.Presentation.discoveryScreen.presenter;

import com.example.foodplanner.Data.Repository;
import com.example.foodplanner.Data.meals.datasource.remote.CategoriesResponseCallback;
import com.example.foodplanner.Data.meals.datasource.remote.IngredientResponseCallback;
import com.example.foodplanner.Data.meals.model.Country;
import com.example.foodplanner.Presentation.discoveryScreen.view.ChipSelectedType;
import com.example.foodplanner.Presentation.discoveryScreen.view.DiscoveryViewer;

import java.util.List;

public class DiscoveryPresenterImp implements DiscoveryPresenter{
    private Repository repository;
    private DiscoveryViewer discoveryViewer;

    public DiscoveryPresenterImp(DiscoveryViewer discoveryViewer) {
        repository = new Repository();
        this.discoveryViewer = discoveryViewer;
    }

    @Override
    public void getAllCategories(CategoriesResponseCallback categoriesResponseCallback) {
        repository.getAllCategories(categoriesResponseCallback);
    }

    @Override
    public void getAllIngredients(IngredientResponseCallback ingredientResponseCallback) {
        repository.getAllIngredients(ingredientResponseCallback);
    }

    @Override
    public List<Country> getAllCountries() {
        return repository.getAllCountries();
    }

    @Override
    public void setList(List<ChipSelectedType> viewList) {
        discoveryViewer.setList(viewList);
    }

}
