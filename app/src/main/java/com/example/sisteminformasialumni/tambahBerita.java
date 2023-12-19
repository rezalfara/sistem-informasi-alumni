package com.example.sisteminformasialumni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class tambahBerita extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private TextInputEditText etJudul, etDeskripsi, etTglPosting;
    private Button btnUploadImage, btnAddBerita;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private ImageView fotoBerita;
    private String imageString;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_berita);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etJudul = findViewById(R.id.etJudul);
        etDeskripsi = findViewById(R.id.etDeskripsi);
        etTglPosting = findViewById(R.id.etTglPosting);
        fotoBerita = findViewById(R.id.fotoBerita);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnAddBerita = findViewById(R.id.btnAddBerita);

        // Set a click listener on the dateEditText to show the DatePickerDialog
        etTglPosting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnAddBerita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String judul = etJudul.getText().toString();
                String deskripsi = etDeskripsi.getText().toString();
                String tglPosting = etTglPosting.getText().toString();

                if (judul != null && !judul.isEmpty() &&
                        deskripsi != null && !deskripsi.isEmpty() &&
                        tglPosting != null && !tglPosting.isEmpty() &&
                        imageString != null && !imageString.isEmpty()) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.urlCreateBerita, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
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

                            params.put("judul", judul);
                            params.put("deskripsi", deskripsi);
                            params.put("tgl_posting", tglPosting);
                            params.put("foto", imageString);
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

    private void showDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getSupportFragmentManager(), "DatePickerDialog");
    }

    // Callback method when a date is set in the DatePickerDialog
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        // Format the selected date
        String selectedDate = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year);

        // Set the selected date to your EditText or do whatever you need with it
        etTglPosting.setText(selectedDate);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(inputStream);
                fotoBerita.setImageBitmap(bitmap);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(tambahBerita.this, NewsActivityAdmin.class));
        finish();
    }

}