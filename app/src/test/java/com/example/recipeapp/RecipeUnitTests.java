package com.example.recipeapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RecipeUnitTests {
    // Julie Young
    @Test
    public void checkEquation() {
        InputMealFragment fragment = new InputMealFragment();
        int calories = fragment.calorieEquation(170, 63, "Female");
        assertEquals(calories, 1337);
    }
    @Test
    public void checkEquation2() {
        InputMealFragment fragment = new InputMealFragment();
        int calories = fragment.calorieEquation(200, 100, "Male");
        assertEquals(calories, 2061);
    }
    // Reese Wang
    @Test
    public void checkEquation3() {
        InputMealFragment fragment = new InputMealFragment();
        int calories = fragment.calorieEquation(210, 110, "Female");
        assertEquals(calories, 2057);
    }

    @Test
    public void checkEquation4() {
        InputMealFragment fragment = new InputMealFragment();
        int calories = fragment.calorieEquation(140, 80, "Male");
        assertEquals(calories, 1486);
    }
    // Saniya Savla
    @Test
    public void validateInputsWithEmptyHeight() {
        Util activity = new Util();
        assertFalse(activity.validateHeight(""));
    }

    @Test
    public void validateWeight_valid() {
        Util activity = new Util();
        assertTrue(activity.validateWeight("124"));
    }
    // Harini Sathu
    @Test
    public void validateInputsWithEmptyWeight() {
        Util activity = new Util();
        assertFalse(activity.validateHeight(""));
    }
    @Test
    public void validateHeight_valid() {
        Util activity = new Util();
        assertTrue(activity.validateHeight("124"));
    }

    //Simona Ivanov
    @Test
    public void inputMealWithEmptyNameReturnsFalse() {
        Util vm = new Util();
        assertFalse(vm.validName(""));
    }
    //Simona Ivanov
    @Test
    public void inputMealWithEmptyCalCountReturnsFalse() {
        Util vm = new Util();
        assertFalse(vm.validCalCount(null));
    }

    //Amritha Pramod
    @Test
    public void inputMealWithValidNameReturnsTrue() {
        Util vm = new Util();
        assertTrue(vm.validName("Banana"));
    }

    //Amritha Pramod
    @Test
    public void inputMealWithValidCals() {
        Util vm = new Util();
        assertTrue(vm.validCalCount("720"));
    }
}


