package com.example.recipeapp;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.recipeapp.viewmodels.MealViewModel;
import com.example.recipeapp.viewmodels.PersonalInformationViewModel;

//imports necessary to use AnyChart
import com.anychart.AnyChartView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class InputMealFragment extends Fragment {
    public InputMealFragment newInstance() {
        return new InputMealFragment();
    }

    private EditText editTextHeight;
    private EditText editTextWeight;
    private Spinner spinnerGender;
    private Button mealDate;
    private EditText calories;
    private EditText mealName;
    private AnyChartView dataVisual1;
    private Calendar calendar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_meal, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mealName = view.findViewById(R.id.mealName);
        calories = view.findViewById(R.id.calorieCount);
        mealDate = view.findViewById(R.id.mealDate);
        //dataVisual1 (displaying user's avg daily calorie intake for current month)
        dataVisual1 = (AnyChartView) view.findViewById(R.id.data_visual_1);
        calendar = Calendar.getInstance();
        MealViewModel vm = new MealViewModel();
        PersonalInformationViewModel userVM = new PersonalInformationViewModel();
        TextView defaultText = view.findViewById(R.id.defaultText);
        TextView heightText = view.findViewById(R.id.heightText);
        TextView weightText = view.findViewById(R.id.weightText);
        TextView genderText = view.findViewById(R.id.genderText);
        TextView goalText = view.findViewById(R.id.calorieGoalText);
        TextView dailyCalorieText = view.findViewById(R.id.dailyCalorieText);


        //Height Observer
        userVM.getHeight().observe(getViewLifecycleOwner(), height -> {
            if (height != null && !(height.isEmpty())) {
                defaultText.setVisibility(View.GONE);
                heightText.setVisibility(View.VISIBLE);
                String heightMessage = "Height: " + height + " cm";
                heightText.setText(heightMessage);
                calorieCalc(heightText, weightText, genderText, goalText);
            }
        });

        //Weight Observer
        userVM.getWeight().observe(getViewLifecycleOwner(), weight -> {
            if (weight != null && !(weight.isEmpty())) {
                defaultText.setVisibility(View.GONE);
                weightText.setVisibility(View.VISIBLE);
                String weightMessage = "Weight: " + weight + " kg";
                weightText.setText(weightMessage);
                calorieCalc(heightText, weightText, genderText, goalText);
            }
        });

        //Gender Observer
        userVM.getGender().observe(getViewLifecycleOwner(), gender -> {
            if (gender != null && !(gender.isEmpty())) {
                defaultText.setVisibility(View.GONE);
                genderText.setVisibility(View.VISIBLE);
                String genderMessage = "Gender: " + gender;
                genderText.setText(genderMessage);
                calorieCalc(heightText, weightText, genderText, goalText);
            }
        });

        //Daily Calorie Count Observer
        vm.getDailyCount().observe(getViewLifecycleOwner(), count -> {
            dailyCalorieText.setText("Calories Inputted Today: " + count);
        });

        userVM.readUserInfo();
        vm.readDailyMeals();
        mealDate.setOnClickListener(v -> showDatePickerDialog());

        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);

        Button button1 = view.findViewById(R.id.button1);
        Button button2 = view.findViewById(R.id.button2);

        ArrayList<String> dataKeys = new ArrayList<>();
        ArrayList<Integer> dataValues = new ArrayList<>();
        vm.getData().observe(getViewLifecycleOwner(), info -> {
            for (HashMap.Entry<String, Integer> element : info.entrySet()) {
                dataKeys.add(element.getKey());
                dataValues.add(element.getValue());
            }
        });

        HashMap<String, Integer> data = new HashMap<>();
        ArrayList<Integer> calorieList = new ArrayList<>();
        vm.readMeals(data, calorieList);

        Button input = view.findViewById(R.id.inputButton);
        input.setOnClickListener(v -> {
            HashMap<String, Integer> dataList = new HashMap<>();
            ArrayList<Integer> cals = new ArrayList<>();
            vm.inputMeal(requireContext(), mealName, calories, mealDate);
            vm.readMeals(dataList, cals);
        });

        // Set click listener for Button 1 for data visual 1
        button1.setOnClickListener(v -> {
            try {
                Intent intent1 = new Intent(getActivity(), Chart1Activity.class);
                intent1.putStringArrayListExtra("dataKeys", dataKeys);
                intent1.putIntegerArrayListExtra("dataValues", dataValues);
                startActivity(intent1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Set click listener for Button 2 for data visual 2
        button2.setOnClickListener(v -> {
            try {
                Intent intent2 = new Intent(getActivity(), Chart2Activity.class);
                intent2.putIntegerArrayListExtra("calorieList", calorieList);
                startActivity(intent2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //opens up a calendar and allows user to select which date to input meal for
    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, monthOfYear, day1) -> {
                    calendar.set(year1, monthOfYear, day1);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "dd-MM-yyyy", Locale.getDefault());
                    String formattedDate = dateFormat.format(calendar.getTime());
                    mealDate.setText(formattedDate);
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }


    private void calorieCalc(TextView heightText, TextView weightText,
                             TextView genderText, TextView goalText) {
        //The equation that we will be using to calculate calorie goal is the Mifflin-St Jeor
        //formula: https://www.calculator.net/calorie-calculator.html
        //The equation for men is BMR = 10W + 6.25H - 5A + 5 and
        // BMR = 10W + 6.25H - 5A - 161 for women
        // where W represents weight, H represents height,and A represents age.
        // Since we are not storing age,
        // we will use the average age in the
        // US in 2022 of 38.9 years. This statistic was reported by the U.S. Census Bureau:
        // https://www.census.gov/newsroom/press-releases/2023/population-estimates-characteristics.html
        // The number of calories will be for a person at rest to
        // maintain their current weight. Note: Most people will not be at rest the entire day.
        // The calculator states than the result is multiplied by a value between 1.2 and 1.95
        // to represent a person's activity level. For this calculation, we will just multiply
        // by 1.5 which falls in the range.
        // Since we are storing calories as ints, we will be rounded the final calculation up to
        // the nearest int.
        if (heightText.getVisibility() == View.VISIBLE && weightText.getVisibility() == View.VISIBLE
                && genderText.getVisibility() == View.VISIBLE) {
            String heightTotal = (String) heightText.getText();
            String[] parsedHeight = heightTotal.split(" ");
            double height = Double.parseDouble(parsedHeight[1]);
            String weightTotal = (String) weightText.getText();
            String[] parsedWeight = weightTotal.split(" ");
            double weight = Double.parseDouble(parsedWeight[1]);
            String genderTotal = (String) genderText.getText();
            String[] parsedGender = genderTotal.split(" ");
            String gender = parsedGender[1];
            int finalCalories = calorieEquation(height, weight, gender);
            goalText.setVisibility(View.VISIBLE);
            goalText.setText("Min Daily Calorie Goal: " + finalCalories);
        }
    }

    public int calorieEquation(double height, double weight, String gender) {
        double calories = (10 * weight) + (6.25 * height) - (5 * 38.9);
        if (gender.equals("Male")) {
            calories = calories + 5;
        } else if (gender.equals("Female")) {
            calories = calories - 161;
        }
        int finalCalories = (int) Math.ceil(calories);
        return (finalCalories);
    }
}
