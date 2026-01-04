package controller;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import util.DatabaseConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class PemasukanSampahController extends BaseController implements Initializable {

    @FXML private Label lblCurrentDate;
    @FXML private Label lblTotalTransaksi;
    @FXML private Label lblTotalPoin;
    @FXML private Label lblTotalBerat;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> cmbSort;
    @FXML private Label lblPaginationInfo;

    @FXML private TableView<PemasukanData> tableData;
    @FXML private TableColumn<PemasukanData, String> colUsername;
    @FXML private TableColumn<PemasukanData, String> colTanggal;
    @FXML private TableColumn<PemasukanData, String> colJenisSampah;
    @FXML private TableColumn<PemasukanData, Double> colBerat;
    @FXML private TableColumn<PemasukanData, Integer> colPoin;
    @FXML private TableColumn<PemasukanData, String> colLokasi;

    private ObservableList<PemasukanData> allData = FXCollections.observableArrayList();
    private ObservableList<PemasukanData> filteredData = FXCollections.observableArrayList();

    private final DecimalFormat currencyFormat = new DecimalFormat("#,###");
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        setupSortFilter();
        updateCurrentDate();
        loadDataFromDatabase();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());
    }

    @Override
    protected void updateUI() {
        if (currentAkun != null) {
            System.out.println("✅ PemasukanSampah: Akses oleh Admin -> " + currentAkun.getNamaLengkap());
        }
    }

    private void setupSortFilter() {
        ObservableList<String> sortOptions = FXCollections.observableArrayList(
                "Waktu: Terbaru", "Waktu: Terlama", "Berat: Tertinggi",
                "Berat: Terendah", "Poin: Tertinggi", "Poin: Terendah", "Jenis Sampah (A-Z)"
        );
        cmbSort.setItems(sortOptions);
        cmbSort.setValue("Waktu: Terbaru");
        cmbSort.setOnAction(event -> handleSort());
    }

    private void handleSort() {
        String selected = cmbSort.getValue();
        if (selected == null) return;
        Comparator<PemasukanData> comparator = null;
        switch (selected) {
            case "Waktu: Terbaru": comparator = Comparator.comparing(PemasukanData::getRawDateTime).reversed(); break;
            case "Waktu: Terlama": comparator = Comparator.comparing(PemasukanData::getRawDateTime); break;
            case "Berat: Tertinggi": comparator = Comparator.comparingDouble(PemasukanData::getBerat).reversed(); break;
            case "Berat: Terendah": comparator = Comparator.comparingDouble(PemasukanData::getBerat); break;
            case "Poin: Tertinggi": comparator = Comparator.comparingInt(PemasukanData::getPoin).reversed(); break;
            case "Poin: Terendah": comparator = Comparator.comparingInt(PemasukanData::getPoin); break;
            case "Jenis Sampah (A-Z)": comparator = Comparator.comparing(PemasukanData::getJenisSampah); break;
        }
        if (comparator != null) FXCollections.sort(filteredData, comparator);
    }

    private void loadDataFromDatabase() {
        allData.clear();
        String query = "SELECT t.waktu_transaksi, a.username, t.jenis_sampah, t.berat_kg, t.total_poin, t.keterangan " +
                "FROM transaksi_sampah t JOIN akun a ON t.idAkun = a.idAkun ORDER BY t.waktu_transaksi DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter viewFormat = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", new Locale("id", "ID"));

            while (rs.next()) {
                String rawDate = rs.getString("waktu_transaksi");
                String username = rs.getString("username");
                String jenisSampah = rs.getString("jenis_sampah");
                double berat = rs.getDouble("berat_kg");
                int poin = rs.getInt("total_poin");
                String lokasi = rs.getString("keterangan");
                if (lokasi == null || lokasi.isEmpty() || lokasi.equals("-")) lokasi = "Bank Sampah Pusat";

                LocalDateTime ldt = LocalDateTime.now();
                String formattedDate = rawDate;
                try {
                    if (rawDate != null && rawDate.contains(".")) rawDate = rawDate.substring(0, rawDate.indexOf("."));
                    ldt = LocalDateTime.parse(rawDate, dbFormat);
                    formattedDate = ldt.format(viewFormat);
                } catch (Exception e) {}

                allData.add(new PemasukanData(username, formattedDate, ldt, jenisSampah, berat, poin, lokasi));
            }
            filteredData.setAll(allData);
            handleSort();
            updateStatistics();
            updatePaginationInfo();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupTable() {

        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colJenisSampah.setCellValueFactory(new PropertyValueFactory<>("jenisSampah"));
        colBerat.setCellValueFactory(new PropertyValueFactory<>("berat"));
        colPoin.setCellValueFactory(new PropertyValueFactory<>("poin"));
        colLokasi.setCellValueFactory(new PropertyValueFactory<>("lokasi"));

        // Styling kolom
        colUsername.setCellFactory(column -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); }
                else { setText(item); setStyle("-fx-font-weight: bold; -fx-text-fill: #685BAB;"); }
            }
        });

        colJenisSampah.setCellFactory(column -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); setText(null); }
                else {
                    Label badge = new Label(item);
                    String style = "-fx-background-color: #DBEAFE; -fx-text-fill: #1E40AF;";
                    String lower = item.toLowerCase();
                    if (lower.contains("plastik")) style = "-fx-background-color: #FEF3C7; -fx-text-fill: #D97706;";
                    else if (lower.contains("organik")) style = "-fx-background-color: #D1FAE5; -fx-text-fill: #059669;";
                    badge.setStyle(style + "-fx-background-radius: 6; -fx-padding: 4 8; -fx-font-weight: bold; -fx-font-size: 11px;");
                    setGraphic(badge); setText(null);
                }
            }
        });

        colBerat.setCellFactory(column -> new TableCell<>() {
            @Override protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText(decimalFormat.format(item) + " kg"); setStyle("-fx-text-fill: #059669; -fx-font-weight: bold;"); }
            }
        });

        colPoin.setCellFactory(column -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText("+" + currencyFormat.format(item)); setStyle("-fx-text-fill: #D97706; -fx-font-weight: bold;"); }
            }
        });

        tableData.setItems(filteredData);
    }

    private void updateCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
        lblCurrentDate.setText(sdf.format(new Date()));
    }

    private void updateStatistics() {
        int totalTransaksi = allData.size();
        int totalPoin = allData.stream().mapToInt(PemasukanData::getPoin).sum();
        double totalBerat = allData.stream().mapToDouble(PemasukanData::getBerat).sum();
        lblTotalTransaksi.setText(String.valueOf(totalTransaksi));
        lblTotalPoin.setText(currencyFormat.format(totalPoin));
        lblTotalBerat.setText(decimalFormat.format(totalBerat));
    }

    private void updatePaginationInfo() {
        lblPaginationInfo.setText("Menampilkan " + filteredData.size() + " dari " + allData.size() + " transaksi");
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase().trim();
        filteredData.clear();
        if (searchText.isEmpty()) filteredData.addAll(allData);
        else {
            for (PemasukanData data : allData) {
                if (data.getUsername().toLowerCase().contains(searchText) ||
                        data.getLokasi().toLowerCase().contains(searchText) ||
                        data.getJenisSampah().toLowerCase().contains(searchText)) {
                    filteredData.add(data);
                }
            }
        }
        handleSort();
        updatePaginationInfo();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class PemasukanData {
        private final StringProperty username;
        private final StringProperty tanggal;
        private final LocalDateTime rawDateTime;
        private final StringProperty jenisSampah;
        private final DoubleProperty berat;
        private final IntegerProperty poin;
        private final StringProperty lokasi;

        public PemasukanData(String username, String tanggal, LocalDateTime rawDateTime, String jenisSampah,
                             double berat, int poin, String lokasi) {
            this.username = new SimpleStringProperty(username);
            this.tanggal = new SimpleStringProperty(tanggal);
            this.rawDateTime = rawDateTime;
            this.jenisSampah = new SimpleStringProperty(jenisSampah);
            this.berat = new SimpleDoubleProperty(berat);
            this.poin = new SimpleIntegerProperty(poin);
            this.lokasi = new SimpleStringProperty(lokasi);
        }

        public String getUsername() { return username.get(); }
        public String getTanggal() { return tanggal.get(); }
        public LocalDateTime getRawDateTime() { return rawDateTime; }
        public String getJenisSampah() { return jenisSampah.get(); }
        public double getBerat() { return berat.get(); }
        public int getPoin() { return poin.get(); }
        public String getLokasi() { return lokasi.get(); }


        public StringProperty usernameProperty() { return username; }
        public StringProperty tanggalProperty() { return tanggal; }
        public StringProperty jenisSampahProperty() { return jenisSampah; }
        public DoubleProperty beratProperty() { return berat; }
        public IntegerProperty poinProperty() { return poin; }
        public StringProperty lokasiProperty() { return lokasi; }
    }
}