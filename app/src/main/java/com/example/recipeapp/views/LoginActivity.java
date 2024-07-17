package com.example.recipeapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.recipeapp.viewmodels.LoginViewModel;

import com.example.recipeapp.R;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmail;
    private EditText loginPassword;
    private final LoginViewModel loginViewModel = new LoginViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.emailUsername);
        loginPassword = findViewById(R.id.editTextTextPassword);
        Button loginButton = findViewById(R.id.signinButton);
        Button createAccountButton = findViewById(R.id.accountCreation);
        Button exitButton = findViewById(R.id.exitApp);

        observe();

        loginButton.setOnClickListener(view -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();
            loginViewModel.login(email, password);
        });

        createAccountButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        exitButton.setOnClickListener(view -> finishAffinity());
    }
    private void observe() {
        loginViewModel.getLoginSuccess().observe(this, isSuccess -> {
            if (isSuccess) {
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
                finish();
            }
        });

        loginViewModel.getLoginError().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
