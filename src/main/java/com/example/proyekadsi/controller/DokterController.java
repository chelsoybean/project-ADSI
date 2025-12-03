package com.example.proyekadsi.controller;

import com.example.proyekadsi.DAO.RekamMedisDAO;
import com.example.proyekadsi.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Date;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


public class DokterController {
    @FXML private TextArea txtKeluhan, txtDiagnosa, txtPenanganan, txtResep;
    @FXML private CheckBox chkLab;

    private RekamMedisDAO dao = new RekamMedisDAO();
    private int currentIdPasien = 1;
    // PERBAIKAN: Nilai ini HARUS sudah ada di tabel janji_temu (seperti yang di-INSERT di Langkah 1)
    private int currentIdJanji = 1;
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
        if(!txtResep.getText().trim().isEmpty()) {
            resep = new Resep(txtResep.getText().trim());
        }

        RujukanLab rujukan = null;
        if(chkLab.isSelected()) {
            // Memastikan objek RujukanLab dibuat dengan jenis tes
            rujukan = new RujukanLab("Cek Lab Standar");
        }

        // Cek hasil penyimpanan dan tampilkan Alert
        if(dao.simpanRekamMedis(rm, resep, rujukan)) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "✅ Data Rekam Medis berhasil disimpan!", ButtonType.OK);
            a.showAndWait();

            // Clear form setelah berhasil
            txtKeluhan.clear();
            txtDiagnosa.clear();
            txtPenanganan.clear();
            txtResep.clear();
            chkLab.setSelected(false);
        } else {
            // Jika GAGAL, tampilkan error (Ini muncul jika ada error SQL/koneksi)
            Alert a = new Alert(Alert.AlertType.ERROR, "❌ Gagal menyimpan data! Cek log konsol atau pastikan ID Janji Temu valid.", ButtonType.OK);
            a.showAndWait();
        }
    }
}