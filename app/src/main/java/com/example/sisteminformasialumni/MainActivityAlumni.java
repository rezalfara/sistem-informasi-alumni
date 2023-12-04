package com.example.sisteminformasialumni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class MainActivityAlumni extends AppCompatActivity {
    private String loggedInNPM;
    private List<Alumni> alumniList;
    private List<Jurusan> jurusanList; // Daftar jurusan
    private List<Tahun_lulus> tahunLulusList; // Daftar tahun lulus
    private RecyclerView recyclerView;
    private AlumniAdapter alumniAdapter;
    BottomNavigationView bottomNavigationView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alumni);

        SharedPreferences sharedPreferences2 = getSharedPreferences("MyPrefs2", MODE_PRIVATE);
        loggedInNPM = sharedPreferences2.getString("npm", "0");

        Intent intent = getIntent();
        if (intent.hasExtra("alumni")){
            Alumni alumni = (Alumni) intent.getSerializableExtra("alumni");
            loggedInNPM = String.valueOf(alumni.getNpm());
        }

        // Tampilkan informasi dalam Toast
        Toast.makeText(getApplicationContext(), "NPM: " + loggedInNPM, Toast.LENGTH_SHORT).show();

        bottomNavigationView = findViewById(R.id.bottom_navigation_alumni);
        // Set checked pada item pertama saat pertama kali dibuka
        bottomNavigationView.getMenu().findItem(R.id.action_page1).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.action_page1) {
                    return false;
                } else if (itemId == R.id.action_page2) {
                    // Find the Alumni object based on the logged-in npm
                    Alumni loggedInAlumni = findAlumniByNpm(loggedInNPM);

                    if (loggedInAlumni != null) {
                        // Buat Intent untuk pindah ke halaman EditProfile
                        Intent intent = new Intent(MainActivityAlumni.this, Profil.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        // Mengirim objek Alumni
                        intent.putExtra("alumni", loggedInAlumni);

                        // Start activity dengan Intent
                        startActivity(intent);
                    } else {
                        // Handle case when Alumni object is not found
                        Toast.makeText(MainActivityAlumni.this, "Alumni not found for npm: " + loggedInNPM, Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
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

        alumniAdapter=new AlumniAdapter(this,alumniList, jurusanList, tahunLulusList);
        recyclerView.setAdapter(alumniAdapter);

        SearchView searchView=findViewById(R.id.searchView);
        searchView.setFocusable(false);
        searchView.setFocusableInTouchMode(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Show bottom navigation when submitting the search
                bottomNavigationView.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        // Initialize SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Set refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Clear the query in the SearchView
                searchView.setQuery("", false);
                // Collapse the SearchView to hide the keyboard
                searchView.clearFocus();
                // Call your method to refresh data
                refreshData();
            }
        });
    }

    private void refreshData() {
        // Clear existing data
        alumniList.clear();
        jurusanList.clear();
        tahunLulusList.clear();

        // Call your methods to reload data
        loadAlumni();
        loadJurusan();
        loadTahunLulus();

        // Notify the adapter that the data has changed
        alumniAdapter.notifyDataSetChanged();

        // Hide the refresh indicator
        swipeRefreshLayout.setRefreshing(false);
    }

    private void filterList(String text) {
        List<Alumni> namesFiltered = new ArrayList<>();

        for (Alumni alumni : alumniList) {
            if (alumni.getNama().toLowerCase().contains(text.toLowerCase())) {
                namesFiltered.add(alumni);
            }
        }

        if (namesFiltered.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            alumniAdapter.setFilteredData(namesFiltered);
            alumniAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(alumniAdapter);
        }
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