package com.example.recipeapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.recipeapp.R;
import com.example.recipeapp.viewmodels.PersonalInformationViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.lifecycle.ViewModelProvider;

public class PersonalInformationActivity extends AppCompatActivity {
    private EditText editTextHeight;
    private EditText editTextWeight;
    private Spinner spinnerGender;
    private PersonalInformationViewModel viewModel;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_personal_information);

        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);
        spinnerGender = findViewById(R.id.spinnerGender);
        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonBack = findViewById(R.id.buttonBackToHome);
        viewModel = new ViewModelProvider(this).get(PersonalInformationViewModel.class);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        buttonSave.setOnClickListener(v -> savePersonalInformation());

        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(PersonalInformationActivity.this, WelcomeActivity.class);
            startActivity(intent);
        });
    }
    private void savePersonalInformation() {
        String height = editTextHeight.getText().toString().trim();
        String weight = editTextWeight.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        PersonalInformationViewModel p = new PersonalInformationViewModel();
        if (!p.validateHeight((height))) {
            editTextHeight.setError("Please enter your height");
            editTextHeight.requestFocus();
            return;
        }
        if (!p.validateWeight((weight))) {
            editTextWeight.setError("Please enter your height");
            editTextWeight.requestFocus();
            return;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            viewModel.savePersonalInformation(user.getEmail(), height, weight, gender);
        }
        viewModel.getIsSaveSuccessful().observe(this, isSuccessful -> {
            if (isSuccessful) {
                Toast.makeText(this, "Information saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save information", Toast.LENGTH_SHORT).show();
            }
        });
    }
}