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

    @FXML private WebView mapView;
    @FXML private AreaChart<String, Number> chartMitra;
    @FXML private AreaChart<String, Number> chartPengguna;
    @FXML private Button btnZoomIn;
    @FXML private Button btnZoomOut;

    private WebEngine webEngine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initMap();
        setupZoomControls();
        if (chartMitra != null && chartPengguna != null) {
            populateCharts();
        }
    }

    private void initMap() {
        if (mapView == null) return;

        webEngine = mapView.getEngine();
        webEngine.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36");

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
                    var map = L.map('map', {
                        zoomControl: false,
                        attributionControl: false,
                        dragging: false,
                        scrollWheelZoom: false,
                        doubleClickZoom: false
                    }).setView([-6.2088, 106.8456], 12);
                    
                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                        maxZoom: 19
                    }).addTo(map);

                    var locations = [
                        {lat: -6.2088, lng: 106.8456, name: "Mitra Pusat"},
                        {lat: -6.1900, lng: 106.8300, name: "Mitra Tanah Abang"},
                        {lat: -6.2200, lng: 106.8500, name: "Mitra Tebet"},
                        {lat: -6.2100, lng: 106.8200, name: "Mitra Sudirman"},
                        {lat: -6.2300, lng: 106.8600, name: "Mitra Jatinegara"}
                    ];

                    locations.forEach(function(loc) {
                        L.marker([loc.lat, loc.lng]).addTo(map)
                         .bindPopup("<b>" + loc.name + "</b><br>Status: <span style='color:green'>Online</span>");
                    });

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

    private void populateCharts() {
        XYChart.Series<String, Number> seriesMitra = new XYChart.Series<>();
        seriesMitra.setName("Mitra Baru");
        seriesMitra.getData().add(new XYChart.Data<>("Sen", 10));
        seriesMitra.getData().add(new XYChart.Data<>("Sel", 15));
        seriesMitra.getData().add(new XYChart.Data<>("Rab", 12));
        seriesMitra.getData().add(new XYChart.Data<>("Kam", 20));
        seriesMitra.getData().add(new XYChart.Data<>("Jum", 25));

        chartMitra.getData().clear();
        chartMitra.getData().add(seriesMitra);

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

    @FXML private void handleNavigateToDashboard() { Main.showDashboardView(); }
    @FXML private void handleNavigateToDaftarPengguna() { Main.showDaftarPenggunaView(); }
    @FXML private void handleNavigateToPemasukanSampah() { Main.showPemasukanSampahView(); }
    @FXML private void handleNavigateToRiwayatPenarikan() { Main.showRiwayatPenarikanView(); }
    @FXML private void handleLogout() { Main.showLoginView(); }
}