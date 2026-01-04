package controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MapsController implements Initializable {

    @FXML
    private Pane mapContainer;

    @FXML
    private VBox listLokasiContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (mapContainer != null) {
            playEntranceAnimation(mapContainer, 0);
        }


        if (listLokasiContainer != null) {
            int delay = 0;
            for (Node node : listLokasiContainer.getChildren()) {
                playListAnimation(node, delay);
                delay += 100;
            }
        }
    }

    private void playEntranceAnimation(Node node, int delay) {

        node.setOpacity(0);
        node.setTranslateY(20);


        FadeTransition fade = new FadeTransition(Duration.millis(800), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setDelay(Duration.millis(delay));


        TranslateTransition translate = new TranslateTransition(Duration.millis(800), node);
        translate.setFromY(20);
        translate.setToY(0);
        translate.setDelay(Duration.millis(delay));

        fade.play();
        translate.play();
    }

    private void playListAnimation(Node node, int delay) {

        node.setOpacity(0);
        node.setTranslateX(50);


        FadeTransition fade = new FadeTransition(Duration.millis(600), node);
        fade.setToValue(1);
        fade.setDelay(Duration.millis(delay));


        TranslateTransition translate = new TranslateTransition(Duration.millis(600), node);
        translate.setToX(0);
        translate.setDelay(Duration.millis(delay));

        fade.play();
        translate.play();
    }
}