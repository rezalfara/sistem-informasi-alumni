package com.example.sisteminformasialumni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HelloWorld extends AppCompatActivity {
    Button btnLogout;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_world);

        btnLogout = findViewById(R.id.btnLogout);

        Intent intent = getIntent();
        if (intent.hasExtra("admin")) {
            Admin admin = (Admin) intent.getSerializableExtra("admin");

            ////////
            bottomNavigationView = findViewById(R.id.bottom_navigation);
            // Set checked pada item pertama saat pertama kali dibuka
            bottomNavigationView.getMenu().findItem(R.id.action_page2).setChecked(true);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();

                    if (itemId == R.id.action_page1) {
                        // Tampilkan halaman kedua tanpa efek transisi
                        Intent intent = new Intent(HelloWorld.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("admin", admin);
                        startActivity(intent);
                    } else if (itemId == R.id.action_page2) {
                        return false;
                    } else if (itemId == R.id.action_page3) {
                        // Tampilkan halaman kedua tanpa efek transisi
                        Intent intent = new Intent(HelloWorld.this, ProfilAdmin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("admin", admin);
                        startActivity(intent);
                    }

                    return false;
                }
            });

            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("isLoggedIn");
                    editor.remove("username"); // Hapus informasi pengguna jika diperlukan
                    editor.apply();


                    startActivity(new Intent(HelloWorld.this, PilihLogin.class));
                    finish();
                }
            });

        }
    }

}