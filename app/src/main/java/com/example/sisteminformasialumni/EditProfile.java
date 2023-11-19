package com.example.sisteminformasialumni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class EditProfile extends AppCompatActivity {
    private EditText etNpm, etNama, etTempatLahir, etEmail, etNoHp, etAlamat;
    private Alumni alumni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

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

}