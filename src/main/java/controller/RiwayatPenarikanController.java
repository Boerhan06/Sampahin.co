package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.RiwayatPenarikan;
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


public class RiwayatPenarikanController extends BaseController implements Initializable {


    @FXML private Label lblCurrentDate;


    @FXML private Label lblTotalPenarikan;
    @FXML private Label lblTotalUangKeluar;
    @FXML private Label lblTransaksiPending;


    @FXML private TextField searchField;
    @FXML private ComboBox<String> cmbSort;
    @FXML private Label lblPaginationInfo;


    @FXML private TableView<RiwayatPenarikan> tableRiwayat;
    @FXML private TableColumn<RiwayatPenarikan, String> colId;
    @FXML private TableColumn<RiwayatPenarikan, String> colUsername;
    @FXML private TableColumn<RiwayatPenarikan, String> colTanggal;
    @FXML private TableColumn<RiwayatPenarikan, String> colMetode;
    @FXML private TableColumn<RiwayatPenarikan, Double> colJumlah;
    @FXML private TableColumn<RiwayatPenarikan, String> colStatus;


    private ObservableList<RiwayatPenarikan> allData = FXCollections.observableArrayList();
    private ObservableList<RiwayatPenarikan> filteredData = FXCollections.observableArrayList();

    private final DecimalFormat currencyFormat = new DecimalFormat("#,###");



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        setupSortFilter();
        updateCurrentDate();
        loadDataFromDatabase();


