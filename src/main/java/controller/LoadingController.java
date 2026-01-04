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

        playEntranceAnimation();


        startPulsingAnimation();


        startStatusAnimation();
    }

    private void playEntranceAnimation() {

        mainCard.setScaleX(0.7);
        mainCard.setScaleY(0.7);
        mainCard.setOpacity(0);


        ScaleTransition scale = new ScaleTransition(Duration.millis(800), mainCard);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.setInterpolator(javafx.animation.Interpolator.EASE_OUT);


        FadeTransition fade = new FadeTransition(Duration.millis(800), mainCard);
        fade.setToValue(1.0);

        scale.play();
        fade.play();
    }

    private void startPulsingAnimation() {

        ScaleTransition pulse = new ScaleTransition(Duration.millis(1000), iconScan);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.15);
        pulse.setToY(1.15);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(ScaleTransition.INDEFINITE);
        
        pulse.play();
    }

    private void startStatusAnimation() {

        FadeTransition fade = new FadeTransition(Duration.millis(1200), lblStatus);
        fade.setFromValue(1.0);
        fade.setToValue(0.6); // Sedikit transparan
        fade.setAutoReverse(true);
        fade.setCycleCount(FadeTransition.INDEFINITE);
        
        fade.play();
    }
}