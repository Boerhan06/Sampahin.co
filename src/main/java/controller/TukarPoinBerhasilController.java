package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class TukarPoinBerhasilController implements Initializable {

    @FXML
    private Label lblTotalPoin;

    @FXML
    private Label lblSuccess;

    @FXML
    private Button btnKembali;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Set Data Dummy (Ambil dari database nanti)
        lblTotalPoin.setText("Total Poin: 10.500");

        // 2. Setup Aksi Tombol
        btnKembali.setOnAction(event -> {
            System.out.println("Navigasi: Kembali ke Halaman Utama");
            // TODO: Masukkan kode untuk pindah scene di sini
        });

        // 3. Jalankan Animasi
        playSuccessAnimation();
    }

    private void playSuccessAnimation() {
        // Animasi Pop-Up (Meletup) untuk teks Berhasil
        lblSuccess.setScaleX(0);
        lblSuccess.setScaleY(0);

        ScaleTransition st = new ScaleTransition(Duration.millis(800), lblSuccess);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setInterpolator(javafx.animation.Interpolator.EASE_OUT); // Efek elastis membal sedikit

        // Animasi Fade In untuk Tombol Kembali
        btnKembali.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(1000), btnKembali);
        ft.setToValue(1.0);
        ft.setDelay(Duration.millis(600)); // Muncul setelah teks selesai

        st.play();
        ft.play();
    }
}