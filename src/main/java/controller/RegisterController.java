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


    @FXML private VBox formContainer; // Container utama untuk dianimasikan
    @FXML private TextField namaField;
    @FXML private TextField emailField;
    @FXML private TextField telpField;
    @FXML private TextField alamatField;
    @FXML private Button btnLanjut;
    @FXML private Hyperlink linkLogin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        playEntranceAnimations();


        loadSavedData();


    }



    @FXML
    private void handleLanjut() {
        String nama = namaField.getText().trim();
        String email = emailField.getText().trim();
        String telp = telpField.getText().trim();
        String alamat = alamatField.getText().trim();


        if (nama.isEmpty() || email.isEmpty() || telp.isEmpty() || alamat.isEmpty()) {
            showAlert("Data Belum Lengkap", "Mohon isi semua kolom data diri.");
            return;
        }


        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert("Format Salah", "Format email tidak valid (contoh: user@email.com).");
            return;
        }


        if (!telp.matches("\\d{10,15}")) {
            showAlert("Format Salah", "Nomor telepon harus berupa angka (10-15 digit).");
            return;
        }


        SessionManager.getInstance().setTempRegistrationData(nama, email, telp, alamat);
        System.out.println("✅ Data Tahap 1 Tersimpan: " + nama);


        Main.showRegisterNextView();
    }


    @FXML
    private void handleBackToLogin() {

        SessionManager.getInstance().clearRegistration();
        Main.showLoginView();
    }



    private void playEntranceAnimations() {
        if (formContainer != null) {

            formContainer.setOpacity(0);
            formContainer.setTranslateY(50);


            FadeTransition fade = new FadeTransition(Duration.millis(800), formContainer);
            fade.setToValue(1);


            TranslateTransition slide = new TranslateTransition(Duration.millis(800), formContainer);
            slide.setToY(0);


            fade.play();
            slide.play();
        }
    }

    private void loadSavedData() {

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
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}