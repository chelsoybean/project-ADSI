package com.example.proyekadsi.model;

public class Resep {
    private String idResep, idRekamMedis, detailObat;
    public Resep(String detailObat) { this.detailObat = detailObat; }
    public String getDetailObat() { return detailObat; }
}