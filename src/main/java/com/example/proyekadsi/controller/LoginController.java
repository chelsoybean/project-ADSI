package com.example.proyekadsi.controller;

import com.example.proyekadsi.HelloApplication;
import com.example.proyekadsi.DAO.AkunPenggunaDAO;
import com.example.proyekadsi.model.AkunPengguna;
import com.example.proyekadsi.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField inputIdField; // Dulu noHpField
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleBox;
    @FXML private TextField namaRegisterField;

    private AkunPenggunaDAO dao = new AkunPenggunaDAO();

    @FXML
    public void initialize() {
        roleBox.getItems().addAll("USER", "ADMIN", "DOKTER");
        roleBox.setValue("USER"); // Default pilihan awal

        // Listener: Ubah tampilan berdasarkan Role yang dipilih
        roleBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateUI(newVal);
        });

        // Jalankan updateUI pertama kali
        updateUI("USER");
    }

    // Method untuk mengubah teks/visibility field
    private void updateUI(String role) {
        // Reset Register field setiap ganti role
        namaRegisterField.setVisible(false);
        namaRegisterField.setManaged(false);
        inputIdField.clear();
        passwordField.clear();

        if ("USER".equals(role)) {
            // Tampilan untuk PASIEN
            inputIdField.setPromptText("Masukkan Nomor HP");
            passwordField.setVisible(false); // Pasien gak butuh password
            passwordField.setManaged(false); // Biar layout naik ke atas (tidak makan tempat)
        } else {
            // Tampilan untuk ADMIN & DOKTER
            inputIdField.setPromptText("Masukkan Username");
            passwordField.setVisible(true);
            passwordField.setManaged(true);
        }
    }

    @FXML
    public void onLoginClick() {
        String role = roleBox.getValue();
        String inputId = inputIdField.getText(); // Bisa No HP atau Username
        String pass = passwordField.getText();

        if (inputId.isEmpty()) {
            showAlert("Warning", "Kolom identitas tidak boleh kosong!");
            return;
        }

        if ("USER".equals(role)) {
            // --- LOGIC USER (PASIEN) ---
            handleLoginUser(inputId);
        } else {
            // --- LOGIC ADMIN & DOKTER ---
            handleLoginAdminDokter(inputId, pass, role);
        }
    }

    private void handleLoginAdminDokter(String username, String pass, String role) {
        if (pass.isEmpty()) {
            showAlert("Error", "Password wajib diisi!");
            return;
        }

        // Di database, kolom 'no_hp' kita anggap sebagai 'username' untuk admin
        AkunPengguna user = dao.authenticateAdminDokter(username, pass, role);

        if (user != null) {
            SessionManager.setLoggedInUser(user);
            if (role.equals("ADMIN")) gotoPage("admin-view.fxml");
            else gotoPage("dokter-view.fxml");
        } else {
            showAlert("Login Gagal", "Username atau Password salah!");
        }
    }

    private void handleLoginUser(String noHp) {
        AkunPengguna user = dao.cekUserByNoHP(noHp);

        if (user != null) {
            // Login Berhasil
            SessionManager.setLoggedInUser(user);
            gotoPage("user-view.fxml");
        } else {
            // Tawarkan Register
            if (!namaRegisterField.isVisible()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "Nomor HP belum terdaftar. Silakan isi Nama Anda, lalu klik tombol Masuk lagi.",
                        ButtonType.OK);
                alert.showAndWait();

                namaRegisterField.setVisible(true);
                namaRegisterField.setManaged(true);
                namaRegisterField.requestFocus();
            } else {
                // Proses Register
                String nama = namaRegisterField.getText();
                if (nama.isEmpty()) {
                    showAlert("Warning", "Nama tidak boleh kosong!");
                    return;
                }

                if (dao.registerUser(noHp, nama)) {
                    showAlert("Sukses", "Pendaftaran Berhasil! Login otomatis...");
                    namaRegisterField.setVisible(false);
                    namaRegisterField.setManaged(false);
                    handleLoginUser(noHp); // Rekursif login
                }
            }
        }
    }

    private void gotoPage(String fxml) {
        try {
            Stage stage = (Stage) inputIdField.getScene().getWindow();
            Scene scene = new Scene(new FXMLLoader(HelloApplication.class.getResource(fxml)).load());
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("System Error", "Gagal memuat halaman " + fxml);
        }
    }

    private void showAlert(String title, String content) {
        new Alert(Alert.AlertType.WARNING, content).show();
    }
}