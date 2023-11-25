package com.example.sisteminformasialumni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class LoginAlumni extends AppCompatActivity {
    private EditText etNpm, etPassword;
    private Button btnLoginAlumni, btnRegisterAlumni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_alumni);

        etNpm = findViewById(R.id.etNpm);
        etPassword = findViewById(R.id.etPassword);
        btnLoginAlumni = findViewById(R.id.btnLoginAlumni);
        btnRegisterAlumni = findViewById(R.id.btnRegisterAlumni);

        btnRegisterAlumni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginAlumni.this, RegisterAlumni.class));
            }
        });

        btnLoginAlumni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String npm = etNpm.getText().toString();
                String password = etPassword.getText().toString();

                if (!(npm.isEmpty() || password.isEmpty())){
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, Db_Contract.urlLoginAlumni + "?npm=" + npm + "&password=" + password, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("Selamat Datang")){
                                // Setelah login berhasil
                                SharedPreferences sharedPreferences2 = getSharedPreferences("MyPrefs2", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences2.edit();
                                editor.putBoolean("isLoggedInAlumni", true);
                                editor.putString("npm", npm);
                                editor.apply();

                                Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();

                                // Start MainActivityAlumni and send the NPM as an extra
                                Intent intent = new Intent(getApplicationContext(), MainActivityAlumni.class);
                                intent.putExtra("npm", npm);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(stringRequest);
                }else {
                    Toast.makeText(getApplicationContext(), "NPM atau Password Salah", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginAlumni.this, PilihLogin.class));
        finish();
    }
}