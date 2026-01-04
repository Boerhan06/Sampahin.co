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

public class MitraDetailController implements Initializable {

    @FXML
    private VBox rightPanel;

    @FXML
    private VBox mainCard;

    @FXML
    private VBox trashListContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        playRightPanelAnimation();


        playPopUpAnimation(mainCard, 200);


        playCascadingAnimation();
    }

    private void playRightPanelAnimation() {
        rightPanel.setTranslateX(300);

        TranslateTransition slide = new TranslateTransition(Duration.millis(800), rightPanel);
        slide.setToX(0);
        slide.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
        slide.play();
    }

    private void playPopUpAnimation(Node node, int delay) {
        node.setOpacity(0);
        node.setScaleX(0.8);
        node.setScaleY(0.8);

        FadeTransition fade = new FadeTransition(Duration.millis(600), node);
        fade.setToValue(1);
        fade.setDelay(Duration.millis(delay));

        ScaleTransition scale = new ScaleTransition(Duration.millis(600), node);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.setDelay(Duration.millis(delay));
        scale.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        fade.play();
        scale.play();
    }

    private void playCascadingAnimation() {
        int delay = 500; 

        for (Node node : trashListContainer.getChildren()) {
            node.setOpacity(0);
            node.setTranslateX(50);

            FadeTransition fade = new FadeTransition(Duration.millis(500), node);
            fade.setToValue(1);
            fade.setDelay(Duration.millis(delay));

            TranslateTransition translate = new TranslateTransition(Duration.millis(500), node);
            translate.setToX(0);
            translate.setDelay(Duration.millis(delay));

            fade.play();
            translate.play();

            delay += 100;
        }
    }
}