package controller;

import com.sampahin.Main;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class PemasukanSampahController {

    // FXML Components
    @FXML private VBox navHome;
    @FXML private VBox navUsers;
    @FXML private VBox navWaste;
    @FXML private VBox navWithdraw;
    @FXML private VBox navLogout;

    @FXML private Label lblCurrentDate;
    @FXML private Label lblTotalTransaksi;
    @FXML private Label lblTotalPoin;
    @FXML private Label lblTotalBerat;

    @FXML private TextField searchField;
    @FXML private TableView<PemasukanData> tableData;
    @FXML private TableColumn<PemasukanData, String> colUsername;
    @FXML private TableColumn<PemasukanData, String> colTanggal;
    @FXML private TableColumn<PemasukanData, String> colJenisSampah;
    @FXML private TableColumn<PemasukanData, Double> colBerat;
    @FXML private TableColumn<PemasukanData, Integer> colPoin;
    @FXML private TableColumn<PemasukanData, String> colLokasi;

    @FXML private Label lblPaginationInfo;

    // Data
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

    private void setupTable() {
        // Setup columns
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colJenisSampah.setCellValueFactory(new PropertyValueFactory<>("jenisSampah"));
        colBerat.setCellValueFactory(new PropertyValueFactory<>("berat"));
        colPoin.setCellValueFactory(new PropertyValueFactory<>("poin"));
        colLokasi.setCellValueFactory(new PropertyValueFactory<>("lokasi"));

        // Custom cell factories for styling
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

        colJenisSampah.setCellFactory(column -> new TableCell<PemasukanData, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-background-color: #DBEAFE; -fx-text-fill: #1E40AF; " +
                            "-fx-background-radius: 6; -fx-padding: 4 8; " +
                            "-fx-font-weight: bold; -fx-font-size: 11px;");
                }
            }
        });

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

        colPoin.setCellFactory(column -> new TableCell<PemasukanData, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(currencyFormat.format(item) + " pts");
                    setStyle("-fx-text-fill: #D97706; -fx-font-weight: bold;");
                }
            }
        });

        colLokasi.setCellFactory(column -> new TableCell<PemasukanData, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: #6b7280; -fx-font-size: 12px;");
                }
            }
        });

        tableData.setItems(filteredData);
    }

    private void loadSampleData() {
        // Sample waste intake data
        String[] usernames = {"Ahmad_R", "Budi_S", "Citra_D", "Dewi_P", "Eko_W",
                "Fitri_M", "Gunawan", "Hana_K", "Indra_L", "Joko_S"};
        String[] wasteTypes = {"Plastik", "Kertas", "Logam", "Kaca", "Organik"};
        String[] locations = {"Jakarta Utara", "Jakarta Selatan", "Jakarta Timur",
                "Jakarta Barat", "Jakarta Pusat", "Tangerang", "Bekasi",
                "Depok", "Bogor"};

        for (int i = 0; i < 50; i++) {
            String username = usernames[i % usernames.length] + (i > 9 ? i : "");
            String date = LocalDate.now().minusDays(i).format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
            String type = wasteTypes[i % wasteTypes.length];
            double weight = 0.5 + (Math.random() * 10.0); // 0.5 - 10.5 kg
            int points = (int) (weight * 100); // 100 points per kg
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

        lblTotalTransaksi.setText(currencyFormat.format(totalTransaksi));
        lblTotalPoin.setText(currencyFormat.format(totalPoin));
        lblTotalBerat.setText(decimalFormat.format(totalBerat));
    }

    private void updateCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        lblCurrentDate.setText(sdf.format(new Date()));
    }

    private void updatePaginationInfo() {
        lblPaginationInfo.setText("Menampilkan " + filteredData.size() + " dari " + allData.size() + " transaksi");
    }

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

    // Navigation handlers
    @FXML
    private void handleNavHome() {
        Main.showDashboardView();
    }

    @FXML
    private void handleNavUsers() {
        Main.showDaftarPenggunaView();
    }

    @FXML
    private void handleNavWithdraw() {
        Main.showRiwayatPenarikanView();
    }

    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Logout");
        alert.setHeaderText("Apakah Anda yakin ingin keluar?");
        alert.setContentText("Anda akan kembali ke halaman login.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                navigateTo("login.fxml", "Login");
            }
        });
    }

    private void navigateTo(String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/" + fxmlFile));
            Stage stage = (Stage) navHome.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Navigasi Gagal", "Tidak dapat membuka halaman " + title);
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Inner class for data model
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

        // Getters and property methods
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
