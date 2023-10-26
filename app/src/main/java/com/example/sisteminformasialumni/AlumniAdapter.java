package com.example.sisteminformasialumni;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AlumniAdapter extends RecyclerView.Adapter<AlumniAdapter.AlumniViewHolder> {
    private Context mCtx;
    private List<Alumni> alumniList;

    public AlumniAdapter(Context mCtx, List<Alumni> alumniList) {
        this.mCtx = mCtx;
        this.alumniList = alumniList;
    }

    @Override
    public AlumniViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.alumni_list, null);
        return new AlumniViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlumniViewHolder holder, int position) {
        Alumni alumni = alumniList.get(position);

        String imgUrl = Db_Contract.pathImage+alumni.getFoto();

        //loading the image
        Glide.with(mCtx)
                .load(imgUrl)
                .into(holder.imgAlumni);

        holder.tvNama.setText(alumni.getNama());
        holder.tvJurusan.setText(alumni.getNama_jurusan());
        holder.tvTahunLulus.setText(String.valueOf(alumni.getTahun_lulus()));

        holder.tvNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Gunakan adapterPosition untuk mengakses elemen dengan benar
                    Alumni selectedAlumni = alumniList.get(adapterPosition);
                    int alumniNpm = selectedAlumni.getNpm();

                    // Menggunakan Intent untuk memanggil class lain
                    Intent intent = new Intent(mCtx, detailAlumni.class);
                    intent.putExtra("alumniNpm", alumniNpm);
                    mCtx.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return alumniList.size();
    }

    class AlumniViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama, tvJurusan, tvTahunLulus;
        ImageView imgAlumni;

        public AlumniViewHolder(View itemView) {
            super(itemView);

            tvNama = itemView.findViewById(R.id.tvNama);
            tvJurusan = itemView.findViewById(R.id.tvJurusan);
            tvTahunLulus = itemView.findViewById(R.id.tvTahunLulus);
            imgAlumni = itemView.findViewById(R.id.imgAlumni);
        }
    }

}
