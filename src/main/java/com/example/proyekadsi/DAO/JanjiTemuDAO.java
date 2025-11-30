package com.example.proyekadsi.DAO;

import com.example.proyekadsi.model.JanjiTemu;
import com.example.proyekadsi.util.DBConnect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JanjiTemuDAO {
    public int buatJanji(String idPasien, String idJadwal, String idDokter, Date tgl) {
        Connection conn = null;
        int noAntrian = -1;
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            // 1. Get Nomor Antrian
            String sqlMax = "SELECT COALESCE(MAX(no_antrian), 0) + 1 FROM janji_temu WHERE id_jadwal = ?";
            PreparedStatement psMax = conn.prepareStatement(sqlMax);
            psMax.setInt(1, Integer.parseInt(idJadwal));
            ResultSet rs = psMax.executeQuery();
            if (rs.next()) noAntrian = rs.getInt(1);

            // 2. Insert Janji Temu
            String sqlInsert = "INSERT INTO janji_temu (id_pasien, id_jadwal, id_dokter, tanggal, no_antrian, status)  VALUES (?,?,?,?,?, 'PENDING')";

            PreparedStatement ps = conn.prepareStatement(sqlInsert);
            ps.setInt(1, Integer.parseInt(idPasien));
            ps.setInt(2, Integer.parseInt(idJadwal));
            ps.setInt(3, Integer.parseInt(idDokter));
            ps.setDate(4, tgl);
            ps.setInt(5, noAntrian);
            ps.executeUpdate();

            // 3. Update Kuota
            PreparedStatement psK = conn.prepareStatement("UPDATE jadwal_praktek SET kuota = kuota - 1 WHERE id_jadwal = ?");
            psK.setInt(1, Integer.parseInt(idJadwal));
            psK.executeUpdate();

            conn.commit();
        } catch (Exception e) {
            try { if(conn!=null) conn.rollback(); } catch(Exception ex){}
            e.printStackTrace();
            return -1;
        }
        return noAntrian;
    }
    // ADMIN: Ambil daftar janji temu hari ini (Untuk Kasir/Resepsionis)
    // Return ResultSet atau List<JanjiTemu> (Disini kita return List custom object biar mudah di TableView)
    public List<JanjiTemu> getJanjiTemuHariIni() {
        List<JanjiTemu> list = new ArrayList<>();
        String sql = "SELECT * FROM janji_temu WHERE tanggal = CURDATE() ORDER BY no_antrian ASC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                // Construct object (sesuaikan dengan constructor model Anda)
                JanjiTemu jt = new JanjiTemu();
                jt.setIdJanjiTemu(rs.getString("id_janji_temu"));
                jt.setIdPasien(rs.getString("id_pasien"));
                jt.setStatus(rs.getString("status")); // PENDING, CONFIRMED, COMPLETED
                jt.setNomorAntrian(rs.getInt("no_antrian"));
                list.add(jt);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ADMIN: Konfirmasi Kehadiran (Update Status jadi WAITING/HADIR)
    public boolean updateStatusJanji(String idJanji, String statusBaru) {
        String sql = "UPDATE janji_temu SET status = ? WHERE id_janji_temu = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statusBaru);
            ps.setInt(2, Integer.parseInt(idJanji));
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }
}