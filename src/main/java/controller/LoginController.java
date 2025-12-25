package controller;

import com.sampahin.Main;
import util.SessionManager;
import dao.AdminDAO; // UBAH: Gunakan DAO khusus Admin
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Admin; // UBAH: Gunakan Model Admin

public class LoginController {

    // --- Elemen UI sesuai fx:id di FXML ---
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;
    @FXML private CheckBox rememberCheckBox; // Tambahan: Ada di FXML

    // --- DAO ---
    // Pastikan Anda sudah membuat class AdminDAO
    private AdminDAO adminDAO = new AdminDAO();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // 1. Validasi Input Kosong
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username dan password harus diisi!");
            // Style merah untuk error (opsional)
            errorLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // 2. Cek Admin di Database (Bukan Pengguna)
        Admin admin = adminDAO.getAdminByUsername(username);

        if (admin == null) {
            errorLabel.setText("Username tidak ditemukan atau bukan Admin!");
            return;
        }

        // 3. Cek Password
        // Asumsi: Class Akun (Parent) memiliki method checkPassword()
        if (!admin.checkPassword(password)) {
            errorLabel.setText("Password salah!");
            return;
        }

        // 4. Login Berhasil
        System.out.println("✅ Login Admin berhasil: " + admin.getNamaLengkap());
        errorLabel.setText("");

        // 5. Simpan ke SessionManager
        SessionManager.getInstance().setLoggedInAkun(admin);

        // 6. Bersihkan Field
        usernameField.clear();
        passwordField.clear();

        // 7. Pindah ke Dashboard
        Main.showDashboardView();
    }

    // Sesuai dengan FXML: onAction="#handleRegister"
    @FXML
    private void handleRegister() {
        // Arahkan ke View Register Admin
        Main.showRegisterView();
    }

    // Sesuai dengan FXML: onAction="#handleForgotPassword"
    @FXML
    private void handleForgotPassword() {
        // Implementasi Lupa Password (bisa alert sementara)
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("Silakan hubungi Super Admin untuk reset password.");
        alert.showAndWait();
    }
}