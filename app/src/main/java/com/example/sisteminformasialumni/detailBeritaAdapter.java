package com.example.sisteminformasialumni;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class detailBeritaAdapter extends RecyclerView.Adapter<detailBeritaAdapter.detailBeritaViewHolder>{
    private Context mCtx;
    private List<Berita> beritaList;

    public detailBeritaAdapter(Context mCtx, List<Berita> beritaList) {
        this.mCtx = mCtx;
        this.beritaList = beritaList;
    }

    @Override
    public detailBeritaAdapter.detailBeritaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.detail_berita_list, null);
        return new detailBeritaAdapter.detailBeritaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(detailBeritaAdapter.detailBeritaViewHolder holder, int position) {
        Berita berita = beritaList.get(position);

        String imgUrl = Db_Contract.pathImageBerita+berita.getFoto();

        //loading the image
        Glide.with(mCtx)
                .load(imgUrl)
                .into(holder.imgBerita);

        holder.tvJudul.setText(berita.getJudul());
        holder.tvDeskripsi.setText(berita.getDeskripsi());
        holder.tvTglPosting.setText(berita.getTgl_posting());

        // Set click listener for imgAlumni
        holder.imgBerita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to open the FullscreenImageActivity
                Intent intent = new Intent(mCtx, FullScreenImage.class);

                // Pass the image URL to the FullscreenImageActivity
                intent.putExtra("imgUrl", imgUrl);

                // Start the FullscreenImageActivity
                mCtx.startActivity(intent);
            }
        });


        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Gunakan adapterPosition untuk mengakses elemen dengan benar
                    Berita selectedBerita = beritaList.get(adapterPosition);

                    int beritaId = selectedBerita.getId_berita();
                    String judul = selectedBerita.getJudul();
                    String foto = selectedBerita.getFoto();
                    String deskripsi = selectedBerita.getDeskripsi();
                    String tgl_posting = selectedBerita.getTgl_posting();

                    // Menggunakan Intent untuk memanggil class lain
                    Intent intent = new Intent(mCtx, updateBerita.class);
                    intent.putExtra("beritaId", beritaId);
                    intent.putExtra("judul", judul);
                    intent.putExtra("foto", foto);
                    intent.putExtra("deskripsi", deskripsi);
                    intent.putExtra("tgl_posting", tgl_posting);

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
                    Berita selectedBerita = beritaList.get(adapterPosition);

                    int beritaId = selectedBerita.getId_berita();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.urlDeleteBerita,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Tindakan setelah data dihapus (misalnya, menampilkan pesan sukses)
                                    // Kemungkinan menampilkan pesan sukses atau memperbarui tampilan RecyclerView
                                    Toast.makeText(mCtx, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(mCtx, MainActivity.class);
                                    intent.putExtra("beritaId", beritaId);
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
                            params.put("beritaId", String.valueOf(beritaId)); // Mengirim ID alumni untuk dihapus
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
        return beritaList.size();
    }

    class detailBeritaViewHolder extends RecyclerView.ViewHolder {

        TextView tvJudul, tvDeskripsi, tvTglPosting;
        ImageView imgBerita;
        Button btnUpdate, btnDelete;


        public detailBeritaViewHolder(View itemView) {
            super(itemView);

            imgBerita = itemView.findViewById(R.id.imgBerita);
            tvJudul = itemView.findViewById(R.id.tvJudul);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            tvTglPosting = itemView.findViewById(R.id.tvTglPosting);

            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }
}
