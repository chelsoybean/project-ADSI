package com.example.proyekadsi.DAO;

import com.example.proyekadsi.model.JadwalPraktek;
import com.example.proyekadsi.util.DBConnect;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JadwalDAO {
    public List<JadwalPraktek> getJadwalByDokter(String idDokter) {
        List<JadwalPraktek> list = new ArrayList<>();
        String sql = "SELECT * FROM jadwal_praktek WHERE id_dokter = ? AND tanggal >= CURDATE() AND kuota > 0 ORDER BY tanggal ASC";
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

    public void generateJadwal() {
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            String sqlMaster = "SELECT id_dokter, hari, jam_mulai, jam_selesai FROM jadwal_tetap";
            ResultSet rs = conn.createStatement().executeQuery(sqlMaster);

            LocalDate today = LocalDate.now();
            String insertSql = "INSERT INTO jadwal_praktek (id_dokter, hari, jam_mulai, jam_selesai, tanggal, kuota) VALUES (?,?,?,?,?,20)";
            PreparedStatement ps = conn.prepareStatement(insertSql);

            while (rs.next()) {
                for (int i = 0; i < 14; i++) {
                    LocalDate target = today.plusDays(i);
                    // (Logic cek hari & cuti disederhanakan)
                    ps.setInt(1, rs.getInt("id_dokter"));
                    ps.setString(2, rs.getString("hari"));
                    ps.setTime(3, rs.getTime("jam_mulai"));
                    ps.setTime(4, rs.getTime("jam_selesai"));
                    ps.setDate(5, java.sql.Date.valueOf(target));
                    ps.executeUpdate();
                }
            }
            conn.commit();
        } catch (Exception e) {
            try { if(conn!=null) conn.rollback(); } catch(SQLException ex){}
        }
    }
}