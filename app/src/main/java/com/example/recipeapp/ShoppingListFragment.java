package com.example.recipeapp;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.recipeapp.model.Ingredient;
import com.example.recipeapp.viewmodels.PantryViewModel;
import com.example.recipeapp.viewmodels.ShoppingListViewModel;
import com.example.recipeapp.views.ShoppingInputActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class ShoppingListFragment extends Fragment {

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // testing purposes: delete if you're implementing actual logic
        ShoppingListViewModel vm = new ShoppingListViewModel();
        PantryViewModel pantryVM = new PantryViewModel();
        Button button = view.findViewById(R.id.shoppingInputButton);
        Button submit = view.findViewById(R.id.buyButton);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ShoppingInputActivity.class);
            startActivity(intent);
        });

        submit.setOnClickListener(v -> {
            for (Ingredient i : vm.getShoppingList().getValue()) {
                if (i.getSelected()) {
                    vm.removeSingleIngredient(i);
                    pantryVM.addIngredient(i);
                }
            }
        });

        vm.readShoppingList();
        LinearLayout layout = view.findViewById(R.id.shoppingListLayout);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        vm.getShoppingList().observe(getViewLifecycleOwner(), items -> {
            layout.removeAllViews();
            for (Ingredient i : vm.getShoppingList().getValue()) {
                View cardView = inflater.inflate(R.layout.shopping_card, null);
                TextView name = cardView.findViewById(R.id.ingredient_name_textview);
                SpannableString recipeName = new SpannableString("Recipe Name: "
                        + i.getName());
                recipeName.setSpan(new StyleSpan(Typeface.BOLD), 0, 11,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                name.setText(recipeName);
                TextView quantity = cardView.findViewById(R.id.ingredient_quantity_textview);
                SpannableString quantityLabel = new SpannableString("Quantity: "
                        + i.getQuantity() + "g");
                quantityLabel.setSpan(new StyleSpan(Typeface.BOLD), 0, 9,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                quantity.setText(quantityLabel);
                SwitchMaterial toggle = cardView.findViewById(R.id.checkbox);
                toggle.setChecked(i.getSelected());
                layout.addView(cardView);
                TextView spacer = new TextView(requireContext());
                layout.addView(spacer);
                EditText newQty = cardView.findViewById(R.id.changeQtyValue_edittext);
                Button changeQtyButton = cardView.findViewById(R.id.changeQuantity_button);
                changeQtyButton.setOnClickListener(v -> {
                    String editTextString = newQty.getText().toString().trim();
                    if (!editTextString.isEmpty()) {
                        int newQuantity = Integer.parseInt(editTextString);
                        i.setQuantity(newQuantity);
                        vm.updateQuantity(i, newQuantity);
                        SpannableString quantityNewLabel = new SpannableString("Quantity: "
                                + i.getQuantity() + "g");
                        quantityLabel.setSpan(new StyleSpan(Typeface.BOLD), 0, 9,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        quantity.setText(quantityNewLabel);
                    }
                });
                toggle.setOnCheckedChangeListener((buttonView, isChecked)
                        -> {
                    i.setSelected(!i.getSelected());
                    vm.updateSelected(i, !i.getSelected());
                });
            }

            //Buy functionality
            //            for (int i = 0; i < layout.getChildCount(); i+=2) {
            //                View cardView = layout.getChildAt(i);
            //                SwitchMaterial toggle = cardView.findViewById(R.id.checkbox);
            //                toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //                    i.setSelected(isChecked);
            //                });
            //            }
        });


        //        ShoppingListViewModel shoppingListViewModel = new
        //        ViewModelProvider(this).get(ShoppingListViewModel.class);
        //        shoppingListViewModel.addItem(getContext(), "abc", 6, 1200, "10-04-2024");
        //        shoppingListViewModel.getShoppingList().observe(getViewLifecycleOwner(), list -> {
        //            System.out.println("final shopping list: " +
        //            shoppingListViewModel.getShoppingList().getValue());
        //       });
    }
}