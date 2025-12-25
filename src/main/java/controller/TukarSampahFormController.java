package controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class TukarSampahFormController implements Initializable {

    @FXML
    private VBox formContainer; // Menampung semua input

    @FXML
    private HBox buttonContainer; // Menampung tombol Done/Cancel

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Animasi Form Input (Muncul satu per satu)
        playFormAnimation();

        // 2. Animasi Tombol (Muncul setelah form selesai)
        playButtonAnimation();
    }

    private void playFormAnimation() {
        int delay = 0;

        // Loop melalui setiap anak (VBox input-group) di dalam formContainer
        for (Node node : formContainer.getChildren()) {
            // Set kondisi awal (Transparan & geser ke bawah sedikit)
            node.setOpacity(0);
            node.setTranslateY(30);

            // Fade In
            FadeTransition fade = new FadeTransition(Duration.millis(600), node);
            fade.setToValue(1);
            fade.setDelay(Duration.millis(delay));

            // Slide Up
            TranslateTransition translate = new TranslateTransition(Duration.millis(600), node);
            translate.setToY(0);
            translate.setDelay(Duration.millis(delay));
            translate.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

            fade.play();
            translate.play();

            // Tambah delay untuk item berikutnya (Cascading Effect)
            delay += 150;
        }
    }

    private void playButtonAnimation() {
        // Tombol muncul agak lambat setelah form mulai muncul
        buttonContainer.setOpacity(0);
        buttonContainer.setTranslateY(20);

        FadeTransition fade = new FadeTransition(Duration.millis(800), buttonContainer);
        fade.setToValue(1);
        fade.setDelay(Duration.millis(800)); // Delay lebih lama

        TranslateTransition translate = new TranslateTransition(Duration.millis(800), buttonContainer);
        translate.setToY(0);
        translate.setDelay(Duration.millis(800));
        translate.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        fade.play();
        translate.play();
    }
}