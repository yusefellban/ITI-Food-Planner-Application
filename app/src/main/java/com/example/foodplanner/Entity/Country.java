package com.example.foodplanner.Entity;

import com.example.foodplanner.adapter.ChipSelectedType;
import com.example.foodplanner.service.CountryCodeService;

public class Country implements ChipSelectedType {
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

        String code = CountryCodeService.getCountryCode(this.name);
        if (code == null) return null;

        return "https://flagcdn.com/w160/" + code.toLowerCase() + ".png";
    }
}
