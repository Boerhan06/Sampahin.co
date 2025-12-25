package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class DetailPenggunaController implements Initializable {

    @FXML
    private VBox mainContent; // Container utama

    @FXML
    private Label lblNamaPengguna;

    @FXML
    private Label lblTotalPoin;

    @FXML
    private VBox cardPoin;

    @FXML
    private VBox cardGrafik;

    @FXML
    private BarChart<String, Number> chartTransaksi;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Load Data Dummy (Ganti dengan data dari Model/Database nanti)
        setupUserData();
        setupChartData();

        // 2. Jalankan Animasi
        playEntranceAnimation();
        setupHoverEffects();
    }

    private void setupUserData() {
        lblNamaPengguna.setText("Budi Santoso"); // Contoh Nama
        lblTotalPoin.setText("15,450");          // Contoh Poin
    }

    private void setupChartData() {
        // Membuat seri data untuk grafik
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Transaksi");

        // Data Dummy 6 Bulan
        series.getData().add(new XYChart.Data<>("Jan", 12));
        series.getData().add(new XYChart.Data<>("Feb", 25));
        series.getData().add(new XYChart.Data<>("Mar", 18));
        series.getData().add(new XYChart.Data<>("Apr", 40)); // Paling tinggi
        series.getData().add(new XYChart.Data<>("Mei", 32));
        series.getData().add(new XYChart.Data<>("Jun", 28));

        chartTransaksi.getData().add(series);
    }

    private void playEntranceAnimation() {
        // Animasi Slide Up & Fade In untuk seluruh konten tengah
        mainContent.setOpacity(0);
        mainContent.setTranslateY(50);

        FadeTransition fade = new FadeTransition(Duration.millis(1000), mainContent);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition translate = new TranslateTransition(Duration.millis(1000), mainContent);
        translate.setFromY(50);
        translate.setToY(0);
        translate.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        fade.play();
        translate.play();
    }

    private void setupHoverEffects() {
        // Efek Membesar saat mouse di atas kartu Poin
        addHoverScale(cardPoin);
        // Efek Membesar saat mouse di atas kartu Grafik
        addHoverScale(cardGrafik);
    }

    private void addHoverScale(VBox node) {
        node.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), node);
            st.setToX(1.02);
            st.setToY(1.02);
            st.play();
        });

        node.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), node);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }
}