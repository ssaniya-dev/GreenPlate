package com.example.recipeapp.views;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeapp.R;
import com.example.recipeapp.model.Ingredient;
import com.example.recipeapp.viewmodels.PantryViewModel;
import com.example.recipeapp.viewmodels.ShoppingListViewModel;


import java.util.ArrayList;

public class ShoppingInputActivity extends AppCompatActivity {
    private EditText ingredientName;
    private EditText ingredientQuantity;
    private EditText caloriesPerServing;
    private Button expirationDate;
    /** @noinspection checkstyle:OperatorWrap*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shopping_input);
        ShoppingListViewModel vm = new ShoppingListViewModel();
        PantryViewModel pm = new PantryViewModel();
        Button viewShoppingListButton = findViewById(R.id.viewShoppingListButton);
        viewShoppingListButton.setOnClickListener(v -> finish());
        ingredientName = findViewById(R.id.ingredientName);
        ingredientQuantity = findViewById(R.id.ingredientQuantity);
        caloriesPerServing = findViewById(R.id.caloriesPerServing);
        expirationDate = findViewById(R.id.expirationDate);
        Button ingInput = findViewById(R.id.inputIngButton);

        expirationDate.setOnClickListener(v ->
                vm.showDatePickerDialog(this, expirationDate));
        ArrayList<Ingredient> pantry = new ArrayList<Ingredient>();
        ArrayList<String> ingData = new ArrayList<>();
        ArrayList<Ingredient> shoppinglist = new ArrayList<Ingredient>();
        vm.readShoppingList();
        vm.getShoppingList().observe(this, info -> {
            for (Ingredient ingredient : info) {
                ingData.add(ingredient.getName());
                shoppinglist.add(ingredient);
            }
        });
        pm.readIngredients();
        pm.getIngredientData().observe(this, pantry::addAll);

        ingInput.setOnClickListener(v -> {
            String ingredientName1 = ingredientName.getText().toString();
            String ingredientQuantityStr = ingredientQuantity.getText().toString();
            String calServingStr = caloriesPerServing.getText().toString();
            String date = expirationDate.getText().toString();
            int oldCalorieCount = -1;
            String oldExpDate = "";
            Ingredient i = null;
            for (Ingredient ingredient : pantry) {
                if (ingredient.getName().equals(ingredientName1)) {
                    oldCalorieCount = ingredient.getCaloriesPerServing();
                    oldExpDate = ingredient.getExpirationDate();
                }
            }
            for (Ingredient ingredient : shoppinglist) {
                if (ingredient.getName().equals(ingredientName1)) {
                    i = ingredient;
                }
            }
            if (!ingredientQuantityStr.isEmpty() && !calServingStr.isEmpty()) {
                int ingredientQuantity1 = Integer.parseInt(ingredientQuantityStr);
                int calServing = Integer.parseInt(calServingStr);
                if (i == null
                    && !ingredientName1.isEmpty()
                        && ingredientQuantity1 > 0 && calServing >= 0) {
                    if (oldCalorieCount == -1
                            || oldCalorieCount == calServing && oldExpDate.equals(date)) {
                        vm.addItem(this, ingredientName1, ingredientQuantity1,
                                calServing, date);
                        Intent intent = new Intent(this, WelcomeActivity.class);
                        startActivity(intent);
                    } else {
                        if (oldCalorieCount != calServing) {
                            caloriesPerServing.setError("This ingredient already"
                                        + " has a calorie count of " + oldCalorieCount);
                        }
                        if (!oldExpDate.equals(date)) {
                            if (!oldExpDate.equals("")) {
                                expirationDate.setError("This ingredient already has an exp date "
                                            + oldExpDate);
                            } else {
                                expirationDate.setError("This ingredient "
                                            + "does not have an exp date.");
                            }
                        }
                    }
                } else {
                    if (i != null) {
                        vm.update(i, ingredientQuantity1, calServing, date);
                        Intent intent = new Intent(this, WelcomeActivity.class);
                        startActivity(intent);
                    }
                    if (ingredientName1.isEmpty()) {
                        ingredientName.setError("Please enter an ingredient name");
                    }
                    if (ingredientQuantity1 <= 0) {
                        ingredientQuantity.setError("Please enter a valid quantity!");
                    }
                    if (calServing < 0) {
                        caloriesPerServing.setError("Please enter a valid calories per serving.");
                    }
                }
            } else {
                if (ingData.contains(ingredientName1)) {
                    ingredientName.setError("This already exists in the cart");
                }
                if (ingredientQuantityStr.isEmpty()) {
                    ingredientQuantity.setError("Please enter an ingredient quantity.");
                }
                if (calServingStr.isEmpty()) {
                    caloriesPerServing.setError("Please enter calories per serving.");
                }
            }
        });
    }
}
