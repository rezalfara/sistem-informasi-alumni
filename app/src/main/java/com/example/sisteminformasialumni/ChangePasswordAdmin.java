package com.example.sisteminformasialumni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordAdmin extends AppCompatActivity {
    private Admin admin;
    TextInputEditText etOld, etNew, etConfirm;
    Button btnChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_admin);

        etOld = findViewById(R.id.etOld);
        etNew = findViewById(R.id.etNew);
        etConfirm = findViewById(R.id.etConfirm);
        btnChange = findViewById(R.id.btnChange);

        // Mendapatkan data dari Intent
        Intent intent = getIntent();
        admin = (Admin) intent.getSerializableExtra("admin");
        String username = admin.getUsername();

        getPasswordbyUsername(username);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = admin.getUsername();
                String baru = etNew.getText().toString();
                String confirm = etConfirm.getText().toString();

                if (!baru.equals(confirm)){
                    Toast.makeText(ChangePasswordAdmin.this, "Password Tidak Sama", Toast.LENGTH_SHORT).show();
                }else {
                    updatePassword(username);
                }
            }
        });
    }

    private void getPasswordbyUsername(String username) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Db_Contract.urlGetPasswordByUsername+username,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parsing data JSON, karena sekarang responsenya adalah objek tunggal bukan array
                            JSONObject admin = new JSONObject(response);

                            // Mendapatkan data dari objek JSON
                            String password = admin.getString("password");

                            etOld.setText(password);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(ChangePasswordAdmin.this, "Error getting password", Toast.LENGTH_SHORT).show();
                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void updatePassword(final String username){
        String baru = etNew.getText().toString();
        // Instantiate a RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create a request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.urlUpdatePasswordByUsername,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("sukses")) {
                            // Data berhasil diperbarui
                            // Lakukan tindakan yang sesuai di sini, misalnya menampilkan pesan sukses kepada
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("isLoggedIn");
                            editor.remove("username"); // Hapus informasi pengguna jika diperlukan
                            editor.apply();

                            Log.d("Response", response);
                            Toast.makeText(ChangePasswordAdmin.this, "Password Telah Diubah, Silahkan Login Kembali!", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(ChangePasswordAdmin.this, Login.class);

                            startActivity(intent);

                        } else {
                            Log.e("Error", response);
                            // Ini adalah contoh pernyataan log
                            // Operasi gagal
                            // Lakukan tindakan yang sesuai di sini, misalnya menampilkan pesan kesalahan kepada pengguna
                            Toast.makeText(ChangePasswordAdmin.this, "GAGAL!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle kesalahan koneksi atau permintaan
                        Toast.makeText(ChangePasswordAdmin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Add your parameters to the request
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", baru);

                // Add other parameters if needed
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}