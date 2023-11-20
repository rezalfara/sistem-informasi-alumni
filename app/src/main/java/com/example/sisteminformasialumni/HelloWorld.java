package com.example.sisteminformasialumni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HelloWorld extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_world);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Set checked pada item pertama saat pertama kali dibuka
        bottomNavigationView.getMenu().findItem(R.id.action_page2).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.action_page1) {
                    // Tampilkan halaman kedua tanpa efek transisi
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                } else if (itemId == R.id.action_page2) {
                    return false;
                } else if (itemId == R.id.action_page3) {
                    // Tampilkan halaman kedua tanpa efek transisi
                    Intent intent = new Intent(getApplicationContext(), HelloWorld.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    bottomNavigationView.getMenu().findItem(R.id.action_page3).setChecked(true);
                    finish();
                }

                return false;
            }
        });
    }
}