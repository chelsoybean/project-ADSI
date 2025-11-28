package com.example.proyekadsi.model;
import java.sql.Time;
import java.sql.Date;

public class JadwalPraktek {
    private String idJadwal;
    private String hari;
    private Time jamMulai;
    private Time jamSelesai;
    private String idDokter;
    private Date tanggal;

    public JadwalPraktek(String id, String hari, Time m, Time s, String idDok, Date tgl) {
        this.idJadwal = id;
        this.hari = hari;
        this.jamMulai = m;
        this.jamSelesai = s;
        this.idDokter = idDok;
        this.tanggal = tgl;
    }

    public String getIdJadwal() { return idJadwal; }
    public Date getTanggal() { return tanggal; }

    @Override
    public String toString() {
        return hari + ", " + tanggal + " [" + jamMulai + " - " + jamSelesai + "]";
    }
}