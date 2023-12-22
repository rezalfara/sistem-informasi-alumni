package com.example.sisteminformasialumni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.AnimationTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
    private Alumni alumni;
    private List<Alumni> alumniList = new ArrayList<>();
    private String loggedInNPM;
    private RecyclerView rvBerita;
    private List<Berita> beritaList = new ArrayList<>();
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        loadAlumni();

        SharedPreferences sharedPreferences2 = getSharedPreferences("MyPrefs2", MODE_PRIVATE);
        loggedInNPM = sharedPreferences2.getString("npm", "0");

        Intent intent = getIntent();
        if (intent.hasExtra("alumni")){
            alumni = (Alumni) intent.getSerializableExtra("alumni");
            loggedInNPM = String.valueOf(alumni.getNpm());
        }

        ImageSlider imageSlider = findViewById(R.id.image_slider);

        bottomNavigationView = findViewById(R.id.bottom_navigation_alumni);
        // Set checked pada item pertama saat pertama kali dibuka
        bottomNavigationView.getMenu().findItem(R.id.action_page2).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.action_page1) {
                    // Find the Alumni object based on the logged-in npm
                    Alumni loggedInAlumni = findAlumniByNpm(loggedInNPM);

                    if (loggedInAlumni != null) {
                        // Buat Intent untuk pindah ke halaman EditProfile
                        Intent intent = new Intent(NewsActivity.this, MainActivityAlumni.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        // Mengirim objek Alumni
                        intent.putExtra("alumni", loggedInAlumni);

                        // Start activity dengan Intent
                        startActivity(intent);
                    } else {
                        // Handle case when Alumni object is not found
                        Toast.makeText(NewsActivity.this, "Alumni not found for npm: " + loggedInNPM, Toast.LENGTH_SHORT).show();
                    }
                }else if (itemId == R.id.action_page2) {
                    return false;
                } else if (itemId == R.id.action_page3) {
                    // Find the Alumni object based on the logged-in npm
                    Alumni loggedInAlumni = findAlumniByNpm(loggedInNPM);

                    if (loggedInAlumni != null) {
                        // Buat Intent untuk pindah ke halaman EditProfile
                        Intent intent = new Intent(NewsActivity.this, Profil.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        // Mengirim objek Alumni
                        intent.putExtra("alumni", loggedInAlumni);

                        // Start activity dengan Intent
                        startActivity(intent);
                    } else {
                        // Handle case when Alumni object is not found
                        Toast.makeText(NewsActivity.this, "Alumni not found for npm: " + loggedInNPM, Toast.LENGTH_SHORT).show();
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

    private Alumni findAlumniByNpm(String loggedInNPM) {
        for (Alumni alumni : alumniList) {
            if (String.valueOf(alumni.getNpm()).equals(loggedInNPM)) {
                return alumni;
            }
        }
        return null; // Alumni not found
    }

    private void loadAlumni() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Db_Contract.urlRead,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject alumni = array.getJSONObject(i);

                                //adding the product to product list
                                alumniList.add(new Alumni(
                                        alumni.getInt("id_alumni"),
                                        alumni.getInt("npm"),
                                        alumni.getString("password"),
                                        alumni.getString("nama"),
                                        alumni.getString("tempat_lahir"),
                                        alumni.getString("tgl_lahir"),
                                        alumni.getString("jk"),
                                        alumni.getString("email"),
                                        alumni.getString("no_hp"),
                                        alumni.getString("alamat"),
                                        alumni.getString("foto"),
                                        alumni.getInt("id_jurusan"),
                                        alumni.getInt("id_tahun_lulus")
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
                            NewsAdapter newsAdapter = new NewsAdapter(NewsActivity.this, beritaList);
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
                                    Intent intent = new Intent(NewsActivity.this, detailBeritaOnly.class);
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
}