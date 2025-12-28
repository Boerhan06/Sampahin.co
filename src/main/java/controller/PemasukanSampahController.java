package controller;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node; // Import Node untuk mengambil Stage
import javafx.scene.input.MouseEvent; // Import MouseEvent

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class PemasukanSampahController {

    // --- FXML Components (Sesuai fx:id di FXML) ---

    // Label Header & Stats
    @FXML private Label lblCurrentDate;
    @FXML private Label lblTotalTransaksi;
    @FXML private Label lblTotalPoin;
    @FXML private Label lblTotalBerat;

    // Controls
    @FXML private TextField searchField;
    @FXML private Label lblPaginationInfo;

    // Table Components
    @FXML private TableView<PemasukanData> tableData;
    @FXML private TableColumn<PemasukanData, String> colUsername;
    @FXML private TableColumn<PemasukanData, String> colTanggal;
    @FXML private TableColumn<PemasukanData, String> colJenisSampah;
    @FXML private TableColumn<PemasukanData, Double> colBerat;
    @FXML private TableColumn<PemasukanData, Integer> colPoin;
    @FXML private TableColumn<PemasukanData, String> colLokasi;

    // --- Data & Formatters ---
    private ObservableList<PemasukanData> allData = FXCollections.observableArrayList();
    private ObservableList<PemasukanData> filteredData = FXCollections.observableArrayList();

    private DecimalFormat currencyFormat = new DecimalFormat("#,###");
    private DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

    @FXML
    public void initialize() {
        setupTable();
        loadSampleData();
        updateStatistics();
        updateCurrentDate();
    }

    // --- Setup Table & Styling ---
    private void setupTable() {
        // Setup columns mapping
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colJenisSampah.setCellValueFactory(new PropertyValueFactory<>("jenisSampah"));
        colBerat.setCellValueFactory(new PropertyValueFactory<>("berat"));
        colPoin.setCellValueFactory(new PropertyValueFactory<>("poin"));
        colLokasi.setCellValueFactory(new PropertyValueFactory<>("lokasi"));

        // 1. Styling Username (Bold, Warna Ungu Utama)
        colUsername.setCellFactory(column -> new TableCell<PemasukanData, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #685BAB;");
                }
            }
        });

        // 2. Styling Tanggal (Abu-abu)
        colTanggal.setCellFactory(column -> new TableCell<PemasukanData, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: #6b7280;");
                }
            }
        });

        // 3. Styling Jenis Sampah (Badge Style)
        colJenisSampah.setCellFactory(column -> new TableCell<PemasukanData, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    setGraphic(null);
                } else {
                    Label badge = new Label(item);
                    // Warna badge dinamis berdasarkan jenis sampah (Opsional)
                    String style = "-fx-background-color: #DBEAFE; -fx-text-fill: #1E40AF;"; // Default Blue
                    if (item.equalsIgnoreCase("Plastik")) style = "-fx-background-color: #FEF3C7; -fx-text-fill: #D97706;"; // Yellow
                    else if (item.equalsIgnoreCase("Organik")) style = "-fx-background-color: #D1FAE5; -fx-text-fill: #059669;"; // Green

                    badge.setStyle(style + "-fx-background-radius: 6; -fx-padding: 4 8; -fx-font-weight: bold; -fx-font-size: 11px;");
                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        // 4. Styling Berat
        colBerat.setCellFactory(column -> new TableCell<PemasukanData, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(decimalFormat.format(item) + " kg");
                    setStyle("-fx-text-fill: #059669; -fx-font-weight: bold;");
                }
            }
        });

        // 5. Styling Poin
        colPoin.setCellFactory(column -> new TableCell<PemasukanData, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText("+" + currencyFormat.format(item));
                    setStyle("-fx-text-fill: #D97706; -fx-font-weight: bold;");
                }
            }
        });

        tableData.setItems(filteredData);
    }

    // --- Data Loading ---
    private void loadSampleData() {
        String[] usernames = {"Ahmad_R", "Budi_S", "Citra_D", "Dewi_P", "Eko_W", "Fitri_M", "Gunawan", "Hana_K"};
        String[] wasteTypes = {"Plastik", "Kertas", "Logam", "Kaca", "Organik"};
        String[] locations = {"Jakarta Utara", "Jakarta Selatan", "Bandung", "Surabaya", "Bekasi"};

        for (int i = 0; i < 30; i++) {
            String username = usernames[i % usernames.length];
            String date = LocalDate.now().minusDays(i).format(DateTimeFormatter.ofPattern("dd MMM yyyy", new Locale("id", "ID")));
            String type = wasteTypes[i % wasteTypes.length];
            double weight = 1.0 + (Math.random() * 9.0);
            int points = (int) (weight * 500); // 500 poin per kg
            String location = locations[i % locations.length];

            allData.add(new PemasukanData(username, date, type, weight, points, location));
        }

        filteredData.addAll(allData);
        updatePaginationInfo();
    }

    private void updateStatistics() {
        int totalTransaksi = allData.size();
        int totalPoin = allData.stream().mapToInt(PemasukanData::getPoin).sum();
        double totalBerat = allData.stream().mapToDouble(PemasukanData::getBerat).sum();

        lblTotalTransaksi.setText(String.valueOf(totalTransaksi));
        lblTotalPoin.setText(currencyFormat.format(totalPoin));
        lblTotalBerat.setText(decimalFormat.format(totalBerat));
    }

    private void updateCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", new Locale("id", "ID"));
        lblCurrentDate.setText(sdf.format(new Date()));
    }

    private void updatePaginationInfo() {
        lblPaginationInfo.setText("Menampilkan " + filteredData.size() + " dari " + allData.size() + " transaksi");
    }

    // --- Event Handlers (Sesuai FXML) ---

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            filteredData.setAll(allData);
        } else {
            filteredData.clear();
            for (PemasukanData data : allData) {
                if (data.getUsername().toLowerCase().contains(searchText) ||
                        data.getLokasi().toLowerCase().contains(searchText) ||
                        data.getJenisSampah().toLowerCase().contains(searchText)) {
                    filteredData.add(data);
                }
            }
        }
        updatePaginationInfo();
    }

    // Navigasi ke Dashboard
    @FXML
    private void handleDashboard(MouseEvent event) {
        navigateTo(event, "Dashboard.fxml", "Dashboard");
    }

    // Navigasi ke Daftar Pengguna
    @FXML
    private void handleUsers(MouseEvent event) {
        navigateTo(event, "DaftarPengguna.fxml", "Daftar Pengguna");
    }

    // Navigasi ke Riwayat Penarikan
    @FXML
    private void handlePenarikan(MouseEvent event) {
        navigateTo(event, "RiwayatPenarikan.fxml", "Riwayat Penarikan");
    }

    // Handle Logout
    @FXML
    private void handleLogout(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Logout");
        alert.setHeaderText("Keluar dari Aplikasi");
        alert.setContentText("Apakah Anda yakin ingin keluar?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Ganti 'Login.fxml' dengan file login Anda yang sebenarnya
                navigateTo(event, "Login.fxml", "Login");
            }
        });
    }

    // Helper Method untuk Navigasi
    private void navigateTo(MouseEvent event, String fxmlFile, String title) {
        try {
            // Mengambil stage dari event source (tombol yang diklik)
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();

            // Load FXML baru
            Parent root = FXMLLoader.load(getClass().getResource("/view/" + fxmlFile));
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Navigasi");
            alert.setHeaderText(null);
            alert.setContentText("Gagal memuat halaman: " + fxmlFile + "\nPastikan file FXML ada di folder /view/");
            alert.showAndWait();
        }
    }

    // --- Inner Class Data Model ---
    public static class PemasukanData {
        private final StringProperty username;
        private final StringProperty tanggal;
        private final StringProperty jenisSampah;
        private final DoubleProperty berat;
        private final IntegerProperty poin;
        private final StringProperty lokasi;

        public PemasukanData(String username, String tanggal, String jenisSampah,
                             double berat, int poin, String lokasi) {
            this.username = new SimpleStringProperty(username);
            this.tanggal = new SimpleStringProperty(tanggal);
            this.jenisSampah = new SimpleStringProperty(jenisSampah);
            this.berat = new SimpleDoubleProperty(berat);
            this.poin = new SimpleIntegerProperty(poin);
            this.lokasi = new SimpleStringProperty(lokasi);
        }

        public String getUsername() { return username.get(); }
        public StringProperty usernameProperty() { return username; }

        public String getTanggal() { return tanggal.get(); }
        public StringProperty tanggalProperty() { return tanggal; }

        public String getJenisSampah() { return jenisSampah.get(); }
        public StringProperty jenisSampahProperty() { return jenisSampah; }

        public double getBerat() { return berat.get(); }
        public DoubleProperty beratProperty() { return berat; }

        public int getPoin() { return poin.get(); }
        public IntegerProperty poinProperty() { return poin; }

        public String getLokasi() { return lokasi.get(); }
        public StringProperty lokasiProperty() { return lokasi; }
    }
}