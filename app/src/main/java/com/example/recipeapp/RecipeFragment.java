package com.example.recipeapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipeapp.model.Cookbook;
import com.example.recipeapp.model.Recipe;
import com.example.recipeapp.viewmodels.PantryViewModel;
import com.example.recipeapp.viewmodels.RecipeViewModel;
import com.example.recipeapp.views.MissingIngredientActivity;

import java.util.HashMap;
import java.util.Map;

public class RecipeFragment extends Fragment {

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)  {
        Spinner filterSpinner = view.findViewById(R.id.FilterSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireActivity(),
                R.array.filter_options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);
        RecipeViewModel recipeViewModel = new RecipeViewModel();
        PantryViewModel pantryViewModel = new PantryViewModel();
        FilterContext filterContext = new FilterContext(new NoFilter());
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        filterContext.setStrategy(new CalorieFilter());
                        break;
                    case 2:
                        filterContext.setStrategy(new IngredientCountFilter());
                        break;
                    default:
                        filterContext.setStrategy(new NoFilter());
                        break;
                }
                recipeViewModel.filterAndUpdateRecipes(filterContext);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterContext.setStrategy(new NoFilter());
            }
        });
        Button addRecipeButton = view.findViewById(R.id.AddRecipeButton);
        addRecipeButton.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(getActivity(), RecipeInput.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        recipeViewModel.readRecipes(filterContext);
        LinearLayout recipeListLayout = view.findViewById(R.id.RecipeListLayout);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        recipeViewModel.getRecipeLiveData().observe(getViewLifecycleOwner(), recipes -> {
            recipeListLayout.removeAllViews();
            for (Recipe r : recipeViewModel.getRecipeLiveData().getValue()) {
                View cardView = inflater.inflate(R.layout.recipe_card, null);
                TextView name = cardView.findViewById(R.id.recipe_name_textview);
                SpannableString recipeName = new SpannableString("Recipe Name: "
                        + r.getName());
                recipeName.setSpan(new StyleSpan(Typeface.BOLD), 0, 11,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                name.setText(recipeName);
                TextView calories = cardView.findViewById(R.id.recipe_calories_textview);
                SpannableString caloriesLabel = new SpannableString("Calories: "
                        + r.getCalories());
                caloriesLabel.setSpan(new StyleSpan(Typeface.BOLD), 0, 9,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                calories.setText(caloriesLabel);
                TextView available = cardView.findViewById(
                        R.id.recipe_ingredients_available_textview);
                HashMap<String, Integer> pantryItems = pantryViewModel.getIngQuantity().getValue();
                Cookbook book = new Cookbook();
                Boolean sufficient = book.sufficientIngredients(pantryItems, r);
                Button addMissingIngredientsButton = cardView.findViewById(
                        R.id.add_missing_ingredients_button);
                if (sufficient) {
                    available.setText("Sufficient Ingredients");
                    available.setTextColor(Color.GREEN);
                    name.setOnClickListener(v -> {
                        Intent intent = new Intent(getContext(), RecipeDetailsActivity.class);
                        intent.putExtra("recipe", r);
                        requireContext().startActivity(intent);
                    });
                    addMissingIngredientsButton.setVisibility(View.GONE);
                } else {
                    available.setText("Insufficient Ingredients");
                    available.setTextColor(Color.RED);
                    name.setOnClickListener(v -> Toast.makeText(getContext(), "You don't have"
                            + " enough ingredients to make this recipe.",
                            Toast.LENGTH_SHORT).show());
                    addMissingIngredientsButton.setVisibility(View.VISIBLE);
                    addMissingIngredientsButton.setOnClickListener(v -> {
                        Intent intent = new Intent(getContext(),
                                MissingIngredientActivity.class);
                        Map<String, Integer> missingIngredients = book
                                .calculateMissingQuantities(r, pantryItems);
                        intent.putExtra("recipe", r);
                        intent.putExtra("missingIngredients",
                                new HashMap<>(missingIngredients));
                        requireContext().startActivity(intent);
                    });
                }
                LinearLayout ingredientsListLayout = cardView.findViewById(
                        R.id.recipe_ingredients_layout);
                for (int i = 0; i < r.getIngredients().size(); i++) {
                    TextView ingredient = new TextView(requireContext());
                    ingredient.setText(r.getIngredients().get(i) + ": "
                            + r.getQuantities().get(i) + "g");
                    ingredientsListLayout.addView(ingredient);
                }
                recipeListLayout.addView(cardView);
                TextView spacer = new TextView(requireContext());
                recipeListLayout.addView(spacer);
            }
        });
        pantryViewModel.getIngQuantity().observe(getViewLifecycleOwner(), pantryItems -> {
            int index = 0;
            for (Recipe r: recipeViewModel.getRecipeLiveData().getValue()) {
                View currView = recipeListLayout.getChildAt(index);
                TextView available = currView.findViewById(
                        R.id.recipe_ingredients_available_textview);
                TextView name = currView.findViewById(R.id.recipe_name_textview);
                Cookbook book = new Cookbook();
                boolean sufficient = book.sufficientIngredients(pantryItems, r);
                Button addMissingIngredientsButton = currView
                        .findViewById(R.id.add_missing_ingredients_button);
                if (sufficient) {
                    available.setText("Sufficient Ingredients");
                    available.setTextColor(Color.GREEN);
                    addMissingIngredientsButton.setVisibility(View.GONE);
                    name.setOnClickListener(v -> {
                        Intent intent = new Intent(getContext(), RecipeDetailsActivity.class);
                        intent.putExtra("recipe", r);
                        requireContext().startActivity(intent);
                    });
                } else {
                    available.setText("Insufficient Ingredients");
                    available.setTextColor(Color.RED);
                    name.setOnClickListener(v -> Toast.makeText(getContext(), "You don't have"
                                    + "enough ingredients to make this recipe.",
                            Toast.LENGTH_SHORT).show());
                    addMissingIngredientsButton.setVisibility(View.VISIBLE);
                    addMissingIngredientsButton.setOnClickListener(v -> {
                        Intent intent = new Intent(getContext(),
                                MissingIngredientActivity.class);
                        Map<String, Integer> missingIngredients = book
                                .calculateMissingQuantities(r, pantryItems);
                        intent.putExtra("recipe", r);
                        intent.putExtra("missingIngredients",
                                new HashMap<>(missingIngredients));
                        requireContext().startActivity(intent);
                    });
                }
                index += 2;
            }
        });
        pantryViewModel.readIngredientQuantities();
    }
}