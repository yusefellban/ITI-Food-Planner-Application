package com.example.foodplanner.service;


import com.example.foodplanner.Entity.Category;

import java.util.List;

public interface CategoriesGetResponse {
    void onSuccess(List<Category> categories);
    void onError(String error);
}
