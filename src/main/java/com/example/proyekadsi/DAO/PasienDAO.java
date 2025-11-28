package com.example.proyekadsi.DAO;

import com.example.proyekadsi.model.Pasien;
import com.example.proyekadsi.util.DBConnect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PasienDAO {
    public List<Pasien> getPasienByAkun(String idAkun) {
        List<Pasien> list = new ArrayList<>();
        String sql = "SELECT * FROM pasien WHERE id_akun_pengguna = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(idAkun));
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(new Pasien(
                        rs.getString("id_pasien"),
                        rs.getString("nik"),
                        rs.getString("nama"),
                        rs.getDate("tgl_lahir"),
                        rs.getString("id_akun_pengguna")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean tambahPasien(String idAkun, String nama, String nik, Date tglLahir) {
        String sql = "INSERT INTO pasien (id_akun_pengguna, nama, nik, tgl_lahir) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(idAkun));
            ps.setString(2, nama);
            ps.setString(3, nik);
            ps.setDate(4, tglLahir);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }
}