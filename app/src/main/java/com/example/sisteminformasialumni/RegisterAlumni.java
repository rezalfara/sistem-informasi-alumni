package com.example.sisteminformasialumni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterAlumni extends AppCompatActivity {
    private EditText etNpm, etPassword;
    private Spinner spinnerJurusan, spinnerTL;
    private Button btnRegisterAlumni;
    private List<Jurusan> jurusanList = new ArrayList<>();
    private List<Tahun_lulus> tahunLulusList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_alumni);

        etNpm = findViewById(R.id.etNpm);
        etPassword = findViewById(R.id.etPassword);
        spinnerTL = findViewById(R.id.spinnerTL);
        spinnerJurusan = findViewById(R.id.spinnerJurusan);
        btnRegisterAlumni = findViewById(R.id.btnRegisterAlumni);

        // Mengambil data jurusan dari database
        fetchDataTahunLulus();
        fetchDataJurusan();

        btnRegisterAlumni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TahunLulus
                String selectedTL = spinnerTL.getSelectedItem().toString();
                String id_tahun_lulus = String.valueOf(getIdTl(Integer.parseInt(selectedTL)));

                //Jurusan
                String selectedJurusan = spinnerJurusan.getSelectedItem().toString();
                String id_jurusan = String.valueOf(getIdJurusan(selectedJurusan));

                String npm = etNpm.getText().toString();
                String password = etPassword.getText().toString();

                if (npm != null && !npm.isEmpty() &&
                        password != null && !password.isEmpty() &&
                        id_jurusan != null && !id_jurusan.isEmpty() &&
                        id_tahun_lulus != null && !id_tahun_lulus.isEmpty()){

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.urlRegisterAlumni, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "Register Alumni Berhasil", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginAlumni.class));
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Ada Data Yang Masih Kosong", Toast.LENGTH_SHORT).show();
                        }
                    })
                    {
                        @Override
                        protected HashMap<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();

                            params.put("npm", npm);
                            params.put("password", password);
                            params.put("id_tahun_lulus", id_tahun_lulus);
                            params.put("id_jurusan", id_jurusan);

                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }else {
                    Toast.makeText(getApplicationContext(), "Ada Data Yang Masih Kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private int getIdJurusan(String namaJurusan) {
        int idJurusan = -1; // ID default jika tidak ditemukan

        for (Jurusan jurusan : jurusanList) {
            if (jurusan.getNama_jurusan().equals(namaJurusan)) {
                idJurusan = jurusan.getId_jurusan();
                break; // Keluar dari loop setelah cocok ditemukan
            }
        }

        return idJurusan;
    }

    private int getIdTl(int namaTahunLulus) {
        int idTl = -1; // ID default jika tidak ditemukan

        for (Tahun_lulus tahun_lulus : tahunLulusList) {
            if (tahun_lulus.getTahun_lulus() == namaTahunLulus) {
                idTl = tahun_lulus.getId_tahun_lulus();
                break; // Keluar dari loop setelah cocok ditemukan
            }
        }

        return idTl;
    }

    private void fetchDataJurusan() {
        StringRequest request = new StringRequest(Request.Method.GET, Db_Contract.urlGetJurusan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id_jurusan"); // Sesuaikan dengan nama kolom di database Anda
                        String namaJurusan = jsonObject.getString("nama_jurusan"); // Sesuaikan dengan nama kolom di database Anda

                        // Tambahkan data ke dalam jurusanList
                        Jurusan jurusan = new Jurusan(id, namaJurusan);
                        jurusanList.add(jurusan);
                    }

                    List<String> namaJurusanList = new ArrayList<>();
                    for (Jurusan jurusan : jurusanList) {
                        namaJurusanList.add(jurusan.getNama_jurusan());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterAlumni.this, android.R.layout.simple_spinner_item, namaJurusanList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerJurusan.setAdapter(adapter);

                    // Sekarang, jurusanList berisi data dari MySQL
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterAlumni.this, "Gagal mengambil data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void fetchDataTahunLulus() {
        StringRequest request = new StringRequest(Request.Method.GET, Db_Contract.urlGetTahunLulus, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    tahunLulusList.clear(); // Bersihkan list sebelum menambahkan data baru

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id_tahun_lulus");
                        int tahunLulus = jsonObject.getInt("tahun_lulus");
                        Tahun_lulus tahun_lulus = new Tahun_lulus(id, tahunLulus);
                        tahunLulusList.add(tahun_lulus);
                    }

                    List<Integer> namaTahunLulusList = new ArrayList<>();
                    for (Tahun_lulus tahun_lulus : tahunLulusList) {
                        namaTahunLulusList.add(tahun_lulus.getTahun_lulus());
                    }

                    ArrayAdapter<Integer> adapter = new ArrayAdapter<>(RegisterAlumni.this, android.R.layout.simple_spinner_item, namaTahunLulusList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTL.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterAlumni.this, "Gagal mengambil data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}