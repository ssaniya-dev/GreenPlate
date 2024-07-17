package com.example.recipeapp.model;

import java.util.HashMap;
import java.util.Map;

public class Cookbook {
    public boolean sufficientIngredients(HashMap<String, Integer> pantry, Recipe recipe) {
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            String ing = recipe.getIngredients().get(i);
            if (!(pantry.containsKey(ing))) {
                return (false);
            } else {
                int qty = pantry.get(recipe.getIngredients().get(i));
                if (qty < recipe.getQuantities().get(i)) {
                    return (false);
                }
            }
        }
        return (true);
    }

    public Map<String, Integer> calculateMissingQuantities(Recipe recipe,
                                                           Map<String, Integer> pantry) {
        Map<String, Integer> missingQuantities = new HashMap<>();

        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            String ingredient = recipe.getIngredients().get(i);
            int recipeQuantity = recipe.getQuantities().get(i);

            int pantryQuantity = pantry.getOrDefault(ingredient, 0);
            int missingQuantity = recipeQuantity - pantryQuantity;

            if (missingQuantity > 0) {
                missingQuantities.put(ingredient, missingQuantity);
            }
        }

        return missingQuantities;
    }
}
