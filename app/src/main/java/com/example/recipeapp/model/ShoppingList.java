package com.example.recipeapp.model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingList {
    private List<Ingredient> shoppingList = new ArrayList<>();

    public ShoppingList(List<Ingredient> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public List<Ingredient> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ArrayList<Ingredient> shoppingList) {
        this.shoppingList = shoppingList;
    }
}
