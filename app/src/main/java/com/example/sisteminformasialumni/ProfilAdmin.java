package com.example.sisteminformasialumni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class ProfilAdmin extends AppCompatActivity {
    private Admin admin;
    TextView tvNama;
    ImageView ivAdmin;
    Button btnEditData, btnChangePassword, btnLogout;
    BottomNavigationView bottomNavigationView;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_admin);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Set checked pada item pertama saat pertama kali dibuka
        bottomNavigationView.getMenu().findItem(R.id.action_page4).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.action_page1) {
                    // Tampilkan halaman kedua tanpa efek transisi
                    Intent intent = new Intent(ProfilAdmin.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("admin", admin);
                    startActivity(intent);
                } else if (itemId == R.id.action_page2) {
                    // Tampilkan halaman kedua tanpa efek transisi
                    Intent intent = new Intent(ProfilAdmin.this, HelloWorld.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("admin", admin);
                    startActivity(intent);
                } else if (itemId == R.id.action_page3) {
                    // Tampilkan halaman kedua tanpa efek transisi
                    Intent intent = new Intent(ProfilAdmin.this, NewsActivityAdmin.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("admin", admin);
                    startActivity(intent);
                } else if (itemId == R.id.action_page4) {
                    return false;
                }

                return false;
            }
        });

        tvNama = findViewById(R.id.tvNama);
        ivAdmin = findViewById(R.id.ivAdmin);
        btnEditData = findViewById(R.id.btnEditData);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnLogout = findViewById(R.id.btnLogout);
        RefreshData();

        // Initialize SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Set refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshData();
                // Hide the refresh indicator
                swipeRefreshLayout.setRefreshing(false);
            }

        });

    }

    private void RefreshData() {
        // Mendapatkan data dari Intent
        Intent intent = getIntent();
        if (intent!=null){
            // Mendapatkan nilai dari extra dengan kunci "alumni"
            admin = (Admin) intent.getSerializableExtra("admin");
            Log.d("ProfilAdmin", "Admin data from intent: " + admin);
            String imgUrlUpdate = intent.getStringExtra("updatedFoto");
            String imgUrl = Db_Contract.pathImageAdmin + admin.getFoto();

            if (admin!=null){

                if (imgUrlUpdate!=null){
                    // Mendekode string base64 ke bitmap dan menampilkan di ImageView
                    Bitmap updatedBitmap = convertBase64ToBitmap(imgUrlUpdate);
                    // Menggunakan Glide untuk menampilkan gambar dari URL ke ImageView
                    Glide.with(getApplicationContext())
                            .load(updatedBitmap) // Gunakan gambar yang sudah didecode
                            .transform(new CircleCrop())
                            .into(ivAdmin);
                }else {
                    // Menggunakan Glide untuk menampilkan gambar dari URL ke ImageView
                    Glide.with(getApplicationContext())
                            .load(imgUrl)
                            .transform(new CircleCrop())
                            .into(ivAdmin);
                }

                // Set other data to UI elements
                tvNama.setText(admin.getNama());

                ivAdmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create an Intent to open the FullscreenImageActivity
                        Intent intent = new Intent(ProfilAdmin.this, FullScreenImage.class);

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
                        Intent intent = new Intent(ProfilAdmin.this, EditProfilAdmin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        if (imgUrlUpdate!=null){
                            intent.putExtra("imgUrlUpdate", imgUrlUpdate);
                        }

                        // Mengirim objek Alumni
                        intent.putExtra("admin", admin);
                        // Start activity dengan Intent
                        startActivity(intent);

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


                        startActivity(new Intent(ProfilAdmin.this, PilihLogin.class));
                        finish();
                    }
                });

                btnChangePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProfilAdmin.this, ChangePasswordAdmin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        // Mengirim objek Alumni
                        intent.putExtra("admin", admin);
                        // Start activity dengan Intent
                        startActivity(intent);
                    }
                });

            }else {
                Toast.makeText(ProfilAdmin.this, "Admin tidak ditemukan", Toast.LENGTH_LONG).show();
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