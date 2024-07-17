package com.example.recipeapp;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.recipeapp.viewmodels.PersonalInformationViewModel;
import com.example.recipeapp.views.WelcomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PersonalInformationFragment extends Fragment {

    private PersonalInformationViewModel mViewModel;
    private EditText editTextHeight;
    private EditText editTextWeight;
    private Spinner spinnerGender;
    private PersonalInformationViewModel viewModel;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public static PersonalInformationFragment newInstance() {
        return new PersonalInformationFragment();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextHeight = view.findViewById(R.id.editTextHeight);
        editTextWeight = view.findViewById(R.id.editTextWeight);
        spinnerGender = view.findViewById(R.id.spinnerGender);
        Button buttonSave = view.findViewById(R.id.buttonSave);
        Button buttonBack = view.findViewById(R.id.buttonBackToHome);
        viewModel = new ViewModelProvider(this).get(PersonalInformationViewModel.class);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        buttonSave.setOnClickListener(v -> savePersonalInformation());

        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), WelcomeActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal_information, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PersonalInformationViewModel.class);
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
        viewModel.getIsSaveSuccessful().observe(getViewLifecycleOwner(), isSuccessful -> {
            if (isSuccessful) {
                Toast.makeText(getContext(),
                        "Information saved successfully", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getContext(),
                        "Failed to save information", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

}