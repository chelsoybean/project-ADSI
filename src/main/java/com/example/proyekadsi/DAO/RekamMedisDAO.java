package com.example.proyekadsi.DAO;

import com.example.proyekadsi.model.RekamMedis;
import com.example.proyekadsi.model.Resep;
import com.example.proyekadsi.model.RujukanLab;
import com.example.proyekadsi.util.DBConnect;
import java.sql.*;

public class RekamMedisDAO {

    public boolean simpanRekamMedis(RekamMedis rm, Resep resep, RujukanLab rujukan) {
        Connection conn = null;
        try {
            // 1. Inisialisasi Koneksi & Transaksi
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            // 2. Insert ke Tabel REKAM_MEDIS (Tabel I)
            String sqlRM = "INSERT INTO rekam_medis (id_pasien, id_dokter, id_janji_temu, tanggal, keluhan, diagnosa, penanganan) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement psRM = conn.prepareStatement(sqlRM, Statement.RETURN_GENERATED_KEYS);
            psRM.setInt(1, rm.getIdPasien());
            psRM.setInt(2, rm.getIdDokter());
            psRM.setInt(3, rm.getIdJanjiTemu());
            psRM.setDate(4, rm.getTanggal());
            psRM.setString(5, rm.getKeluhan());
            psRM.setString(6, rm.getDiagnosa());
            psRM.setString(7, rm.getPenanganan());
            psRM.executeUpdate();

            // Ambil ID Rekam Medis yang baru dibuat
            ResultSet rsKey = psRM.getGeneratedKeys();
            int idRM = 0;
            if(rsKey.next()) idRM = rsKey.getInt(1);

            // 3. Insert ke Tabel RESEP (Jika ada)
            if(resep != null && resep.getDetailObat() != null && !resep.getDetailObat().isEmpty()) {
                PreparedStatement psR = conn.prepareStatement("INSERT INTO resep (id_rekam_medis, detail_obat) VALUES (?,?)");
                psR.setInt(1, idRM);
                psR.setString(2, resep.getDetailObat());
                psR.executeUpdate();
            }

            // 4. Insert ke Tabel RUJUKAN_LAB (Jika ada)
            if(rujukan != null && rujukan.getJenisTes() != null && !rujukan.getJenisTes().isEmpty()) {
                PreparedStatement psL = conn.prepareStatement("INSERT INTO rujukan_lab (id_rekam_medis, jenis_tes) VALUES (?,?)");
                psL.setInt(1, idRM);
                psL.setString(2, rujukan.getJenisTes());
                psL.executeUpdate();
            }

            // 5. ✅ PERBAIKAN KRITIS: Update Status Janji Temu menjadi 'COMPLETED' (Tabel G)
            String sqlUpdateJanji = "UPDATE janji_temu SET status = 'COMPLETED' WHERE id_janji_temu = ?";
            PreparedStatement psUpdateJanji = conn.prepareStatement(sqlUpdateJanji);
            psUpdateJanji.setInt(1, rm.getIdJanjiTemu());
            psUpdateJanji.executeUpdate();

            // Selesai: Commit semua perubahan
            conn.commit();
            return true;

        } catch (Exception e) {
            System.err.println("Gagal menyimpan Rekam Medis. Terjadi error SQL/koneksi:");
            e.printStackTrace();
            if(conn != null) {
                try {
                    System.err.println("Melakukan Rollback...");
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if(conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}