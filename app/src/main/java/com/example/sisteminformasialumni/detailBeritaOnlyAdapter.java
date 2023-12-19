package com.example.sisteminformasialumni;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class detailBeritaOnlyAdapter extends RecyclerView.Adapter<detailBeritaOnlyAdapter.detailBeritaOnlyViewHolder>{
    private Context mCtx;
    private List<Berita> beritaList;

    public detailBeritaOnlyAdapter(Context mCtx, List<Berita> beritaList) {
        this.mCtx = mCtx;
        this.beritaList = beritaList;
    }

    @Override
    public detailBeritaOnlyAdapter.detailBeritaOnlyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.detail_berita_only_list, null);
        return new detailBeritaOnlyAdapter.detailBeritaOnlyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(detailBeritaOnlyAdapter.detailBeritaOnlyViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return beritaList.size();
    }

    class detailBeritaOnlyViewHolder extends RecyclerView.ViewHolder {

        TextView tvJudul, tvDeskripsi, tvTglPosting;
        ImageView imgBerita;

        public detailBeritaOnlyViewHolder(View itemView) {
            super(itemView);

            imgBerita = itemView.findViewById(R.id.imgBerita);
            tvJudul = itemView.findViewById(R.id.tvJudul);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            tvTglPosting = itemView.findViewById(R.id.tvTglPosting);
        }
    }
}
