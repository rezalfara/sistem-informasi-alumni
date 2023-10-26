package com.example.sisteminformasialumni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

public class tambahAlumni extends AppCompatActivity {

    private TextInputEditText etNpm, etNama, etTempatLahir, etTglLahir, etJk, etEmail, etPhone, etAlamat, etFoto, etTahunLulus, etJurusan;
    private Button btnAddAlumni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_alumni);

        etNpm = findViewById(R.id.etNpm);
        etNama = findViewById(R.id.etNama);
        etTempatLahir = findViewById(R.id.etTempatLahir);
        etTglLahir = findViewById(R.id.etTglLahir);
        etJk = findViewById(R.id.etJk);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAlamat = findViewById(R.id.etAlamat);
        etFoto = findViewById(R.id.etFoto);
        etTahunLulus = findViewById(R.id.etTahunLulus);
        etJurusan = findViewById(R.id.etJurusan);
        btnAddAlumni = findViewById(R.id.btnAddAlumni);

        btnAddAlumni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String npm = etNpm.getText().toString();
                String nama = etNama.getText().toString();
                String tempatLahir = etTempatLahir.getText().toString();
                String tglLahir = etTglLahir.getText().toString();
                String jk = etJk.getText().toString();
                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();
                String alamat = etAlamat.getText().toString();
                String foto = etFoto.getText().toString();
                String tahun_lulus = etTahunLulus.getText().toString();
                String jurusan = etJurusan.getText().toString();

                if (!(npm.isEmpty() || nama.isEmpty() || tempatLahir.isEmpty() || tglLahir.isEmpty() || jk.isEmpty() || email.isEmpty() || phone.isEmpty() || alamat.isEmpty() || foto.isEmpty() || tahun_lulus.isEmpty() || jurusan.isEmpty())){
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.urlCreate, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    })
                    {
                        @Override
                        protected HashMap<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();

                            params.put("npm", npm);
                            params.put("nama", nama);
                            params.put("tempat_lahir", tempatLahir);
                            params.put("tgl_lahir", tglLahir);
                            params.put("jk", jk);
                            params.put("email", email);
                            params.put("no_hp", phone);
                            params.put("alamat", alamat);
                            params.put("foto", foto);
                            params.put("id_tahun_lulus", tahun_lulus);
                            params.put("id_jurusan", jurusan);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(tambahAlumni.this, MainActivity.class));
        finish();
    }
}