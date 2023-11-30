package com.example.sisteminformasialumni;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
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

public class AlumniAdapter extends RecyclerView.Adapter<AlumniAdapter.AlumniViewHolder> {
    private Context mCtx;
    private List<Alumni> alumniList;
    private List<Jurusan> jurusanList; // Tambahkan List jurusanList
    private List<Tahun_lulus> tahunLulusList;
    private List<Alumni> filteredList;


    public AlumniAdapter(Context mCtx, List<Alumni> alumniList, List<Jurusan> jurusanList, List<Tahun_lulus> tahunLulusList) {
        this.mCtx = mCtx;
        this.alumniList = alumniList;
        this.jurusanList = jurusanList; // Inisialisasi jurusanList
        this.tahunLulusList = tahunLulusList;
        this.filteredList = new ArrayList<>(alumniList);

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

        holder.llAlumniList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Gunakan adapterPosition untuk mengakses elemen dengan benar
                    Alumni selectedAlumni = alumniList.get(adapterPosition);
                    int alumniId = selectedAlumni.getId_alumni();
                    int jurusanId = selectedAlumni.getId_jurusan();
                    int tlId = selectedAlumni.getId_tahun_lulus();
                    String fotoAlumni = selectedAlumni.getFoto();

                    SharedPreferences sharedPreferences = mCtx.getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

                    SharedPreferences sharedPreferences2 = mCtx.getSharedPreferences("MyPrefs2", MODE_PRIVATE);
                    boolean isLoggedInAlumni = sharedPreferences2.getBoolean("isLoggedInAlumni", false);

                    if (isLoggedIn){
                        // Menggunakan Intent untuk memanggil class lain
                        Intent intent = new Intent(mCtx, detailAlumni.class);
                        intent.putExtra("alumniId", alumniId);
                        intent.putExtra("jurusanId", jurusanId);
                        intent.putExtra("tlId", tlId);
                        intent.putExtra("foto",fotoAlumni);
                        mCtx.startActivity(intent);
                    } else if (isLoggedInAlumni) {
                        // Menggunakan Intent untuk memanggil class lain
                        Intent intent = new Intent(mCtx, detailAlumniOnly.class);
                        intent.putExtra("alumniId", alumniId);
                        intent.putExtra("jurusanId", jurusanId);
                        intent.putExtra("tlId", tlId);
                        intent.putExtra("foto",fotoAlumni);
                        mCtx.startActivity(intent);
                    }

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return alumniList.size();
    }

    class AlumniViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llAlumniList;
        TextView tvNama, tvJurusan, tvTahunLulus;
        ImageView imgAlumni;

        public AlumniViewHolder(View itemView) {
            super(itemView);

            llAlumniList = itemView.findViewById(R.id.llAlumniList);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvJurusan = itemView.findViewById(R.id.tvJurusan);
            tvTahunLulus = itemView.findViewById(R.id.tvTahunLulus);
            imgAlumni = itemView.findViewById(R.id.imgAlumni);
        }
    }

    public void setFilteredData(List<Alumni> filteredList){
        this.alumniList=filteredList;
    }

}
