package com.example.sisteminformasialumni;

public class Berita {
    private int id_berita;
    private String judul;
    private String foto;
    private String deskripsi;
    private String tgl_posting;

    public Berita(int id_berita, String judul, String foto, String deskripsi, String tgl_posting) {
        this.id_berita = id_berita;
        this.judul= judul;
        this.foto = foto;
        this.deskripsi = deskripsi;
        this.tgl_posting = tgl_posting;
    }

    public int getId_berita() {
        return id_berita;
    }

    public void setId_berita(int id_berita) {
        this.id_berita = id_berita;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTgl_posting() {
        return tgl_posting;
    }

    public void setTgl_posting(String tgl_posting) {
        this.tgl_posting = tgl_posting;
    }
}
