package com.example.proyekadsi.model;

public class AkunPengguna {
    private String idAkunPengguna;
    private String username; // Untuk Admin/Dokter
    private String noHp;     // Untuk Pasien
    private String nama;

    public AkunPengguna(String id, String username, String noHp, String nama) {
        this.idAkunPengguna = id;
        this.username = username;
        this.noHp = noHp;
        this.nama = nama;
    }

    public String getIdAkunPengguna() { return idAkunPengguna; }
    public String getUsername() { return username; }
    public String getNoHp() { return noHp; }
    public String getNama() { return nama; }
}