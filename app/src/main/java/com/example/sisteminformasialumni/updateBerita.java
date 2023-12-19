package com.example.sisteminformasialumni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class updateBerita extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private TextInputEditText etId, etJudul, etTglPosting, etDeskripsi;
    private Button btnUploadImage, btnUpdateBerita;
    private List<Berita> beritaList = new ArrayList<>();
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private ImageView fotoBerita;
    private ImageButton btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_berita);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etId = findViewById(R.id.etId);
        etJudul = findViewById(R.id.etJudul);
        etDeskripsi = findViewById(R.id.etDeskripsi);
        etTglPosting = findViewById(R.id.etTglPosting);
        fotoBerita = findViewById(R.id.fotoBerita);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnUpdateBerita = findViewById(R.id.btnUpdateBerita);

        int id = getIntent().getIntExtra("beritaId", 0);
        String judul = getIntent().getStringExtra("judul");
        String foto = getIntent().getStringExtra("foto");
        String deskripsi = getIntent().getStringExtra("deskripsi");
        String tgl_posting = getIntent().getStringExtra("tgl_posting");

        // Display the data in EditText fields
        etId.setText(String.valueOf(id));
        etJudul.setText(judul);
        etDeskripsi.setText(deskripsi);
        etTglPosting.setText(tgl_posting);

        // Mengambil gambar dari path dan menampilkannya di ImageView
        String imgPath = Db_Contract.pathImageBerita + foto;
        // Menggunakan Glide untuk menampilkan gambar dari URL ke ImageView
        Glide.with(this)
                .load(imgPath)
                .into(fotoBerita);

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

        // Set a click listener for the updateButton
        btnUpdateBerita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the updated data from EditText fields
                int beritaId = Integer.parseInt(etId.getText().toString());
                String judul = etJudul.getText().toString();
                String deskripsi = etDeskripsi.getText().toString();
                String tgl_posting = etTglPosting.getText().toString();

                // Send a request to your server to update the data
                updateData(beritaId, judul, deskripsi, tgl_posting); // Implement this method

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void updateData(final int beritaId, final String judul, final String deskripsi, final String tgl_posting) {

        // Declare imageString outside the if statement
        String imageString;

        // Check if the user has selected a new photo
        if (bitmap != null) {
            imageString = convertBitmapToBase64(bitmap);
        } else {
            // Get the Drawable from the ImageView
            Drawable drawable = fotoBerita.getDrawable();

            if (drawable != null){
                // Convert the Drawable to a Bitmap
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap updatedBitmap = bitmapDrawable.getBitmap();

                // Convert the Bitmap to a base64-encoded string
                imageString = convertBitmapToBase64(updatedBitmap);
            }else {
                Toast.makeText(updateBerita.this, "Foto wajib ada!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Instantiate a RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Create a request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.urlUpdateBerita,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("sukses")) {
                            // Data berhasil diperbarui
                            // Lakukan tindakan yang sesuai di sini, misalnya menampilkan pesan sukses kepada pengguna
                            Log.d("Response", response);
                            Toast.makeText(updateBerita.this, "Data Berhasil DiUpdate", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(updateBerita.this, detailBerita.class);
                            // Jika perlu, Anda dapat mengirim data tambahan ke halaman tujuan
                            intent.putExtra("beritaId", beritaId);
                            intent.putExtra("judul", judul);
                            intent.putExtra("deskripsi", deskripsi);
                            intent.putExtra("tgl_posting", tgl_posting);
                            intent.putExtra("foto", imageString);
                            startActivity(intent);
                            finish();

                        } else {
                            Log.e("Error", response);
                            // Ini adalah contoh pernyataan log
                            // Operasi gagal
                            // Lakukan tindakan yang sesuai di sini, misalnya menampilkan pesan kesalahan kepada pengguna
                            Toast.makeText(updateBerita.this, "GAGAL!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle kesalahan koneksi atau permintaan
                        Toast.makeText(updateBerita.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Add your parameters to the request
                Map<String, String> params = new HashMap<>();
                params.put("id_berita", String.valueOf(beritaId));
                params.put("judul", judul);
                params.put("deskripsi", deskripsi);
                params.put("tgl_posting", tgl_posting);
                params.put("foto", imageString);
                // Add other parameters if needed
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}