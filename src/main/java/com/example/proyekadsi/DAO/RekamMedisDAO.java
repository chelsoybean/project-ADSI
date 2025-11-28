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
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

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

            ResultSet rsKey = psRM.getGeneratedKeys();
            int idRM = 0;
            if(rsKey.next()) idRM = rsKey.getInt(1);

            if(resep != null) {
                PreparedStatement psR = conn.prepareStatement("INSERT INTO resep (id_rekam_medis, detail_obat) VALUES (?,?)");
                psR.setInt(1, idRM); psR.setString(2, resep.getDetailObat());
                psR.executeUpdate();
            }

            if(rujukan != null) {
                PreparedStatement psL = conn.prepareStatement("INSERT INTO rujukan_lab (id_rekam_medis, jenis_tes) VALUES (?,?)");
                psL.setInt(1, idRM); psL.setString(2, rujukan.getJenisTes());
                psL.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            try { if(conn!=null) conn.rollback(); } catch(Exception ex){}
            return false;
        }
    }
}