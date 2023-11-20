package com.example.sisteminformasialumni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EditProfile extends AppCompatActivity {
    private EditText etNpm, etNama, etTempatLahir, etEmail, etNoHp, etAlamat;
    private Alumni alumni;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Set checked pada item pertama saat pertama kali dibuka
        bottomNavigationView.getMenu().findItem(R.id.action_page3).setChecked(true);

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
                    // Tampilkan halaman kedua tanpa efek transisi
                    Intent intent = new Intent(getApplicationContext(), HelloWorldAlumni.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    // Mengirim objek Alumni
                    intent.putExtra("alumni", alumni);
                    startActivity(intent);
                    finish();
                } else if (itemId == R.id.action_page3) {
                    return false;
                }

                return false;
            }
        });

        etNpm = findViewById(R.id.etNpm);
        etNama = findViewById(R.id.etNama);
        etTempatLahir = findViewById(R.id.etTempatLahir);
        etEmail = findViewById(R.id.etEmail);
        etNoHp = findViewById(R.id.etPhone);
        etAlamat = findViewById(R.id.etAlamat);

        // Mendapatkan data dari Intent
        Intent intent = getIntent();
        if (intent != null) {
            // Mendapatkan nilai dari extra dengan kunci "alumni"
            alumni = (Alumni) intent.getSerializableExtra("alumni");

            // Menetapkan nilai ke elemen-elemen UI
            if (alumni != null) {
                etNpm.setText(String.valueOf(alumni.getNpm()));
                etNama.setText(alumni.getNama());
                etTempatLahir.setText(alumni.getTempat_lahir());
                etEmail.setText(alumni.getEmail());
                etNoHp.setText(alumni.getNo_hp());
                etAlamat.setText(alumni.getAlamat());
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

}