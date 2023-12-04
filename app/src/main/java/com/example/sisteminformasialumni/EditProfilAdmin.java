package com.example.sisteminformasialumni;

import androidx.appcompat.app.AppCompatActivity;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProfilAdmin extends AppCompatActivity {
    private TextInputEditText etId, etUsername, etNama, etAlamat;
    private Button btnUploadImage, btnUpdateAdmin;
    private Admin admin;
    private List<Admin> adminList = new ArrayList<>();
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private ImageView fotoAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_admin);

        etId = findViewById(R.id.etId);
        etUsername = findViewById(R.id.etUsername);
        etNama = findViewById(R.id.etNama);
        etAlamat = findViewById(R.id.etAlamat);
        fotoAdmin = findViewById(R.id.fotoAdmin);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnUpdateAdmin = findViewById(R.id.btnUpdateAdmin);

        // Mendapatkan data dari Intent
        Intent intent = getIntent();
        if (intent != null) {
            // Mendapatkan nilai dari extra dengan kunci "alumni"

            String imgUrlUpdate = getIntent().getStringExtra("imgUrlUpdate");

            admin = (Admin) intent.getSerializableExtra("admin");

            if (admin != null) {
                etId.setText(String.valueOf(admin.getId_admin()));
                etUsername.setText(admin.getUsername());
                etNama.setText(admin.getNama());
                etAlamat.setText(admin.getAlamat());

                if (imgUrlUpdate!=null){
                    // Use the base64Image as needed, e.g., decode it into a Bitmap and load it into an ImageView
                    Bitmap decodedBitmap = decodeBase64ToBitmap(imgUrlUpdate);

                    fotoAdmin.setImageBitmap(decodedBitmap);
                }else {
                    // Mengambil gambar dari path dan menampilkannya di ImageView
                    String imgPath = Db_Contract.pathImageAdmin + admin.getFoto();

                    // Menggunakan Glide untuk menampilkan gambar dari URL ke ImageView
                    Glide.with(this)
                            .load(imgPath)
                            .into(fotoAdmin);
                }

                btnUploadImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFileChooser();
                    }
                });

                btnUpdateAdmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Get the updated data from EditText fields
                        String adminId = etId.getText().toString();
                        String username = etUsername.getText().toString();
                        String nama = etNama.getText().toString();
                        String alamat = etAlamat.getText().toString();

                        // Send a request to your server to update the data
                        updateData(Integer.parseInt(adminId), username, nama, alamat); // Implement this method

                    }
                });
            }

        }
    }

    private Bitmap decodeBase64ToBitmap(String base64String) {
        if (base64String != null) {
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } else {
            // Handle the case where base64String is null (return a default Bitmap or handle accordingly)
            return null;
        }
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void updateData(final int adminId, final String username, final String nama, final String alamat) {

        // Declare imageString outside the if statement
        String imageString;

        // Check if the user has selected a new photo
        if (bitmap != null) {
            imageString = convertBitmapToBase64(bitmap);
        } else {
            // Get the Drawable from the ImageView
            Drawable drawable = fotoAdmin.getDrawable();

            if (drawable != null){
                // Convert the Drawable to a Bitmap
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap updatedBitmap = bitmapDrawable.getBitmap();

                // Convert the Bitmap to a base64-encoded string
                imageString = convertBitmapToBase64(updatedBitmap);
            }else {
                Toast.makeText(EditProfilAdmin.this, "Foto wajib ada!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Instantiate a RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create a request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.urlUpdateAdmin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("sukses")) {
                            // Data berhasil diperbarui
                            // Lakukan tindakan yang sesuai di sini, misalnya menampilkan pesan sukses kepada pengguna
                            Log.d("Response", response);
                            Toast.makeText(EditProfilAdmin.this, "Data Berhasil DiUpdate", Toast.LENGTH_SHORT).show();

                            admin.setUsername(username); // Update other fields as needed
                            admin.setNama(nama);
                            admin.setAlamat(alamat);

                            Intent intent = new Intent(EditProfilAdmin.this, ProfilAdmin.class);
                            // Jika perlu, Anda dapat mengirim data tambahan ke halaman tujuan
                            intent.putExtra("admin", admin);
                            intent.putExtra("updatedFoto", imageString); // Mengirim foto baru sebagai string base64
                            startActivity(intent);

                        } else {
                            Log.e("Error", response);
                            // Ini adalah contoh pernyataan log
                            // Operasi gagal
                            // Lakukan tindakan yang sesuai di sini, misalnya menampilkan pesan kesalahan kepada pengguna
                            Toast.makeText(EditProfilAdmin.this, "GAGAL!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle kesalahan koneksi atau permintaan
                        Toast.makeText(EditProfilAdmin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Add your parameters to the request
                Map<String, String> params = new HashMap<>();
                params.put("id_admin", String.valueOf(adminId));
                params.put("username", username);
                params.put("nama", nama);
                params.put("alamat", alamat);
                params.put("foto", imageString);
                // Add other parameters if needed
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
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
                fotoAdmin.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}