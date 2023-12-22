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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsActivityAdmin extends AppCompatActivity {
    Button btnAdd;
    private Admin admin;
    private List<Admin> adminList = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    private RecyclerView rvBerita;
    private String loggedInUsername;
    private List<Berita> beritaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_admin);

        loadAdmin();

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewsActivityAdmin.this, tambahBerita.class));
                finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString("username", "0");

        Intent intent = getIntent();
        if (intent.hasExtra("admin")){
            admin = (Admin) intent.getSerializableExtra("admin");
            loggedInUsername = String.valueOf(admin.getUsername());
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Set checked pada item pertama saat pertama kali dibuka
        bottomNavigationView.getMenu().findItem(R.id.action_page3).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.action_page1) {
                    // Find the Admin object based on the logged-in username
                    Admin loggedInAdmin = findAdminByUsername(loggedInUsername);

                    if (loggedInAdmin != null) {
                        // Buat Intent untuk pindah ke halaman EditProfile
                        Intent intent = new Intent(NewsActivityAdmin.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        // Mengirim objek Admin
                        intent.putExtra("admin", loggedInAdmin);

                        // Start activity dengan Intent
                        startActivity(intent);
                    } else {
                        // Handle case when Alumni object is not found
                        Toast.makeText(NewsActivityAdmin.this, "Admin not found for username: " + loggedInUsername, Toast.LENGTH_SHORT).show();
                    }
                } else if (itemId == R.id.action_page2) {
                    // Find the Admin object based on the logged-in username
                    Admin loggedInAdmin = findAdminByUsername(loggedInUsername);

                    if (loggedInAdmin != null) {
                        // Buat Intent untuk pindah ke halaman EditProfile
                        Intent intent = new Intent(NewsActivityAdmin.this, HelloWorld.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        // Mengirim objek Admin
                        intent.putExtra("admin", loggedInAdmin);

                        // Start activity dengan Intent
                        startActivity(intent);
                    } else {
                        // Handle case when Alumni object is not found
                        Toast.makeText(NewsActivityAdmin.this, "Admin not found for username: " + loggedInUsername, Toast.LENGTH_SHORT).show();
                    }
                }else if (itemId == R.id.action_page3) {
                    return false;
                }else if (itemId == R.id.action_page4) {
                    // Find the Admin object based on the logged-in username
                    Admin loggedInAdmin = findAdminByUsername(loggedInUsername);

                    if (loggedInAdmin != null) {
                        // Buat Intent untuk pindah ke halaman EditProfile
                        Intent intent = new Intent(NewsActivityAdmin.this, ProfilAdmin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        // Mengirim objek Admin
                        intent.putExtra("admin", loggedInAdmin);

                        // Start activity dengan Intent
                        startActivity(intent);
                    } else {
                        // Handle case when Alumni object is not found
                        Toast.makeText(NewsActivityAdmin.this, "Admin not found for username: " + loggedInUsername, Toast.LENGTH_SHORT).show();
                    }
                }

                return false;
            }
        });

        //getting the recyclerview from xml
        rvBerita = findViewById(R.id.rvBerita);
        rvBerita.setHasFixedSize(true);
        rvBerita.setLayoutManager(new LinearLayoutManager(this));

        //initializing the productlist
        beritaList = new ArrayList<>();

        //this method will fetch and parse json
        //to display it in recyclerview
        loadBerita();

    }

    private void loadBerita() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Db_Contract.urlReadBerita,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject berita = array.getJSONObject(i);

                                //adding the product to product list
                                beritaList.add(new Berita(
                                        berita.getInt("id_berita"),
                                        berita.getString("judul"),
                                        berita.getString("foto"),
                                        berita.getString("deskripsi"),
                                        berita.getString("tgl_posting")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            NewsAdapter newsAdapter = new NewsAdapter(NewsActivityAdmin.this, beritaList);
                            rvBerita.setAdapter(newsAdapter);

                            // Image list for the ImageSlider
                            ArrayList<SlideModel> imageList = new ArrayList<>();
                            ImageSlider imageSlider = findViewById(R.id.image_slider);
                            // Assuming YourBeritaClass has getImageUrl() and getTitle() methods
                            for (Berita berita : beritaList) {
                                String imageUrl = Db_Contract.pathImageBerita + berita.getFoto();
                                String title = berita.getJudul();

                                // Assuming ScaleTypes.CENTER_CROP, change it based on your needs
                                imageList.add(new SlideModel(imageUrl, title, ScaleTypes.CENTER_INSIDE));
                            }
                            imageSlider.setImageList(imageList);

                            // Set ItemClickListener for ImageSlider
                            imageSlider.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void doubleClick(int i) {
                                    // Handle double click if needed
                                }

                                @Override
                                public void onItemSelected(int position) {
                                    // Pass the selected position to the NewsAdapter
                                    // Gunakan adapterPosition untuk mengakses elemen dengan benar
                                    Berita selectedBerita = beritaList.get(position);
                                    int beritaId = selectedBerita.getId_berita();

                                    // Menggunakan Intent untuk memanggil class lain
                                    Intent intent = new Intent(NewsActivityAdmin.this, detailBerita.class);
                                    intent.putExtra("beritaId", beritaId);
                                    startActivity(intent);

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private Admin findAdminByUsername(String loggedInUsername) {
        for (Admin admin : adminList) {
            if (admin.getUsername().equals(loggedInUsername)) {
                return admin;
            }
        }
        return null; // Alumni not found
    }

    private void loadAdmin() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Db_Contract.urlReadAdmin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject admin = array.getJSONObject(i);

                                //adding the product to product list
                                adminList.add(new Admin(
                                        admin.getInt("id_admin"),
                                        admin.getString("username"),
                                        admin.getString("nama"),
                                        admin.getString("password"),
                                        admin.getString("alamat"),
                                        admin.getString("foto")
                                ));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}