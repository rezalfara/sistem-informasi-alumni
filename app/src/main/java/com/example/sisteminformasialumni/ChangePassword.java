package com.example.sisteminformasialumni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {
    private Alumni alumni;
    TextInputEditText etOld, etNew, etConfirm;
    Button btnChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        etOld = findViewById(R.id.etOld);
        etNew = findViewById(R.id.etNew);
        etConfirm = findViewById(R.id.etConfirm);
        btnChange = findViewById(R.id.btnChange);

        // Mendapatkan data dari Intent
        Intent intent = getIntent();
        alumni = (Alumni) intent.getSerializableExtra("alumni");
        int npm = alumni.getNpm();
//        // Tampilkan informasi dalam Toast
//        Toast.makeText(getApplicationContext(), "NPM: " + npm, Toast.LENGTH_LONG).show();
        getPasswordbyNpm(npm);



        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String npm = String.valueOf(alumni.getNpm());
                String baru = etNew.getText().toString();
                String confirm = etConfirm.getText().toString();

                if (!baru.equals(confirm)){
                    Toast.makeText(ChangePassword.this, "Password Tidak Sama", Toast.LENGTH_SHORT).show();
                }else {
                    updatePassword(Integer.parseInt(npm));
                }
            }
        });
    }

    private void getPasswordbyNpm(int npm) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Db_Contract.urlGetPasswordByNpm+npm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parsing data JSON, karena sekarang responsenya adalah objek tunggal bukan array
                            JSONObject alumni = new JSONObject(response);

                            // Mendapatkan data dari objek JSON
                            String password = alumni.getString("password");

                            etOld.setText(password);

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

    private void updatePassword(final int npm){
        String baru = etNew.getText().toString();
        // Instantiate a RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create a request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.urlUpdatePasswordByNpm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("sukses")) {
                            // Data berhasil diperbarui
                            // Lakukan tindakan yang sesuai di sini, misalnya menampilkan pesan sukses kepada
                            SharedPreferences sharedPreferences2 = getSharedPreferences("MyPrefs2", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences2.edit();
                            editor.remove("isLoggedInAlumni");
                            editor.remove("npm"); // Hapus informasi pengguna jika diperlukan
                            editor.apply();

                            Log.d("Response", response);
                            Toast.makeText(ChangePassword.this, "Password Telah Diubah, Silahkan Login Kembali!", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(ChangePassword.this, LoginAlumni.class);

                            startActivity(intent);

                        } else {
                            Log.e("Error", response);
                            // Ini adalah contoh pernyataan log
                            // Operasi gagal
                            // Lakukan tindakan yang sesuai di sini, misalnya menampilkan pesan kesalahan kepada pengguna
                            Toast.makeText(ChangePassword.this, "GAGAL!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle kesalahan koneksi atau permintaan
                        Toast.makeText(ChangePassword.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Add your parameters to the request
                Map<String, String> params = new HashMap<>();
                params.put("npm", String.valueOf(npm));
                params.put("password", baru);

                // Add other parameters if needed
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}