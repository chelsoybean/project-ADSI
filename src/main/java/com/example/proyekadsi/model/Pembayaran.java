package com.example.proyekadsi.model;

import java.sql.Date;

public class Pembayaran {
    private String idPembayaran;
    private String idJanjiTemu;
    private Date tglPembayaran;
    private double jumlah;
    private String metodePembayaran;
    private String status;

    // 1. Constructor Kosong
    public Pembayaran() {
    }

    // 2. Constructor Lengkap
    public Pembayaran(String idPembayaran, String idJanjiTemu, Date tglPembayaran, double jumlah, String metodePembayaran, String status) {
        this.idPembayaran = idPembayaran;
        this.idJanjiTemu = idJanjiTemu;
        this.tglPembayaran = tglPembayaran;
        this.jumlah = jumlah;
        this.metodePembayaran = metodePembayaran;
        this.status = status;
    }

    // 3. Getter & Setter
    public String getIdPembayaran() {
        return idPembayaran;
    }

    public void setIdPembayaran(String idPembayaran) {
        this.idPembayaran = idPembayaran;
    }

    public String getIdJanjiTemu() {
        return idJanjiTemu;
    }

    public void setIdJanjiTemu(String idJanjiTemu) {
        this.idJanjiTemu = idJanjiTemu;
    }

    public Date getTglPembayaran() {
        return tglPembayaran;
    }

    public void setTglPembayaran(Date tglPembayaran) {
        this.tglPembayaran = tglPembayaran;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public String getMetodePembayaran() {
        return metodePembayaran;
    }

    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}