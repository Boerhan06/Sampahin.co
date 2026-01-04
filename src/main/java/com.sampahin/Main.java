package com.sampahin;

import controller.BaseController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Akun;
import models.Mitra;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Sampahin.co - Admin System");


        showLoginView();

        primaryStage.show();
    }



    public static void showLoginView() {
        changeScene("/view/LoginView.fxml", "Login System", true);
    }

    public static void showRegisterView() {
        changeScene("/view/RegisterView.fxml", "Registrasi Akun Baru", true);
    }

    public static void showRegisterNextView() {
        changeScene("/view/RegisterNextView.fxml", "Registrasi Tahap 2", true);
    }




    public static void showDashboardView(Akun akun) {
        if (akun instanceof Mitra) {

            showViewWithAkun("/view/DashboardMitraView.fxml", "Dashboard Mitra", akun);
        } else {

            showViewWithAkun("/view/DashboardView.fxml", "Dashboard Admin", akun);
        }
    }


    public static void showDashboardMitraView(Akun akun) {
        showViewWithAkun("/view/DashboardMitraView.fxml", "Dashboard Mitra", akun);
    }

    public static void showDaftarPenggunaView(Akun akun) {
        showViewWithAkun("/view/DaftarPenggunaView.fxml", "Kelola Pengguna", akun);
    }

    public static void showPemasukanSampahView(Akun akun) {
        showViewWithAkun("/view/PemasukanSampahView.fxml", "Input Pemasukan Sampah", akun);
    }

    public static void showRiwayatPenarikanView(Akun akun) {
        showViewWithAkun("/view/RiwayatPenarikanView.fxml", "Riwayat Transaksi", akun);
    }

    public static void showEditProfilView(Akun akun) {
        showViewWithAkun("/view/EditProfilView.fxml", "Edit Profil Saya", akun);
    }




    private static void showViewWithAkun(String fxmlPath, String title, Akun akun) {

        FXMLLoader loader = changeScene(fxmlPath, title, false);

        if (loader != null) {

            Object controller = loader.getController();


            if (controller instanceof BaseController) {

                ((BaseController) controller).setAkunData(akun);
            } else {
                System.err.println("⚠️ WARNING: Controller untuk " + fxmlPath + " tidak extends BaseController. Data akun tidak terkirim.");
            }
        }
    }


    private static FXMLLoader changeScene(String fxmlPath, String title, boolean isSmallWindow) {
        FXMLLoader loader = null;
        try {

            URL url = Main.class.getResource(fxmlPath);
            if (url == null) {
                System.err.println("❌ FATAL ERROR: File FXML Tidak Ditemukan: " + fxmlPath);
                System.err.println("   Pastikan file ada di folder src/main/resources/view/");
                return null;
            }


            loader = new FXMLLoader(url);
            Parent root = loader.load();


            if (primaryStage.getScene() == null) {
                primaryStage.setScene(new Scene(root));
            } else {
                primaryStage.getScene().setRoot(root);
            }


            primaryStage.setTitle("Sampahin.co - " + title);


            if (isSmallWindow) {

                primaryStage.setMaximized(false);
                primaryStage.setWidth(1024);
                primaryStage.setHeight(650);
                primaryStage.setResizable(false);
                primaryStage.centerOnScreen();
            } else {

                primaryStage.setResizable(true);


                if (!primaryStage.isMaximized()) {
                    primaryStage.setMaximized(true);
                }
            }

            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Terjadi kesalahan saat memuat view: " + e.getMessage());
        }

        return loader;
    }

    public static void main(String[] args) {
        launch(args);
    }
}