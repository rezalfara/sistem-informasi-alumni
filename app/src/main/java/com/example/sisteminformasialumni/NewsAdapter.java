package com.example.sisteminformasialumni;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{
    private Context mCtx;
    private List<Berita> beritaList;

    public NewsAdapter(Context mCtx, List<Berita> beritaList) {
        this.mCtx = mCtx;
        this.beritaList = beritaList;
    }

    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.berita_list, null);
        return new NewsAdapter.NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.NewsViewHolder holder, int position) {
        Berita berita = beritaList.get(position);

        String imgUrl = Db_Contract.pathImageBerita+berita.getFoto();

        //loading the image
        Glide.with(mCtx)
                .load(imgUrl)
                .into(holder.imgBerita);

        holder.tvJudul.setText(berita.getJudul());
        holder.tvTglPosting.setText(berita.getTgl_posting());


        holder.llBeritaList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Gunakan adapterPosition untuk mengakses elemen dengan benar
                    Berita selectedBerita = beritaList.get(adapterPosition);
                    int beritaId = selectedBerita.getId_berita();

                    SharedPreferences sharedPreferences = mCtx.getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

                    SharedPreferences sharedPreferences2 = mCtx.getSharedPreferences("MyPrefs2", MODE_PRIVATE);
                    boolean isLoggedInAlumni = sharedPreferences2.getBoolean("isLoggedInAlumni", false);

                    if (isLoggedIn){
                        // Menggunakan Intent untuk memanggil class lain
                        Intent intent = new Intent(mCtx, detailBerita.class);
                        intent.putExtra("beritaId", beritaId);
                        mCtx.startActivity(intent);
                    } else if (isLoggedInAlumni) {
                        // Menggunakan Intent untuk memanggil class lain
                        Intent intent = new Intent(mCtx, detailBeritaOnly.class);
                        intent.putExtra("beritaId", beritaId);
                        mCtx.startActivity(intent);
                    }

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return beritaList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llBeritaList;
        TextView tvJudul, tvTglPosting;
        ImageView imgBerita;

        public NewsViewHolder(View itemView) {
            super(itemView);

            llBeritaList = itemView.findViewById(R.id.llBeritaList);
            tvJudul = itemView.findViewById(R.id.tvJudul);
            tvTglPosting = itemView.findViewById(R.id.tvTglPosting);
            imgBerita = itemView.findViewById(R.id.imgBerita);
        }
    }

}
