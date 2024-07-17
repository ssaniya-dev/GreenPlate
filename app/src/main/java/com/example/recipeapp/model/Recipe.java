package com.example.recipeapp.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {
    private String name;
    private int calories;
    private String instructions;
    private List<String> ingredients;
    private List<Integer> quantities;

    public Recipe(String name, int calories, String instructions,
                  List<String> ingredients, List<Integer> quantities) {
        this.name = name;
        this.calories = calories;
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.quantities = quantities;
    }

    @NonNull
    public String toString() {
        return "Recipe: " + this.name + " Calories: "
                + this.calories + " Instructions: " + instructions;
    }
    public int getCalories() {
        return this.calories;
    }
    public int getIngredientCount() {
        return this.ingredients.size();
    }
    public String getName() {
        return this.name;
    }
    public String getInstructions() {
        return instructions;
    }
    public List<String> getIngredients() {
        return ingredients;
    }
    public List<Integer> getQuantities() {
        return quantities;
    }
}
