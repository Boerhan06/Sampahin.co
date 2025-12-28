package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController implements Initializable {

    @FXML
    private VBox mainCard;

    @FXML
    private Label iconScan;

    @FXML
    private Label lblStatus;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Animasi Masuk (Kartu Pop-Up)
        playEntranceAnimation();

        // 2. Animasi Icon (Berdenyut/Scanning)
        startPulsingAnimation();

        // 3. Animasi Status (Breathing Effect)
        startStatusAnimation();
    }

    private void playEntranceAnimation() {
        // Set kondisi awal (Kecil & Transparan)
        mainCard.setScaleX(0.7);
        mainCard.setScaleY(0.7);
        mainCard.setOpacity(0);

        // Scale Up (Efek Elastis)
        ScaleTransition scale = new ScaleTransition(Duration.millis(800), mainCard);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        // Fade In
        FadeTransition fade = new FadeTransition(Duration.millis(800), mainCard);
        fade.setToValue(1.0);

        scale.play();
        fade.play();
    }

    private void startPulsingAnimation() {
        // Membuat icon membesar-mengecil terus menerus
        ScaleTransition pulse = new ScaleTransition(Duration.millis(1000), iconScan);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.15); // Membesar 15%
        pulse.setToY(1.15);
        pulse.setAutoReverse(true); // Kembali mengecil
        pulse.setCycleCount(ScaleTransition.INDEFINITE); // Ulang selamanya
        
        pulse.play();
    }

    private void startStatusAnimation() {
        // Membuat box status berkedip halus (transparansi)
        FadeTransition fade = new FadeTransition(Duration.millis(1200), lblStatus);
        fade.setFromValue(1.0);
        fade.setToValue(0.6); // Sedikit transparan
        fade.setAutoReverse(true);
        fade.setCycleCount(FadeTransition.INDEFINITE);
        
        fade.play();
    }
}