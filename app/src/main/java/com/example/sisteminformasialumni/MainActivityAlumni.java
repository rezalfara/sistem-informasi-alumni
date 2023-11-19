package com.example.sisteminformasialumni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivityAlumni extends AppCompatActivity {
    private List<Alumni> alumniList;
    private List<Jurusan> jurusanList; // Daftar jurusan
    private List<Tahun_lulus> tahunLulusList; // Daftar tahun lulus
    private RecyclerView recyclerView;
    private AlumniAdapter alumniAdapter;
    Button btnEditData, btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alumni);

        SharedPreferences sharedPreferences2 = getSharedPreferences("MyPrefs2", MODE_PRIVATE);
        String loggedInNPM = sharedPreferences2.getString("npm", "0");

        // Tampilkan informasi dalam Toast
        Toast.makeText(getApplicationContext(), "NPM: " + loggedInNPM, Toast.LENGTH_SHORT).show();


        btnEditData = findViewById(R.id.btnEditData);
        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences2 = getSharedPreferences("MyPrefs2", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences2.edit();
                editor.remove("isLoggedInAlumni");
                editor.remove("npm"); // Hapus informasi pengguna jika diperlukan
                editor.apply();


                startActivity(new Intent(MainActivityAlumni.this, PilihLogin.class));
                finish();
            }
        });

        btnEditData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Find the Alumni object based on the logged-in npm
                Alumni loggedInAlumni = findAlumniByNpm(loggedInNPM);

                if (loggedInAlumni != null) {
                    // Buat Intent untuk pindah ke halaman EditProfile
                    Intent intent = new Intent(MainActivityAlumni.this, EditProfile.class);

                    // Tambahkan data tambahan ke Intent jika diperlukan
                    intent.putExtra("npm", loggedInNPM);

                    // Mengirim objek Alumni
                    intent.putExtra("alumni", loggedInAlumni);

                    // Start activity dengan Intent
                    startActivity(intent);
                } else {
                    // Handle case when Alumni object is not found
                    Toast.makeText(MainActivityAlumni.this, "Alumni not found for npm: " + loggedInNPM, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the productlist
        alumniList = new ArrayList<>();
        jurusanList = new ArrayList<>();
        tahunLulusList = new ArrayList<>();

        //this method will fetch and parse json
        //to display it in recyclerview
        loadAlumni();
        loadJurusan();
        loadTahunLulus();
    }

    private Alumni findAlumniByNpm(String loggedInNPM) {
        for (Alumni alumni : alumniList) {
            if (String.valueOf(alumni.getNpm()).equals(loggedInNPM)) {
                return alumni;
            }
        }
        return null; // Alumni not found
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
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

                            //creating adapter object and setting it to recyclerview
                            AlumniAdapter alumniAdapter = new AlumniAdapter(MainActivityAlumni.this, alumniList, jurusanList, tahunLulusList);
                            recyclerView.setAdapter(alumniAdapter);

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

    private void loadJurusan() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Db_Contract.urlGetJurusan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject jurusan = array.getJSONObject(i);

                                //adding the product to product list
                                jurusanList.add(new Jurusan(
                                        jurusan.getInt("id_jurusan"),
                                        jurusan.getString("nama_jurusan")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            AlumniAdapter alumniAdapter = new AlumniAdapter(MainActivityAlumni.this, alumniList, jurusanList, tahunLulusList);
                            recyclerView.setAdapter(alumniAdapter);

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

    private void loadTahunLulus() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Db_Contract.urlGetTahunLulus,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject tahun_lulus = array.getJSONObject(i);

                                //adding the product to product list
                                tahunLulusList.add(new Tahun_lulus(
                                        tahun_lulus.getInt("id_tahun_lulus"),
                                        tahun_lulus.getInt("tahun_lulus")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            AlumniAdapter alumniAdapter = new AlumniAdapter(MainActivityAlumni.this, alumniList, jurusanList, tahunLulusList);
                            recyclerView.setAdapter(alumniAdapter);

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