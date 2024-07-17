package com.example.recipeapp.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.recipeapp.R;
import com.example.recipeapp.viewmodels.ShoppingListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MissingIngredientActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_ingredient);

        Intent intent = getIntent();
        Map<String, Integer> missingIngredients = (Map<String, Integer>)
                intent.getSerializableExtra("missingIngredients");

        ListView listView = findViewById(R.id.missing_ingredients_list);
        ArrayList<String> missingIngredientsList = new ArrayList<>(
                missingIngredients.keySet());
        IngredientAdapter adapter = new IngredientAdapter(
                this, missingIngredientsList, missingIngredients);
        listView.setAdapter(adapter);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }
    private class IngredientAdapter extends ArrayAdapter<String> {
        private final Map<String, Integer> missingIngredients;
        IngredientAdapter(Context context, List<String> ingredients,
                          Map<String, Integer> missingIngredients) {
            super(context, 0, ingredients);
            this.missingIngredients = missingIngredients;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.list_item_missing_ingredient, parent, false);
            }

            String ingredient = getItem(position);

            TextView ingredientName = convertView.findViewById(R.id.ingredient_name);
            EditText caloriesInput = convertView.findViewById(R.id.calories_input);
            Button addButton = convertView.findViewById(R.id.add_button);

            ingredientName.setText(ingredient);
            addButton.setOnClickListener(v -> {
                // Handle the add button click event here
                // For example, you can get the number
                // of calories entered by the user and add it to the ingredient
                String calories = caloriesInput.getText().toString();

                if (!calories.isEmpty()) {
                    ShoppingListViewModel shopVM = new ShoppingListViewModel();
                    EditText name = new EditText(getContext());
                    name.setText(ingredient);
                    EditText quantityInput = new EditText(getContext());
                    Integer quantity = missingIngredients.get(ingredient);
                    quantityInput.setText(quantity.toString());
                    for (String item: missingIngredients.keySet()) {
                        shopVM.addItem(getContext(), item, missingIngredients.get(item), 0, "N/A");
                    }
                    addButton.setText("Done!");
                }
            });

            return convertView;
        }
    }
}