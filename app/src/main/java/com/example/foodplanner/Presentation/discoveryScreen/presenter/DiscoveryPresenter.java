package com.example.foodplanner.Presentation.discoveryScreen.presenter;

import com.example.foodplanner.Data.meals.datasource.remote.CategoriesResponseCallback;
import com.example.foodplanner.Data.meals.datasource.remote.IngredientResponseCallback;
import com.example.foodplanner.Data.meals.model.Country;
import com.example.foodplanner.Presentation.discoveryScreen.view.ChipSelectedType;

import java.util.List;

public interface DiscoveryPresenter {
     void getAllCategories(CategoriesResponseCallback categoriesResponseCallback) ;
     void getAllIngredients(IngredientResponseCallback ingredientResponseCallback) ;
     List<Country> getAllCountries() ;
     void setList(List<ChipSelectedType> viewList) ;
}
