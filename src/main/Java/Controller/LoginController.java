// package Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Akun; // Import model Akun
import models.Admin;
import models.Pengguna;
// import services.AuthService; // Nanti Anda akan butuh ini
// import utils.SessionManager; // Dan ini

import java.io.IOException;

/**
 * Controller untuk mengelola LoginView.fxml (yang Anda sebut FromLogin).
 * TUGAS: Mengambil input, memvalidasi, dan menavigasi.
 * LOKASI: Seharusnya di package 'controller'.
 */
public class LoginController {

    // --- 1. Injeksi Komponen FXML ---
    // Nama variabel ini (misal: usernameField) harus SAMA PERSIS
    // dengan fx:id di file LoginView.fxml Anda.

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    // --- 2. Referensi ke Service (Best Practice) ---
    // private AuthService authService; // (Dibuat di constructor)

    public LoginController() {
        // Di aplikasi nyata, Anda akan inisialisasi service di sini
        // this.authService = new AuthService();
        System.out.println("LoginController diinisialisasi.");
    }

    // --- 3. Event Handler untuk Tombol Login ---

    /**
     * Method ini akan dipanggil ketika tombol login ditekan.
     * Harus di-set di FXML: onAction="#handleLoginButtonAction"
     */
    @FXML
    protected void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validasi simpel
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username dan password tidak boleh kosong.");
            return;
        }

        // --- 4. Logika Otentikasi ---
        // Di aplikasi nyata, Anda akan memanggil service:
        // Akun akun = authService.login(username, password);

        // --- !! Placeholder (Simulasi Login) !! ---
        // Ganti ini dengan koneksi ke AuthService & Database Anda
        Akun akun = mockLogin(username, password);
        // --- !! Batas Placeholder !! ---

        // --- 5. Navigasi ---
        if (akun != null) {
            // BERHASIL LOGIN
            System.out.println("Login Berhasil: " + akun.getNamaLengkap() + " sebagai " + akun.getRole());

            // Simpan sesi pengguna (Best practice)
            // SessionManager.getInstance().setLoggedInAkun(akun);

            // Tentukan dashboard mana yang akan dibuka
            String fxmlFile;
            if (akun.getRole().equals("Admin")) {
                fxmlFile = "/views/AdminDashboardView.fxml"; // Path ke FXML dashboard Admin
            } else if (akun.getRole().equals("Pengguna")) {
                fxmlFile = "/views/PenggunaDashboardView.fxml"; // Path ke FXML dashboard Pengguna
            } else {
                // (Tambahkan role 'Mitra' jika perlu)
                errorLabel.setText("Role tidak dikenal: " + akun.getRole());
                return;
            }

            // Panggil method navigasi
            navigateTo(event, fxmlFile);

        } else {
            // GAGAL LOGIN
            errorLabel.setText("Username atau password salah.");
        }
    }

    /**
     * Method helper untuk berpindah scene (tampilan).
     * @param event Event tombol yang memicu navigasi
     * @param fxmlFile Path ke file FXML tujuan (e.g., "/views/Dashboard.fxml")
     */
    private void navigateTo(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Gagal memuat halaman: " + e.getMessage());
        }
    }

    /**
     * [HANYA UNTUK TESTING - HAPUS NANTI]
     * Simulasi panggilan ke AuthService.
     */
    private Akun mockLogin(String username, String password) {
        if (username.equals("admin") && password.equals("admin123")) {
            // (Constructor Admin versi lengkap dari DB)
            return new Admin(1, "Admin Utama", "Kantor Pusat", "08123", "admin@sampahin.co", "admin",
                    "HASHED_admin123", true, java.time.LocalDateTime.now(), java.time.LocalDateTime.now(), "ADM001");
        }
        if (username.equals("pengguna") && password.equals("user123")) {
            // (Constructor Pengguna versi lengkap dari DB)
            return new Pengguna(2, "Burhan Abdur", "Purwakarta", "08567", "burhan@gmail.com", "pengguna",
                    "HASHED_user123", true, java.time.LocalDateTime.now(), java.time.LocalDateTime.now(),
                    "KRT001", "11223344", new BigDecimal("1500.0"), new BigDecimal("0.0"), java.time.LocalDate.now());
        }
        return null; // Login gagal
    }
}