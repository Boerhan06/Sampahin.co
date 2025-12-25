package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MapsDetailController implements Initializable {

    @FXML
    private VBox rightPanel; // Panel Utama Kanan

    @FXML
    private VBox infoCard;   // Kartu Info (Admin/Lokasi)

    @FXML
    private VBox statCard1;  // Kartu Grafik

    @FXML
    private VBox statCard2;  // Kartu Poin

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Animasi Panel Kanan (Muncul dari kanan layar)
        playPanelEntrance();

        // 2. Animasi Elemen di dalamnya (Muncul setelah panel masuk)
        playElementEntrance(infoCard, 400);
        playElementEntrance(statCard1, 600);
        playElementEntrance(statCard2, 700);
    }

    private void playPanelEntrance() {
        // Set posisi awal di luar layar (sebelah kanan)
        rightPanel.setTranslateX(500);

        TranslateTransition slide = new TranslateTransition(Duration.millis(800), rightPanel);
        slide.setToX(0); // Kembali ke posisi 0 (terlihat)
        // Efek Ease Out agar pendaratan animasi halus
        slide.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
        slide.play();
    }

    private void playElementEntrance(Node node, int delay) {
        // Set awal transparan dan sedikit kecil
        node.setOpacity(0);
        node.setScaleX(0.8);
        node.setScaleY(0.8);

        // Fade In
        FadeTransition fade = new FadeTransition(Duration.millis(500), node);
        fade.setToValue(1);
        fade.setDelay(Duration.millis(delay));

        // Scale Up (Membesar normal)
        ScaleTransition scale = new ScaleTransition(Duration.millis(500), node);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.setDelay(Duration.millis(delay));
        // Efek membal sedikit (bouncy)
        scale.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        fade.play();
        scale.play();
    }
}