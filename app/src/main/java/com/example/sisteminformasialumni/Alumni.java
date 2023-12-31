package com.example.sisteminformasialumni;

import java.io.Serializable;

public class Alumni implements Serializable{
    private int id_alumni;
    private int npm;
    private String password;
    private String nama;
    private String tempat_lahir;
    private String tgl_lahir;
    private String jk;
    private String email;
    private String no_hp;
    private String alamat;
    private String foto;
    private int id_jurusan;
    private int id_tahun_lulus;

    public Alumni(int id_alumni, int npm, String password, String nama, String tempat_lahir, String tgl_lahir, String jk, String email, String no_hp, String alamat, String foto, int id_jurusan, int id_tahun_lulus) {
        this.id_alumni = id_alumni;
        this.npm = npm;
        this.password = password;
        this.nama = nama;
        this.tempat_lahir = tempat_lahir;
        this.tgl_lahir = tgl_lahir;
        this.jk = jk;
        this.email = email;
        this.no_hp = no_hp;
        this.alamat = alamat;
        this.foto = foto;
        this.id_jurusan = id_jurusan;
        this.id_tahun_lulus = id_tahun_lulus;
    }

    public int getId_alumni() {
        return id_alumni;
    }

    public void setId_alumni(int id_alumni) {
        this.id_alumni = id_alumni;
    }

    public int getNpm() {
        return npm;
    }

    public void setNpm(int npm) {
        this.npm = npm;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getId_jurusan() {
        return id_jurusan;
    }

    public void setId_jurusan(int id_jurusan) {
        this.id_jurusan = id_jurusan;
    }

    public int getId_tahun_lulus() {
        return id_tahun_lulus;
    }

    public void setId_tahun_lulus(int id_tahun_lulus) {
        this.id_tahun_lulus = id_tahun_lulus;
    }
}
