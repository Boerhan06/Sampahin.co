package com.sampahin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        URL fxmlUrl = getClass().getResource("/view/LoginView.fxml"); 

        if (fxmlUrl == null) {
            System.err.println("GAGAL MEMUAT: Tidak dapat menemukan /view/LoginView.fxml");
            System.err.println("CEK LAGI WOIII");
            return;
        }
        
        Parent root = FXMLLoader.load(fxmlUrl);
        
        // Ukuran window login :)
        Scene scene = new Scene(root, 500, 400);

        primaryStage.setTitle("Sampahin.co Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}