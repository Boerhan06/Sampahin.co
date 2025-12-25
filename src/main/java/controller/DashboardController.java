package controller;

import com.sampahin.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    // --- Komponen UI ---
    @FXML private WebView mapView;
    @FXML private AreaChart<String, Number> chartMitra;
    @FXML private AreaChart<String, Number> chartPengguna;

    // --- Tombol Zoom Peta ---
    @FXML private Button btnZoomIn;
    @FXML private Button btnZoomOut;

    private WebEngine webEngine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Inisialisasi Peta (LeafletJS)
        initMap();

        // 2. Setup Fungsi Tombol Zoom
        setupZoomControls();

        // 3. Isi Data ke Grafik
        if (chartMitra != null && chartPengguna != null) {
            populateCharts();
        }

        // Catatan: Animasi background dihapus agar tampilan lebih bersih (Clean UI)
    }

    // ==========================================
    // LOGIC PETA (OpenStreetMap + Leaflet)
    // ==========================================
    private void initMap() {
        if (mapView == null) return;

        webEngine = mapView.getEngine();

        // PENTING: User Agent agar peta dimuat dengan benar (seolah-olah browser Chrome)
        webEngine.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36");

        // HTML & JavaScript untuk peta
        String htmlMap = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="utf-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <link rel="stylesheet" href="https://unpkg.com/leaflet@1.7.1/dist/leaflet.css" />
                <script src="https://unpkg.com/leaflet@1.7.1/dist/leaflet.js"></script>
                <style>
                    html, body { margin: 0; padding: 0; height: 100%; width: 100%; overflow: hidden; }
                    #map { height: 100%; width: 100%; background: #ffffff; }
                </style>
            </head>
            <body>
                <div id="map"></div>
                <script>
                    // 1. Setup Map (Fokus Jakarta)
                    var map = L.map('map', {
                        zoomControl: false, // Zoom control kita buat sendiri di JavaFX
                        attributionControl: false,
                        dragging: true,
                        scrollWheelZoom: true,
                        doubleClickZoom: true
                    }).setView([-6.2088, 106.8456], 12);
                    
                    // 2. Load Tile Layer (Tampilan Peta)
                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                        maxZoom: 19
                    }).addTo(map);

                    // 3. Data Dummy Marker Mitra
                    var locations = [
                        {lat: -6.2088, lng: 106.8456, name: "Mitra Pusat"},
                        {lat: -6.1900, lng: 106.8300, name: "Mitra Tanah Abang"},
                        {lat: -6.2200, lng: 106.8500, name: "Mitra Tebet"},
                        {lat: -6.2100, lng: 106.8200, name: "Mitra Sudirman"},
                        {lat: -6.2300, lng: 106.8600, name: "Mitra Jatinegara"}
                    ];

                    // 4. Render Marker ke Peta
                    locations.forEach(function(loc) {
                        L.marker([loc.lat, loc.lng]).addTo(map)
                         .bindPopup("<b>" + loc.name + "</b><br>Status: <span style='color:green'>Online</span>");
                    });

                    // 5. Fungsi Zoom yang dipanggil dari Java
                    function jsZoomIn() { map.zoomIn(); }
                    function jsZoomOut() { map.zoomOut(); }
                </script>
            </body>
            </html>
        """;

        webEngine.loadContent(htmlMap);
    }

    private void setupZoomControls() {
        if (btnZoomIn != null) {
            btnZoomIn.setOnAction(e -> {
                if (webEngine != null) webEngine.executeScript("jsZoomIn()");
            });
        }

        if (btnZoomOut != null) {
            btnZoomOut.setOnAction(e -> {
                if (webEngine != null) webEngine.executeScript("jsZoomOut()");
            });
        }
    }

    // ==========================================
    // LOGIC GRAFIK (Charts)
    // ==========================================
    private void populateCharts() {
        // Data Grafik Mitra
        XYChart.Series<String, Number> seriesMitra = new XYChart.Series<>();
        seriesMitra.setName("Mitra Baru");
        seriesMitra.getData().add(new XYChart.Data<>("Sen", 10));
        seriesMitra.getData().add(new XYChart.Data<>("Sel", 15));
        seriesMitra.getData().add(new XYChart.Data<>("Rab", 12));
        seriesMitra.getData().add(new XYChart.Data<>("Kam", 20));
        seriesMitra.getData().add(new XYChart.Data<>("Jum", 25));

        chartMitra.getData().clear();
        chartMitra.getData().add(seriesMitra);

        // Data Grafik Pengguna
        XYChart.Series<String, Number> seriesPengguna = new XYChart.Series<>();
        seriesPengguna.setName("User Aktif");
        seriesPengguna.getData().add(new XYChart.Data<>("Sen", 50));
        seriesPengguna.getData().add(new XYChart.Data<>("Sel", 80));
        seriesPengguna.getData().add(new XYChart.Data<>("Rab", 70));
        seriesPengguna.getData().add(new XYChart.Data<>("Kam", 120));
        seriesPengguna.getData().add(new XYChart.Data<>("Jum", 150));

        chartPengguna.getData().clear();
        chartPengguna.getData().add(seriesPengguna);
    }

    // ==========================================
    // NAVIGASI HALAMAN (Sidebar)
    // ==========================================

    @FXML
    private void handleNavigateToDashboard() {
        Main.showDashboardView();
    }

    @FXML
    private void handleNavigateToDaftarPengguna() {
        Main.showDaftarPenggunaView();
    }

    @FXML
    private void handleNavigateToPemasukanSampah() {
        Main.showPemasukanSampahView();
    }

    @FXML
    private void handleNavigateToRiwayatPenarikan() {
        Main.showRiwayatPenarikanView();
    }

    @FXML
    private void handleEditprofil(){
        Main.showEditProfilView();
    }

    @FXML
    private void handleLogout() {
        Main.showLoginView();
    }
}