package com.example.sisteminformasialumni;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class detailBeritaOnly extends AppCompatActivity {
    private List<Berita> detailBeritaList;
    private RecyclerView rvDetailBerita;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berita);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //getting the recyclerview from xml
        rvDetailBerita = findViewById(R.id.rvDetailBerita);
        rvDetailBerita.setHasFixedSize(true);
        rvDetailBerita.setLayoutManager(new LinearLayoutManager(this));

//        //initializing the productlist
        detailBeritaList = new ArrayList<>();

        // Menerima data dari Intent
        Intent intent = getIntent();
        int beritaId = intent.getIntExtra("beritaId", 0); // 0 adalah nilai default jika data tidak ditemukan

        loadBeritaById(beritaId);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(detailBeritaOnly.this, NewsActivity.class));
        finish();
    }

    private void loadBeritaById(int beritaId) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Db_Contract.urlReadBeritaById+beritaId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parsing data JSON, karena sekarang responsenya adalah objek tunggal bukan array
                            JSONObject berita = new JSONObject(response);

                            // Mendapatkan data dari objek JSON
                            int id_berita = berita.getInt("id_berita");
                            String judul = berita.getString("judul");
                            String foto = berita.getString("foto");
                            String deskripsi = berita.getString("deskripsi");
                            String tanggalPosting = berita.getString("tgl_posting");

                            // Menambahkan data ke detailAlumniList
                            detailBeritaList.add(new Berita(
                                    id_berita, judul, foto, deskripsi, tanggalPosting
                            ));

                            // Membuat adapter dan mengaturnya ke recyclerview
                            detailBeritaOnlyAdapter detailBeritaOnlyAdapter = new detailBeritaOnlyAdapter(detailBeritaOnly.this, detailBeritaList);
                            rvDetailBerita.setAdapter(detailBeritaOnlyAdapter);

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
