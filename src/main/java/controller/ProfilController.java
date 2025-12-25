package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfilController implements Initializable {

    @FXML
    private HBox profileCard;

    @FXML
    private VBox cardHistory;

    @FXML
    private VBox cardPoints;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Animasi Kartu Profil Utama (Zoom In + Fade)
        playMainCardAnimation();

        // 2. Animasi Kartu Bawah (Slide Up)
        playBottomCardAnimation(cardHistory, 200);
        playBottomCardAnimation(cardPoints, 350);
    }

    private void playMainCardAnimation() {
        profileCard.setOpacity(0);
        profileCard.setScaleX(0.9);
        profileCard.setScaleY(0.9);

        FadeTransition fade = new FadeTransition(Duration.millis(800), profileCard);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(800), profileCard);
        scale.setToX(1.0);
        scale.setToY(1.0);
        // Efek membal halus
        scale.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        fade.play();
        scale.play();
    }

    private void playBottomCardAnimation(Node node, int delay) {
        node.setOpacity(0);
        node.setTranslateY(50);

        FadeTransition fade = new FadeTransition(Duration.millis(600), node);
        fade.setToValue(1);
        fade.setDelay(Duration.millis(delay));

        TranslateTransition translate = new TranslateTransition(Duration.millis(600), node);
        translate.setToY(0);
        translate.setDelay(Duration.millis(delay));
        translate.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        fade.play();
        translate.play();
    }
}