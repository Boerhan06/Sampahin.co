package controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import models.Akun; // Import Akun

import java.net.URL;
import java.util.ResourceBundle;

public class TukarSampahController implements Initializable {

    @FXML private VBox searchContainer;
    @FXML private VBox userListContainer;
    @FXML private Button btnScanMaps;
    @FXML private Pane infoPanel;

    // --- Variabel Data ---
    private Akun currentAkun;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Animasi Header & Search Bar (Slide Down)
        playSlideDownAnimation(searchContainer, 0);

        // 2. Animasi Panel Kanan (Slide Left)
        playSlideLeftAnimation(btnScanMaps, 200);
        playSlideLeftAnimation(infoPanel, 300);

        // 3. Animasi List User (Slide Up Berurutan)
        playListCascadingAnimation();
    }

    public void setAkunData(Akun akun) {
        this.currentAkun = akun;
    }

    private void playSlideDownAnimation(Node node, int delay) {
        node.setOpacity(0);
        node.setTranslateY(-30);

        FadeTransition fade = new FadeTransition(Duration.millis(800), node);
        fade.setToValue(1);
        fade.setDelay(Duration.millis(delay));

        TranslateTransition translate = new TranslateTransition(Duration.millis(800), node);
        translate.setToY(0);
        translate.setDelay(Duration.millis(delay));

        fade.play();
        translate.play();
    }

    private void playSlideLeftAnimation(Node node, int delay) {
        node.setOpacity(0);
        node.setTranslateX(50); // Muncul dari kanan

        FadeTransition fade = new FadeTransition(Duration.millis(800), node);
        fade.setToValue(1);
        fade.setDelay(Duration.millis(delay));

        TranslateTransition translate = new TranslateTransition(Duration.millis(800), node);
        translate.setToX(0);
        translate.setDelay(Duration.millis(delay));

        fade.play();
        translate.play();
    }

    private void playListCascadingAnimation() {
        int delay = 400;

        for (Node node : userListContainer.getChildren()) {
            node.setOpacity(0);
            node.setTranslateY(50); // Muncul dari bawah

            FadeTransition fade = new FadeTransition(Duration.millis(600), node);
            fade.setToValue(1);
            fade.setDelay(Duration.millis(delay));

            TranslateTransition translate = new TranslateTransition(Duration.millis(600), node);
            translate.setToY(0);
            translate.setDelay(Duration.millis(delay));

            fade.play();
            translate.play();

            delay += 150; // Delay bertingkat
        }
    }
}