package com.example.sisteminformasialumni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class updateAlumni extends AppCompatActivity {
    private TextInputEditText etId, etNpm, etNama, etTempatLahir, etTglLahir, etJk, etEmail, etPhone, etAlamat, etFoto, etTahunLulus, etJurusan;
    private Button btnUpdateAlumni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_alumni);

        etId = findViewById(R.id.etId);
        etNpm = findViewById(R.id.etNpm);
        etNama = findViewById(R.id.etNama);
        etTempatLahir = findViewById(R.id.etTempatLahir);
        etTglLahir = findViewById(R.id.etTglLahir);
        etJk = findViewById(R.id.etJk);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAlamat = findViewById(R.id.etAlamat);
        etFoto = findViewById(R.id.etFoto);
        etJurusan = findViewById(R.id.etJurusan);
        etTahunLulus = findViewById(R.id.etTahunLulus);

        btnUpdateAlumni = findViewById(R.id.btnUpdateAlumni);

        int id = getIntent().getIntExtra("alumniId", 0);
        int npm = getIntent().getIntExtra("npm",0);
        String nama = getIntent().getStringExtra("nama");
        String tempat_lahir = getIntent().getStringExtra("tempat_lahir");
        String tgl_lahir = getIntent().getStringExtra("tgl_lahir");
        String jk = getIntent().getStringExtra("jk");
        String email = getIntent().getStringExtra("email");
        String no_hp = getIntent().getStringExtra("no_hp");
        String alamat = getIntent().getStringExtra("alamat");
        String foto = getIntent().getStringExtra("foto");
        int id_jurusan = getIntent().getIntExtra("jurusan",0);
        int id_tahun_lulus = getIntent().getIntExtra("tahun_lulus",0);

        // Display the data in EditText fields
        etId.setText(String.valueOf(id));
        etNpm.setText(String.valueOf(npm));
        etNama.setText(nama);
        etTempatLahir.setText(tempat_lahir);
        etTglLahir.setText(tgl_lahir);
        etJk.setText(jk);
        etEmail.setText(email);
        etPhone.setText(no_hp);
        etAlamat.setText(alamat);
        etFoto.setText(foto);
        etJurusan.setText(String.valueOf(id_jurusan));
        etTahunLulus.setText(String.valueOf(id_tahun_lulus));

        // Set a click listener for the updateButton
        btnUpdateAlumni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the updated data from EditText fields
                int alumniId = Integer.parseInt(etId.getText().toString());
                int npm = Integer.parseInt(etNpm.getText().toString());
                String nama = etNama.getText().toString();
                String tempat_lahir = etTempatLahir.getText().toString();
                String tgl_lahir = etTglLahir.getText().toString();
                String jk = etJk.getText().toString();
                String email = etEmail.getText().toString();
                String no_hp = etPhone.getText().toString();
                String alamat = etAlamat.getText().toString();
                String foto = etFoto.getText().toString();
                int id_jurusan = Integer.parseInt(etJurusan.getText().toString());
                int id_tahun_lulus = Integer.parseInt(etTahunLulus.getText().toString());

                // Send a request to your server to update the data
                updateData(alumniId, npm, nama, tempat_lahir, tgl_lahir, jk, email, no_hp, alamat, foto, id_jurusan, id_tahun_lulus); // Implement this method
                // Provide user feedback (e.g., a Toast message) about the update result
            }
        });
    }

    private void updateData(final int alumniId,final int npm,final String nama,final String tempat_lahir,final String tgl_lahir,final String jk,final String email,final String no_hp,final String alamat,final String foto,final int id_jurusan,final int id_tahun_lulus) {
        // Instantiate a RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create a request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.urlUpdate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("sukses")) {
                            // Data berhasil diperbarui
                            // Lakukan tindakan yang sesuai di sini, misalnya menampilkan pesan sukses kepada pengguna
                            Log.d("Response", response);
                            Toast.makeText(updateAlumni.this, "Data Berhasil DiUpdate", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(updateAlumni.this, detailAlumni.class);
                            // Jika perlu, Anda dapat mengirim data tambahan ke halaman tujuan
                            intent.putExtra("alumniId", alumniId);
                            intent.putExtra("npm", npm);
                            intent.putExtra("nama", nama);
                            intent.putExtra("tempat_lahir", tempat_lahir);
                            intent.putExtra("tgl_lahir", tgl_lahir);
                            intent.putExtra("jk", jk);
                            intent.putExtra("email", email);
                            intent.putExtra("no_hp", no_hp);
                            intent.putExtra("alamat", alamat);
                            intent.putExtra("foto", foto);
                            intent.putExtra("jurusan", id_jurusan);
                            intent.putExtra("tahun_lulus", id_tahun_lulus);
                            startActivity(intent);

                        } else {
                            Log.e("Error", response);
                            // Ini adalah contoh pernyataan log
                            // Operasi gagal
                            // Lakukan tindakan yang sesuai di sini, misalnya menampilkan pesan kesalahan kepada pengguna
                            Toast.makeText(updateAlumni.this, "GAGAL!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle kesalahan koneksi atau permintaan
                        Toast.makeText(updateAlumni.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Add your parameters to the request
                Map<String, String> params = new HashMap<>();
                params.put("id_alumni", String.valueOf(alumniId));
                params.put("npm", String.valueOf(npm));
                params.put("nama", nama);
                params.put("tempat_lahir", tempat_lahir);
                params.put("tgl_lahir", tgl_lahir);
                params.put("jk", jk);
                params.put("email", email);
                params.put("no_hp", no_hp);
                params.put("alamat", alamat);
                params.put("foto", foto);
                params.put("id_jurusan", String.valueOf(id_jurusan));
                params.put("id_tahun_lulus", String.valueOf(id_tahun_lulus));
                // Add other parameters if needed
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}