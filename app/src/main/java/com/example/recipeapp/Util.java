package com.example.recipeapp;

import com.example.recipeapp.model.Ingredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Util {
    public boolean validateHeight(String height) {
        return !height.isEmpty();
    }
    public boolean validateWeight(String weight) {
        return !weight.isEmpty();
    }

    public boolean validName(String mealName) {
        return !mealName.isEmpty();
    }
    public boolean validCalCount(String calories) {
        return calories != null;
    }
    public boolean validateName(String name) {
        return !name.isEmpty();
    }
    public boolean validateInstructions(String instr) {
        return !instr.isEmpty();
    }
    public boolean validateCalories(String calories) {
        return !calories.isEmpty();
    }

    public boolean validateIngredients(List<String> ingredients, List<Integer> quantities) {
        return ingredients != null && quantities != null && ingredients.size() > 0
                && quantities.size() > 0 && ingredients.size() == quantities.size();
    }

    public boolean validateQuantities(ArrayList<Integer> validIngs) {
        for (Integer quantity : validIngs) {
            if (quantity <= 0) {
                return false;
            }
        }
        return true;
    }

    public boolean duplicateIngredients(ArrayList<String> ings, String newIng) {
        for (String ing : ings) {
            if (newIng.equals(ing)) {
                return true;
            }
        }
        return false;
    }

    public boolean replaceSuccessful(Ingredient i, Ingredient j) {
        if (i.getName().equals(j.getName())) {
            j.setCaloriesPerServing(i.getCaloriesPerServing());
            j.setExpirationDate(i.getExpirationDate());
        }
        return (Objects.equals(i.getCaloriesPerServing(), j.getCaloriesPerServing()));
    }

    public boolean containsNumber(String str) {
        return str.matches(".*\\d+.*");
    }

    public boolean canCook(HashMap<String, Integer> ings, HashMap<String, Integer> recipe) {
        boolean cook = false;
        for (HashMap.Entry<String, Integer> e1 : recipe.entrySet()) {
            if (ings.containsKey(e1.getKey())) {
                if (ings.get(e1.getKey()) >= e1.getValue()) {
                    cook = true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return cook;
    }

    public boolean updateDailyCalories(int dailyCals, HashMap<String, Integer> meals) {
        int cals = 0;
        for (HashMap.Entry<String, Integer> e1 : meals.entrySet()) {
            cals += e1.getValue();
        }
        return dailyCals < cals;
    }

    public boolean checkIngredientDeletion(HashMap<String, Integer> ingredients,
                                           HashMap<String, Integer> recipeStuff) {
        String k = "";

        for (HashMap.Entry<String, Integer> e : recipeStuff.entrySet()) {
            if (ingredients.get(e.getKey()) == e.getValue()) {
                k = e.getKey();
                ingredients.remove(e.getKey());
            } else {
                ingredients.put(e.getKey(), ingredients.get(e.getKey()) - e.getValue());
            }
        }
        //there shouldn't be ingredients with quantity of 0
        for (HashMap.Entry<String, Integer> e : ingredients.entrySet()) {
            if (e.getValue() == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean checkForDeduction(HashMap<String, Integer> ingredients,
                                                  HashMap<String, Integer> recipeStuff) {
        HashMap<String, Integer> newMap = new HashMap<>(ingredients);
        for (HashMap.Entry<String, Integer> e : recipeStuff.entrySet()) {
            if (ingredients.get(e.getKey()) == e.getValue()) {
                newMap.remove(e.getKey());
            } else {
                newMap.put(e.getKey(), ingredients.get(e.getKey()) - e.getValue());
            }
        }
        for (HashMap.Entry<String, Integer> e : recipeStuff.entrySet()) {
            if (ingredients.get(e.getKey()) == newMap.get(e.getKey())) {
                return false;
            }
        }
        return true;

    }

    public void addMissingIngredient(HashMap<String, Integer>
                                             ingredients, String ingredientName,
                                     int initialQuantity) {
        if (!ingredients.containsKey(ingredientName)) {
            // If not present, add it with the initial quantity
            ingredients.put(ingredientName, initialQuantity);
        }
    }
  
    public ArrayList<Boolean> isChecked(ArrayList<Ingredient> ingList) {
        ArrayList<Boolean> res = new ArrayList<Boolean>();
        for (Ingredient ingredient : ingList) {
            res.add(ingredient.getSelected());
        }
        return (res);
    }
  
    // returns quantity of newly added item. if it already exists, return old quantity
    public int checkRepeatedListItems(
            HashMap<String, Integer> currShoppingList, Ingredient newItem) {
        if (currShoppingList.containsKey(newItem.getName())) {
            for (String key : currShoppingList.keySet()) {
                if (key.equals(newItem.getName())) {
                    int oldQuantity = currShoppingList.get(key);
                    currShoppingList.replace(key, newItem.getQuantity());
                    return oldQuantity;
                }
            }
            return -5000;
        } else {
            return newItem.getQuantity();
        }
    }
}
