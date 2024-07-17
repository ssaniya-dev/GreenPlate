package com.example.recipeapp;

import com.example.recipeapp.model.Recipe;

import java.util.List;

public class NoFilter implements FilterStrategy {
    @Override
    public List<Recipe> filter(List<Recipe> recipes) {
        return recipes;
    }
}
