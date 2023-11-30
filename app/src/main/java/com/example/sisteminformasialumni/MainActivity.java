package com.example.sisteminformasialumni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class MainActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private List<Alumni> alumniList;
    private List<Admin> adminList;
    private List<Jurusan> jurusanList; // Daftar jurusan
    private List<Tahun_lulus> tahunLulusList; // Daftar tahun lulus
    private RecyclerView recyclerView;
    private AlumniAdapter alumniAdapter;
    Button btnCreate, btnLogout;
    BottomNavigationView bottomNavigationView;
    // Declare SwipeRefreshLayout
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String loggedInUsername = sharedPreferences.getString("username", "0");

        // Tampilkan informasi dalam Toast
        Toast.makeText(getApplicationContext(), "Username: " + loggedInUsername, Toast.LENGTH_SHORT).show();

        btnCreate = findViewById(R.id.btnAdd);
        btnLogout = findViewById(R.id.btnLogout);

        adminList = new ArrayList<>();
        loadAdmin();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Set checked pada item pertama saat pertama kali dibuka
        bottomNavigationView.getMenu().findItem(R.id.action_page1).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.action_page1) {
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                    finish();
                    return false;
                } else if (itemId == R.id.action_page2) {
                    // Tampilkan halaman kedua tanpa efek transisi
                    Intent intent = new Intent(getApplicationContext(), HelloWorld.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                }else if (itemId == R.id.action_page3) {
                    // Find the Alumni object based on the logged-in npm
                    Admin loggedInAdmin = findAdminByUsername(loggedInUsername);

                    if (loggedInAdmin != null) {
                        // Buat Intent untuk pindah ke halaman EditProfile
                        Intent intent = new Intent(MainActivity.this, ProfilAdmin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        // Mengirim objek Alumni
                        intent.putExtra("admin", loggedInAdmin);

                        // Start activity dengan Intent
                        startActivity(intent);
                    } else {
                        // Handle case when Alumni object is not found
                        Toast.makeText(MainActivity.this, "Admin not found for username: " + loggedInUsername, Toast.LENGTH_SHORT).show();
                    }
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


                startActivity(new Intent(MainActivity.this, PilihLogin.class));
                finish();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, tambahAlumni.class));
                finish();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void loadAlumni() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
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
                            AlumniAdapter alumniAdapter = new AlumniAdapter(MainActivity.this, alumniList, jurusanList, tahunLulusList);
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

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
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
                            AlumniAdapter alumniAdapter = new AlumniAdapter(MainActivity.this, alumniList, jurusanList, tahunLulusList);
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

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
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
                            AlumniAdapter alumniAdapter = new AlumniAdapter(MainActivity.this, alumniList, jurusanList, tahunLulusList);
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