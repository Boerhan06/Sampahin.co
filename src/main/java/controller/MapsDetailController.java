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
    private VBox rightPanel;

    @FXML
    private VBox infoCard;

    @FXML
    private VBox statCard1;

    @FXML
    private VBox statCard2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        playPanelEntrance();


        playElementEntrance(infoCard, 400);
        playElementEntrance(statCard1, 600);
        playElementEntrance(statCard2, 700);
    }

    private void playPanelEntrance() {

        rightPanel.setTranslateX(500);

        TranslateTransition slide = new TranslateTransition(Duration.millis(800), rightPanel);
        slide.setToX(0);
        slide.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
        slide.play();
    }

    private void playElementEntrance(Node node, int delay) {

        node.setOpacity(0);
        node.setScaleX(0.8);
        node.setScaleY(0.8);


        FadeTransition fade = new FadeTransition(Duration.millis(500), node);
        fade.setToValue(1);
        fade.setDelay(Duration.millis(delay));


        ScaleTransition scale = new ScaleTransition(Duration.millis(500), node);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.setDelay(Duration.millis(delay));

        scale.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        fade.play();
        scale.play();
    }
}