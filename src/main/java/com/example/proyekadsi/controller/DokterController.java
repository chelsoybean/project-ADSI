package com.example.proyekadsi.controller;

import com.example.proyekadsi.DAO.*;
import com.example.proyekadsi.model.*;
import com.example.proyekadsi.util.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.sql.Date;
import java.util.List;

public class DokterController {
    // --- TABEL ANTRIAN ---
    @FXML private TableView<JanjiTemu> tableAntrian;
    @FXML private TableColumn<JanjiTemu, String> colNo, colIdPasien, colStatus;

    // --- FORM REKAM MEDIS ---
    @FXML private VBox formArea; // Utk disable/enable form
    @FXML private Label lblPasienSelected; // Menampilkan nama/id pasien yg sedang diperiksa
    @FXML private TextArea txtKeluhan, txtDiagnosa, txtPenanganan, txtResep;
    @FXML private CheckBox chkLab;

    private RekamMedisDAO rmDAO = new RekamMedisDAO();
    private JanjiTemuDAO janjiDAO = new JanjiTemuDAO();
    private DokterDAO dokterDAO = new DokterDAO();

    // Variabel penampung pasien yang SEDANG DIPILIH
    private JanjiTemu selectedJanji = null;
    private String idDokterLogin = null;

    @FXML
    public void initialize() {
        // 1. Cari tahu siapa dokter yang login
        String idAkun = SessionManager.getLoggedInUser().getIdAkunPengguna();
        idDokterLogin = dokterDAO.getIdDokterByAkun(idAkun);

        if (idDokterLogin == null) {
            new Alert(Alert.AlertType.ERROR, "Akun ini tidak terhubung dengan data Dokter!").show();
            return;
        }

        // 2. Setup Tabel
        colNo.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getNomorAntrian())));
        colIdPasien.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIdPasien()));
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));

        // 3. Matikan form dulu sebelum pilih pasien
        formArea.setDisable(true);

        // 4. Load Data
        refreshAntrian();
    }

    // Dipanggil saat Dokter klik tombol "Refresh"
    @FXML
    public void onRefresh() {
        refreshAntrian();
    }

    private void refreshAntrian() {
        List<JanjiTemu> list = janjiDAO.getAntrianSiapDokter(idDokterLogin);
        tableAntrian.getItems().setAll(list);
    }

    // Dipanggil saat Dokter klik salah satu baris di tabel
    @FXML
    public void onPilihPasien() {
        selectedJanji = tableAntrian.getSelectionModel().getSelectedItem();

        if (selectedJanji != null) {
            // Aktifkan form
            formArea.setDisable(false);
            lblPasienSelected.setText("Sedang Memeriksa Pasien ID: " + selectedJanji.getIdPasien());

            // Bersihkan field lama
            txtKeluhan.clear(); txtDiagnosa.clear(); txtPenanganan.clear(); txtResep.clear();
            chkLab.setSelected(false);
        }
    }

    @FXML
    public void onSimpan() {
        if (selectedJanji == null) return;

        // 1. Siapkan Data RM
        RekamMedis rm = new RekamMedis();
        rm.setIdPasien(Integer.parseInt(selectedJanji.getIdPasien()));
        rm.setIdDokter(Integer.parseInt(idDokterLogin));
        rm.setIdJanjiTemu(Integer.parseInt(selectedJanji.getIdJanjiTemu()));
        rm.setTanggal(new Date(System.currentTimeMillis()));
        rm.setKeluhan(txtKeluhan.getText());
        rm.setDiagnosa(txtDiagnosa.getText());
        rm.setPenanganan(txtPenanganan.getText());

        // 2. Siapkan Resep & Lab (Optional)
        Resep resep = null;
        if(!txtResep.getText().isEmpty()) resep = new Resep(txtResep.getText());

        RujukanLab rujukan = null;
        if(chkLab.isSelected()) rujukan = new RujukanLab("Cek Lab Standar");

        // 3. Simpan ke Database
        if(rmDAO.simpanRekamMedis(rm, resep, rujukan)) {
            // 4. Update status janji jadi COMPLETED (Selesai)
            janjiDAO.selesaikanJanji(selectedJanji.getIdJanjiTemu());

            new Alert(Alert.AlertType.INFORMATION, "Rekam Medis Tersimpan & Pasien Selesai!").show();

            // Reset UI
            formArea.setDisable(true);
            lblPasienSelected.setText("-");
            refreshAntrian(); // Pasien yg tadi akan hilang dari list karena statusnya bukan CONFIRMED lagi
        } else {
            new Alert(Alert.AlertType.ERROR, "Gagal menyimpan data.").show();
        }
    }
}