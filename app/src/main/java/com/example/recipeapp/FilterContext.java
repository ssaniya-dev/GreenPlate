package com.example.recipeapp;

import com.example.recipeapp.model.Recipe;

import java.util.List;

public class FilterContext {
    private FilterStrategy strategy;
    public FilterContext(FilterStrategy strategy) {
        this.strategy = strategy;
    }
    public void setStrategy(FilterStrategy strategy) {
        this.strategy = strategy;
    }
    public List<Recipe> filterRecipes(List<Recipe> recipes) {
        return strategy.filter(recipes);
    }
}
