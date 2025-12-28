package controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class TukarPoinGagalController implements Initializable {

    @FXML
    private Label lblTotalPoin;

    @FXML
    private Label lblMessage;

    @FXML
    private Button btnKembali;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Set Data Dummy (Ambil dari database nanti)
        lblTotalPoin.setText("Total Poin: 12.500");

        // 2. Setup Aksi Tombol
        btnKembali.setOnAction(event -> {
            System.out.println("Navigasi: Kembali ke Halaman Utama / Ulangi");
            // TODO: Masukkan kode untuk pindah scene di sini
        });

        // 3. Jalankan Animasi
        playFailureAnimation();
    }

    private void playFailureAnimation() {
        // Animasi Shake (Guncangan) untuk teks Gagal
        TranslateTransition shake = new TranslateTransition(Duration.millis(100), lblMessage);
        shake.setFromX(0);
        shake.setToX(10); // Geser kanan sedikit
        shake.setCycleCount(6); // Ulangi 6 kali (kiri-kanan-kiri...)
        shake.setAutoReverse(true);
        
        // Animasi Fade In untuk Tombol Kembali
        btnKembali.setOpacity(0);
        FadeTransition fadeBtn = new FadeTransition(Duration.millis(1000), btnKembali);
        fadeBtn.setToValue(1.0);
        fadeBtn.setDelay(Duration.millis(500)); // Muncul setelah teks selesai goyang

        shake.play();
        fadeBtn.play();
    }
}