package com.example.recipeapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recipeapp.R;
import com.example.recipeapp.viewmodels.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity {
    private EditText signupEmail;
    private EditText signupPassword;
    private final SignUpViewModel signUpViewModel = new SignUpViewModel();
    // private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupEmail = findViewById(R.id.enterUsername);
        signupPassword = findViewById(R.id.enterPassword);
        Button signupButton = findViewById(R.id.createAccount);

        observe();

        signupButton.setOnClickListener(v -> {
            String email = signupEmail.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();
            signUpViewModel.signUp(email, password);
        });

    }
    private void observe() {
        signUpViewModel.getSignUpSuccess().observe(this, isSuccess -> {
            if (isSuccess) {
                Toast.makeText(SignUpActivity.this,
                        "Sign Up Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        signUpViewModel.getSignUpError().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(SignUpActivity.this,
                        "Sign Up Failed: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
