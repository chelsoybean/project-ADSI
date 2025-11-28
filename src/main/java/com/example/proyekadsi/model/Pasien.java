package com.example.proyekadsi.model;
import java.sql.Date;

public class Pasien {
    private String idPasien;
    private String nik;
    private String nama;
    private Date tglLahir;
    private String idAkunPengguna;

    public Pasien(String id, String nik, String nama, Date tglLahir, String idAkunPengguna) {
        this.idPasien = id;
        this.nik = nik;
        this.nama = nama;
        this.tglLahir = tglLahir;
        this.idAkunPengguna = idAkunPengguna;
    }

    public String getIdPasien() { return idPasien; }
    public String getNama() { return nama; }
    public String getNik() { return nik; }
    public Date getTglLahir() { return tglLahir; }

    @Override
    public String toString() {
        return nama + " (" + nik + ")";
    }
}