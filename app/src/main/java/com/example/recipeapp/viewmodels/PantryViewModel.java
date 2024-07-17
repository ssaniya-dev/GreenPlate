package com.example.recipeapp.viewmodels;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.recipeapp.model.Ingredient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class PantryViewModel {
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Calendar calendar;
    private MutableLiveData<HashMap<String, Integer>> ingQuantity = new MutableLiveData
            <>(new HashMap<>());

    private List<Ingredient> allIngredients = new ArrayList<>();

    private MutableLiveData<List<Ingredient>> ingredientList = new MutableLiveData<>();

    private MutableLiveData<Boolean> isSaveSuccessful = new MutableLiveData<>();

    public LiveData<List<Ingredient>> getIngredientData() {
        return ingredientList;
    }
  
    public LiveData<HashMap<String, Integer>> getIngQuantity() {
        return ingQuantity;
    }
  
    public AtomicBoolean inputIngredient(Context context, EditText ingredientName,
                                EditText quantity, EditText caloriesPerServing,
                                         Button expirationDate) {
        AtomicBoolean successful = new AtomicBoolean(false);
        String ingredientName1 = ingredientName.getText().toString();
        String strIngredientQuantity = quantity.getText().toString();
        String strIngredientCalories = caloriesPerServing.getText().toString();
        String expDate = expirationDate.getText().toString();
        int ingredientQuantity = Integer.parseInt(quantity.getText().toString());
        int ingredientCalories = Integer.parseInt(caloriesPerServing.getText().toString());
        if (ingredientName1.isEmpty()) {
            ingredientName.setError("Please enter the name of your ingredient!");
        } else if (strIngredientQuantity.isEmpty()) {
            quantity.setError("Please enter the quantity of your ingredient!");
        } else if (ingredientQuantity <= 0) {
            quantity.setError("Please enter a valid quantity!");
        } else if (strIngredientCalories.isEmpty()) {
            caloriesPerServing.setError(
                    "Please enter the calories per serving for this ingredient!");
        } else {
            ingredientName.setError(null);
            quantity.setError(null);
            caloriesPerServing.setError(null);
            expirationDate.setError(null);
            Ingredient newIngredient = new Ingredient(ingredientName1, ingredientQuantity,
                    ingredientCalories, expDate);
            FirebaseDatabase database = FirebaseDatabase
                    .getInstance("https://recipeapp-1fba1-default-rtdb.firebaseio.com/");
            DatabaseReference pantryDB = database.getReference().child("pantry/"
                    + user.getUid());

            pantryDB.push().setValue(newIngredient)
                    .addOnSuccessListener(success -> {
                        successful.set(true);
                        Toast.makeText(context,
                                "Ingredient inputted successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(failure -> {
                        successful.set(false);
                        Toast.makeText(context,
                                "Could not input ingredient", Toast.LENGTH_SHORT).show();
                    });
        }
        return successful;
    }

    //Adding ingredient from shopping list to pantry
    public void addIngredient(Ingredient ingredient) {
        FirebaseDatabase ingDB = FirebaseDatabase
                .getInstance("https://recipeapp-1fba1-default-rtdb.firebaseio.com/");
        DatabaseReference pantryDB = ingDB.getReference().child("pantry/"
                + user.getUid());

        pantryDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if userId exists
                if (snapshot.exists()) {
                    boolean found = false;
                    for (DataSnapshot ingredientSnapshot : snapshot.getChildren()) {
                        // Access each ingredient under the userId
                        String ingredientId = ingredientSnapshot.getKey();
                        String ingredientName = ingredientSnapshot.child("name")
                                .getValue(String.class);
                        int ingredientQty = ingredientSnapshot.child("quantity")
                                .getValue(Integer.class);

                        if (ingredient.getName().equals(ingredientName)) {
                            found = true;
                            int newQty = ingredient.getQuantity() + ingredientQty;
                            ingredient.setQuantity(newQty);
                            pantryDB.child(ingredientId).setValue(ingredient)
                                    .addOnSuccessListener(
                                            aVoid -> isSaveSuccessful.postValue(true))
                                    .addOnFailureListener(
                                            e -> isSaveSuccessful.postValue(false));
                        }
                    }
                    if (!found) {
                        System.out.println("NOT FOUND");
                        pantryDB.push().setValue(ingredient);
                    }
                } else {
                    Log.d("TAG", "User ID not found in pantry");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error reading data from Firebase: " + error.getMessage());
            }
        });
    }


    public void readIngredientQuantities() {
        FirebaseDatabase ingDB = FirebaseDatabase
                .getInstance("https://recipeapp-1fba1-default-rtdb.firebaseio.com/");
        DatabaseReference pantryDB = ingDB.getReference().child("pantry/"
                + user.getUid());
        pantryDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Integer> currQty = new HashMap<>();
                for (DataSnapshot ingredient : snapshot.getChildren()) {
                    String name = ingredient.child("name").getValue(String.class);
                    int qty = ingredient.child("quantity").getValue(Integer.class);
                    currQty.put(name, qty);
                }
                ingQuantity.setValue(currQty);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error reading data from Firebase: " + error.getMessage());
            }
        });
    }
    public void readIngredients() {
        FirebaseDatabase ingDB = FirebaseDatabase
                .getInstance("https://recipeapp-1fba1-default-rtdb.firebaseio.com/");
        DatabaseReference pantryDB = ingDB.getReference().child("pantry/"
                + user.getUid());
        pantryDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String ingredientName = dataSnapshot.child("name").getValue(String.class);
                    Integer ingredientQuantity = dataSnapshot.child("quantity")
                            .getValue(Integer.class);
                    Integer calories = dataSnapshot.child("caloriesPerServing")
                            .getValue(Integer.class);
                    String expirationDate = dataSnapshot.child("expirationDate")
                            .getValue(String.class);
                    if (ingredientQuantity > 0) {
                        // ingredientList.add(ingredientName);
                        allIngredients.add(new Ingredient(ingredientName,
                                ingredientQuantity, calories, expirationDate));
                    }
                    Log.d("FirebaseData",
                            "Ingredient Name: " + ingredientName + ", Quantity: "
                                    + ingredientQuantity + ", Calories: "
                                    + calories + ", expirationDate: " + expirationDate);
                }
                // ingList.setValue(ingredientList);
                ingredientList.setValue(allIngredients);
            }
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error reading data from Firebase: " + error.getMessage());
            }
        });
    }

    public void updateQuantity(Ingredient toUpdate, Integer newQty) {

        Map<String, Object> updatedIngredient = new HashMap<>();
        updatedIngredient.put("name", toUpdate.getName());
        updatedIngredient.put("quantity", newQty);
        updatedIngredient.put("expirationDate", toUpdate.getExpirationDate());
        updatedIngredient.put("caloriesPerServing", toUpdate.getCaloriesPerServing());

        FirebaseDatabase ingDB = FirebaseDatabase
                .getInstance("https://recipeapp-1fba1-default-rtdb.firebaseio.com/");
        DatabaseReference pantryDB = ingDB.getReference().child("pantry/"
                + user.getUid());

        pantryDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if userId exists
                if (snapshot.exists()) {
                    for (DataSnapshot ingredientSnapshot : snapshot.getChildren()) {
                        // Access each ingredient under the userId
                        String ingredientId = ingredientSnapshot.getKey();
                        String ingredientName = ingredientSnapshot.child("name")
                                .getValue(String.class);

                        if (toUpdate.getName().equals(ingredientName)) {
                            Log.d("TAG", toUpdate.getName() + "found in userId: "
                                    + user.getUid() + ", ingredientId: " + ingredientId);
                            if (newQty <= 0) {
                                List<Ingredient> currIngredients = ingredientList.getValue();
                                pantryDB.child(ingredientId).removeValue()
                                        .addOnSuccessListener(
                                                aVoid -> isSaveSuccessful.postValue(true))
                                        .addOnFailureListener(
                                                e -> isSaveSuccessful.postValue(false));
                                System.out.println("value removed");
                                currIngredients.remove(toUpdate);
                                ingredientList.setValue(currIngredients);
                            } else {
                                pantryDB.child(ingredientId).setValue(updatedIngredient)
                                        .addOnSuccessListener(
                                                aVoid -> isSaveSuccessful.postValue(true))
                                        .addOnFailureListener(
                                                e -> isSaveSuccessful.postValue(false));
                            }
                        }
                    }
                } else {
                    Log.d("TAG", "User ID not found in pantry");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error reading data from Firebase: " + error.getMessage());
            }
        });


    }

    public void showDatePickerDialog(Context context, Button expDate) {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, year1, monthOfYear, day1) -> {
                    calendar.set(year1, monthOfYear, day1);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "dd-MM-yyyy", Locale.getDefault());
                    String formattedDate = dateFormat.format(calendar.getTime());
                    expDate.setText(formattedDate);
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }

}