package com.example.sisteminformasialumni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HelloWorldAlumni extends AppCompatActivity {
    private Alumni alumni;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_world_alumni);

        // Mendapatkan data dari Intent
        Intent intent = getIntent();
        if (intent != null) {
            // Mendapatkan nilai dari extra dengan kunci "alumni"
            alumni = (Alumni) intent.getSerializableExtra("alumni");

        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Set checked pada item pertama saat pertama kali dibuka
        bottomNavigationView.getMenu().findItem(R.id.action_page2).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.action_page1) {
                    // Tampilkan halaman kedua tanpa efek transisi
                    Intent intent = new Intent(getApplicationContext(), MainActivityAlumni.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                } else if (itemId == R.id.action_page2) {
                    return false;
                } else if (itemId == R.id.action_page3) {
                    Intent intent = new Intent(getApplicationContext(), EditProfile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    // Mengirim objek Alumni
                    intent.putExtra("alumni", alumni);
                    // Start activity dengan Intent
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}