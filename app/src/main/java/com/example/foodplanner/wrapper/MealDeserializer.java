package com.example.foodplanner.wrapper;

import com.example.foodplanner.Entity.Meal;
import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MealDeserializer implements JsonDeserializer<Meal> {
    @Override
    public Meal deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Meal meal = new Meal();
        //read normal data
        meal.setId(getOrEmpty(jsonObject, "idMeal"));
        meal.setName(getOrEmpty(jsonObject, "strMeal"));
        meal.setInstructions(getOrEmpty(jsonObject, "strInstructions"));
        meal.setThumbnailUrl(getOrEmpty(jsonObject, "strMealThumb"));
        meal.setCategory(getOrEmpty(jsonObject, "strCategory"));
        meal.setArea(getOrEmpty(jsonObject, "strArea"));
        meal.setYoutubeUrl(getOrEmpty(jsonObject, "strYoutube"));
        meal.setTags(getOrEmpty(jsonObject, "strTags"));

        //get the list of ingredients
        List<String> ingredientsList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            String ingredient = getOrEmpty(jsonObject, "strIngredient" + i);
            String measure = getOrEmpty(jsonObject, "strMeasure" + i);
            if (!ingredient.isEmpty()) {
                ingredientsList.add(measure + " " + ingredient);
            } else {
                break;
            }
        }
        meal.setIngredients(ingredientsList);
        return meal;
    }

    //to avoid nulls
    private String getOrEmpty(JsonObject json, String key) {
        if (json.has(key) && !json.get(key).isJsonNull()) {
            return json.get(key).getAsString().trim();
        }
        return "";
    }

}