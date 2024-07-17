package com.example.recipeapp.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeapp.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen); // Set the layout for your splash screen

        // Use a handler to navigate to the LoginActivity after a certain delay
        new android.os.Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Finish the SplashActivity to prevent going back to it
        }, 2000); // Set the delay time in milliseconds (e.g., 2000ms or 2 seconds)
    }
}
