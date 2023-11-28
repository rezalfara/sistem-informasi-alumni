package com.example.sisteminformasialumni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class EditProfile extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TextInputEditText etId, etNpm, etNama, etTempatLahir, etTglLahir, etEmail, etPhone, etAlamat;
    private Spinner spinnerJurusan, spinnerTL;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonMale, radioButtonFemale;
    private Button btnUploadImage, btnUpdateAlumni;
    private Alumni alumni;
    private Alumni updatedAlumni;
    private List<Jurusan> jurusanList = new ArrayList<>();
    private List<Tahun_lulus> tahunLulusList = new ArrayList<>();
    private List<Alumni> alumniList = new ArrayList<>();

    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private ImageView fotoAlumni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

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

        // Mendapatkan data dari Intent
        Intent intent = getIntent();
        if (intent != null) {
            // Mendapatkan nilai dari extra dengan kunci "alumni"

            String imgUrlUpdate = getIntent().getStringExtra("imgUrlUpdate");

            alumni = (Alumni) intent.getSerializableExtra("alumni");

            if (alumni != null) {
                etId.setText(String.valueOf(alumni.getId_alumni()));
                etNpm.setText(String.valueOf(alumni.getNpm()));
                etNama.setText(alumni.getNama());
                etTempatLahir.setText(alumni.getTempat_lahir());
                etTglLahir.setText(alumni.getTgl_lahir());

                //Jenis kelamin
                if (alumni.getJk().equals("Laki-laki")) {
                    radioButtonMale.setChecked(true); // Radio button "Laki-laki" dipilih
                } else if (alumni.getJk().equals("Perempuan")) {
                    radioButtonFemale.setChecked(true); // Radio button "Perempuan" dipilih
                }
                etEmail.setText(alumni.getEmail());
                etPhone.setText(alumni.getNo_hp());
                etAlamat.setText(alumni.getAlamat());

                fetchDataJurusan();
                fetchDataTahunLulus();

                if (imgUrlUpdate!=null){
                    // Use the base64Image as needed, e.g., decode it into a Bitmap and load it into an ImageView
                    Bitmap decodedBitmap = decodeBase64ToBitmap(imgUrlUpdate);

                    fotoAlumni.setImageBitmap(decodedBitmap);
                }else {
                    // Mengambil gambar dari path dan menampilkannya di ImageView
                    String imgPath = Db_Contract.pathImage + alumni.getFoto();

                    // Menggunakan Glide untuk menampilkan gambar dari URL ke ImageView
                    Glide.with(this)
                            .load(imgPath)
                            .into(fotoAlumni);
                }

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
                        String alumniId = etId.getText().toString();
                        String npm = etNpm.getText().toString();
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
                        updateData(Integer.parseInt(alumniId), Integer.parseInt(npm), nama, tempat_lahir, tgl_lahir, gender, email, no_hp, alamat, id_jurusan, id_tahun_lulus); // Implement this method

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
                Toast.makeText(EditProfile.this, "Foto wajib ada!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(EditProfile.this, "Data Berhasil DiUpdate", Toast.LENGTH_SHORT).show();

                            alumni.setNpm(npm);
                            alumni.setNama(nama); // Update other fields as needed
                            alumni.setTempat_lahir(tempat_lahir);
                            alumni.setTgl_lahir(tgl_lahir);
                            alumni.setJk(jk);
                            alumni.setEmail(email);
                            alumni.setNo_hp(no_hp);
                            alumni.setAlamat(alamat);
                            alumni.setId_jurusan(id_jurusan);
                            alumni.setId_tahun_lulus(id_tahun_lulus);
//                            alumni.setFoto(imageString); // Update other fields as needed

                            Intent intent = new Intent(EditProfile.this, Profil.class);
                            // Jika perlu, Anda dapat mengirim data tambahan ke halaman tujuan
                            intent.putExtra("alumni", alumni);
                            intent.putExtra("updatedFoto", imageString); // Mengirim foto baru sebagai string base64
                            startActivity(intent);

                        } else {
                            Log.e("Error", response);
                            // Ini adalah contoh pernyataan log
                            // Operasi gagal
                            // Lakukan tindakan yang sesuai di sini, misalnya menampilkan pesan kesalahan kepada pengguna
                            Toast.makeText(EditProfile.this, "GAGAL!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle kesalahan koneksi atau permintaan
                        Toast.makeText(EditProfile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
//                fotoAlumni.setImageBitmap(bitmap);
                if (bitmap != null) {
                    fotoAlumni.setImageBitmap(bitmap);
                }
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

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EditProfile.this, android.R.layout.simple_spinner_item, namaJurusanList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerJurusan.setAdapter(adapter);

                    // Update the spinner selection based on the Alumni object
                    if (alumni != null) {
                        int id_jurusan = alumni.getId_jurusan();
                        for (int i = 0; i < spinnerJurusan.getCount(); i++) {
                            if (id_jurusan == jurusanList.get(i).getId_jurusan()) {
                                // ID jurusan matches, set the spinner to that item
                                spinnerJurusan.setSelection(i);
                                break;
                            }
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
                Toast.makeText(EditProfile.this, "Gagal mengambil data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

                    ArrayAdapter<Integer> adapter = new ArrayAdapter<>(EditProfile.this, android.R.layout.simple_spinner_item, namaTahunLulusList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTL.setAdapter(adapter);

                    // Update the spinner selection based on the Alumni object
                    if (alumni != null) {
                        int id_tl = alumni.getId_tahun_lulus();
                        for (int i = 0; i < spinnerTL.getCount(); i++) {
                            if (id_tl == tahunLulusList.get(i).getId_tahun_lulus()) {
                                // ID jurusan matches, set the spinner to that item
                                spinnerTL.setSelection(i);
                                break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProfile.this, "Gagal mengambil data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


}