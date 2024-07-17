package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recipeapp.model.Ingredient;
import com.example.recipeapp.viewmodels.RecipeInputViewModal;

import java.util.ArrayList;
import java.util.List;


public class RecipeInput extends AppCompatActivity {
    private EditText nameInput;
    private EditText instrInput;
    private EditText calorieInput;
    private EditText ingredientInput;
    private EditText ingredientQuantityInput;
    private Button addIngredientButton;
    private List<Ingredient> l = new ArrayList<>();

    private RecipeInputViewModal viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recipe_input);
        viewModel = new ViewModelProvider(this).get(RecipeInputViewModal.class);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, RecipeInputFragment.newInstance())
                    .commitNow();
        }
        Button backButton = findViewById(R.id.buttonToRecipe);
        Button saveButton = findViewById(R.id.buttonSave);
        nameInput = findViewById(R.id.etRecipeName);
        instrInput = findViewById(R.id.etRecipeInstructions);
        calorieInput = findViewById(R.id.etCalorieCount);
        ingredientInput = findViewById(R.id.etIngredientName);
        ingredientQuantityInput = findViewById(R.id.etIngredientAmount);

        backButton.setOnClickListener(back -> finish());
        viewModel.getIsSaveSuccessful().observe(this, isSuccess -> {
            if (Boolean.TRUE.equals(isSuccess)) {
                Toast.makeText(this, "Recipe saved successfully.", Toast.LENGTH_SHORT).show();
            } else if (Boolean.FALSE.equals(isSuccess)) {
                Toast.makeText(this, "Failed to save recipe. Please try again.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        addIngredientButton = findViewById(R.id.buttonAddIngredient);
        addIngredientButton.setOnClickListener(v -> addIngredientEditText());
        saveButton.setOnClickListener(v -> saveRecipeInformation());
    }
    @SuppressLint("SetTextI18n")
    private void addIngredientEditText() {
        String ingredientName = ingredientInput.getText().toString().trim();
        String ingredientQuantity = ingredientQuantityInput.getText().toString().trim();
        if (ingredientName.isEmpty() || ingredientQuantity.isEmpty()
                || Integer.parseInt(ingredientQuantity) == 0) {
            if (ingredientName.isEmpty()) {
                ingredientInput.setError("Please enter an ingredient for this recipe");
                ingredientInput.requestFocus();
            }
            if (ingredientQuantity.isEmpty()) {
                ingredientQuantityInput.setError("Please enter a quantity for this recipe");
                ingredientQuantityInput.requestFocus();
            }
            if (Integer.parseInt(ingredientQuantity) == 0) {
                ingredientQuantityInput.setError("Please enter a quantity greater than 0");
                ingredientQuantityInput.requestFocus();
            }
        } else {
            Integer quantity = Integer.parseInt(ingredientQuantity);
            Ingredient ing = new Ingredient(ingredientName, quantity);
            l.add(ing);
            addIngredientButton.setText("Add Ingredient (" + l.size() + " added)");
            ingredientInput.setText("");
            ingredientQuantityInput.setText("");
        }
    }
    private void saveRecipeInformation() {
        Util u = new Util();
        String name = nameInput.getText().toString().trim();
        String instructions = instrInput.getText().toString().trim();
        String calories = calorieInput.getText().toString().trim();
        if (!u.validateName(name) || !u.validateCalories(calories)
                || !u.validateInstructions(instructions) || l.isEmpty()) {
            if (!u.validateName(name)) {
                nameInput.setError("Please enter a name for this recipe");
                nameInput.requestFocus();
            }
            if (!u.validateInstructions(instructions)) {
                instrInput.setError("Please enter instructions for this recipe");
                instrInput.requestFocus();
            }
            if (!u.validateCalories(calories)) {
                calorieInput.setError("Please enter the calories for this recipe");
                calorieInput.requestFocus();
            }
            if (l.isEmpty()) {
                addIngredientButton.setError("Please enter an ingredient");
                addIngredientButton.requestFocus();
            }
        } else {
            viewModel.saveRecipe(name, instructions, calories, l);
        }
    }
}
