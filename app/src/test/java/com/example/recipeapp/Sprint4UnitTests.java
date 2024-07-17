package com.example.recipeapp;


import static org.junit.Assert.assertEquals;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.recipeapp.model.Cookbook;
import com.example.recipeapp.model.Ingredient;
import com.example.recipeapp.model.Recipe;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Sprint4UnitTests {
    // Saniya Savla
    @Test
    public void replaceCalAndExpSuccess() {
        Util u = new Util();
        Ingredient i = new Ingredient("test", 32, 21, "6/25/2004");
        Ingredient j = new Ingredient("test", 32, 20, "");
        assertTrue(u.replaceSuccessful(i, j));
    }
    @Test
    public void replaceCalAndExpFailure() {
        Util u = new Util();
        Ingredient i = new Ingredient("test", 32, 21, "6/25/2004");
        Ingredient j = new Ingredient("nothing", 32, 20, "");
        assertFalse(u.replaceSuccessful(i, j));
    }

    //Amritha Pramod
    @Test
    public void checkCook() {
        Util u = new Util();
        HashMap<String, Integer> ings = new HashMap<>();
        HashMap<String, Integer> recipe = new HashMap<>();

        ings.put("carrots", 80);
        recipe.put("carrots", 50);
        assertTrue(u.canCook(ings, recipe));

        ings.put("lettuce", 67);
        ings.put("cabbage", 90);
        recipe.put("lettuce", 67);
        recipe.put("radish", 20);
        assertFalse(u.canCook(ings, recipe));
    }

    //Amritha Pramod
    @Test
    public void checkDailyCals() {
        Util u = new Util();
        HashMap<String, Integer> meals = new HashMap<>();

        meals.put("cake", 902);
        meals.put("pie", 503);
        meals.put("noodles", 670);

        assertFalse(u.updateDailyCalories(2075, meals));
        assertTrue(u.updateDailyCalories(1405, meals));
    }

    //Simona Ivanov
    @Test
    public void checkDeletionIfQuantity0() {
        Util u = new Util();
        HashMap<String, Integer> ingredients = new HashMap<>();
        ingredients.put("milk", 20);
        ingredients.put("cereal", 30);
        HashMap<String, Integer> recipeStuff = new HashMap<>();
        recipeStuff.put("milk", 20);
        recipeStuff.put("cereal", 15);
        assertTrue(u.checkIngredientDeletion(ingredients, recipeStuff));
    }

    @Test
    public void checkQuantitiesDeducted() {
        Util u = new Util();
        HashMap<String, Integer> ingredients = new HashMap<>();
        ingredients.put("milk", 20);
        ingredients.put("cereal", 30);
        ingredients.put("broccoli", 500);
        HashMap<String, Integer> recipeStuff = new HashMap<>();
        recipeStuff.put("milk", 20);
        recipeStuff.put("cereal", 15);
        assertTrue(u.checkForDeduction(ingredients, recipeStuff));
    }

    // Harini Sathu
    @Test
    public void testAddMissingIngredient() {
        Util u = new Util();
        HashMap<String, Integer> ingredients = new HashMap<>();
        String ingredientName = "apple";
        int initialQuantity = 0;

        // Add the ingredient with an initial quantity of 0
        u.addMissingIngredient(ingredients, ingredientName, initialQuantity);

        // Check if the ingredient is added successfully with the initial quantity
        assertTrue(ingredients.containsKey(ingredientName));
        assertEquals(initialQuantity, (int) ingredients.get(ingredientName));
    }

    @Test
    public void testCookbook() {
        Cookbook cb = new Cookbook();
        HashMap<String, Integer> pantry = new HashMap<>();
        pantry.put("flour", 100);
        pantry.put("sugar", 50);
        pantry.put("butter", 200);

        List<Integer> pantryValues = new ArrayList<>(pantry.values());

        // Act
        boolean containsNull = false;
        for (Integer value : pantryValues) {
            if (value == null) {
                containsNull = true;
                break;
            }
        }

        assertTrue("Pantry contains null values", !containsNull);
    }

    //Julie Young
    @Test
    public void checkSelectedTrue() {
        Util u = new Util();
        Ingredient i1 = new Ingredient("I1", 12, 12, "N/A", true);
        Ingredient i2 = new Ingredient("I2", 12, 12, "N/A", true);
        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(i1);
        ingredientList.add(i2);
        ArrayList<Boolean> res = u.isChecked(ingredientList);
        ArrayList<Boolean> ans = new ArrayList<Boolean>();
        ans.add(true);
        ans.add(true);
        Assert.assertEquals(res, ans);
    }

    @Test
    public void checkSelectedFalse() {
        Util u = new Util();
        Ingredient i1 = new Ingredient("I1", 12, 12, "N/A", false);
        Ingredient i2 = new Ingredient("I2", 12, 12, "N/A", false);
        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(i1);
        ingredientList.add(i2);
        ArrayList<Boolean> res = u.isChecked(ingredientList);
        ArrayList<Boolean> ans = new ArrayList<Boolean>();
        ans.add(false);
        ans.add(false);
        Assert.assertEquals(res, ans);
    }
    // Reese Wang
    @Test
    public void checkNewShoppingListItemQuantity(){
        Util u = new Util();
        HashMap<String, Integer> shoppingList = new HashMap<>();
        shoppingList.put("milk", 20);
        shoppingList.put("cereal", 20);
        shoppingList.put("broccoli", 50);
        Ingredient addToList = new Ingredient("milk", 300, 20, "10-28-2004", false);
        int quantity = u.checkRepeatedListItems(shoppingList, addToList);
        assertTrue(quantity == 20);
    }

    @Test
    public void checkNewShoppingListItemQuantity2(){
        Util u = new Util();
        HashMap<String, Integer> shoppingList = new HashMap<>();
        shoppingList.put("milk", 20);
        shoppingList.put("cereal", 20);
        shoppingList.put("broccoli", 50);
        Ingredient addToList = new Ingredient("milk2", 300, 20, "10-28-2004", false);
        int quantity = u.checkRepeatedListItems(shoppingList, addToList);
        assertTrue(quantity == 300);
    }
}
