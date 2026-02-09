package com.example.foodplanner.Data.user.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_profile")
public class UserEntity {
    @PrimaryKey
    @NonNull
    public String uid ;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "email")
    public String email;
    @ColumnInfo(name = "imagePath")
    public String imagePath;

    public UserEntity() {
    }

    @Ignore
    public UserEntity(String uid, String name, String email, String imagePath) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.imagePath = imagePath;
    }
    @Ignore

    public UserEntity(@NonNull String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
