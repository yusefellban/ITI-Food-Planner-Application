package com.example.foodplanner.Data.favorites.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_meals")
public class FavoriteMealEntity {
    
    @PrimaryKey
    private int mealId;
    
    private String name;
    private String thumbnailUrl;
    private String category;
    private String area;
    private long timestamp;

    public FavoriteMealEntity() {
    }

    public FavoriteMealEntity(int mealId, String name, String thumbnailUrl, String category, String area, long timestamp) {
        this.mealId = mealId;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.category = category;
        this.area = area;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
