package com.example.proyekadsi.model;

public class AkunPengguna {
    private String idAkunPengguna;
    private String noHp;
    private String nama;

    public AkunPengguna(String id, String noHp, String nama) {
        this.idAkunPengguna = id;
        this.noHp = noHp;
        this.nama = nama;
    }

    public String getIdAkunPengguna() { return idAkunPengguna; }
    public String getNoHp() { return noHp; }
    public String getNama() { return nama; }
}