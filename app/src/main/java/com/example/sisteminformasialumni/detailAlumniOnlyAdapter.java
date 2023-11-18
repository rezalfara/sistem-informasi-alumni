package com.example.sisteminformasialumni;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

public class detailAlumniOnlyAdapter extends RecyclerView.Adapter<detailAlumniOnlyAdapter.detailAlumniOnlyViewHolder>{
    private Context mCtx;
    private List<Alumni> alumniList;
    private List<Jurusan> jurusanList;
    private List<Tahun_lulus> tahunLulusList;

    public detailAlumniOnlyAdapter(Context mCtx, List<Alumni> alumniList, List<Jurusan> jurusanList, List<Tahun_lulus> tahunLulusList) {
        this.mCtx = mCtx;
        this.alumniList = alumniList;
        this.jurusanList = jurusanList;
        this.tahunLulusList = tahunLulusList;
    }

    @Override
    public detailAlumniOnlyAdapter.detailAlumniOnlyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.detail_alumni_only_list, null);
        return new detailAlumniOnlyAdapter.detailAlumniOnlyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(detailAlumniOnlyAdapter.detailAlumniOnlyViewHolder holder, int position) {
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

        // Set click listener for imgAlumni
        holder.imgAlumni.setOnClickListener(new View.OnClickListener() {
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
        return alumniList.size();
    }

    class detailAlumniOnlyViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama, tvNpm, tvTempatLahir, tvTglLahir, tvJk, tvEmail, tvNoHp, tvAlamat, tvJurusan, tvTahunLulus;
        ImageView imgAlumni;

        public detailAlumniOnlyViewHolder(View itemView) {
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
