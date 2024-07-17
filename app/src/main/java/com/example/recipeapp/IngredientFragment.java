package com.example.recipeapp;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.recipeapp.model.Ingredient;
import com.example.recipeapp.viewmodels.PantryViewModel;
import com.example.recipeapp.views.IngredientInput;


public class IngredientFragment extends Fragment {
    private PantryViewModel pantryViewModel;

    public IngredientFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_ingredient_list, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button button = view.findViewById(R.id.ingredientInputButton);

        pantryViewModel = new PantryViewModel();
        pantryViewModel.readIngredients();
        pantryViewModel.readIngredientQuantities();

        LinearLayout ingredientListLayout = view.findViewById(R.id.ingredients_layout);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        pantryViewModel.getIngredientData().observe(getViewLifecycleOwner(), ingredients -> {
            ingredientListLayout.removeAllViews();

            for (Ingredient ingredient : pantryViewModel.getIngredientData().getValue()) {
                // Inflate the ingredient card layout
                System.out.println("Ingredient: " + ingredient);
                View cardView = inflater.inflate(R.layout.ingredient_card, null);
                TextView name = cardView.findViewById(R.id.ingredient_name_textview);
                TextView ingredientQuantity = cardView.findViewById(
                        R.id.ingredient_quantity_textview);
                // Set the name of the ingredient
                name.setText(ingredient.getName());
                System.out.println("Quantity: " + ingredient.getQuantity());
                ingredientQuantity.setText(ingredient.getQuantity().toString() + "g");
                // Add the card view to the layout
                ingredientListLayout.addView(cardView);
                // Add a spacer
                View spacer = new View(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 16);
                spacer.setLayoutParams(layoutParams);
                ingredientListLayout.addView(spacer);

                // listener for updating text
                EditText newQty = cardView.findViewById(R.id.changeQtyValue_edittext);
                Button changeQtyButton = cardView.findViewById(R.id.changeQuantity_button);

                // Set up listener for the Change Quantity button
                changeQtyButton.setOnClickListener(v -> {
                    String editTextString = newQty.getText().toString().trim();

                    if (!editTextString.isEmpty()) {
                        int newQuantity = Integer.parseInt(editTextString);
                        // Update the quantity of the ingredient object
                        ingredient.setQuantity(newQuantity);

                        pantryViewModel.updateQuantity(ingredient, newQuantity);

                        ingredientQuantity.setText(ingredient.getQuantity().toString() + "g");
                    }
                });
            }
        });
        button.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), IngredientInput.class);
            startActivity(intent);
        });

        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);
    }
}
