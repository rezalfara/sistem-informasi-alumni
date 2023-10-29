package com.example.sisteminformasialumni;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        holder.tvJurusan.setText(String.valueOf(alumni.getId_jurusan()));
        holder.tvTahunLulus.setText(String.valueOf(alumni.getId_tahun_lulus()));

        holder.imgAlumni.setOnClickListener(new View.OnClickListener() {
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
                    intent.putExtra("jurusan", id_jurusan);
                    intent.putExtra("tahun_lulus", id_tahun_lulus);

                    mCtx.startActivity(intent);
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
