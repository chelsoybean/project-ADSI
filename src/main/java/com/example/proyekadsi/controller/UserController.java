package com.example.proyekadsi.controller;

import com.example.proyekadsi.DAO.*;
import com.example.proyekadsi.model.*;
import com.example.proyekadsi.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.sql.Date;

public class UserController {
    @FXML private ComboBox<Dokter> comboDokter;
    @FXML private ListView<JadwalPraktek> listJadwal;

    // Dropdown pilih pasien (Satu HP bisa punya banyak pasien: Bapak, Ibu, Anak)
    @FXML private ComboBox<Pasien> comboPasien;

    // Form Tambah Pasien (Muncul jika user klik "+ Pasien Baru")
    @FXML private VBox boxTambahPasien;
    @FXML private TextField inputNama, inputNIK;
    @FXML private DatePicker inputTglLahir;

    @FXML private Label lblStatus; // Notifikasi nomor antrian

    private DokterDAO dokterDAO = new DokterDAO();
    private JadwalDAO jadwalDAO = new JadwalDAO();
    private PasienDAO pasienDAO = new PasienDAO();
    private JanjiTemuDAO janjiDAO = new JanjiTemuDAO();

    @FXML
    public void initialize() {
        // 1. User lgsg bisa lihat dokter & jadwal (meski blm pilih pasien)
        comboDokter.getItems().setAll(dokterDAO.getAllDokter());

        // 2. Load daftar pasien yg tertaut dgn HP ini
        loadPasienList();

        // Sembunyikan form tambah pasien dulu
        boxTambahPasien.setVisible(false);
        boxTambahPasien.setManaged(false);
    }

    private void loadPasienList() {
        String idAkun = SessionManager.getLoggedInUser().getIdAkunPengguna();
        comboPasien.getItems().setAll(pasienDAO.getPasienByAkun(idAkun));

        // Jika list kosong (Akun baru), otomatis buka form tambah pasien
        if (comboPasien.getItems().isEmpty()) {
            onShowTambahPasien();
            lblStatus.setText("Silakan daftarkan data diri pasien terlebih dahulu.");
        }
    }

    // Dipanggil saat dropdown Dokter dipilih
    @FXML
    public void onDokterSelected() {
        if(comboDokter.getValue() != null) {
            String id = comboDokter.getValue().getIdDokter();
            listJadwal.getItems().setAll(jadwalDAO.getJadwalByDokter(id));
        }
    }

    // Tombol "+ Pasien Baru"
    @FXML
    public void onShowTambahPasien() {
        boxTambahPasien.setVisible(true);
        boxTambahPasien.setManaged(true);
        System.out.println("Show Form Dipanggil");

        inputNama.requestFocus();
    }

    // Tombol "Simpan Data Pasien"
    // INI BAGIAN: "melengkapi nama, NIK, dan tanggal lahir"
    @FXML
    public void onSimpanPasien() {
        String nama = inputNama.getText();
        String nik = inputNIK.getText();

        if(nama.isEmpty() || nik.isEmpty() || inputTglLahir.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Isi Nama, NIK, dan Tgl Lahir!").show();
            return;
        }

        String idAkun = SessionManager.getLoggedInUser().getIdAkunPengguna();

        if(pasienDAO.tambahPasien(idAkun, nama, nik, Date.valueOf(inputTglLahir.getValue()))) {
            new Alert(Alert.AlertType.INFORMATION, "Data Pasien Tersimpan!").show();

            // Refresh dropdown dan pilih pasien yg baru dibuat
            loadPasienList();
            comboPasien.getSelectionModel().selectLast();

            // Tutup form
            boxTambahPasien.setVisible(false);
            boxTambahPasien.setManaged(false);
            inputNama.clear(); inputNIK.clear();
        } else {
        new Alert(Alert.AlertType.ERROR, "Gagal menyimpan data pasien! Periksa console.").show();
    }
    }

    @FXML
    public void onBuatJanji() {
        Pasien p = comboPasien.getValue();
        JadwalPraktek j = listJadwal.getSelectionModel().getSelectedItem();
        Dokter d = comboDokter.getValue();

        if (p == null || j == null) {
            new Alert(Alert.AlertType.WARNING, "Lengkapi data dulu!").show();
            return;
        }

        // Panggil DAO yang baru (Return String)
        String hasil = janjiDAO.buatJanji(p.getIdPasien(), j.getIdJadwal(), d.getIdDokter(), j.getTanggal());

        if (hasil.startsWith("OK:")) {
            String noAntrian = hasil.split(":")[1];
            lblStatus.setText("BERHASIL! No Antrian: " + noAntrian);
            new Alert(Alert.AlertType.INFORMATION, "Sukses! No Antrian: " + noAntrian).show();
        } else {
            // TAMPILKAN ERROR ASLI DARI DATABASE
            new Alert(Alert.AlertType.ERROR, hasil).show();
        }
    }
}