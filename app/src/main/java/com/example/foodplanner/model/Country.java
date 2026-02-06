package com.example.foodplanner.model;

import com.example.foodplanner.view.discoveryScreen.ChipSelectedType;
import com.example.foodplanner.datasource.local.CountryCodeLocalDataSource;

public class Country implements ChipSelectedType {
    private String id;
   private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getViewType() {
        return 2;
    }
    public String getImageUrl() {
        if (this.name == null) return null;

        String code = CountryCodeLocalDataSource.getCountryCode(this.name);
        if (code == null) return null;

        return "https://flagcdn.com/w160/" + code.toLowerCase() + ".png";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
