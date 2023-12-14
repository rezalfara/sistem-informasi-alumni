package com.example.sisteminformasialumni;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private ImageButton btnTogglePassword, btnBack;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_alumni);

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etNpm = findViewById(R.id.etNpm);
        etPassword = findViewById(R.id.etPassword);
        btnLoginAlumni = findViewById(R.id.btnLoginAlumni);
        btnRegisterAlumni = findViewById(R.id.btnRegisterAlumni);
        btnTogglePassword = findViewById(R.id.btnTogglePassword);

        btnTogglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePasswordVisibility();
            }
        });

        btnRegisterAlumni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginAlumni.this, RegisterAlumni.class));
            }
        });

        btnLoginAlumni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show ProgressDialog
                ProgressDialog progressDialog = new ProgressDialog(LoginAlumni.this);
                progressDialog.setMessage("Logging in...");
                progressDialog.setCancelable(false);
                progressDialog.show();

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

                                // Dismiss ProgressDialog
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                                    }
                                }, 3000); // 3000 milliseconds delay

                                // Start MainActivityAlumni and send the NPM as an extra
                                Intent intent = new Intent(getApplicationContext(), MainActivityAlumni.class);
                                intent.putExtra("npm", npm);
                                startActivity(intent);
                            }else{
                                // Dismiss ProgressDialog
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "NPM atau password salah", Toast.LENGTH_SHORT).show();
                                    }
                                }, 2000); // 3000 milliseconds delay
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Dismiss ProgressDialog
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    progressDialog.dismiss();
                                }
                            }, 2000); // 3000 milliseconds delay
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(stringRequest);
                }else {
                    // Dismiss ProgressDialog
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 2000); // 3000 milliseconds delay
                    Toast.makeText(getApplicationContext(), "Ada data yang masih kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;

        if (isPasswordVisible) {
            etPassword.setInputType(etPassword.getInputType() & ~android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btnTogglePassword.setImageResource(R.drawable.hide); // Ganti gambar mata tertutup
        } else {
            etPassword.setInputType(etPassword.getInputType() | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btnTogglePassword.setImageResource(R.drawable.show); // Ganti gambar mata terbuka
        }

        // Set cursor position to the end
        etPassword.setSelection(etPassword.getText().length());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginAlumni.this, PilihLogin.class));
        finish();
    }
}