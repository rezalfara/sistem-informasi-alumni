package com.example.sisteminformasialumni;

public class Alumni {
    private int npm;
    private String nama;
    private String tempat_lahir;
    private String tgl_lahir;
    private String jk;
    private String email;
    private String no_hp;
    private String alamat;
    private String foto;
    private String nama_jurusan;
    private int tahun_lulus;

    public Alumni(int npm, String nama, String tempat_lahir, String tgl_lahir, String jk, String email, String no_hp, String alamat, String foto, String nama_jurusan, int tahun_lulus) {
        this.npm = npm;
        this.nama = nama;
        this.tempat_lahir = tempat_lahir;
        this.tgl_lahir = tgl_lahir;
        this.jk = jk;
        this.email = email;
        this.no_hp = no_hp;
        this.alamat = alamat;
        this.foto = foto;
        this.nama_jurusan = nama_jurusan;
        this.tahun_lulus = tahun_lulus;
    }

    public int getNpm() {
        return npm;
    }

    public void setNpm(int npm) {
        this.npm = npm;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTempat_lahir() {
        return tempat_lahir;
    }

    public void setTempat_lahir(String tempat_lahir) {
        this.tempat_lahir = tempat_lahir;
    }

    public String getTgl_lahir() {
        return tgl_lahir;
    }

    public void setTgl_lahir(String tgl_lahir) {
        this.tgl_lahir = tgl_lahir;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNama_jurusan() {
        return nama_jurusan;
    }

    public void setNama_jurusan(String nama_jurusan) {
        this.nama_jurusan = nama_jurusan;
    }

    public int getTahun_lulus() {
        return tahun_lulus;
    }

    public void setTahun_lulus(int tahun_lulus) {
        this.tahun_lulus = tahun_lulus;
    }
}
