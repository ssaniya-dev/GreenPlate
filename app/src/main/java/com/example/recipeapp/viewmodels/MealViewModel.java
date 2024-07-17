package com.example.recipeapp.viewmodels;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anychart.chart.common.dataentry.DataEntry;

import com.example.recipeapp.model.Meal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MealViewModel {
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private List<Integer> currentCals;
    private List<DataEntry> plotData;
    private String currMonthName;
    private Calendar calendar;

    private MutableLiveData<HashMap<String, Integer>> hm = new MutableLiveData<>(new HashMap<>());
    public LiveData<HashMap<String, Integer>> getData() {
        return hm;
    }
    private MutableLiveData<Integer> dailyCount = new MutableLiveData<>();

    public boolean validName(String mealName) {
        return !mealName.isEmpty();
    }
    public boolean validCalCount(String calories) {
        return !calories.isEmpty();
    }

    public void inputMeal(Context context, EditText mealName,
                          EditText calories, Button mealDate) {
        String nameOfMeal = mealName.getText().toString();
        String cals = calories.getText().toString();
        String date = mealDate.getText().toString();
        if (nameOfMeal.isEmpty()) {
            mealName.setError("Please enter the name of your meal!");
        } else if (cals.isEmpty()) {
            calories.setError("Please enter the amount of calories in your meal!");
        } else if (date.isEmpty()) {
            mealDate.setError("Please enter when you had your meal!");
        } else {
            mealName.setError(null);
            calories.setError(null);
            mealDate.setError(null);
            Meal myMeal = new Meal(nameOfMeal, cals, date);
            FirebaseDatabase database = FirebaseDatabase
                    .getInstance("https://recipeapp-1fba1-default-rtdb.firebaseio.com/");
            DatabaseReference mealsref = database.getReference().child("meals/"
                    + user.getUid());

            mealsref.push().setValue(myMeal)
                    .addOnSuccessListener(success -> {
                        Toast.makeText(context,
                                "Meal inputted successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(failure -> {
                        Toast.makeText(context,
                                "Could not input meal", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    public void inputMeal(Context context, String mealName,
                          String calories, String mealDate) {
        Meal myMeal = new Meal(mealName, calories, mealDate);
        FirebaseDatabase database = FirebaseDatabase
                .getInstance("https://recipeapp-1fba1-default-rtdb.firebaseio.com/");
        DatabaseReference mealsref = database.getReference().child("meals/"
                + user.getUid());

        mealsref.push().setValue(myMeal)
                .addOnSuccessListener(success -> {
                    Toast.makeText(context,
                            "Meal inputted successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(failure -> {
                    Toast.makeText(context,
                            "Could not input meal", Toast.LENGTH_SHORT).show();
                });
    }

    public void readMeals(HashMap<String, Integer> data, ArrayList<Integer> calorieList) {
        FirebaseDatabase database = FirebaseDatabase
                .getInstance("https://recipeapp-1fba1-default-rtdb.firebaseio.com/");
        DatabaseReference mealsref = database.getReference().child("meals/"
                + user.getUid());

        //get current month
        calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;

        //get current day
        int currentDay = calendar.get(Calendar.DATE);

        mealsref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String mealName = dataSnapshot.child("mealName").getValue(String.class);
                    String calories = dataSnapshot.child("calories").getValue(String.class);
                    String date = dataSnapshot.child("date").getValue(String.class);

                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        assert date != null;
                        Date newDate = dateFormat.parse(date);

                        // Extract month
                        assert newDate != null;
                        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                        String month = monthFormat.format(newDate);
                        int intMonth = Integer.parseInt(month);

                        // Extract day
                        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                        String day = dayFormat.format(newDate);
                        int intDay = Integer.parseInt(day);

                        assert calories != null;
                        int intCalories = Integer.parseInt(calories);

                        if (intMonth == currentMonth) {
                            if (data.containsKey(day)) {
                                int newCal = data.get(day) + intCalories;
                                data.put(day, newCal);
                            } else {
                                data.put(day, intCalories);
                            }
                        }

                        if (intDay == currentDay) {
                            calorieList.add(intCalories);
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("FirebaseData",
                            "Meal: " + mealName + ", Calories: " + calories + ", Date: " + date);
                }
                hm.setValue(data);
            }
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error reading data from Firebase: " + error.getMessage());
            }
        });
    }

    public LiveData<Integer> getDailyCount() {
        return dailyCount;
    }

    public void readDailyMeals() {
        FirebaseDatabase database = FirebaseDatabase
                .getInstance("https://recipeapp-1fba1-default-rtdb.firebaseio.com/");
        DatabaseReference mealsRef = database.getReference().child("meals/"
                + user.getUid());
        mealsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int calorieCount = 0;
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "dd-MM-yyyy", Locale.getDefault());
                Calendar calendar = Calendar.getInstance();
                String today = dateFormat.format(calendar.getTime());
                for (DataSnapshot meal: snapshot.getChildren()) {
                    if (today.equals(meal.child("date").getValue(String.class))) {
                        String calorieString = meal.child("calories").getValue(String.class);
                        calorieCount = calorieCount + Integer.parseInt(calorieString);
                    }
                }
                dailyCount.setValue(calorieCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error reading data from Firebase: " + error.getMessage());
            }
        });
    }

    private void updateChart1() {


    }
}