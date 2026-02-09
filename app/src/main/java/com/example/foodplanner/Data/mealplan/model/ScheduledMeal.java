package com.example.foodplanner.Data.mealplan.model;

public class ScheduledMeal {
    private String mealId;
    private String mealName;
    private String mealImageUrl;
    private String category;
    private String scheduledDate; // Format: yyyy-MM-dd

    public ScheduledMeal() {
    }

    public ScheduledMeal(String mealId, String mealName, String mealImageUrl, String category, String scheduledDate) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.mealImageUrl = mealImageUrl;
        this.category = category;
        this.scheduledDate = scheduledDate;
    }

    // Getters and Setters
    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealImageUrl() {
        return mealImageUrl;
    }

    public void setMealImageUrl(String mealImageUrl) {
        this.mealImageUrl = mealImageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
}
