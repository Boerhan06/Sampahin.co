package controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class TukarPoinController implements Initializable {

    @FXML
    private Label lblNamaAkun;

    @FXML
    private Label lblIdAkun;

    @FXML
    private VBox cardTunai;

    @FXML
    private VBox cardBarang;

    @FXML
    private VBox listTunai; // Container untuk list Tukar Tunai

    @FXML
    private VBox listBarang; // Container untuk list Tukar Barang

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Set Data Profil (Bisa diambil dari SessionManager/Model nanti)
        lblNamaAkun.setText("Budi Santoso");
        lblIdAkun.setText("ID: USER-2024-001");

        // 2. Load Data Sampah ke Tampilan
        loadDataTukarTunai();
        loadDataTukarBarang();

        // 3. Jalankan Animasi Masuk
        playEntranceAnimation();
    }

    // Method untuk mengisi kolom Tukar Tunai dengan data sampah & berat
    private void loadDataTukarTunai() {
        // Contoh Data Dummy (Ganti dengan looping dari Database/Model kamu)
        addWasteItem(listTunai, "Plastik PET", "5.0 Kg", "Rp 15.000 (Tukar Tunai)");
        addWasteItem(listTunai, "Kardus Bekas", "10.0 Kg", "Rp 20.000 (Tukar Tunai)");
        addWasteItem(listTunai, "Kaleng Aluminium", "2.5 Kg", "Rp 12.500 (Tukar Tunai)");
        addWasteItem(listTunai, "Kertas HVS", "4.0 Kg", "Rp 8.000 (Tukar Tunai)");
    }

    // Method untuk mengisi kolom Tukar Barang
    private void loadDataTukarBarang() {
        // Contoh Data Dummy
        addWasteItem(listBarang, "Botol Kaca", "10 Pcs", "Voucher Listrik 20k");
        addWasteItem(listBarang, "Elektronik Bekas", "1 Unit", "Sembako Paket A");
        addWasteItem(listBarang, "Minyak Jelantah", "2 Liter", "Sabun Cuci Piring");
    }

    // Helper untuk membuat tampilan item list secara visual
    private void addWasteItem(VBox container, String jenis, String berat, String nilai) {
        VBox itemBox = new VBox();
        itemBox.getStyleClass().add("waste-item-box"); // Menggunakan CSS .waste-item-box
        
        Label lblMain = new Label(jenis + " (" + berat + ")");
        lblMain.getStyleClass().add("waste-text");
        
        Label lblSub = new Label("Nilai: " + nilai);
        lblSub.getStyleClass().add("waste-subtext");
        
        itemBox.getChildren().addAll(lblMain, lblSub);
        
        // Efek Hover Simpel
        itemBox.setOnMouseEntered(e -> itemBox.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-cursor: hand; -fx-background-radius: 10;"));
        itemBox.setOnMouseExited(e -> itemBox.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 10;"));

        container.getChildren().add(itemBox);
    }

    private void playEntranceAnimation() {
        // Animasi Slide Up untuk kedua kartu
        cardTunai.setTranslateY(50);
        cardTunai.setOpacity(0);
        
        cardBarang.setTranslateY(50);
        cardBarang.setOpacity(0);

        // Animasi Kartu 1
        TranslateTransition tt1 = new TranslateTransition(Duration.millis(800), cardTunai);
        tt1.setToY(0);
        FadeTransition ft1 = new FadeTransition(Duration.millis(800), cardTunai);
        ft1.setToValue(1);

        // Animasi Kartu 2 (Sedikit delay agar bergelombang)
        TranslateTransition tt2 = new TranslateTransition(Duration.millis(800), cardBarang);
        tt2.setToY(0);
        tt2.setDelay(Duration.millis(200));
        FadeTransition ft2 = new FadeTransition(Duration.millis(800), cardBarang);
        ft2.setToValue(1);
        ft2.setDelay(Duration.millis(200));

        tt1.play(); ft1.play();
        tt2.play(); ft2.play();
    }
}