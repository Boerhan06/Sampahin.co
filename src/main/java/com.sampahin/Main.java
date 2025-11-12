package com.sampahin; // Pastikan package Anda benar

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL; // Pastikan ini di-import

/**
 * Kelas Main untuk menjalankan aplikasi JavaFX.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        
        /* * PERBAIKAN UTAMA ANDA ADA DI SINI.
         * * Tanda "/" di awal path ("/view/LoginView.fxml")
         * adalah perintah "mutlak" (absolute) yang artinya:
         * "Mulai pencarian dari folder 'root' (target/classes), JANGAN dari 
         * folder 'com/sampahin' tempat file Main.java ini berada."
         * * Ini akan menemukan folder 'view' Anda.
         */
        URL fxmlUrl = getClass().getResource("/view/LoginView.fxml"); 

        // Pengecekan error jika file FXML tetap tidak ditemukan
        if (fxmlUrl == null) {
            System.err.println("GAGAL MEMUAT: Tidak dapat menemukan /view/LoginView.fxml");
            System.err.println("Pastikan Anda sudah menambahkan blok <resources> di pom.xml dan menjalankan Clean and Build.");
            return;
        }
        
        Parent root = FXMLLoader.load(fxmlUrl);
        
        // Atur ukuran window login Anda
        Scene scene = new Scene(root, 500, 400); 

        primaryStage.setTitle("Sampahin.co Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * main() method ini dibutuhkan untuk IDE dan build JAR.
     * (Jangan panggil launch() dari sini jika Anda menggunakan Launcher.java)
     */
    public static void main(String[] args) {
        launch(args);
    }
}