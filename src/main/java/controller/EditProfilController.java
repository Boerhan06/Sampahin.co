package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class EditProfilController implements Initializable {

    @FXML
    private VBox profileCard; // Hubungkan ke fx:id="profileCard"

    @FXML
    private Button btnSimpan; // Hubungkan ke fx:id="btnSimpan"

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Jalankan animasi masuk untuk kartu profil
        playEntranceAnimation();

        // 2. Tambahkan efek hover tambahan untuk tombol simpan
        setupButtonAnimation();
    }

    private void playEntranceAnimation() {
        // Set kondisi awal (sedikit turun dan transparan)
        profileCard.setOpacity(0);
        profileCard.setTranslateY(30);

        // Animasi Fade In (Muncul)
        FadeTransition fade = new FadeTransition(Duration.millis(800), profileCard);
        fade.setFromValue(0);
        fade.setToValue(1);

        // Animasi Slide Up (Naik ke posisi semula)
        TranslateTransition translate = new TranslateTransition(Duration.millis(800), profileCard);
        translate.setFromY(30);
        translate.setToY(0);

        // Efek 'Overshoot' kecil agar terlihat elastis
        translate.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        fade.play();
        translate.play();
    }

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
}