package com.example.recipeapp;

import com.example.recipeapp.model.Recipe;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class IngredientCountFilter implements FilterStrategy {
    @Override
    public List<Recipe> filter(List<Recipe> recipes) {
        return recipes.stream()
                .sorted(Comparator.comparingInt(Recipe::getIngredientCount))
                .collect(Collectors.toList());
    }
}
