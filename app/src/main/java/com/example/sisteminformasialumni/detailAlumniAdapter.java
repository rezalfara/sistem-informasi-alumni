package com.example.sisteminformasialumni;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.appcompat.app.AlertDialog;

public class detailAlumniAdapter extends RecyclerView.Adapter<detailAlumniAdapter.detailAlumniViewHolder>{
    private Context mCtx;
    private List<Alumni> alumniList;
    private List<Jurusan> jurusanList;
    private List<Tahun_lulus> tahunLulusList;

    public detailAlumniAdapter(Context mCtx, List<Alumni> alumniList, List<Jurusan> jurusanList, List<Tahun_lulus> tahunLulusList) {
        this.mCtx = mCtx;
        this.alumniList = alumniList;
        this.jurusanList = jurusanList;
        this.tahunLulusList = tahunLulusList;
    }

    @Override
    public detailAlumniAdapter.detailAlumniViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.detail_alumni_list, null);
        return new detailAlumniAdapter.detailAlumniViewHolder(view);
    }

    @Override
    public void onBindViewHolder(detailAlumniAdapter.detailAlumniViewHolder holder, int position) {
        Alumni alumni = alumniList.get(position);

        String imgUrl = Db_Contract.pathImage+alumni.getFoto();

        //loading the image
        Glide.with(mCtx)
                .load(imgUrl)
                .into(holder.imgAlumni);

        holder.tvNama.setText(alumni.getNama());
        holder.tvNpm.setText(String.valueOf(alumni.getNpm()));
        holder.tvTempatLahir.setText(alumni.getTempat_lahir());
        holder.tvTglLahir.setText(alumni.getTgl_lahir());
        holder.tvJk.setText(alumni.getJk());
        holder.tvEmail.setText(alumni.getEmail());
        holder.tvNoHp.setText(alumni.getNo_hp());
        holder.tvAlamat.setText(alumni.getAlamat());

        int idJurusan = alumni.getId_jurusan();
        for (Jurusan jurusan : jurusanList){
            if (jurusan.getId_jurusan() == idJurusan){
                holder.tvJurusan.setText(jurusan.getNama_jurusan());
                break;
            }
        }

        int idTl = alumni.getId_tahun_lulus();
        for (Tahun_lulus tahun_lulus : tahunLulusList){
            if (tahun_lulus.getId_tahun_lulus() == idTl){
                holder.tvTahunLulus.setText(String.valueOf(tahun_lulus.getTahun_lulus()));
                break;
            }
        }

//        // Set click listener for imgAlumni
//        holder.imgAlumni.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    // Create an AlertDialog to display the full-screen image
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
//                    LayoutInflater inflater = LayoutInflater.from(mCtx);
//                    View dialogView = inflater.inflate(R.layout.dialog_fullscreen_image, null);
//
//                    // Find the ImageView in the dialog layout
//                    ImageView fullscreenImage = dialogView.findViewById(R.id.fullScreenImageView);
//
//                    // Set the image in the full-screen dialog
//                    String imgUrl = Db_Contract.pathImage + alumniList.get(position).getFoto();
//                    Glide.with(mCtx)
//                            .load(imgUrl)
//                            .into(fullscreenImage);
//
//                    // Set up the AlertDialog
//                    builder.setView(dialogView);
//                    AlertDialog dialog = builder.create();
//                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//
//                    // Show the full-screen dialog
//                    dialog.show();
//                }
//        });

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Gunakan adapterPosition untuk mengakses elemen dengan benar
                    Alumni selectedAlumni = alumniList.get(adapterPosition);

                    int alumniId = selectedAlumni.getId_alumni();
                    int npm = selectedAlumni.getNpm();
                    String nama = selectedAlumni.getNama();
                    String tempat_lahir = selectedAlumni.getTempat_lahir();
                    String tgl_lahir = selectedAlumni.getTgl_lahir();
                    String jk = selectedAlumni.getJk();
                    String email = selectedAlumni.getEmail();
                    String no_hp = selectedAlumni.getNo_hp();
                    String alamat = selectedAlumni.getAlamat();
                    String foto = selectedAlumni.getFoto();
                    int id_jurusan = selectedAlumni.getId_jurusan();
                    int id_tahun_lulus = selectedAlumni.getId_tahun_lulus();

                    // Menggunakan Intent untuk memanggil class lain
                    Intent intent = new Intent(mCtx, updateAlumni.class);
                    intent.putExtra("alumniId", alumniId);
                    intent.putExtra("npm", npm);
                    intent.putExtra("nama", nama);
                    intent.putExtra("tempat_lahir", tempat_lahir);
                    intent.putExtra("tgl_lahir", tgl_lahir);
                    intent.putExtra("jk", jk);
                    intent.putExtra("email", email);
                    intent.putExtra("no_hp", no_hp);
                    intent.putExtra("alamat", alamat);
                    intent.putExtra("foto", foto);
                    intent.putExtra("jurusanId", id_jurusan);
                    intent.putExtra("tlId", id_tahun_lulus);

                    mCtx.startActivity(intent);
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Gunakan adapterPosition untuk mengakses elemen dengan benar
                    Alumni selectedAlumni = alumniList.get(adapterPosition);

                    int alumniId = selectedAlumni.getId_alumni();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.urlDelete,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Tindakan setelah data dihapus (misalnya, menampilkan pesan sukses)
                                    // Kemungkinan menampilkan pesan sukses atau memperbarui tampilan RecyclerView
                                    Toast.makeText(mCtx, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(mCtx, MainActivity.class);
                                    intent.putExtra("alumniId", alumniId);
                                    mCtx.startActivity(intent);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Tindakan jika ada kesalahan dalam permintaan (misalnya, menampilkan pesan kesalahan)
                                    Toast.makeText(mCtx, "Gagal menghapus data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("alumniId", String.valueOf(alumniId)); // Mengirim ID alumni untuk dihapus
                            return params;
                        }
                    };

                    // Tambahkan permintaan ke RequestQueue
                    RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
                    requestQueue.add(stringRequest);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return alumniList.size();
    }

    class detailAlumniViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama, tvNpm, tvTempatLahir, tvTglLahir, tvJk, tvEmail, tvNoHp, tvAlamat, tvJurusan, tvTahunLulus;
        ImageView imgAlumni;
        Button btnUpdate, btnDelete;


        public detailAlumniViewHolder(View itemView) {
            super(itemView);

            imgAlumni = itemView.findViewById(R.id.imgAlumni);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvNpm = itemView.findViewById(R.id.tvNpm);
            tvTempatLahir = itemView.findViewById(R.id.tvTempatLahir);
            tvTglLahir = itemView.findViewById(R.id.tvTglLahir);
            tvJk = itemView.findViewById(R.id.tvJk);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvNoHp = itemView.findViewById(R.id.tvNoHp);
            tvAlamat = itemView.findViewById(R.id.tvAlamat);
            tvJurusan = itemView.findViewById(R.id.tvJurusan);
            tvTahunLulus = itemView.findViewById(R.id.tvTahunLulus);

            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }

}
