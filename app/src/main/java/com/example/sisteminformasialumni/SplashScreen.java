package com.example.sisteminformasialumni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(getApplicationContext(), Login.class));
                if (isLoggedIn) {
                    // Pengguna sudah login, arahkan ke MainActivity
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    // Pengguna belum login, arahkan ke Login
                    startActivity(new Intent(getApplicationContext(), Login.class));
                }
                finish();
            }
        }, 1000L); //3000 L = 3 detik
    }
}