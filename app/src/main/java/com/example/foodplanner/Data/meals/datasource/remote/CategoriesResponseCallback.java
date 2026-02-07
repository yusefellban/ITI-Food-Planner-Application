package com.example.foodplanner.Data.meals.datasource.remote;


import com.example.foodplanner.Data.meals.model.Category;

import java.util.List;

public interface CategoriesResponseCallback {
    void onSuccess(List<Category> categories);
    void onError(String error);
}
