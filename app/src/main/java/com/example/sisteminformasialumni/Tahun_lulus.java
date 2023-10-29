package com.example.sisteminformasialumni;

public class Tahun_lulus {
    private int id_tahun_lulus;
    private int tahun_lulus;

    public Tahun_lulus(int id_tahun_lulus, int tahun_lulus) {
        this.id_tahun_lulus = id_tahun_lulus;
        this.tahun_lulus = tahun_lulus;
    }

    public int getId_tahun_lulus() {
        return id_tahun_lulus;
    }

    public void setId_tahun_lulus(int id_tahun_lulus) {
        this.id_tahun_lulus = id_tahun_lulus;
    }

    public int getTahun_lulus() {
        return tahun_lulus;
    }

    public void setTahun_lulus(int tahun_lulus) {
        this.tahun_lulus = tahun_lulus;
    }
}
