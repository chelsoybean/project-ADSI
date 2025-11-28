package com.example.proyekadsi.DAO;

import com.example.proyekadsi.model.AkunPengguna;
import com.example.proyekadsi.util.DBConnect;
import java.sql.*;

public class AkunPenggunaDAO {

    // 1. Login KHUSUS Admin & Dokter (Wajib Password)
    public AkunPengguna authenticateAdminDokter(String noHp, String password, String role) {
        String sql = "SELECT * FROM akun_pengguna WHERE no_hp = ? AND password = ? AND role = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, noHp);
            ps.setString(2, password);
            ps.setString(3, role);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new AkunPengguna(
                        rs.getString("id_akun_pengguna"),
                        rs.getString("no_hp"),
                        rs.getString("nama")
                );
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // 2. Cek User (Pasien) berdasarkan No HP saja (Tanpa Password)
    // Digunakan untuk mengecek apakah nomor ini sudah pernah berobat/daftar sebelumnya
    public AkunPengguna cekUserByNoHP(String noHp) {
        String sql = "SELECT * FROM akun_pengguna WHERE no_hp = ? AND role = 'USER'";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, noHp);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new AkunPengguna(
                        rs.getString("id_akun_pengguna"),
                        rs.getString("no_hp"),
                        rs.getString("nama")
                );
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // 3. Register User Baru (Jika No HP belum ada)
    // Default password diisi '123' karena user login via No HP, tapi database kolom password not null
    public boolean registerUser(String noHp, String nama) {
        String sql = "INSERT INTO akun_pengguna (no_hp, nama, password, role) VALUES (?, ?, '123', 'USER')";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, noHp);
            ps.setString(2, nama);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}