package controller;

import com.sampahin.Main;
import dao.AdminDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import models.Admin;
import util.DatabaseConnection;
import util.SessionManager;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class RegisterNextController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField adminPinField; // Field ini sekarang sudah ada di FXML
    @FXML private Button btnBuatAkun;
    @FXML private Hyperlink linkKembali;

    private AdminDAO adminDAO = new AdminDAO();
    private static final String SECRET_PIN = "ADMIN123";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Binding tombol secara manual
        btnBuatAkun.setOnAction(event -> handleFinalRegister());
        linkKembali.setOnAction(event -> Main.showRegisterView());
    }

    private void handleFinalRegister() {
        // Ambil input
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String pin = adminPinField.getText();

        // 1. Validasi Input Kosong
        if (username.isEmpty() || password.isEmpty() || pin.isEmpty()) {
            showAlert("Data Kurang", "Mohon isi Username, Password, dan PIN.");
            return;
        }

        // 2. Cek PIN
        if (!pin.equals(SECRET_PIN)) {
            showAlert("Gagal", "PIN Admin Salah! Anda tidak memiliki izin.");
            return;
        }

        // 3. Ambil Data dari Sesi (Tahap 1)
        SessionManager session = SessionManager.getInstance();
        String nama = session.getTempNama();

        // Debugging: Cek apakah data sesi ada
        if (nama == null) {
            showAlert("Error Sesi", "Data tahap 1 hilang. Silakan kembali dan isi ulang.");
            return;
        }

        // 4. Cek Koneksi DB
        if (!DatabaseConnection.testConnection()) {
            showAlert("Koneksi Error", "Tidak dapat terhubung ke Database.");
            return;
        }

        // 5. Cek Duplikasi Username
        if (adminDAO.getAdminByUsername(username) != null) {
            showAlert("Username Terpakai", "Username ini sudah ada, pilih yang lain.");
            return;
        }

        try {
            // Setup Objek Admin
            Admin newAdmin = new Admin();
            newAdmin.setNamaLengkap(nama);
            newAdmin.setEmail(session.getTempEmail());
            newAdmin.setNoTelepon(session.getTempTelepon());
            newAdmin.setAlamat(session.getTempAlamat());
            newAdmin.setUsername(username);
            newAdmin.setIsActive(true);

            // Generate ID
            String idAdmin = "ADM-" + (1000 + new Random().nextInt(9000));
            newAdmin.setIdAdmin(idAdmin);

            // Simpan ke DB
            boolean success = adminDAO.save(newAdmin, password);

            if (success) {
                session.clearRegistration();
                System.out.println("✅ Akun berhasil dibuat: " + username);

                // Pesan Sukses
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Berhasil");
                alert.setHeaderText(null);
                alert.setContentText("Registrasi Berhasil! Silakan Login.");
                alert.showAndWait();

                // Pindah ke Login
                Main.showLoginView();
            } else {
                showAlert("Gagal", "Database menolak penyimpanan data.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error Sistem", e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}