package com.example.proyekadsi.model;

public class Dokter {
    private String idDokter;
    private String nama;
    private String spesialisasi;

    public Dokter(String id, String nama, String spesialisasi) {
        this.idDokter = id;
        this.nama = nama;
        this.spesialisasi = spesialisasi;
    }

    public String getIdDokter() { return idDokter; }
    public String getNama() { return nama; }

    @Override
    public String toString() { return nama + " - " + spesialisasi; }
}