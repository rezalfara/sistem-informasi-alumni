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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class updateAlumni extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private TextInputEditText etId, etNpm, etNama, etTempatLahir, etTglLahir, etEmail, etPhone, etAlamat;
    private Spinner spinnerJurusan, spinnerTL;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonMale, radioButtonFemale;
    private Button btnUploadImage, btnUpdateAlumni;
    private List<Jurusan> jurusanList = new ArrayList<>();
    private List<Tahun_lulus> tahunLulusList = new ArrayList<>();
    private List<Alumni> alumniList = new ArrayList<>();

    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private ImageView fotoAlumni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_alumni);

        etId = findViewById(R.id.etId);
        etNpm = findViewById(R.id.etNpm);
        etNama = findViewById(R.id.etNama);
        etTempatLahir = findViewById(R.id.etTempatLahir);
        etTglLahir = findViewById(R.id.etTglLahir);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAlamat = findViewById(R.id.etAlamat);
        spinnerJurusan = findViewById(R.id.spinnerJurusan);
        spinnerTL = findViewById(R.id.spinnerTL);
        fotoAlumni = findViewById(R.id.fotoAlumni);

        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);

        btnUploadImage = findViewById(R.id.btnUploadImage);
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

        // Display the data in EditText fields
        etId.setText(String.valueOf(id));
        etNpm.setText(String.valueOf(npm));
        etNama.setText(nama);
        etTempatLahir.setText(tempat_lahir);
        etTglLahir.setText(tgl_lahir);

        //Jenis kelamin
        if (jk.equals("Laki-laki")) {
            radioButtonMale.setChecked(true); // Radio button "Laki-laki" dipilih
        } else if (jk.equals("Perempuan")) {
            radioButtonFemale.setChecked(true); // Radio button "Perempuan" dipilih
        }

        //Jurusan
        fetchDataJurusan();

        //Tahun Lulus
        fetchDataTahunLulus();


        etEmail.setText(email);
        etPhone.setText(no_hp);
        etAlamat.setText(alamat);

        // Mengambil gambar dari path dan menampilkannya di ImageView
        String imgPath = Db_Contract.pathImage + foto;

        // Menggunakan Glide untuk menampilkan gambar dari URL ke ImageView
        Glide.with(this)
                .load(imgPath)
                .into(fotoAlumni);

        etTglLahir.setOnClickListener(new View.OnClickListener() {
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
        btnUpdateAlumni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
                String gender = "Unknown"; // Nilai default

                if (selectedGenderId == radioButtonMale.getId()) {
                    gender = "Laki-laki";
                } else if (selectedGenderId == radioButtonFemale.getId()) {
                    gender = "Perempuan";
                }

                // Get the updated data from EditText fields
                int alumniId = Integer.parseInt(etId.getText().toString());
                int npm = Integer.parseInt(etNpm.getText().toString());
                String nama = etNama.getText().toString();
                String tempat_lahir = etTempatLahir.getText().toString();
                String tgl_lahir = etTglLahir.getText().toString();
                String email = etEmail.getText().toString();
                String no_hp = etPhone.getText().toString();
                String alamat = etAlamat.getText().toString();

                //TahunLulus
                String selectedTL = spinnerTL.getSelectedItem().toString();
                int id_tahun_lulus = getIdTl(Integer.parseInt(selectedTL));

                //Jurusan
                String selectedJurusan = spinnerJurusan.getSelectedItem().toString();
                int id_jurusan = getIdJurusan(selectedJurusan);

                // Send a request to your server to update the data
                updateData(alumniId, npm, nama, tempat_lahir, tgl_lahir, gender, email, no_hp, alamat, id_jurusan, id_tahun_lulus); // Implement this method

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
        etTglLahir.setText(selectedDate);
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
                fotoAlumni.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(updateAlumni.this, android.R.layout.simple_spinner_item, namaJurusanList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerJurusan.setAdapter(adapter);

                    int id_jurusan = getIntent().getIntExtra("jurusanId",0);

                    for (int i = 0; i < spinnerJurusan.getCount(); i++) {
                        if (id_jurusan == jurusanList.get(i).getId_jurusan()) {
                            // ID jurusan cocok, Anda dapat mengatur Spinner ke item tersebut
                            spinnerJurusan.setSelection(i);
                            break; // Keluar dari loop setelah item cocok ditemukan
                        }
                    }

                    // Sekarang, jurusanList berisi data dari MySQL
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(updateAlumni.this, "Gagal mengambil data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
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

                    ArrayAdapter<Integer> adapter = new ArrayAdapter<>(updateAlumni.this, android.R.layout.simple_spinner_item, namaTahunLulusList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTL.setAdapter(adapter);

                    int id_tl = getIntent().getIntExtra("tlId",0);

                    for (int i = 0; i < spinnerTL.getCount(); i++) {
                        if (id_tl == tahunLulusList.get(i).getId_tahun_lulus()) {
                            // ID jurusan cocok, Anda dapat mengatur Spinner ke item tersebut
                            spinnerTL.setSelection(i);
                            break; // Keluar dari loop setelah item cocok ditemukan
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(updateAlumni.this, "Gagal mengambil data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
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

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void updateData(final int alumniId, final int npm, final String nama, final String tempat_lahir, final String tgl_lahir, final String gender, final String email, final String no_hp, final String alamat, final int id_jurusan, final int id_tahun_lulus) {

        // Declare imageString outside the if statement
        String imageString;

        // Check if the user has selected a new photo
        if (bitmap != null) {
            imageString = convertBitmapToBase64(bitmap);
        } else {
            // Get the Drawable from the ImageView
            Drawable drawable = fotoAlumni.getDrawable();

            if (drawable != null){
                // Convert the Drawable to a Bitmap
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap updatedBitmap = bitmapDrawable.getBitmap();

                // Convert the Bitmap to a base64-encoded string
                imageString = convertBitmapToBase64(updatedBitmap);
            }else {
                Toast.makeText(updateAlumni.this, "Foto wajib ada!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Instantiate a RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String jk = gender;

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
                            intent.putExtra("foto", imageString);
                            intent.putExtra("jurusanId", id_jurusan);
                            intent.putExtra("tlId", id_tahun_lulus);
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
                params.put("foto", imageString);
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