package com.example.proyekadsi.model;

import java.sql.Date;

public class JanjiTemu {
    private String idJanjiTemu;
    private String idPasien;
    private String idJadwal;
    private String idDokter;
    private Date tanggal;
    private int nomorAntrian;
    private String status; // 'PENDING', 'ARRIVED', 'CONFIRMED', 'COMPLETED', 'CANCELLED'
    private String keluhan;

    // 1. Constructor Kosong (Wajib untuk pembuatan objek di DAO)
    public JanjiTemu() {
    }

    // 2. Constructor Lengkap (Opsional)
    public JanjiTemu(String idJanjiTemu, String idPasien, String idJadwal, String idDokter, Date tanggal, int nomorAntrian, String status) {
        this.idJanjiTemu = idJanjiTemu;
        this.idPasien = idPasien;
        this.idJadwal = idJadwal;
        this.idDokter = idDokter;
        this.tanggal = tanggal;
        this.nomorAntrian = nomorAntrian;
        this.status = status;
    }

    // 3. GETTERS & SETTERS (Wajib agar data bisa muncul di TableView Admin)

    public String getIdJanjiTemu() {
        return idJanjiTemu;
    }

    public void setIdJanjiTemu(String idJanjiTemu) {
        this.idJanjiTemu = idJanjiTemu;
    }

    public String getIdPasien() {
        return idPasien;
    }

    public void setIdPasien(String idPasien) {
        this.idPasien = idPasien;
    }

    public String getIdJadwal() {
        return idJadwal;
    }

    public void setIdJadwal(String idJadwal) {
        this.idJadwal = idJadwal;
    }

    public String getIdDokter() {
        return idDokter;
    }

    public void setIdDokter(String idDokter) {
        this.idDokter = idDokter;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public int getNomorAntrian() {
        return nomorAntrian;
    }

    public void setNomorAntrian(int nomorAntrian) {
        this.nomorAntrian = nomorAntrian;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeluhan() {
        return keluhan;
    }

    public void setKeluhan(String keluhan) {
        this.keluhan = keluhan;
    }
}