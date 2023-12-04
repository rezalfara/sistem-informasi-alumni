package com.example.sisteminformasialumni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class detailAlumni extends AppCompatActivity {
    private List<Alumni> detailAlumniList;
    private List<Jurusan> jurusanList;
    private List<Tahun_lulus> tahunLulusList;
    private RecyclerView rvDetailAlumni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_alumni);

        //getting the recyclerview from xml
        rvDetailAlumni = findViewById(R.id.rvDetailAlumni);
        rvDetailAlumni.setHasFixedSize(true);
        rvDetailAlumni.setLayoutManager(new LinearLayoutManager(this));

//        //initializing the productlist
        detailAlumniList = new ArrayList<>();
        jurusanList = new ArrayList<>();
        tahunLulusList = new ArrayList<>();

        // Menerima data dari Intent
        Intent intent = getIntent();
        int alumniId = intent.getIntExtra("alumniId", 0); // 0 adalah nilai default jika data tidak ditemukan
        int jurusanId = intent.getIntExtra("jurusanId", 0); // 0 adalah nilai default jika data tidak ditemukan
        int tlId = intent.getIntExtra("tlId", 0); // 0 adalah nilai default jika data tidak ditemukan

        loadAlumniById(alumniId);
        loadJurusan(jurusanId);
        loadTahunLulus(tlId);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(detailAlumni.this, MainActivity.class));
        finish();
    }

    private void loadAlumniById(int alumniId) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Db_Contract.urlReadById+alumniId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parsing data JSON, karena sekarang responsenya adalah objek tunggal bukan array
                            JSONObject alumni = new JSONObject(response);

                            // Mendapatkan data dari objek JSON
                            int id_alumni = alumni.getInt("id_alumni");
                            int npm = alumni.getInt("npm");
                            String password = alumni.getString("password");
                            String nama = alumni.getString("nama");
                            String tempatLahir = alumni.getString("tempat_lahir");
                            String tanggalLahir = alumni.getString("tgl_lahir");
                            String jenisKelamin = alumni.getString("jk");
                            String email = alumni.getString("email");
                            String noHp = alumni.getString("no_hp");
                            String alamat = alumni.getString("alamat");
                            String foto = alumni.getString("foto");
                            int id_jurusan = alumni.getInt("id_jurusan");
                            int id_tahunLulus = alumni.getInt("id_tahun_lulus");

                            // Menambahkan data ke detailAlumniList
                            detailAlumniList.add(new Alumni(
                                    id_alumni, npm, password, nama, tempatLahir, tanggalLahir, jenisKelamin, email, noHp, alamat, foto, id_jurusan, id_tahunLulus
                            ));

                            // Membuat adapter dan mengaturnya ke recyclerview
                            detailAlumniAdapter detailAlumniAdapter = new detailAlumniAdapter(detailAlumni.this, detailAlumniList, jurusanList, tahunLulusList);
                            rvDetailAlumni.setAdapter(detailAlumniAdapter);

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

    private void loadJurusan(int jurusanId) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Db_Contract.urlGetJurusanById+jurusanId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parsing data JSON, karena sekarang responsenya adalah objek tunggal bukan array
                            JSONObject jurusan = new JSONObject(response);

                            // Mendapatkan data dari objek JSON
                            int id_jurusan = jurusan.getInt("id_jurusan");
                            String nama_jurusan = jurusan.getString("nama_jurusan");

                            // Menambahkan data ke detailAlumniList
                            jurusanList.add(new Jurusan(
                                    id_jurusan, nama_jurusan
                            ));

                            // Membuat adapter dan mengaturnya ke recyclerview
                            detailAlumniAdapter detailAlumniAdapter = new detailAlumniAdapter(detailAlumni.this, detailAlumniList, jurusanList, tahunLulusList);
                            rvDetailAlumni.setAdapter(detailAlumniAdapter);

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

    private void loadTahunLulus(int tlId) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Db_Contract.urlGetTahunLulusById+tlId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parsing data JSON, karena sekarang responsenya adalah objek tunggal bukan array
                            JSONObject tahun_lulus = new JSONObject(response);

                            // Mendapatkan data dari objek JSON
                            int id_tahun_lulus = tahun_lulus.getInt("id_tahun_lulus");
                            int nama_tahun_lulus = tahun_lulus.getInt("tahun_lulus");

                            // Menambahkan data ke detailAlumniList
                            tahunLulusList.add(new Tahun_lulus(
                                    id_tahun_lulus, nama_tahun_lulus
                            ));

                            // Membuat adapter dan mengaturnya ke recyclerview
                            detailAlumniAdapter detailAlumniAdapter = new detailAlumniAdapter(detailAlumni.this, detailAlumniList, jurusanList, tahunLulusList);
                            rvDetailAlumni.setAdapter(detailAlumniAdapter);

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