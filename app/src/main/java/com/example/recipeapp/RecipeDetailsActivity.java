package com.example.recipeapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeapp.model.Ingredient;
import com.example.recipeapp.model.Recipe;
import com.example.recipeapp.viewmodels.MealViewModel;
import com.example.recipeapp.viewmodels.PantryViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {
    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details_activity);
        Recipe recipe = (Recipe) getIntent().getSerializableExtra("recipe");

        if (recipe != null) {
            TextView recipeName = findViewById(R.id.name_of_recipe);
            TextView calories = findViewById(R.id.calories_in_recipe);
            ListView ingredients = findViewById(R.id.ingredients_of_recipe);
            TextView instructions = findViewById(R.id.instructions_of_recipe);
            Button backButton = findViewById(R.id.backButton);
            Button cook = findViewById(R.id.cookButton);
            recipeName.setText(recipe.getName());
            String calsWithLabel = recipe.getCalories() + " calories";
            calories.setText(calsWithLabel);

            List<String> ings = recipe.getIngredients();
            List<Integer> quants = recipe.getQuantities();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, ings);
            ingredients.setAdapter(adapter);

            instructions.setText(recipe.getInstructions());
            backButton.setOnClickListener(back -> finish());

            MealViewModel vm = new MealViewModel();
            cook.setOnClickListener(e -> {
                try {
                    //today's date
                    Date today = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedToday = dateFormat.format(today);

                    String recipeNameText = recipeName.getText().toString();
                    String caloriesText = calories.getText().toString();

                    String cals = "";
                    for (int i = 0; i < caloriesText.length(); i++) {
                        if (Character.isDigit(caloriesText.charAt(i))) {
                            cals += caloriesText.charAt(i);
                        }
                    }

                    PantryViewModel pvm = new PantryViewModel();
                    pvm.getIngredientData().observe(this, info -> {
                        for (int i = 0; i < ings.size(); i++) {
                            for (Ingredient ingredient : info) {
                                if (ings.get(i).equals(ingredient.getName())) {
                                    int newQ = ingredient.getQuantity() - quants.get(i);
                                    ingredient.setQuantity(newQ);
                                    pvm.updateQuantity(ingredient, newQ);
                                }
                            }
                        }
                    });
                    pvm.readIngredients();
                    vm.inputMeal(this, recipeNameText, cals, formattedToday);
                    vm.readDailyMeals();

                } catch (Exception ex) {
                    Log.e("bro", "error" + ex.getMessage());
                    ex.printStackTrace();
                }
            });
        }
    }
}
