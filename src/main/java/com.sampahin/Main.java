package com.sampahin;

import controller.DashboardController; // Pastikan package controller benar
import models.Akun;                    // Pastikan package models benar

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Sampahin.co - Admin System");

        // Mulai dari halaman Login
        showLoginView();
    }

    // --- NAVIGASI LAYAR ---

    public static void showLoginView() {
        changeScene("/view/LoginView.fxml", "Login Admin", true);
    }

    public static void showRegisterView() {
        changeScene("/view/RegisterView.fxml", "Registrasi - Data Diri", true);
    }

    public static void showRegisterNextView() {
        changeScene("/view/RegisterNextView.fxml", "Registrasi - Setup Akun", false);
    }

    /**
     * KHUSUS DASHBOARD: Menerima data Akun agar bisa ditampilkan namanya.
     */
    public static void showDashboardView(Akun akun) {
        // Kita panggil changeScene, tapi kita tampung return value-nya (loader)
        FXMLLoader loader = changeScene("/view/DashboardView.fxml", "Dashboard Utama", false);

        if (loader != null) {
            // Ambil controller dari loader
            Object controllerObj = loader.getController();

            // Cek apakah controller-nya benar DashboardController
            if (controllerObj instanceof DashboardController) {
                DashboardController controller = (DashboardController) controllerObj;
                // Kirim data akun ke controller dashboard
                controller.setAkunData(akun);
            }
        }
    }

    // Overloading method untuk testing tanpa login (opsional)
    public static void showDashboardView() {
        showDashboardView(null);
    }

    public static void showDaftarPenggunaView() {
        changeScene("/view/daftar-pengguna.fxml", "Daftar Pengguna", false);
    }

    public static void showPemasukanSampahView() {
        changeScene("/view/PemasukanSampahView.fxml", "Pemasukan Sampah", false);
    }

    public static void showRiwayatPenarikanView() {
        changeScene("/view/riwayat-penarikan.fxml", "Riwayat Penarikan Poin", false);
    }

    public static  void showEditProfilView() {
        changeScene("/view/EditProfilView.fxml", "Edit Profil", false);
    }

    // --- HELPER UTAMA (GANTI SCENE) ---

    // Return FXMLLoader agar bisa akses controller
    private static FXMLLoader changeScene(String fxmlPath, String title, boolean isSmallWindow) {
        FXMLLoader loader = null;
        try {
            URL url = Main.class.getResource(fxmlPath);
            if (url == null) {
                System.err.println("❌ FXML Tidak Ditemukan: " + fxmlPath);
                return null;
            }

            loader = new FXMLLoader(url);
            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Sampahin.co - " + title);

            // --- LOGIKA TAMPILAN (Sesuai Permintaan) ---
            if (isSmallWindow) {
                // --- MODE LAYAR KECIL (Login/Register) ---
                primaryStage.setMaximized(false);
                primaryStage.setWidth(1024);
                primaryStage.setHeight(700);
                primaryStage.setResizable(false);
                primaryStage.centerOnScreen();
            } else {
                // --- MODE LAYAR BESAR (Dashboard) ---
                primaryStage.setResizable(true);

                // Trik Paksa Full Screen ulang agar tidak bug
                primaryStage.setMaximized(true);
            }

            // Hanya center jika window kecil, kalau full screen biarkan
            if (isSmallWindow) {
                primaryStage.centerOnScreen();
            }

            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return loader; // Kembalikan loader
    }

    public static void main(String[] args) {
        System.setProperty("prism.allowhidpi", "false");
        launch(args);
    }
}