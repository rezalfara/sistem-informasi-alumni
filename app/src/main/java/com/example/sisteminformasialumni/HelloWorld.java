    package com.example.sisteminformasialumni;
    
    import android.Manifest;
    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.content.pm.PackageManager;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.graphics.Color;
    import android.graphics.Matrix;
    import android.os.AsyncTask;
    import android.os.Build;
    import android.os.Bundle;
    import android.os.Environment;
    import android.util.Log;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.Spinner;
    import android.widget.Toast;
    
    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.app.ActivityCompat;
    import androidx.core.content.ContextCompat;
    
    import com.android.volley.Request;
    import com.android.volley.RequestQueue;
    import com.android.volley.Response;
    import com.android.volley.VolleyError;
    import com.android.volley.toolbox.JsonArrayRequest;
    import com.android.volley.toolbox.StringRequest;
    import com.android.volley.toolbox.Volley;
    import com.github.mikephil.charting.charts.BarChart;
    import com.github.mikephil.charting.charts.PieChart;
    import com.github.mikephil.charting.components.Legend;
    import com.github.mikephil.charting.components.XAxis;
    import com.github.mikephil.charting.components.YAxis;
    import com.github.mikephil.charting.data.BarData;
    import com.github.mikephil.charting.data.BarDataSet;
    import com.github.mikephil.charting.data.BarEntry;
    import com.github.mikephil.charting.data.PieData;
    import com.github.mikephil.charting.data.PieDataSet;
    import com.github.mikephil.charting.data.PieEntry;
    import com.github.mikephil.charting.formatter.ValueFormatter;
    import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
    import com.github.mikephil.charting.utils.ColorTemplate;
    import com.google.android.material.bottomnavigation.BottomNavigationView;
    import com.itextpdf.text.BadElementException;
    import com.itextpdf.text.Chunk;
    import com.itextpdf.text.Document;
    import com.itextpdf.text.DocumentException;
    import com.itextpdf.text.Element;
    import com.itextpdf.text.Font;
    import com.itextpdf.text.FontFactory;
    import com.itextpdf.text.Image;
    import com.itextpdf.text.PageSize;
    import com.itextpdf.text.Paragraph;
    import com.itextpdf.text.Phrase;
    import com.itextpdf.text.Rectangle;
    import com.itextpdf.text.pdf.PdfPCell;
    import com.itextpdf.text.pdf.PdfPTable;
    import com.itextpdf.text.pdf.PdfWriter;
    import com.squareup.picasso.Picasso;
    
    
    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;
    
    import java.io.ByteArrayOutputStream;
    import java.io.File;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.io.InputStream;
    import java.net.HttpURLConnection;
    import java.net.URL;
    import java.net.URLConnection;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.UUID;
    import java.util.concurrent.Executor;
    import java.util.concurrent.Executors;
    
    public class HelloWorld extends AppCompatActivity {
        private static final int REQUEST_CODE = 11;
        private static String READ_STORAGE_PERMISSION;
        Button btnDownload;
        BottomNavigationView bottomNavigationView;
        private List<Alumni> alumniList = new ArrayList<>();
        private List<Tahun_lulus> tahunLulusList = new ArrayList<>();
        private List<Jurusan> jurusanList = new ArrayList<>();
        Spinner SpinnerTL;
        private PieChart pieChart;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_hello_world);

            pieChart = findViewById(R.id.pieChart);
            pieChart.getDescription().setEnabled(false);
            // Set up bar chart
            // Get data from your PHP script (replace this with your actual data retrieval logic)
            getDataFromPHP();


            btnDownload = findViewById(R.id.btnDownload);
            SpinnerTL = findViewById(R.id.yearSpinner);
    
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                READ_STORAGE_PERMISSION = Manifest.permission.READ_MEDIA_IMAGES;
            }else {
                READ_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
            }
    
            Intent intent = getIntent();
            if (intent.hasExtra("admin")) {
                Admin admin = (Admin) intent.getSerializableExtra("admin");
    
                ////////
                bottomNavigationView = findViewById(R.id.bottom_navigation);
                // Set checked pada item pertama saat pertama kali dibuka
                bottomNavigationView.getMenu().findItem(R.id.action_page2).setChecked(true);
                bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int itemId = item.getItemId();
    
                        if (itemId == R.id.action_page1) {
                            // Tampilkan halaman kedua tanpa efek transisi
                            Intent intent = new Intent(HelloWorld.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("admin", admin);
                            startActivity(intent);
                        } else if (itemId == R.id.action_page2) {
                            return false;
                        } else if (itemId == R.id.action_page3) {
                            // Tampilkan halaman kedua tanpa efek transisi
                            Intent intent = new Intent(HelloWorld.this, NewsActivityAdmin.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("admin", admin);
                            startActivity(intent);
                        } else if (itemId == R.id.action_page4) {
                            // Tampilkan halaman kedua tanpa efek transisi
                            Intent intent = new Intent(HelloWorld.this, ProfilAdmin.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("admin", admin);
                            startActivity(intent);
                        }
    
                        return false;
                    }
                });
    
                loadAlumni();
                fetchDataTahunLulus();
                loadJurusan();
    
                btnDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPermissionDialog();
                    }
                });
            }
        }

        private void showPermissionDialog() {
            if (ContextCompat.checkSelfPermission(this, READ_STORAGE_PERMISSION) == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Accepted", Toast.LENGTH_SHORT).show();
                downloadPdfAsync(); // Call the async method
            }else {
                ActivityCompat.requestPermissions(this, new String[]{ READ_STORAGE_PERMISSION }, REQUEST_CODE);
            }
        }
    
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    
            if (requestCode == REQUEST_CODE){
                if (grantResults.length > 0){
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission Accepted", Toast.LENGTH_SHORT).show();
                        downloadPdfAsync(); // Call the async method
                    }else {
                        Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                    }
                }
    
            }else {
                showPermissionDialog();
            }
        }

        private void downloadPdfAsync() {
            new DownloadPdfAsyncTask().execute();
        }
    
        private class DownloadPdfAsyncTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    String selectedYear = SpinnerTL.getSelectedItem().toString();
                    int id_tahun_lulus = getIdTl(Integer.parseInt(selectedYear));

                    Document document = new Document(PageSize.A2.rotate());
                    String uniqueId = UUID.randomUUID().toString();
                    String pdfFileName = "Alumni_Data_" + selectedYear + "_" + uniqueId + ".pdf";
                    String pdfFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + pdfFileName;
    
                    PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
                    document.open();
    
                    // Add text above the table
                    //Create a paragraph with the desired text
                    Paragraph header = new Paragraph();
                    // Set alignment and other formatting if needed
                    header.setAlignment(Element.ALIGN_CENTER);
                    // Create a custom font with the desired size, bold, and underline
                    Font customFont = FontFactory.getFont(FontFactory.HELVETICA, 30, Font.BOLD | Font.UNDERLINE);
                    // Create a chunk with the text and custom font
                    Chunk chunk = new Chunk("Alumni Data Report - " + selectedYear, customFont);
                    header.add(chunk);
                    // Add the paragraph to the document
                    document.add(header);
                    // Add some spacing after the paragraph
                    document.add(new Paragraph("\n\n"));
    
                    // Create a table with 11 columns
                    PdfPTable table = new PdfPTable(12);
                    table.setWidthPercentage(100);
                    // Contoh pengaturan lebar tabel
                    float[] columnWidths = {4f, 8f, 10f, 10f, 7f, 10f, 9f, 10f, 8f, 10f, 7f, 7f};
                    table.setWidths(columnWidths);
    
    
                    // Membuat objek Font dengan properti bold
                    Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    
                    // Add table headers
                    PdfPCell no = new PdfPCell(new Phrase("No", boldFont));
                    no.setPadding(8);
                    no.setHorizontalAlignment(Element.ALIGN_CENTER); // Mengatur alignment ke tengah
                    table.addCell(no);
    
                    PdfPCell npm = new PdfPCell(new Phrase("NPM", boldFont));
                    npm.setPadding(8);
                    npm.setHorizontalAlignment(Element.ALIGN_CENTER); // Mengatur alignment ke tengah
                    table.addCell(npm);
    
                    PdfPCell nama = new PdfPCell(new Phrase("Nama", boldFont));
                    nama.setPadding(8);
                    nama.setHorizontalAlignment(Element.ALIGN_CENTER); // Mengatur alignment ke tengah
                    table.addCell(nama);
    
                    PdfPCell tempat_lahir = new PdfPCell(new Phrase("Tempat Lahir", boldFont));
                    tempat_lahir.setPadding(8);
                    tempat_lahir.setHorizontalAlignment(Element.ALIGN_CENTER); // Mengatur alignment ke tengah
                    table.addCell(tempat_lahir);
    
                    PdfPCell tgl_lahir = new PdfPCell(new Phrase("Tanggal Lahir", boldFont));
                    tgl_lahir.setPadding(8);
                    tgl_lahir.setHorizontalAlignment(Element.ALIGN_CENTER); // Mengatur alignment ke tengah
                    table.addCell(tgl_lahir);
    
                    PdfPCell jk = new PdfPCell(new Phrase("Jenis Kelamin", boldFont));
                    jk.setPadding(8);
                    jk.setHorizontalAlignment(Element.ALIGN_CENTER); // Mengatur alignment ke tengah
                    table.addCell(jk);
    
                    PdfPCell email = new PdfPCell(new Phrase("Email", boldFont));
                    email.setPadding(8);
                    email.setHorizontalAlignment(Element.ALIGN_CENTER); // Mengatur alignment ke tengah
                    table.addCell(email);
    
                    PdfPCell phone = new PdfPCell(new Phrase("Phone", boldFont));
                    phone.setPadding(8);
                    phone.setHorizontalAlignment(Element.ALIGN_CENTER); // Mengatur alignment ke tengah
                    table.addCell(phone);
    
                    PdfPCell alamat = new PdfPCell(new Phrase("Alamat", boldFont));
                    alamat.setPadding(8);
                    alamat.setHorizontalAlignment(Element.ALIGN_CENTER); // Mengatur alignment ke tengah
                    table.addCell(alamat);
    
                    PdfPCell foto = new PdfPCell(new Phrase("Foto", boldFont));
                    foto.setPadding(8);
                    foto.setHorizontalAlignment(Element.ALIGN_CENTER); // Mengatur alignment ke tengah
                    table.addCell(foto);
    
                    PdfPCell jur = new PdfPCell(new Phrase("Jurusan", boldFont));
                    jur.setPadding(8);
                    jur.setHorizontalAlignment(Element.ALIGN_CENTER); // Mengatur alignment ke tengah
                    table.addCell(jur);
    
                    PdfPCell tahun = new PdfPCell(new Phrase("Tahun Lulus", boldFont));
                    tahun.setPadding(8);
                    tahun.setHorizontalAlignment(Element.ALIGN_CENTER); // Mengatur alignment ke tengah
                    table.addCell(tahun);
    
                    // Set the total width of the table to make it adjust according to content
    //                table.setTotalWidth(PageSize.A4.rotate().getWidth()); // Menggunakan lebar halaman A4 dalam mode landscape
    
                    int nomor = 1;
                    // Add alumni data to the PDF
                    for (Alumni alumni : alumniList) {
                        for (Jurusan jurusan : jurusanList){
                            for (Tahun_lulus tahun_lulus : tahunLulusList){
                                if (alumni.getId_tahun_lulus() == id_tahun_lulus && alumni.getId_jurusan() == jurusan.getId_jurusan() && alumni.getId_tahun_lulus() == tahun_lulus.getId_tahun_lulus()) {
    
                                    PdfPCell noCell = new PdfPCell(new Phrase(String.valueOf(nomor)));
                                    noCell.setPadding(8);
                                    noCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Mengatur alignment ke tengah
                                    table.addCell(noCell);
    
                                    PdfPCell npmCell = new PdfPCell(new Phrase(String.valueOf(alumni.getNpm())));
                                    npmCell.setPadding(8);
                                    table.addCell(npmCell);
    
                                    PdfPCell namaCell = new PdfPCell(new Phrase(alumni.getNama()));
                                    namaCell.setPadding(8);
                                    table.addCell(namaCell);
    
                                    PdfPCell tempatCell = new PdfPCell(new Phrase(alumni.getTempat_lahir()));
                                    tempatCell.setPadding(8);
                                    table.addCell(tempatCell);
    
                                    PdfPCell tglCell = new PdfPCell(new Phrase(alumni.getTgl_lahir()));
                                    tglCell.setPadding(8);
                                    table.addCell(tglCell);
    
                                    PdfPCell jkCell = new PdfPCell(new Phrase(alumni.getJk()));
                                    jkCell.setPadding(8);
                                    table.addCell(jkCell);
    
                                    PdfPCell emailCell = new PdfPCell(new Phrase(alumni.getEmail()));
                                    emailCell.setPadding(8);
                                    table.addCell(emailCell);
    
                                    PdfPCell phoneCell = new PdfPCell(new Phrase(alumni.getNo_hp()));
                                    phoneCell.setPadding(8);
                                    table.addCell(phoneCell);
    
                                    PdfPCell alamatCell = new PdfPCell(new Phrase(alumni.getAlamat()));
                                    alamatCell.setPadding(8);
                                    table.addCell(alamatCell);
    
    //                                PdfPCell fotoCell = new PdfPCell(new Phrase(alumni.getFoto()));
    //                                fotoCell.setPadding(8);
    //                                table.addCell(fotoCell);
    
                                    // Load image from URL using Picasso
                                    Bitmap bitmap = loadBitmapFromUrl(Db_Contract.pathImage + alumni.getFoto());
                                    PdfPCell fotoCell = new PdfPCell(new Phrase(""));
                                    fotoCell.setPadding(8);
                                    if (bitmap != null) {
                                        Image image = Image.getInstance(getBytesFromBitmap(bitmap));
                                        image.scaleAbsolute(40, 40); // Sesuaikan ukuran gambar sesuai kebutuhan
                                        image.setAlignment(Element.ALIGN_CENTER); // Mengatur alignment gambar ke tengah
                                        fotoCell.addElement(image);
                                    }
                                    table.addCell(fotoCell);
    
                                    PdfPCell jurusanCell = new PdfPCell(new Phrase(jurusan.getNama_jurusan()));
                                    jurusanCell.setPadding(8);
                                    table.addCell(jurusanCell);
    
                                    PdfPCell tlCell = new PdfPCell(new Phrase(String.valueOf(tahun_lulus.getTahun_lulus())));
                                    tlCell.setPadding(8);
                                    table.addCell(tlCell);
    
                                    nomor++;
    
                                }
                            }
                        }
                    }
    
                    document.add(table);
                    document.close();
    
                    return pdfFilePath;
    
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
    
            @Override
            protected void onPostExecute(String pdfFilePath) {
                super.onPostExecute(pdfFilePath);
    
                if (pdfFilePath != null) {
                    Toast.makeText(HelloWorld.this, "PDF Downloaded: " + pdfFilePath, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(HelloWorld.this, "Error creating PDF", Toast.LENGTH_SHORT).show();
                }
            }
        }
    
        // Method to load Bitmap from URL using Picasso
        private Bitmap loadBitmapFromUrl(String url) {
            try {
                return Picasso.get().load(url).get();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    
        // Method to convert Bitmap to byte array
        private byte[] getBytesFromBitmap(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
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
    
                        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(HelloWorld.this, android.R.layout.simple_spinner_item, namaTahunLulusList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        SpinnerTL.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
    
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(HelloWorld.this, "Gagal mengambil data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }
    
        private void loadJurusan() {
    
            /*
             * Creating a String Request
             * The request type is GET defined by first parameter
             * The URL is defined in the second parameter
             * Then we have a Response Listener and a Error Listener
             * In response listener we will get the JSON response as a String
             * */
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Db_Contract.urlGetJurusan,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //converting the string to json array object
                                JSONArray array = new JSONArray(response);
    
                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {
    
                                    //getting product object from json array
                                    JSONObject jurusan = array.getJSONObject(i);
    
                                    //adding the product to product list
                                    jurusanList.add(new Jurusan(
                                            jurusan.getInt("id_jurusan"),
                                            jurusan.getString("nama_jurusan")
                                    ));
                                }
    
    
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
    
        private void loadAlumni() {
    
            /*
             * Creating a String Request
             * The request type is GET defined by first parameter
             * The URL is defined in the second parameter
             * Then we have a Response Listener and a Error Listener
             * In response listener we will get the JSON response as a String
             * */
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Db_Contract.urlRead,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //converting the string to json array object
                                JSONArray array = new JSONArray(response);
    
                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {
    
                                    //getting product object from json array
                                    JSONObject alumni = array.getJSONObject(i);
    
                                    //adding the product to product list
                                    alumniList.add(new Alumni(
                                            alumni.getInt("id_alumni"),
                                            alumni.getInt("npm"),
                                            alumni.getString("password"),
                                            alumni.getString("nama"),
                                            alumni.getString("tempat_lahir"),
                                            alumni.getString("tgl_lahir"),
                                            alumni.getString("jk"),
                                            alumni.getString("email"),
                                            alumni.getString("no_hp"),
                                            alumni.getString("alamat"),
                                            alumni.getString("foto"),
                                            alumni.getInt("id_jurusan"),
                                            alumni.getInt("id_tahun_lulus")
                                    ));
                                }
    
    
    
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
    
        @Override
        public void onBackPressed() {
            super.onBackPressed();
            finishAffinity();
        }

        // Replace this method with actual data retrieval logic from your PHP script
        private void displayPieChart(ArrayList<PieEntry> entries) {
            // Create a dataset
            PieDataSet dataSet = new PieDataSet(entries, null);
            dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
            // Set ukuran font label di dalam sektor
            dataSet.setValueTextSize(15f); // Gantilah dengan ukuran font yang diinginkan
            // Set space between slices (adjust the value as needed, default is 0f)
            dataSet.setSliceSpace(5f); // Set the desired space in pixels

            // Create PieData object and set the dataset
            PieData pieData = new PieData(dataSet);

            // Set various configurations for the PieChart (optional)
            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.setUsePercentValues(false);
            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleColor(Color.WHITE);
            pieChart.setTransparentCircleRadius(0f);

            // Set the PieData to the chart
            pieChart.setData(pieData);
            // Set custom value formatter for the PieChart
            pieData.setValueFormatter(new IntegerValueFormatter());


            // Refresh the chart to display the data
            pieChart.invalidate();
        }

        private void getDataFromPHP() {
            // Dummy data for demonstration
            ArrayList<PieEntry> entries = new ArrayList<>();
            // entries.add(new PieEntry(30f, "2022")); // Uncomment this line if you want to keep the dummy data

            StringRequest stringRequest = new StringRequest(Request.Method.GET, Db_Contract.urlCountAlumni,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //converting the string to json array object
                                JSONArray array = new JSONArray(response);

                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {
                                    //getting product object from json array
                                    JSONObject alumni = array.getJSONObject(i);
                                    entries.add(new PieEntry(alumni.getInt("alumni_count") , alumni.getString("nama_jurusan")));
                                }

                                // Display the pie chart with the retrieved data
                                displayPieChart(entries);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });

            //adding our stringrequest to queue
            Volley.newRequestQueue(this).add(stringRequest);
        }

    }

    // Custom ValueFormatter for displaying integer values in PieChart
    class IntegerValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf(Math.round(value));
        }

        @Override
        public String getPieLabel(float value, PieEntry pieEntry) {
            return String.valueOf(Math.round(value));
        }
    }