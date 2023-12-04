package com.example.sisteminformasialumni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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

public class tambahAlumni extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private TextInputEditText etNpm, etNama, etTempatLahir, etTglLahir, etEmail, etPhone, etAlamat;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonMale, radioButtonFemale;
    private Spinner spinnerJurusan, spinnerTL;
    private Button btnUploadImage, btnAddAlumni;
    private List<Jurusan> jurusanList = new ArrayList<>();
    private List<Tahun_lulus> tahunLulusList = new ArrayList<>();

    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private ImageView fotoAlumni;
    private String imageString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_alumni);

        etNpm = findViewById(R.id.etNpm);
        etNama = findViewById(R.id.etNama);
        etTempatLahir = findViewById(R.id.etTempatLahir);
        etTglLahir = findViewById(R.id.etTglLahir);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAlamat = findViewById(R.id.etAlamat);
        fotoAlumni = findViewById(R.id.fotoAlumni);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnAddAlumni = findViewById(R.id.btnAddAlumni);

        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);

        spinnerTL = findViewById(R.id.spinnerTL);
        spinnerJurusan = findViewById(R.id.spinnerJurusan);

        // Set a click listener on the dateEditText to show the DatePickerDialog
        etTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        // Mengambil data jurusan dari database
        fetchDataTahunLulus();
        fetchDataJurusan();

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnAddAlumni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Jenis Kelamin
                int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
                String gender = "Unknown"; // Nilai default

                if (selectedGenderId == radioButtonMale.getId()) {
                    gender = "Laki-laki";
                } else if (selectedGenderId == radioButtonFemale.getId()) {
                    gender = "Perempuan";
                }

                //TahunLulus
                String selectedTL = spinnerTL.getSelectedItem().toString();
                String id_tahun_lulus = String.valueOf(getIdTl(Integer.parseInt(selectedTL)));

                //Jurusan
                String selectedJurusan = spinnerJurusan.getSelectedItem().toString();
                String id_jurusan = String.valueOf(getIdJurusan(selectedJurusan));

                String npm = etNpm.getText().toString();
                String nama = etNama.getText().toString();
                String tempatLahir = etTempatLahir.getText().toString();
                String tglLahir = etTglLahir.getText().toString();
                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();
                String alamat = etAlamat.getText().toString();

                if (npm != null && !npm.isEmpty() &&
                        nama != null && !nama.isEmpty() &&
                        tempatLahir != null && !tempatLahir.isEmpty() &&
                        tglLahir != null && !tglLahir.isEmpty() &&
                        gender != null && !gender.isEmpty() &&
                        email != null && !email.isEmpty() &&
                        phone != null && !phone.isEmpty() &&
                        alamat != null && !alamat.isEmpty() &&
                        imageString != null && !imageString.isEmpty() &&
                        id_jurusan != null && !id_jurusan.isEmpty() &&
                        id_tahun_lulus != null && !id_tahun_lulus.isEmpty()) {

                    String jk = gender;

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.urlCreate, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "Tambah Data Berhasil", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Ada Data Yang Masih Kosong", Toast.LENGTH_SHORT).show();
                        }
                    })
                    {
                        @Override
                        protected HashMap<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();

                            params.put("npm", npm);
                            params.put("nama", nama);
                            params.put("tempat_lahir", tempatLahir);
                            params.put("tgl_lahir", tglLahir);
                            params.put("jk", jk);
                            params.put("email", email);
                            params.put("no_hp", phone);
                            params.put("alamat", alamat);
                            params.put("foto", imageString);
                            params.put("id_tahun_lulus", id_tahun_lulus);
                            params.put("id_jurusan", id_jurusan);

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

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(tambahAlumni.this, MainActivity.class));
        finish();
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

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(tambahAlumni.this, android.R.layout.simple_spinner_item, namaJurusanList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerJurusan.setAdapter(adapter);

                    // Sekarang, jurusanList berisi data dari MySQL
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(tambahAlumni.this, "Gagal mengambil data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
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

                    ArrayAdapter<Integer> adapter = new ArrayAdapter<>(tambahAlumni.this, android.R.layout.simple_spinner_item, namaTahunLulusList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTL.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(tambahAlumni.this, "Gagal mengambil data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}