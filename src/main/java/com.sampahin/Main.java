package com.sampahin;

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
        showLoginView();
    }

    // --- GANTIII LAYARRR ---

    public static void showLoginView() {
        changeScene("/view/LoginView.fxml", "Login Admin", true);
    }

    public static void showRegisterView() {
        changeScene("/view/RegisterView.fxml", "Registrasi - Data Diri", true);
    }

    public static void showRegisterNextView() {
        changeScene("/view/RegisterNextView.fxml", "Registrasi - Setup Akun", false);
    }

    public static void showDashboardView() {
        changeScene("/view/DashboardView.fxml", "Dashboard Utama", false);
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

    // --- Helper ---
    private static void changeScene(String fxmlPath, String title, boolean isSmallWindow) {
        try {
            URL url = Main.class.getResource(fxmlPath);
            if (url == null) {
                System.err.println("❌ FXML Tidak Ditemukan: " + fxmlPath);
                return;
            }
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Sampahin.co - " + title);

            if (isSmallWindow) {
                // --- MODE LAYAR KECIL (Login/Register) ---
                primaryStage.setMaximized(false); // Matikan full screen
                primaryStage.setWidth(1024);      // Lebar tetap
                primaryStage.setHeight(700);      // Tinggi tetap
                primaryStage.setResizable(false); // Gabisa ditarik-tarik user
                primaryStage.centerOnScreen();    // Taruh tengah
            } else {
                // --- MODE LAYAR BESAR (Dashboard) ---
                primaryStage.setResizable(true);  // User boleh atur ukuran
                primaryStage.setMaximized(true);  // Paksa Full Screen
            }
            primaryStage.centerOnScreen();
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.setProperty("prism.allowhidpi", "false");
        launch(args);
    }
}
