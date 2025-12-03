package com.example.proyekadsi.DAO;

import com.example.proyekadsi.model.JadwalPraktek;
import com.example.proyekadsi.util.DBConnect;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JadwalDAO {

    // Helper: Mengubah DayOfWeek Inggris ke Indonesia
    private String getNamaHariIndo(DayOfWeek dow) {
        switch (dow) {
            case MONDAY: return "Senin";
            case TUESDAY: return "Selasa";
            case WEDNESDAY: return "Rabu";
            case THURSDAY: return "Kamis";
            case FRIDAY: return "Jumat";
            case SATURDAY: return "Sabtu";
            case SUNDAY: return "Minggu";
            default: return "";
        }
    }

    public List<JadwalPraktek> getJadwalByDokter(String idDokter) {
        List<JadwalPraktek> list = new ArrayList<>();
        String sql = "SELECT * FROM jadwal_praktek " +
                "WHERE id_dokter = ? AND tanggal >= CURRENT_DATE AND kuota > 0 " +
                "ORDER BY tanggal ASC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(idDokter));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new JadwalPraktek(
                        rs.getString("id_jadwal"),
                        rs.getString("hari"),
                        rs.getTime("jam_mulai"),
                        rs.getTime("jam_selesai"),
                        rs.getString("id_dokter"),
                        rs.getDate("tanggal")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // LOGIC UTAMA: GENERATE JADWAL 2 MINGGU YANG VALID
    public void generateJadwal() {
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false); // Mulai Transaksi

            // 1. Ambil Template Jadwal Tetap
            String sqlMaster = "SELECT id_dokter, hari, jam_mulai, jam_selesai FROM jadwal_tetap";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlMaster);

            // Simpan template ke memory sementara biar resultset tidak konflik dengan insert
            // (Opsional, tapi lebih aman langsung di loop jika driver mendukung)

            String insertSql = "INSERT INTO jadwal_praktek (id_dokter, hari, jam_mulai, jam_selesai, tanggal, kuota) VALUES (?,?,?,?,?,20)";
            PreparedStatement ps = conn.prepareStatement(insertSql);

            LocalDate today = LocalDate.now();

            while (rs.next()) {
                int idDokter = rs.getInt("id_dokter");
                String hariTemplate = rs.getString("hari"); // Misal: "Senin"
                Time jamMulai = rs.getTime("jam_mulai");
                Time jamSelesai = rs.getTime("jam_selesai");

                // Loop 14 hari ke depan
                for (int i = 0; i < 14; i++) {
                    LocalDate targetDate = today.plusDays(i);
                    String hariTarget = getNamaHariIndo(targetDate.getDayOfWeek());

                    // CEK 1: Apakah Hari Tanggal INI sama dengan Hari Template?
                    // (Misal: Apakah tgl 05-Des-2025 adalah "Senin"?)
                    if (hariTarget.equalsIgnoreCase(hariTemplate)) {

                        // CEK 2: Apakah Dokter Cuti pada tanggal ini?
                        if (!isDokterCuti(conn, idDokter, java.sql.Date.valueOf(targetDate))) {

                            // Jika cocok dan tidak cuti -> INSERT
                            ps.setInt(1, idDokter);
                            ps.setString(2, hariTarget); // Masukkan "Senin", "Selasa", dll
                            ps.setTime(3, jamMulai);
                            ps.setTime(4, jamSelesai);
                            ps.setDate(5, java.sql.Date.valueOf(targetDate));
                            ps.executeUpdate();
                        }
                    }
                }
            }
            conn.commit();
        } catch (Exception e) {
            try { if(conn!=null) conn.rollback(); } catch(SQLException ex){}
            e.printStackTrace();
        }
    }

    // Helper Cek Cuti
    private boolean isDokterCuti(Connection conn, int idDokter, Date tanggal) {
        String sql = "SELECT COUNT(*) FROM cuti WHERE id_dokter = ? AND ? BETWEEN tgl_mulai AND tgl_selesai";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDokter);
            ps.setDate(2, tanggal);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Return true jika ada data cuti
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}