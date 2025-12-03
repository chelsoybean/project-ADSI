package com.example.proyekadsi.DAO;

import com.example.proyekadsi.model.Dokter;
import com.example.proyekadsi.util.DBConnect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DokterDAO {

    // Method lama (biarkan saja)
    public List<Dokter> getAllDokter() {
        List<Dokter> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM dokter")) {
            while (rs.next()) {
                list.add(new Dokter(
                        rs.getString("id_dokter"),
                        rs.getString("nama"),
                        rs.getString("spesialisasi")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public String getIdDokterByAkun(String idAkun) {
        String sql = "SELECT id_dokter FROM dokter WHERE id_akun_pengguna = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(idAkun));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("id_dokter");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}