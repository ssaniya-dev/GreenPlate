package com.example.recipeapp.model;

import java.util.ArrayList;

public class Pantry {
    private ArrayList<Ingredient> pantryItems = new ArrayList<>();

    public Pantry(ArrayList<Ingredient> pantryItems) {
        this.pantryItems = pantryItems;
    }

    public ArrayList<Ingredient> getPantryItems() {
        return pantryItems;
    }

    public void setPantryItems(ArrayList<Ingredient> pantryItems) {
        this.pantryItems = pantryItems;
    }
}
