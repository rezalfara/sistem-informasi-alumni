package com.example.sisteminformasialumni;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class updateAlumniAdapter extends RecyclerView.Adapter<updateAlumniAdapter.updateAlumniViewHolder>{

    private Context mCtx;
    private List<Alumni> detailAlumniList;

    public updateAlumniAdapter(Context mCtx, List<Alumni> detailAlumniList) {
        this.mCtx = mCtx;
        this.detailAlumniList = detailAlumniList;
    }

    @Override
    public updateAlumniAdapter.updateAlumniViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_update_alumni, null);
        return new updateAlumniAdapter.updateAlumniViewHolder(view);
    }

    @Override
    public void onBindViewHolder(updateAlumniAdapter.updateAlumniViewHolder holder, int position) {
        Alumni alumni = detailAlumniList.get(position);

        holder.etId.setText(String.valueOf(alumni.getId_alumni()));
        holder.etNama.setText(alumni.getNama());
        holder.etNpm.setText(String.valueOf(alumni.getNpm()));
        holder.etTempatLahir.setText(alumni.getTempat_lahir());
        holder.etTglLahir.setText(alumni.getTgl_lahir());
        holder.etJk.setText(alumni.getJk());
        holder.etEmail.setText(alumni.getEmail());
        holder.etPhone.setText(alumni.getNo_hp());
        holder.etAlamat.setText(alumni.getAlamat());
        holder.etJurusan.setText(String.valueOf(alumni.getId_jurusan()));
        holder.etTahunLulus.setText(String.valueOf(alumni.getId_tahun_lulus()));

    }

    @Override
    public int getItemCount() {
        return detailAlumniList.size();
    }

    class updateAlumniViewHolder extends RecyclerView.ViewHolder {

        TextInputEditText etId, etNpm, etNama, etTempatLahir, etTglLahir, etJk, etEmail, etPhone, etAlamat, etFoto, etTahunLulus, etJurusan;

        public updateAlumniViewHolder(View itemView) {
            super(itemView);

            etId = itemView.findViewById(R.id.etId);
            etNpm = itemView.findViewById(R.id.etNpm);
            etNama = itemView.findViewById(R.id.etNama);
            etTempatLahir = itemView.findViewById(R.id.etTempatLahir);
            etTglLahir = itemView.findViewById(R.id.etTglLahir);
            etJk = itemView.findViewById(R.id.etJk);
            etEmail = itemView.findViewById(R.id.etEmail);
            etPhone = itemView.findViewById(R.id.etPhone);
            etPhone = itemView.findViewById(R.id.etPhone);
            etAlamat = itemView.findViewById(R.id.etAlamat);
            etFoto = itemView.findViewById(R.id.etFoto);
            etTahunLulus = itemView.findViewById(R.id.etTahunLulus);
            etJurusan = itemView.findViewById(R.id.etJurusan);

        }
    }

}
