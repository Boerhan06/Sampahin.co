package controller;

import com.sampahin.Main;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import util.SessionManager;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    // --- Komponen FXML ---
    @FXML private VBox formContainer; // Container utama untuk dianimasikan
    @FXML private TextField namaField;
    @FXML private TextField emailField;
    @FXML private TextField telpField;
    @FXML private TextField alamatField;
    @FXML private Button btnLanjut;
    @FXML private Hyperlink linkLogin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Jalankan Animasi Masuk
        playEntranceAnimations();

        // 2. Isi ulang data jika user kembali dari Tahap 2 (User Experience)
        loadSavedData();

        // Event handler manual (Backup jika FXML onAction bermasalah)
        // Tapi utamakan penggunaan onAction di FXML
    }

    // --- METHOD NAVIGASI & AKSI ---

    @FXML
    private void handleLanjut() {
        String nama = namaField.getText().trim();
        String email = emailField.getText().trim();
        String telp = telpField.getText().trim();
        String alamat = alamatField.getText().trim();

        // 1. Validasi Kosong
        if (nama.isEmpty() || email.isEmpty() || telp.isEmpty() || alamat.isEmpty()) {
            showAlert("Data Belum Lengkap", "Mohon isi semua kolom data diri.");
            return;
        }

        // 2. Validasi Format Email (Simple Regex)
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert("Format Salah", "Format email tidak valid (contoh: user@email.com).");
            return;
        }

        // 3. Validasi No Telepon (Harus Angka & Minimal 10 digit)
        if (!telp.matches("\\d{10,15}")) {
            showAlert("Format Salah", "Nomor telepon harus berupa angka (10-15 digit).");
            return;
        }

        // 4. Simpan ke SessionManager (Agar dibawa ke Tahap 2)
        SessionManager.getInstance().setTempRegistrationData(nama, email, telp, alamat);
        System.out.println("✅ Data Tahap 1 Tersimpan: " + nama);

        // 5. Pindah ke Halaman Berikutnya
        Main.showRegisterNextView();
    }

    // Method ini WAJIB ADA karena dipanggil oleh onAction="#handleBackToLogin" di FXML
    @FXML
    private void handleBackToLogin() {
        // Bersihkan data sampah jika batal daftar
        SessionManager.getInstance().clearRegistration();
        Main.showLoginView();
    }

    // --- HELPER METHODS ---

    private void playEntranceAnimations() {
        if (formContainer != null) {
            // Set posisi awal (Agak di bawah & transparan)
            formContainer.setOpacity(0);
            formContainer.setTranslateY(50);

            // Animasi Fade In (Muncul perlahan)
            FadeTransition fade = new FadeTransition(Duration.millis(800), formContainer);
            fade.setToValue(1);

            // Animasi Slide Up (Naik ke atas)
            TranslateTransition slide = new TranslateTransition(Duration.millis(800), formContainer);
            slide.setToY(0);

            // Mainkan bersamaan
            fade.play();
            slide.play();
        }
    }

    private void loadSavedData() {
        // Mengambil data dari SessionManager (jika ada)
        SessionManager session = SessionManager.getInstance();

        if (session.getTempNama() != null) {
            namaField.setText(session.getTempNama());
        }
        if (session.getTempEmail() != null) {
            emailField.setText(session.getTempEmail());
        }
        if (session.getTempTelepon() != null) {
            telpField.setText(session.getTempTelepon());
        }
        if (session.getTempAlamat() != null) {
            alamatField.setText(session.getTempAlamat());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING); // Gunakan tipe Warning agar kuning (tidak menakutkan)
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}