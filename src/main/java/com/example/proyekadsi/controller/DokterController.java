package com.example.proyekadsi.controller;

import com.example.proyekadsi.DAO.RekamMedisDAO;
import com.example.proyekadsi.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Date;

public class DokterController {
    @FXML private TextArea txtKeluhan, txtDiagnosa, txtPenanganan, txtResep;
    @FXML private CheckBox chkLab;

    private RekamMedisDAO dao = new RekamMedisDAO();
    private int currentIdPasien = 1;
    private int currentIdJanji = 100;
    private int currentIdDokter = 1;

    @FXML
    public void onSimpan() {
        RekamMedis rm = new RekamMedis();
        rm.setIdPasien(currentIdPasien);
        rm.setIdDokter(currentIdDokter);
        rm.setIdJanjiTemu(currentIdJanji);
        rm.setTanggal(new Date(System.currentTimeMillis()));
        rm.setKeluhan(txtKeluhan.getText());
        rm.setDiagnosa(txtDiagnosa.getText());
        rm.setPenanganan(txtPenanganan.getText());

        Resep resep = null;
        if(!txtResep.getText().isEmpty()) resep = new Resep(txtResep.getText());

        RujukanLab rujukan = null;
        if(chkLab.isSelected()) rujukan = new RujukanLab("Cek Lab Standar");

        if(dao.simpanRekamMedis(rm, resep, rujukan)) {
            new Alert(Alert.AlertType.INFORMATION, "Data Medis Tersimpan").show();
        }
    }
}