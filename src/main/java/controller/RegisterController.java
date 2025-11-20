package controller;

import dao.AdminDAO;
import models.Admin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class RegisterController {
    
    // HANYA 3 FIELD INI YANG ADA DI FXML 
    @FXML private TextField namaField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    
    @FXML private Label statusLabel;

    // PERBAIKAN: Menggunakan AdminDAO
    private AdminDAO adminDAO;

    public RegisterController() {
        this.adminDAO = new AdminDAO();
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        // 1. Ambil data dari 3 form saja
        String nama = namaField.getText();
        String username = usernameField.getText();
        String pass = passwordField.getText();

        // 2. Validasi Input
        if (nama.isEmpty() || username.isEmpty() || pass.isEmpty()) {
            statusLabel.setText("Isi Nama, Username, dan Password!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // 3. ISI DATA DUMMY (Karena di FXML simple tidak ada inputnya)
        // Tapi database WAJIB diisi (NOT NULL)
        String emailDummy = username + "@admin.sampahin.co"; 
        String teleponDummy = "0800000000";
        String alamatDummy = "Kantor Pusat";
        
        // PERBAIKAN: Generate ID Admin, bukan ID Kartu
        String idAdmin = "ADM-" + new Random().nextInt(9999);
        
        // 4. Buat Objek ADMIN Baru
        // (Pastikan urutan ini sesuai dengan Constructor di models/Admin.java)
        Admin adminBaru = new Admin(
                nama, 
                alamatDummy,  
                teleponDummy, 
                emailDummy,    
                username, 
                pass,      // Password Plain
                idAdmin    // ID Admin
        );

        // 5. Simpan ke Database via AdminDAO
        // Ingat: adminDAO.save butuh objek admin DAN password plain
        boolean isSuccess = adminDAO.save(adminBaru, pass);

        if (isSuccess) {
            showAlert("Sukses", "Akun ADMIN berhasil dibuat! Silakan Login.");
            handleBackToLogin(event);
        } else {
            statusLabel.setText("Gagal. Username mungkin sudah ada.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        try {
            URL fxmlUrl = getClass().getResource("/view/LoginView.fxml");
            if (fxmlUrl == null) return;

            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            stage.setTitle("Login - Sampahin.co");
            stage.setScene(new Scene(root));
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}