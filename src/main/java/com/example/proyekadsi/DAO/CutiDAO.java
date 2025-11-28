package com.example.proyekadsi.DAO;

import com.example.proyekadsi.util.DBConnect;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

public class CutiDAO {

    // Use Case: Admin Input Jadwal Cuti
    public boolean tambahCuti(String idDokter, Date tglMulai, Date tglSelesai, String keterangan) {
        String sql = "INSERT INTO cuti (id_dokter, tgl_mulai, tgl_selesai, keterangan, status) VALUES (?, ?, ?, ?, 'APPROVED')";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(idDokter));
            ps.setDate(2, tglMulai);
            ps.setDate(3, tglSelesai);
            ps.setString(4, keterangan);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}