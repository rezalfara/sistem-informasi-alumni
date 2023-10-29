package com.example.sisteminformasialumni;

public class Jurusan {
    private int id_jurusan;
    private String nama_jurusan;

    public Jurusan(int id_jurusan, String nama_jurusan) {
        this.id_jurusan = id_jurusan;
        this.nama_jurusan = nama_jurusan;
    }

    public int getId_jurusan() {
        return id_jurusan;
    }

    public void setId_jurusan(int id_jurusan) {
        this.id_jurusan = id_jurusan;
    }

    public String getNama_jurusan() {
        return nama_jurusan;
    }

    public void setNama_jurusan(String nama_jurusan) {
        this.nama_jurusan = nama_jurusan;
    }
}
