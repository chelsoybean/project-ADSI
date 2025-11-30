package com.example.proyekadsi.DAO;

import com.example.proyekadsi.model.Dokter;
import com.example.proyekadsi.util.DBConnect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DokterDAO {
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
            System.out.println("LOAD DOKTER: " +
                    rs.getString("nama") + " | " + rs.getString("spesialisasi"));

        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}