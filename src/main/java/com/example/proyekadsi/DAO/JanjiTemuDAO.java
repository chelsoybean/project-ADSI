package com.example.proyekadsi.DAO;

import com.example.proyekadsi.model.JanjiTemu;
import com.example.proyekadsi.util.DBConnect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JanjiTemuDAO {

    // METHOD 1: BUAT JANJI (User Booking)
    public String buatJanji(String idPasien, String idJadwal, String idDokter, Date tgl) {
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false); // Mulai Transaksi

            // Parsing ID ke Integer
            int pId = Integer.parseInt(idPasien);
            int jId = Integer.parseInt(idJadwal);
            int dId = Integer.parseInt(idDokter);

            // 1. Generate Nomor Antrian
            // PERBAIKAN: Ganti 'no_antrian' jadi 'nomor_antrian'
            int nomorAntrian = 1;
            String sqlMax = "SELECT COALESCE(MAX(nomor_antrian), 0) + 1 FROM janji_temu WHERE id_jadwal = ?";
            PreparedStatement psMax = conn.prepareStatement(sqlMax);
            psMax.setInt(1, jId);
            ResultSet rs = psMax.executeQuery();
            if (rs.next()) nomorAntrian = rs.getInt(1);

            // 2. Insert Janji Temu
            // PERBAIKAN: Ganti 'no_antrian' jadi 'nomor_antrian'
            String sqlInsert = "INSERT INTO janji_temu (id_pasien, id_jadwal, id_dokter, tanggal, nomor_antrian, status) VALUES (?,?,?,?,?, 'PENDING')";
            PreparedStatement ps = conn.prepareStatement(sqlInsert);
            ps.setInt(1, pId);
            ps.setInt(2, jId);
            ps.setInt(3, dId);
            ps.setDate(4, tgl);
            ps.setInt(5, nomorAntrian);

            ps.executeUpdate();

            // 3. Kurangi Kuota Jadwal
            String sqlKuota = "UPDATE jadwal_praktek SET kuota = kuota - 1 WHERE id_jadwal = ?";
            PreparedStatement psK = conn.prepareStatement(sqlKuota);
            psK.setInt(1, jId);
            psK.executeUpdate();

            conn.commit(); // Simpan permanen
            return "OK:" + nomorAntrian; // Berhasil

        } catch (Exception e) {
            try { if(conn!=null) conn.rollback(); } catch(Exception ex){}
            e.printStackTrace();
            return "ERROR: " + e.getMessage(); // Gagal
        }
    }

    // METHOD 2: AMBIL DAFTAR JANJI HARI INI (Admin)
    public List<JanjiTemu> getJanjiTemuHariIni() {
        List<JanjiTemu> list = new ArrayList<>();
        // PERBAIKAN: Ganti 'no_antrian' jadi 'nomor_antrian'
        String sql = "SELECT * FROM janji_temu WHERE tanggal = CURRENT_DATE ORDER BY nomor_antrian ASC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                JanjiTemu jt = new JanjiTemu();
                // Di database id_janji_temu, di model kita sebut apa?
                // Cek lagi model Anda, sepertinya di DB namanya id_janji_temu (sesuai script rename saya),
                // tapi kalau pake script teman Anda murni itu namanya 'id_janji'.
                // Mari kita pakai 'id_janji_temu' sesuai asumsi kita sudah rename.
                // Jika error "column id_janji_temu does not exist", ganti string dibawah jadi "id_janji"

                try {
                    jt.setIdJanjiTemu(rs.getString("id_janji_temu"));
                } catch (SQLException e) {
                    // Fallback kalau kolomnya masih id_janji
                    jt.setIdJanjiTemu(rs.getString("id_janji"));
                }

                jt.setIdPasien(rs.getString("id_pasien"));
                jt.setStatus(rs.getString("status"));
                jt.setNomorAntrian(rs.getInt("nomor_antrian")); // PERBAIKAN NAMA KOLOM
                list.add(jt);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // METHOD 3: UPDATE STATUS (Admin)
    public boolean updateStatusJanji(String idJanji, String statusBaru) {
        // Cek nama kolom PK, apakah id_janji atau id_janji_temu
        String sql = "UPDATE janji_temu SET status = ? WHERE id_janji_temu = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statusBaru);
            ps.setInt(2, Integer.parseInt(idJanji));
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            // Coba fallback ke id_janji
            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement ps = conn.prepareStatement("UPDATE janji_temu SET status = ? WHERE id_janji = ?")) {
                ps.setString(1, statusBaru);
                ps.setInt(2, Integer.parseInt(idJanji));
                return ps.executeUpdate() > 0;
            } catch (Exception ex) { return false; }
        }
    }

    // METHOD 4: AMBIL ANTRIAN SIAP PERIKSA (Dokter)
    public List<JanjiTemu> getAntrianSiapDokter(String idDokter) {
        List<JanjiTemu> list = new ArrayList<>();
        // PERBAIKAN: nomor_antrian
        String sql = "SELECT * FROM janji_temu WHERE id_dokter = ? AND status = 'CONFIRMED' AND tanggal = CURRENT_DATE ORDER BY nomor_antrian ASC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(idDokter));
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                JanjiTemu jt = new JanjiTemu();
                try { jt.setIdJanjiTemu(rs.getString("id_janji_temu")); }
                catch (SQLException e) { jt.setIdJanjiTemu(rs.getString("id_janji")); }

                jt.setIdPasien(rs.getString("id_pasien"));
                jt.setStatus(rs.getString("status"));
                jt.setNomorAntrian(rs.getInt("nomor_antrian")); // PERBAIKAN
                list.add(jt);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // METHOD 5: SELESAIKAN JANJI (Dokter)
    public void selesaikanJanji(String idJanji) {
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE janji_temu SET status = 'COMPLETED' WHERE id_janji_temu = ?")) {
            ps.setInt(1, Integer.parseInt(idJanji));
            ps.executeUpdate();
        } catch (Exception e) {
            // Fallback
            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement ps = conn.prepareStatement("UPDATE janji_temu SET status = 'COMPLETED' WHERE id_janji = ?")) {
                ps.setInt(1, Integer.parseInt(idJanji));
                ps.executeUpdate();
            } catch (Exception ex) {}
        }
    }
}