package com.example.recipeapp.model;

public class Meal {

    private String name;
    private String calories;

    private String date;

    public Meal(String name, String calories, String date) {
        this.name = name;
        this.calories = calories;
        this.date = date;
    }
    public String getMealName() {
        return name;
    }
    public String getCalories() {
        return calories;
    }

    public String getDate() {
        return date;
    }
}
