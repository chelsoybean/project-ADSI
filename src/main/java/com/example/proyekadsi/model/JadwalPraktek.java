package com.example.proyekadsi.model;

import java.sql.Time;
import java.sql.Date;
import java.text.SimpleDateFormat; // Library untuk format waktu

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

    // Method ini yang menentukan tampilan di ListView User
    @Override
    public String toString() {
        // Format Waktu (Hanya Jam:Menit)
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        // Format Tanggal (Contoh: 28 Nov 2025)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

        String jamAwal = (jamMulai != null) ? timeFormat.format(jamMulai) : "00:00";
        String jamAkhir = (jamSelesai != null) ? timeFormat.format(jamSelesai) : "00:00";
        String tglStr = (tanggal != null) ? dateFormat.format(tanggal) : "-";

        // Hasil: "Senin, 28 Nov 2025 [08:00 - 12:00]"
        return hari + ", " + tglStr + " [" + jamAwal + " - " + jamAkhir + "]";
    }
}