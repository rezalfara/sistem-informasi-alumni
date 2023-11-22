package com.example.sisteminformasialumni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profil extends AppCompatActivity {
    private Alumni alumni;
    TextView tvNama;
    ImageView ivAlumni;
    Button btnEditData, btnChangePassword, btnLogout;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        bottomNavigationView = findViewById(R.id.bottom_navigation_alumni);
        // Set checked pada item pertama saat pertama kali dibuka
        bottomNavigationView.getMenu().findItem(R.id.action_page2).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.action_page1) {
                    // Tampilkan halaman kedua tanpa efek transisi
                    Intent intent = new Intent(Profil.this, MainActivityAlumni.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("alumni", alumni);
                    startActivity(intent);
                    finish();
                } else if (itemId == R.id.action_page2) {
                    return false;
                }

                return false;
            }
        });


        tvNama = findViewById(R.id.tvNama);
        ivAlumni = findViewById(R.id.ivAlumni);
        btnEditData = findViewById(R.id.btnEditData);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnLogout = findViewById(R.id.btnLogout);

        // Mendapatkan data dari Intent
        Intent intent = getIntent();
        if (intent != null) {
            // Mendapatkan nilai dari extra dengan kunci "alumni"
            alumni = (Alumni) intent.getSerializableExtra("alumni");


            // Menetapkan nilai ke elemen-elemen UI
            if (alumni != null) {
                String imgUrl = Db_Contract.pathImage+alumni.getFoto();
                if (imgUrl != null) {
                    //loading the image
                    Glide.with(this)
                            .load(imgUrl)
                            .transform(new CircleCrop())
                            .into(ivAlumni);
                }
                tvNama.setText(alumni.getNama());
            }
        }

        ivAlumni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgUrl = Db_Contract.pathImage+alumni.getFoto();
                // Create an Intent to open the FullscreenImageActivity
                Intent intent = new Intent(Profil.this, FullScreenImage.class);

                // Pass the image URL to the FullscreenImageActivity
                intent.putExtra("imgUrl", imgUrl);

                // Start the FullscreenImageActivity
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences2 = getSharedPreferences("MyPrefs2", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences2.edit();
                editor.remove("isLoggedInAlumni");
                editor.remove("npm"); // Hapus informasi pengguna jika diperlukan
                editor.apply();


                startActivity(new Intent(Profil.this, PilihLogin.class));
                finish();
            }
        });

        btnEditData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profil.this, EditProfile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                // Mengirim objek Alumni
                intent.putExtra("alumni", alumni);
                // Start activity dengan Intent
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}