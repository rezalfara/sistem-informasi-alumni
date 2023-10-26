package com.example.sisteminformasialumni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

public class detailAlumni extends AppCompatActivity {

    private List<Alumni> detailAlumniList;
    private RecyclerView rvDetailAlumni;
    private detailAlumniAdapter detailAlumniAdapter;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_alumni);

        btnUpdate = findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(detailAlumni.this, Login.class));
                finish();
            }
        });

        //getting the recyclerview from xml
        rvDetailAlumni = findViewById(R.id.rvDetailAlumni);
        rvDetailAlumni.setHasFixedSize(true);
        rvDetailAlumni.setLayoutManager(new LinearLayoutManager(this));

//        //initializing the productlist
        detailAlumniList = new ArrayList<>();


        // Menerima data dari Intent
        Intent intent = getIntent();
        int alumniNpm = intent.getIntExtra("alumniNpm", 0); // 0 adalah nilai default jika data tidak ditemukan

        loadAlumniByNpm(alumniNpm);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(detailAlumni.this, MainActivity.class));
        finish();
    }

    private void loadAlumniByNpm(int alumniNpm) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Db_Contract.urlReadById+alumniNpm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parsing data JSON, karena sekarang responsenya adalah objek tunggal bukan array
                            JSONObject alumni = new JSONObject(response);

                            // Mendapatkan data dari objek JSON
                            int npm = alumni.getInt("npm");
                            String nama = alumni.getString("nama");
                            String tempatLahir = alumni.getString("tempat_lahir");
                            String tanggalLahir = alumni.getString("tgl_lahir");
                            String jenisKelamin = alumni.getString("jk");
                            String email = alumni.getString("email");
                            String noHp = alumni.getString("no_hp");
                            String alamat = alumni.getString("alamat");
                            String foto = alumni.getString("foto");
                            String jurusan = alumni.getString("nama_jurusan");
                            int tahunLulus = alumni.getInt("tahun_lulus");

                            // Menambahkan data ke detailAlumniList
                            detailAlumniList.add(new Alumni(
                                    npm, nama, tempatLahir, tanggalLahir, jenisKelamin, email, noHp, alamat, foto, jurusan, tahunLulus
                            ));

                            // Membuat adapter dan mengaturnya ke recyclerview
                            detailAlumniAdapter detailAlumniAdapter = new detailAlumniAdapter(detailAlumni.this, detailAlumniList);
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