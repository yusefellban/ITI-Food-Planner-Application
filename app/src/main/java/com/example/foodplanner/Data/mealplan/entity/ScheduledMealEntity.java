package com.example.foodplanner.Data.mealplan.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "scheduled_meals")
public class ScheduledMealEntity {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String mealId;
    private String mealName;
    private String mealImageUrl;
    private String category;
    private String scheduledDate; // Format: yyyy-MM-dd
    private String userId; // Firebase UID
    private long timestamp;

    public ScheduledMealEntity() {
    }

    public ScheduledMealEntity(String mealId, String mealName, String mealImageUrl, 
                               String category, String scheduledDate, String userId, long timestamp) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.mealImageUrl = mealImageUrl;
        this.category = category;
        this.scheduledDate = scheduledDate;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
