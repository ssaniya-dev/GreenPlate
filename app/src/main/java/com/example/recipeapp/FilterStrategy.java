package com.example.recipeapp;

import com.example.recipeapp.model.Recipe;

import java.util.List;

public interface FilterStrategy {
    List<Recipe> filter(List<Recipe> recipes);
}
