package com.example.proyekadsi.model;
import java.sql.Date;

public class RekamMedis {
    private String idRekamMedis;
    private int idPasien, idDokter, idJanjiTemu;
    private String keluhan, diagnosa, penanganan;
    private Date tanggal;

    public RekamMedis() {}

    public void setIdPasien(int id) { this.idPasien = id; }
    public void setIdDokter(int id) { this.idDokter = id; }
    public void setIdJanjiTemu(int id) { this.idJanjiTemu = id; }
    public void setKeluhan(String s) { this.keluhan = s; }
    public void setDiagnosa(String s) { this.diagnosa = s; }
    public void setPenanganan(String s) { this.penanganan = s; }
    public void setTanggal(Date d) { this.tanggal = d; }

    public int getIdPasien() { return idPasien; }
    public int getIdDokter() { return idDokter; }
    public int getIdJanjiTemu() { return idJanjiTemu; }
    public String getKeluhan() { return keluhan; }
    public String getDiagnosa() { return diagnosa; }
    public String getPenanganan() { return penanganan; }
    public Date getTanggal() { return tanggal; }
}