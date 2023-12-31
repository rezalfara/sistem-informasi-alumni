package com.example.sisteminformasialumni;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

        if (alumni.getJk() == "Laki-laki"){
            holder.radioButtonMale.setChecked(true);
        }else if (alumni.getJk() == "Perempuan"){
            holder.radioButtonFemale.setChecked(true);
        }

        holder.etEmail.setText(alumni.getEmail());
        holder.etPhone.setText(alumni.getNo_hp());
        holder.etAlamat.setText(alumni.getAlamat());
        holder.spinnerJurusan.getSelectedItem().toString();
        holder.spinnerTL.getSelectedItem().toString();

    }

    @Override
    public int getItemCount() {
        return detailAlumniList.size();
    }

    class updateAlumniViewHolder extends RecyclerView.ViewHolder {

        TextInputEditText etId, etNpm, etNama, etTempatLahir, etTglLahir, etEmail, etPhone, etAlamat;
        RadioGroup radioGroupGender;
        RadioButton radioButtonMale, radioButtonFemale;
        Spinner spinnerJurusan, spinnerTL;

        public updateAlumniViewHolder(View itemView) {
            super(itemView);

            etId = itemView.findViewById(R.id.etId);
            etNpm = itemView.findViewById(R.id.etNpm);
            etNama = itemView.findViewById(R.id.etNama);
            etTempatLahir = itemView.findViewById(R.id.etTempatLahir);
            etTglLahir = itemView.findViewById(R.id.etTglLahir);
            radioGroupGender = itemView.findViewById(R.id.radioGroupGender);
            radioButtonMale = itemView.findViewById(R.id.radioButtonMale);
            radioButtonFemale = itemView.findViewById(R.id.radioButtonFemale);

            etEmail = itemView.findViewById(R.id.etEmail);
            etPhone = itemView.findViewById(R.id.etPhone);
            etPhone = itemView.findViewById(R.id.etPhone);
            etAlamat = itemView.findViewById(R.id.etAlamat);
            spinnerTL = itemView.findViewById(R.id.spinnerTL);
            spinnerJurusan = itemView.findViewById(R.id.spinnerJurusan);

        }
    }

}
