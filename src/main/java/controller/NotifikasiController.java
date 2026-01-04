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
    private VBox notifContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        playCascadingAnimation();
    }

    private void playCascadingAnimation() {
        int delay = 0;

        for (Node node : notifContainer.getChildren()) {

            node.setOpacity(0);
            node.setTranslateX(50);


            FadeTransition fade = new FadeTransition(Duration.millis(500), node);
            fade.setToValue(1);
            fade.setDelay(Duration.millis(delay));


            TranslateTransition translate = new TranslateTransition(Duration.millis(500), node);
            translate.setToX(0);
            translate.setDelay(Duration.millis(delay));

            translate.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

            fade.play();
            translate.play();


            delay += 100;
        }
    }
}