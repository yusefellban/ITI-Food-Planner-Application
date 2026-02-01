package com.example.foodplanner.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Meal {
    @SerializedName("idMeal")
    private String id;
    @SerializedName("strMeal")
    private String name;
    @SerializedName("strCategory")
    private String category;
    @SerializedName("strArea")
    private String area;
    @SerializedName("strInstructions")

    private String instructions;
    @SerializedName("strMealThumb")

    private String thumbnailUrl;
    @SerializedName("strYoutube")

    private String youtubeUrl;
    @SerializedName("strTags")

    private String tags;

 /// Deserializable will provide this list
    private HashMap<String,String> ingredients;

    public Meal() {
    }


// Getters and Setters

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public HashMap<String, String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(HashMap<String, String> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", area='" + area + '\'' +
                ", instructions='" + instructions + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", youtubeUrl='" + youtubeUrl + '\'' +
                ", tags='" + tags + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }

    public List<String> getTagsAsList() {
        if (tags == null || tags.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String[] splitTags = tags.split(",");

        List<String> tagList = new ArrayList<>();
        for (String tag : splitTags) {
            tagList.add(tag.trim());
        }

        return tagList;
    }

}
