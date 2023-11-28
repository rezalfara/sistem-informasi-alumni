package com.example.sisteminformasialumni;

import java.io.Serializable;

public class Admin implements Serializable {
    private int id_admin;
    private String username;
    private String nama;
    private String password;
    private String alamat;
    private String foto;

    public Admin(int id_admin, String username, String nama, String password, String alamat, String foto) {
        this.id_admin = id_admin;
        this.username = username;
        this.nama = nama;
        this.password = password;
        this.alamat = alamat;
        this.foto = foto;
    }

    public int getId_admin() {
        return id_admin;
    }

    public void setId_admin(int id_admin) {
        this.id_admin = id_admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