        searchField.textProperty().addListener((obs, oldVal, newVal) -> handleSearch());
    }

    @Override
    protected void updateUI() {

        if (currentAkun != null) {
            System.out.println("✅ RiwayatPenarikan: Akses oleh Admin " + currentAkun.getNamaLengkap());
        }
    }



    private void setupSortFilter() {
        ObservableList<String> sortOptions = FXCollections.observableArrayList(
                "Waktu: Terbaru",
                "Waktu: Terlama",
                "Nominal: Tertinggi",
                "Nominal: Terendah",
                "Status: Pending (Prioritas)",
                "Status: Berhasil",
                "Status: Gagal"
        );

        cmbSort.setItems(sortOptions);
        cmbSort.setValue("Waktu: Terbaru");


        cmbSort.setOnAction(e -> handleSort());
    }

    @FXML
    private void handleSort() {
        String selected = cmbSort.getValue();
        if (selected == null) return;

        Comparator<RiwayatPenarikan> comparator = null;

        switch (selected) {
            case "Waktu: Terbaru":
                comparator = Comparator.comparing(RiwayatPenarikan::getRawDateTime).reversed();
                break;
            case "Waktu: Terlama":
                comparator = Comparator.comparing(RiwayatPenarikan::getRawDateTime);
                break;
            case "Nominal: Tertinggi":
                comparator = Comparator.comparingDouble(RiwayatPenarikan::getJumlah).reversed();
                break;
            case "Nominal: Terendah":
                comparator = Comparator.comparingDouble(RiwayatPenarikan::getJumlah);
                break;
            case "Status: Pending (Prioritas)":
                comparator = Comparator.comparing((RiwayatPenarikan r) ->
                                r.getStatus().equalsIgnoreCase("Pending") ? 0 : 1)
                        .thenComparing(Comparator.comparing(RiwayatPenarikan::getRawDateTime).reversed());
                break;
            case "Status: Berhasil":
                comparator = Comparator.comparing((RiwayatPenarikan r) ->
                                r.getStatus().equalsIgnoreCase("Berhasil") ? 0 : 1)
                        .thenComparing(Comparator.comparing(RiwayatPenarikan::getRawDateTime).reversed());
                break;
            case "Status: Gagal":
                comparator = Comparator.comparing((RiwayatPenarikan r) ->
                                r.getStatus().equalsIgnoreCase("Gagal") ? 0 : 1)
                        .thenComparing(Comparator.comparing(RiwayatPenarikan::getRawDateTime).reversed());
                break;
        }

        if (comparator != null) {
            FXCollections.sort(filteredData, comparator);
        }
    }



    private void loadDataFromDatabase() {
        allData.clear();


        String query = "SELECT r.*, a.username FROM riwayat_penarikan r " +
                "JOIN akun a ON r.idAkun = a.idAkun " +
                "ORDER BY r.waktu_transaksi DESC";

        Connection conn = DatabaseConnection.getInstance().getConnection();

        if (conn == null) {
            showAlert("Error Koneksi", "Tidak dapat terhubung ke database.");
            return;
        }

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter viewFormat = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", new Locale("id", "ID"));

            while (rs.next()) {
                String id = "WD-" + rs.getInt("id_penarikan");
                String username = rs.getString("username");
                String metode = rs.getString("metode_penarikan");
                double jumlah = rs.getDouble("jumlah_penarikan");
                String status = rs.getString("status_penarikan");
                String rawDate = rs.getString("waktu_transaksi");


                LocalDateTime ldt = LocalDateTime.now();
                String formattedDate = rawDate;
                try {
                    if (rawDate != null && rawDate.contains(".")) {
                        rawDate = rawDate.substring(0, rawDate.indexOf("."));
                    }
                    ldt = LocalDateTime.parse(rawDate, dbFormat);
                    formattedDate = ldt.format(viewFormat);
                } catch (Exception e) {
                    System.err.println("Gagal memproses format tanggal: " + e.getMessage());
                }


                allData.add(new RiwayatPenarikan(id, username, formattedDate, ldt, metode, jumlah, status));
            }


            filteredData.setAll(allData);
            tableRiwayat.setItems(filteredData);


            handleSort();
            updateStatistics();
            updatePaginationInfo();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error Database", "Gagal mengambil data: " + e.getMessage());
        }
    }

    private void handleUpdateStatus(RiwayatPenarikan data, String newStatus) {
        if (data == null) return;


        if (!data.getStatus().equalsIgnoreCase("Pending")) {
            return;
        }


        String idStr = data.getIdPenarikan().replace("WD-", "");
        String sql = "UPDATE riwayat_penarikan SET status_penarikan = ? WHERE id_penarikan = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, Integer.parseInt(idStr));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {

                loadDataFromDatabase();
            }

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            showAlert("Error Update", "Gagal memperbarui status transaksi.");
        }
    }



    private void setupTable() {
        // Hubungkan kolom dengan property pada model RiwayatPenarikan
        colId.setCellValueFactory(new PropertyValueFactory<>("idPenarikan"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colMetode.setCellValueFactory(new PropertyValueFactory<>("metode"));
        colJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));


        colId.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else {
                    setText(item);
                    setStyle("-fx-font-family: 'Consolas', monospace; -fx-text-fill: #6b7280;");
                }
            }
        });


        colUsername.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else {
                    setText(item);
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #1f2937;");
                }
            }
        });


        colJumlah.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else {
                    setText("Rp " + currencyFormat.format(item));
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #EF4444;");
                }
            }
        });


        colMetode.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); setText(null); }
                else {
                    Label badge = new Label(item);
                    String style = "-fx-background-color: #F3F4F6; -fx-text-fill: #374151;";
                    String lower = item.toLowerCase();
                    if (lower.contains("bca") || lower.contains("bank")) style = "-fx-background-color: #DBEAFE; -fx-text-fill: #1E40AF;";
                    else if (lower.contains("tunai")) style = "-fx-background-color: #D1FAE5; -fx-text-fill: #065F46;";
                    else if (lower.contains("ovo") || lower.contains("gopay") || lower.contains("dana"))
                        style = "-fx-background-color: #FCE7F3; -fx-text-fill: #9D174D;";

                    badge.setStyle(style + "-fx-background-radius: 4; -fx-padding: 2 8; -fx-font-size: 11px; -fx-font-weight: bold;");
                    setGraphic(badge); setText(null);
                }
            }
        });


        colStatus.setCellFactory(col -> new TableCell<RiwayatPenarikan, String>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    if (item.equalsIgnoreCase("Pending")) {

                        MenuButton statusBtn = new MenuButton(item.toUpperCase());
                        statusBtn.setStyle("-fx-background-color: #FEECDC; -fx-text-fill: #8A2C0D; " +
                                "-fx-background-radius: 12; -fx-font-size: 10px; -fx-font-weight: bold; -fx-cursor: hand;");

                        MenuItem actionApprove = new MenuItem("Setujui (Berhasil)");
                        MenuItem actionReject = new MenuItem("Tolak (Gagal)");

                        actionApprove.setOnAction(e -> handleUpdateStatus(getTableRow().getItem(), "Berhasil"));
                        actionReject.setOnAction(e -> handleUpdateStatus(getTableRow().getItem(), "Gagal"));

                        statusBtn.getItems().addAll(actionApprove, actionReject);
                        setGraphic(statusBtn);
                    } else {

                        Label badge = new Label(item.toUpperCase());
                        String style = "-fx-background-radius: 12; -fx-padding: 4 10; -fx-font-size: 10px; -fx-font-weight: bold;";
                        if (item.equalsIgnoreCase("Berhasil") || item.equalsIgnoreCase("Approved"))
                            style += "-fx-background-color: #DEF7EC; -fx-text-fill: #03543F;";
                        else
                            style += "-fx-background-color: #FDE8E8; -fx-text-fill: #9B1C1C;";

                        badge.setStyle(style);
                        setGraphic(badge);
                    }
                    setText(null);
                }
            }
        });
    }



    private void updateStatistics() {
        int total = allData.size();
        double totalUang = allData.stream()
                .filter(d -> "Berhasil".equalsIgnoreCase(d.getStatus()) || "Approved".equalsIgnoreCase(d.getStatus()))
                .mapToDouble(RiwayatPenarikan::getJumlah)
                .sum();
        long pending = allData.stream()
                .filter(d -> "Pending".equalsIgnoreCase(d.getStatus()))
                .count();

        lblTotalPenarikan.setText(String.valueOf(total));
        lblTotalUangKeluar.setText("Rp " + currencyFormat.format(totalUang));
        lblTransaksiPending.setText(String.valueOf(pending));
    }

    private void updateCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
        lblCurrentDate.setText(sdf.format(new Date()));
    }

    private void updatePaginationInfo() {
        lblPaginationInfo.setText("Menampilkan " + filteredData.size() + " data penarikan");
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase().trim();
        filteredData.clear();

        if (keyword.isEmpty()) {
            filteredData.addAll(allData);
        } else {
            for (RiwayatPenarikan data : allData) {
                if (data.getUsername().toLowerCase().contains(keyword) ||
                        data.getIdPenarikan().toLowerCase().contains(keyword) ||
                        data.getMetode().toLowerCase().contains(keyword) ||
                        data.getStatus().toLowerCase().contains(keyword)) {
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
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}