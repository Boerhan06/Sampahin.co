package controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class PemasukanDetailController implements Initializable {

    @FXML
    private VBox mainCard;

    @FXML
    private GridPane columnGrid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        playMainCardEntrance();


        playColumnAnimations();
    }

    private void playMainCardEntrance() {
        mainCard.setOpacity(0);
        mainCard.setTranslateY(30);

        FadeTransition fade = new FadeTransition(Duration.millis(800), mainCard);
        fade.setToValue(1);

        TranslateTransition translate = new TranslateTransition(Duration.millis(800), mainCard);
        translate.setToY(0);
        translate.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        fade.play();
        translate.play();
    }

    private void playColumnAnimations() {
        int delay = 300;


        for (Node node : columnGrid.getChildren()) {
            if (node instanceof VBox) {

                node.setOpacity(0);
                node.setTranslateY(-50);


                FadeTransition fade = new FadeTransition(Duration.millis(600), node);
                fade.setToValue(1);
                fade.setDelay(Duration.millis(delay));


                TranslateTransition translate = new TranslateTransition(Duration.millis(600), node);
                translate.setToY(0);
                translate.setDelay(Duration.millis(delay));
                translate.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

                fade.play();
                translate.play();

                delay += 150;
            }
        }
    }
}