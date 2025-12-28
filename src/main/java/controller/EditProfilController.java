package controller;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditProfilController implements Initializable {

    // --- Komponen FXML (Sesuai dengan fx:id di FXML) ---
    @FXML private Circle profileImage;
    @FXML private TextField txtNama;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnSimpan;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Setup Data Awal (Dummy Data)
        loadProfilData();

        // 2. Setup Animasi Tombol (Sesuai request Anda sebelumnya)
        setupButtonAnimation();
    }

    private void loadProfilData() {
        // Mengisi form dengan data saat ini (contoh)
        txtNama.setText("Admin Utama");
        txtUsername.setText("admin_01");

        // Set gambar default jika ada (Placeholder image)
        // Image defaultImage = new Image("/image/default_profile.png");
        // profileImage.setFill(new ImagePattern(defaultImage));
    }

    // --- Logika Tombol Simpan ---
    @FXML
    private void handleSimpan() {
        String nama = txtNama.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (nama.isEmpty() || username.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Nama dan Username tidak boleh kosong!");
            return;
        }

        // Simulasi proses simpan
        System.out.println("Menyimpan Data...");
        System.out.println("Nama: " + nama);
        System.out.println("Username: " + username);
        if (!password.isEmpty()) {
            System.out.println("Password diubah.");
        }

        showAlert(Alert.AlertType.INFORMATION, "Sukses", "Profil berhasil diperbarui!");
    }

    // --- Logika Ganti Foto (FileChooser) ---
    @FXML
    private void handleUbahFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Foto Profil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        // Mendapatkan Stage saat ini untuk membuka dialog
        Stage stage = (Stage) btnSimpan.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                // Mengubah file menjadi Image dan memasukkannya ke dalam Circle (Avatar)
                Image image = new Image(selectedFile.toURI().toString());
                profileImage.setFill(new ImagePattern(image));

                System.out.println("Foto profil diubah: " + selectedFile.getName());
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat gambar.");
            }
        }
    }

    // --- Animasi (Dari kode lama Anda) ---
    private void setupButtonAnimation() {
        // Efek membesar sedikit saat mouse masuk
        btnSimpan.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), btnSimpan);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });

        // Kembali ke ukuran normal saat mouse keluar
        btnSimpan.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), btnSimpan);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }

    // --- Navigasi Sidebar (Wajib ada agar tidak error "Method Not Found") ---

    @FXML
    private void handleDashboard(MouseEvent event) {
        navigateTo(event, "Dashboard.fxml", "Dashboard");
    }

    @FXML
    private void handleUsers(MouseEvent event) {
        navigateTo(event, "DaftarPengguna.fxml", "Daftar Pengguna");
    }

    @FXML
    private void handleWaste(MouseEvent event) {
        navigateTo(event, "PemasukanSampah.fxml", "Pemasukan Sampah");
    }

    @FXML
    private void handleWithdraw(MouseEvent event) {
        navigateTo(event, "RiwayatPenarikan.fxml", "Riwayat Penarikan");
    }

    @FXML
    private void handleLogout(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Logout");
        alert.setHeaderText(null);
        alert.setContentText("Apakah Anda yakin ingin keluar?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                navigateTo(event, "Login.fxml", "Login");
            }
        });
    }

    // Helper Method Navigasi
    private void navigateTo(MouseEvent event, String fxmlFile, String title) {
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();

            // Asumsi file FXML ada di folder /view/
            Parent root = FXMLLoader.load(getClass().getResource("/view/" + fxmlFile));
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Navigasi", "Tidak dapat membuka halaman: " + fxmlFile);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}