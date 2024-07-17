package com.example.recipeapp.model;

public class Ingredient {
    private String name;
    private Integer quantity;
    private Integer caloriesPerServing;
    private String expirationDate;
    private Boolean selected;

    public Ingredient() {

    }
    public Ingredient(String name, Integer quantity,
                      Integer caloriesPerServing, String expirationDate,
                      Boolean selected) {
        this.name = name;
        this.quantity = quantity;
        this.caloriesPerServing = caloriesPerServing;
        if (expirationDate != null) {
            this.expirationDate = expirationDate;
        } else {
            this.expirationDate = "N/A";
        }
        this.selected = selected;
    }
    public Ingredient(String name, Integer quantity,
                      Integer caloriesPerServing, String expirationDate) {
        this.name = name;
        this.quantity = quantity;
        this.caloriesPerServing = caloriesPerServing;
        if (expirationDate != null) {
            this.expirationDate = expirationDate;
        } else {
            this.expirationDate = "N/A";
        }
        this.selected = false;
    }
    public Ingredient(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }
    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer newQty) {
        this.quantity = newQty;
    }
    public void setCaloriesPerServing(Integer cal) {
        this.caloriesPerServing = cal;
    }
    public void setExpirationDate(String exp) {
        this.expirationDate = exp;
    }
    public void toggleSelected() {
        this.selected = !this.selected;
    }
    public Integer getCaloriesPerServing() {
        return caloriesPerServing;
    }

    public String getExpirationDate() {
        return expirationDate;
    }
    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean currSelect) {
        selected = currSelect;
    }

    public String toString() {
        return "Ingredient Name: " + this.getName() + ", Quantity: "
                + this.getQuantity() + ", Calories: "
                + this.getCaloriesPerServing() + ", expirationDate: "
                + this.getExpirationDate() + ", selected: "
                + this.getSelected();
    }
}
