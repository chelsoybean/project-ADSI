package com.example.proyekadsi.controller;

import com.example.proyekadsi.DAO.*;
import com.example.proyekadsi.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Date;
import java.util.List;

public class AdminController {

    // --- TAB 1: MANAJEMEN JADWAL ---
    @FXML private ComboBox<Dokter> comboDokterCuti;
    @FXML private DatePicker tglMulaiCuti, tglSelesaiCuti;
    @FXML private TextField txtKeteranganCuti;

    // --- TAB 2: PELAYANAN PASIEN (RESEPSIONIS & KASIR) ---
    @FXML private TableView<JanjiTemu> tableAntrian;
    @FXML private TableColumn<JanjiTemu, String> colId, colPasien, colStatus, colAntrian;
    @FXML private TextField inputBayar; // Input nominal bayar

    // DAO
    private JadwalDAO jadwalDAO = new JadwalDAO();
    private CutiDAO cutiDAO = new CutiDAO();
    private DokterDAO dokterDAO = new DokterDAO();
    private JanjiTemuDAO janjiDAO = new JanjiTemuDAO();
    private PembayaranDAO bayarDAO = new PembayaranDAO();

    @FXML
    public void initialize() {
        // Init Tab 1
        comboDokterCuti.getItems().setAll(dokterDAO.getAllDokter());

        // Init Tab 2 (Setup Tabel)
        setupTable();
        refreshTabelAntrian();
    }

    // --- LOGIC TAB 1: CUTI & GENERATE ---

    @FXML
    public void onSimpanCuti() {
        Dokter d = comboDokterCuti.getValue();
        if (d != null && tglMulaiCuti.getValue() != null && tglSelesaiCuti.getValue() != null) {
            boolean sukses = cutiDAO.tambahCuti(
                    d.getIdDokter(),
                    Date.valueOf(tglMulaiCuti.getValue()),
                    Date.valueOf(tglSelesaiCuti.getValue()),
                    txtKeteranganCuti.getText()
            );
            if(sukses) showAlert("Sukses", "Data Cuti Berhasil Disimpan");
            else showAlert("Error", "Gagal simpan cuti");
        }
    }

    @FXML
    public void onGenerateJadwal() {
        jadwalDAO.generateJadwal();
        showAlert("Info", "Jadwal 2 Minggu ke depan berhasil digenerate!");
    }

    // --- LOGIC TAB 2: KONFIRMASI & BAYAR ---

    private void setupTable() {
        colId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdJanjiTemu()));
        colPasien.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdPasien())); // Harusnya nama, tp ID dulu utk simpel
        colStatus.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus()));
        colAntrian.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getNomorAntrian())));
    }

    @FXML
    public void onRefreshTabel() {
        refreshTabelAntrian();
    }

    private void refreshTabelAntrian() {
        List<JanjiTemu> list = janjiDAO.getJanjiTemuHariIni();
        tableAntrian.getItems().setAll(list);
    }

    @FXML
    public void onKonfirmasiHadir() {
        JanjiTemu selected = tableAntrian.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Ubah status PENDING -> ARRIVED/WAITING
            janjiDAO.updateStatusJanji(selected.getIdJanjiTemu(), "ARRIVED");
            refreshTabelAntrian();
            showAlert("Sukses", "Pasien dikonfirmasi hadir. Silakan minta pembayaran.");
        }
    }

    @FXML
    public void onProsesBayar() {
        JanjiTemu selected = tableAntrian.getSelectionModel().getSelectedItem();
        if (selected != null && !inputBayar.getText().isEmpty()) {
            double nominal = Double.parseDouble(inputBayar.getText());

            // Simpan Pembayaran & Update Status jadi CONFIRMED (Siap Masuk Poli)
            boolean sukses = bayarDAO.prosesPembayaran(selected.getIdJanjiTemu(), nominal, "CASH");

            if (sukses) {
                showAlert("Sukses", "Pembayaran Lunas. Pasien siap dipanggil Dokter.");
                refreshTabelAntrian();
            }
        } else {
            showAlert("Warning", "Pilih pasien dan isi nominal bayar.");
        }
    }

    private void showAlert(String title, String content) {
        new Alert(Alert.AlertType.INFORMATION, content).show();
    }
}