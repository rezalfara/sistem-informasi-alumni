package com.example.sisteminformasialumni;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class detailAlumniAdapter extends RecyclerView.Adapter<detailAlumniAdapter.detailAlumniViewHolder>{
    private Context mCtx;
    private List<Alumni> alumniList;

    public detailAlumniAdapter(Context mCtx, List<Alumni> alumniList) {
        this.mCtx = mCtx;
        this.alumniList = alumniList;
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
        holder.tvJurusan.setText(alumni.getNama_jurusan());
        holder.tvTahunLulus.setText(String.valueOf(alumni.getTahun_lulus()));

    }

    @Override
    public int getItemCount() {
        return alumniList.size();
    }

    class detailAlumniViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama, tvNpm, tvTempatLahir, tvTglLahir, tvJk, tvEmail, tvNoHp, tvAlamat, tvJurusan, tvTahunLulus;
        ImageView imgAlumni;

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
        }
    }

}
