package com.example.recipeapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.recipeapp.model.Ingredient;
import com.example.recipeapp.model.Cookbook;
import com.example.recipeapp.model.Recipe;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Sprint3UnitTests {
    // Saniya Savla
    @Test
    public void checkRecipeCaloriesEmpty() {
        Util u = new Util();
        String calories = "";
        assertFalse(u.validateCalories(calories));
    }
    @Test
    public void checkRecipeNameValid() {
        Util u = new Util();
        String name = "Bacon and Eggs";
        assertTrue(u.validateName(name));
    }

    // Reese Wang
    @Test
    public void checkRecipeInstructionsValid() {
        Util u = new Util();
        String instructions = "Do this. Then do this. Then finish doing this.";
        assertTrue(u.validateInstructions(instructions));
    }

    @Test
    public void checkValidRecipe() {
        Util u = new Util();
        Recipe r = new Recipe("Recipe", 199, "instructions",
                new ArrayList<String>(), new ArrayList<Integer>());
        assertFalse(u.validateIngredients(r.getIngredients(), r.getQuantities()));
    }

    // Amritha Pramod
    @Test
    public void checkForDuplicateIngredients() {
        Util u = new Util();
        ArrayList<String> ings = new ArrayList<>();
        ings.add("carrots");
        ings.add("broccoli");
        assertTrue(u.duplicateIngredients(ings, "broccoli"));
    }



    // Amritha Pramod
    @Test
    public void checkValidQuantities() {
        Util u = new Util();
        ArrayList<Integer> validIngs = new ArrayList<>();
        validIngs.add(3);
        validIngs.add(-1);
        assertFalse(u.validateQuantities(validIngs));
    }

    //Julie Young
    @Test
    public void checkMissingIngredient() {
        Cookbook book = new Cookbook();
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("Milk");
        ArrayList<Integer> qty = new ArrayList<>();
        qty.add(10);
        Recipe r = new Recipe(
                "Recipe", 200, "instructions", ingredients, qty);
        HashMap<String, Integer> pantry = new HashMap<>();
        assertFalse(book.sufficientIngredients(pantry, r));
    }
    @Test
    public void checkInsufficientQty() {
        Cookbook book = new Cookbook();
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("Milk");
        ArrayList<Integer> qty = new ArrayList<>();
        qty.add(200);
        Recipe r = new Recipe(
                "Recipe", 200, "instructions", ingredients, qty);
        HashMap<String, Integer> pantry = new HashMap<>();
        pantry.put("Milk", 100);
        assertFalse(book.sufficientIngredients(pantry, r));
    }

    //Simona Ivanov
    @Test
    public void checkSufficient() {
        Cookbook book = new Cookbook();
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("Milk");
        ingredients.add("Eggs");
        ArrayList<Integer> qty = new ArrayList<>();
        qty.add(200);
        qty.add(10);
        Recipe r = new Recipe(
                "Recipe", 200, "instructions", ingredients, qty);
        HashMap<String, Integer> pantry = new HashMap<>();
        pantry.put("Milk", 300);
        pantry.put("Eggs", 40);
        assertTrue(book.sufficientIngredients(pantry, r));
    }
    @Test
    public void checkMissingQty() {
        Cookbook book = new Cookbook();
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("Cereal");
        ingredients.add("Milk");
        ArrayList<Integer> qty = new ArrayList<>();
        qty.add(50);
        Recipe recipe = new Recipe("Cereal", 300, "Put cereal in milk",
                ingredients, qty);
        HashMap<String, Integer> pantry = new HashMap<>();
        assertFalse(book.sufficientIngredients(pantry, recipe));
    }
    // Harini Sathu
    @Test
    public void checkValidIngredientName() {
        Util u = new Util();
        Ingredient i = new Ingredient("Rice", 500);
        assertTrue("Ingredient name should not be empty", !i.getName().isEmpty());
    }
    @Test
    public void checkValidAmountOfIngredient() {
        Util u = new Util();
        Ingredient i = new Ingredient("Rice", 500);
        assertTrue("Ingredient quantity should be greater than 0", i.getQuantity() > 0);
    }

    @Test
    public void checkValidIngredientNameForNumbers() {
        // we want to make sure that there aren't any illegal numbers in the string
        Util u = new Util();
        String validName = "Tomato";
        assertFalse("Ingredient name should not contain numbers", u.containsNumber(validName));
        String invalidName = "Rice123";
        assertTrue("Ingredient name should contain numbers", u.containsNumber(invalidName));
    }
}
