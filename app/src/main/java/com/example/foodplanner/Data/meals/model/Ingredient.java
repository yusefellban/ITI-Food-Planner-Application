package com.example.foodplanner.Data.meals.model;

import com.example.foodplanner.Presentation.discoveryScreen.ChipSelectedType;
import com.google.gson.annotations.SerializedName;

public class Ingredient implements ChipSelectedType {

    @SerializedName("idIngredient")
    private String id;
    @SerializedName("strIngredient")
    private String name;
    @SerializedName("strThumb")
    private String imageUrl;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int getViewType() {
        return 3;
    }
}
