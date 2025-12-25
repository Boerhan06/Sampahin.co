package controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class NotifikasiController implements Initializable {

    @FXML
    private VBox notifContainer; // Container yang menampung semua HBox notifikasi

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Jalankan animasi cascading saat halaman dibuka
        playCascadingAnimation();
    }

    private void playCascadingAnimation() {
        int delay = 0;

        for (Node node : notifContainer.getChildren()) {
            // Set kondisi awal (Transparan & geser ke kanan)
            node.setOpacity(0);
            node.setTranslateX(50);

            // Animasi Fade In
            FadeTransition fade = new FadeTransition(Duration.millis(500), node);
            fade.setToValue(1);
            fade.setDelay(Duration.millis(delay));

            // Animasi Slide dari Kanan
            TranslateTransition translate = new TranslateTransition(Duration.millis(500), node);
            translate.setToX(0);
            translate.setDelay(Duration.millis(delay));
            // Efek ease-out agar pendaratan halus
            translate.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

            fade.play();
            translate.play();

            // Tambah delay untuk item berikutnya agar muncul berurutan
            delay += 100;
        }
    }
}