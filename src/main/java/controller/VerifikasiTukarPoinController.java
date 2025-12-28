package controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class VerifikasiTukarPoinController implements Initializable {

    @FXML
    private VBox formContainer; // Container utama form

    @FXML
    private TextField tfInput1;

    @FXML
    private TextField tfInput2;

    @FXML
    private Button btnSetuju;

    @FXML
    private Button btnBatalkan;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Setup Animasi Masuk
        playEntranceAnimation();

        // 2. Setup Aksi Tombol
        btnSetuju.setOnAction(event -> handleSetuju());
        btnBatalkan.setOnAction(event -> handleBatalkan());
    }

    private void playEntranceAnimation() {
        // Posisi awal agak turun dan transparan
        formContainer.setOpacity(0);
        formContainer.setTranslateY(50);

        // Animasi Fade In
        FadeTransition fade = new FadeTransition(Duration.millis(1000), formContainer);
        fade.setFromValue(0);
        fade.setToValue(1);

        // Animasi Slide Up
        TranslateTransition translate = new TranslateTransition(Duration.millis(1000), formContainer);
        translate.setFromY(50);
        translate.setToY(0);
        translate.setInterpolator(javafx.animation.Interpolator.EASE_OUT); // Gerakan smooth

        fade.play();
        translate.play();
    }

    private void handleSetuju() {
        System.out.println("Tombol Setuju Diklik. Proses Penukaran...");
        // TODO: Tambahkan logika simpan ke database atau pindah ke halaman 'Berhasil'
        // Contoh pindah view bisa diletakkan di sini
    }

    private void handleBatalkan() {
        System.out.println("Tombol Batalkan Diklik. Kembali ke menu...");
        // TODO: Tambahkan logika kembali ke halaman sebelumnya
    }
}