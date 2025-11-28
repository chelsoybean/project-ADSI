package com.example.proyekadsi.DAO;

import com.example.proyekadsi.util.DBConnect;
import java.sql.*;

public class PembayaranDAO {

    // Use Case: Menerima Pembayaran
    // Ini melakukan 2 hal: Insert tabel Pembayaran & Update status JanjiTemu
    public boolean prosesPembayaran(String idJanjiTemu, double jumlah, String metode) {
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false); // Transaksi

            // 1. Insert ke Tabel Pembayaran
            String sqlBayar = "INSERT INTO pembayaran (id_janji_temu, tgl_pembayaran, jumlah, metode_pembayaran, status) VALUES (?, CURDATE(), ?, ?, 'LUNAS')";
            PreparedStatement psBayar = conn.prepareStatement(sqlBayar);
            psBayar.setInt(1, Integer.parseInt(idJanjiTemu));
            psBayar.setDouble(2, jumlah);
            psBayar.setString(3, metode); // CASH/TRANSFER
            psBayar.executeUpdate();

            // 2. Update Status Janji Temu jadi 'CONFIRMED' (Siap dipanggil Dokter)
            String sqlUpdate = "UPDATE janji_temu SET status = 'CONFIRMED' WHERE id_janji_temu = ?";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, Integer.parseInt(idJanjiTemu));
            psUpdate.executeUpdate();

            conn.commit();
            return true;

        } catch (Exception e) {
            try { if(conn!=null) conn.rollback(); } catch(SQLException ex){}
            e.printStackTrace();
            return false;
        }
    }
}