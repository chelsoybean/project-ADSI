package com.example.proyekadsi.DAO;

import com.example.proyekadsi.model.AkunPengguna;
import com.example.proyekadsi.util.DBConnect;
import java.sql.*;

public class AkunPenggunaDAO {

    // 1. Login Admin/Dokter (Cari berdasarkan USERNAME)
    public AkunPengguna authenticateAdminDokter(String usernameInput, String password, String role) {
        String sql = "SELECT * FROM akun_pengguna WHERE username = ? AND password = ? AND role = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usernameInput);
            ps.setString(2, password);
            ps.setString(3, role);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new AkunPengguna(
                        rs.getString("id_akun_pengguna"),
                        rs.getString("username"), // Ambil data username
                        rs.getString("no_hp"),    // Ini bakal null, tapi gpp
                        rs.getString("nama")
                );
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // 2. Cek User/Pasien (Cari berdasarkan NO_HP)
    public AkunPengguna cekUserByNoHP(String noHpInput) {
        String sql = "SELECT * FROM akun_pengguna WHERE no_hp = ? AND role = 'USER'";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, noHpInput);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new AkunPengguna(
                        rs.getString("id_akun_pengguna"),
                        rs.getString("username"), // Ini bakal null
                        rs.getString("no_hp"),    // Ambil data no_hp
                        rs.getString("nama")
                );
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // 3. Register User Baru (Simpan ke kolom NO_HP)
    public boolean registerUser(String noHpInput, String nama) {
        // Kolom username dikosongkan (NULL) untuk user biasa
        String sql = "INSERT INTO akun_pengguna (no_hp, nama, password, role) VALUES (?, ?, '123', 'USER')";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, noHpInput);
            ps.setString(2, nama);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}