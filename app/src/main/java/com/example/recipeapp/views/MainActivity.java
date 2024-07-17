package com.example.recipeapp.views;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.example.recipeapp.R;


public class MainActivity extends AppCompatActivity {

    //    @Override
    //    protected void onCreate(Bundle savedInstanceState) {
    //        super.onCreate(savedInstanceState);
    //        setContentView(R.layout.splash_screen);
    //    }

    /**
     * Creates the instance of the splash screen to be viewed before the app actually launches
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>
     *           Note: Otherwise it is null.
     *     </i></b>
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen); // Set the layout for your splash screen

        // Use a handler to navigate to the LoginActivity after a certain delay
        new android.os.Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Finish the SplashActivity to prevent going back to it
        }, 2000); // Set the delay time in milliseconds (e.g., 2000ms or 2 seconds)
    }
}