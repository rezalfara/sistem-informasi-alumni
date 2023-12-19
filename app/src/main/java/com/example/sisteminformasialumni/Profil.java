package com.example.sisteminformasialumni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profil extends AppCompatActivity {
    private Alumni alumni;
    private Alumni updatedAlumni;
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
        bottomNavigationView.getMenu().findItem(R.id.action_page3).setChecked(true);
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
                }else if (itemId == R.id.action_page2) {
                    // Tampilkan halaman kedua tanpa efek transisi
                    Intent intent = new Intent(Profil.this, NewsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("alumni", alumni);
                    startActivity(intent);
                } else if (itemId == R.id.action_page3) {
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
        if (intent!=null){
            // Mendapatkan nilai dari extra dengan kunci "alumni"
            alumni = (Alumni) intent.getSerializableExtra("alumni");
            String imgUrlUpdate = intent.getStringExtra("updatedFoto");
            String imgUrl = Db_Contract.pathImage + alumni.getFoto();

            if (alumni!=null){

                if (imgUrlUpdate!=null){
                    // Mendekode string base64 ke bitmap dan menampilkan di ImageView
                    Bitmap updatedBitmap = convertBase64ToBitmap(imgUrlUpdate);
                    // Menggunakan Glide untuk menampilkan gambar dari URL ke ImageView
                    Glide.with(this)
                            .load(updatedBitmap) // Gunakan gambar yang sudah didecode
                            .transform(new CircleCrop())
                            .into(ivAlumni);
                }else {
                    // Menggunakan Glide untuk menampilkan gambar dari URL ke ImageView
                    Glide.with(this)
                            .load(imgUrl)
                            .transform(new CircleCrop())
                            .into(ivAlumni);
                }

                // Set other data to UI elements
                tvNama.setText(alumni.getNama());

                ivAlumni.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create an Intent to open the FullscreenImageActivity
                        Intent intent = new Intent(Profil.this, FullScreenImage.class);

                        if (imgUrlUpdate!=null){
                            intent.putExtra("imgUrlUpdate", imgUrlUpdate);
                            Log.d("Profil", "imgUrlUpdate in Profil: " + imgUrlUpdate); // Tambahkan log ini
                        }else{
                            intent.putExtra("imgUrl", imgUrl);
                            Log.d("Profil", "imgUrl in Profil: " + imgUrl); // Tambahkan log ini
                        }

                        // Start the FullscreenImageActivity
                        startActivity(intent);
                    }
                });

                btnEditData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Profil.this, EditProfile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        if (imgUrlUpdate!=null){
                            intent.putExtra("imgUrlUpdate", imgUrlUpdate);
                        }
//
                        // Mengirim objek Alumni
                        intent.putExtra("alumni", alumni);
                        // Start activity dengan Intent
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

                btnChangePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Profil.this, ChangePassword.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        // Mengirim objek Alumni
                        intent.putExtra("alumni", alumni);
                        // Start activity dengan Intent
                        startActivity(intent);
                    }
                });

            }else {
                Toast.makeText(Profil.this, "Alumni tidak ditemukan", Toast.LENGTH_LONG).show();
            }
        }
    }

    private Bitmap convertBase64ToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}