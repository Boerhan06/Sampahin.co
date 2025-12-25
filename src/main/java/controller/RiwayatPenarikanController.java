package controller;

import com.sampahin.Main;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class RiwayatPenarikanController {

    @FXML private Label lblCurrentDate;
    @FXML private Label lblTotalRequest;
    @FXML private Label lblPending;
    @FXML private Label lblTotalPoinDitarik;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortComboBox;

    @FXML private TableView<WithdrawalData> tableWithdrawal;
    @FXML private TableColumn<WithdrawalData, String> colUsername;
    @FXML private TableColumn<WithdrawalData, String> colIdKartu;
    @FXML private TableColumn<WithdrawalData, String> colTanggal;
    @FXML private TableColumn<WithdrawalData, Number> colJumlahPoin;
    @FXML private TableColumn<WithdrawalData, BigDecimal> colNilaiRupiah;
    @FXML private TableColumn<WithdrawalData, String> colMetode;
    @FXML private TableColumn<WithdrawalData, String> colStatus;
    @FXML private TableColumn<WithdrawalData, Void> colAction;

    @FXML private Label lblPagination;
    @FXML private Label lblPageNumber;

    private ObservableList<WithdrawalData> allData = FXCollections.observableArrayList();
    private ObservableList<WithdrawalData> filteredData = FXCollections.observableArrayList();

    private int currentPage = 1;
    private final int rowsPerPage = 20;

    @FXML
    public void initialize() {
        setupCurrentDate();
        setupSortOptions();
        setupTableColumns();
        loadSampleData();
        setupSearchListener();
        updateStats();
        updateTable();
    }

    private void setupCurrentDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
        lblCurrentDate.setText(today.format(formatter));
    }
    private void navigateTo(
            String fxmlFile,
            String title
    ){}
    private void setupSortOptions() {
        sortComboBox.setItems(FXCollections.observableArrayList(
                "Tanggal Terbaru",
                "Tanggal Terlama",
                "Poin Tertinggi",
                "Poin Terendah",
                "Status: Pending",
                "Status: Approved"
        ));

        sortComboBox.setValue("Tanggal Terbaru");
        sortComboBox.setOnAction(e -> handleSort());
    }

    private void setupTableColumns() {
        // Username Column
        colUsername.setCellValueFactory(data -> data.getValue().usernameProperty());
        colUsername.setCellFactory(col -> new TableCell<WithdrawalData, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #1f2937; -fx-font-size: 14px;");
                }
            }
        });

        // ID Kartu Column
        colIdKartu.setCellValueFactory(data -> data.getValue().idKartuProperty());
        colIdKartu.setCellFactory(col -> new TableCell<WithdrawalData, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-font-family: 'Courier New', monospace; -fx-text-fill: #6b7280; -fx-font-size: 13px;");
                }
            }
        });

        // Tanggal Column
        colTanggal.setCellValueFactory(data -> data.getValue().tanggalProperty());
        colTanggal.setCellFactory(col -> new TableCell<WithdrawalData, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: #4b5563; -fx-font-size: 13px;");
                }
            }
        });

        // Jumlah Poin Column
        colJumlahPoin.setCellValueFactory(data -> data.getValue().jumlahPoinProperty());
        colJumlahPoin.setCellFactory(col -> new TableCell<WithdrawalData, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,d", item.intValue()));
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #685BAB; -fx-font-size: 14px;");
                }
            }
        });

        // Nilai Rupiah Column
        colNilaiRupiah.setCellValueFactory(data -> data.getValue().nilaiRupiahProperty());
        colNilaiRupiah.setCellFactory(col -> new TableCell<WithdrawalData, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                    setText(formatter.format(item));
                    setStyle("-fx-font-weight: 600; -fx-text-fill: #059669; -fx-font-size: 14px;");
                }
            }
        });

        // Metode Column
        colMetode.setCellValueFactory(data -> data.getValue().metodeProperty());
        colMetode.setCellFactory(col -> new TableCell<WithdrawalData, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: #6b7280; -fx-font-size: 13px;");
                }
            }
        });

        // Status Column
        colStatus.setCellValueFactory(data -> data.getValue().statusProperty());
        colStatus.setCellFactory(col -> new TableCell<WithdrawalData, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label badge = new Label(status.toUpperCase());
                    badge.setAlignment(Pos.CENTER);

                    switch (status.toLowerCase()) {
                        case "pending":
                            badge.getStyleClass().add("status-pending");
                            break;
                        case "approved":
                            badge.getStyleClass().add("status-approved");
                            break;
                        case "rejected":
                            badge.getStyleClass().add("status-rejected");
                            break;
                    }

                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        // Action Column
        colAction.setCellFactory(col -> new TableCell<WithdrawalData, Void>() {
            private final Button btnApprove = new Button("✓");
            private final Button btnReject = new Button("✗");
            private final Button btnView = new Button("👁");
            private final HBox container = new HBox(5, btnApprove, btnReject, btnView);

            {
                btnApprove.getStyleClass().add("btn-approve");
                btnReject.getStyleClass().add("btn-reject");
                btnView.getStyleClass().add("btn-view");
                container.setAlignment(Pos.CENTER);

                btnApprove.setOnAction(e -> {
                    WithdrawalData data = getTableRow().getItem();
                    if (data != null) {
                        handleApprove(data);
                    }
                });

                btnReject.setOnAction(e -> {
                    WithdrawalData data = getTableRow().getItem();
                    if (data != null) {
                        handleReject(data);
                    }
                });

                btnView.setOnAction(e -> {
                    WithdrawalData data = getTableRow().getItem();
                    if (data != null) {
                        handleView(data);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    WithdrawalData data = getTableRow().getItem();
                    if (data != null && "pending".equalsIgnoreCase(data.getStatus())) {
                        setGraphic(container);
                    } else {
                        setGraphic(btnView);
                    }
                }
            }
        });
    }

    private void loadSampleData() {
        String[] usernames = {"ahmad_r", "budi_s", "citra_m", "dewi_l", "eko_p", "fajar_w", "gita_n", "hadi_k"};
        String[] metodes = {"Bank Transfer", "GoPay", "OVO", "DANA", "ShopeePay"};
        String[] statuses = {"Pending", "Approved", "Rejected"};

        for (int i = 1; i <= 100; i++) {
            String username = usernames[i % usernames.length];
            String idKartu = String.format("USR-%05d", 1000 + i);
            String tanggal = String.format("%02d Mei 2024", (i % 28) + 1);
            int poin = (int) (Math.random() * 5000) + 500;
            BigDecimal rupiah = BigDecimal.valueOf(poin * 10);
            String metode = metodes[i % metodes.length];
            String status = statuses[i % 3];

            allData.add(new WithdrawalData(username, idKartu, tanggal, poin, rupiah, metode, status));
        }

        filteredData.setAll(allData);
    }

    private void setupSearchListener() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filterData(newVal);
        });
    }

    private void filterData(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            filteredData.setAll(allData);
        } else {
            String lowerSearch = searchText.toLowerCase().trim();
            filteredData.setAll(allData.filtered(data ->
                    data.getUsername().toLowerCase().contains(lowerSearch) ||
                            data.getIdKartu().toLowerCase().contains(lowerSearch)
            ));
        }
        currentPage = 1;
        updateTable();
        updateStats();
    }

    private void handleSort() {
        String sortOption = sortComboBox.getValue();

        switch (sortOption) {
            case "Tanggal Terbaru":
                // Already in order
                break;
            case "Tanggal Terlama":
                FXCollections.reverse(filteredData);
                break;
            case "Poin Tertinggi":
                filteredData.sort((a, b) -> Integer.compare(b.getJumlahPoin(), a.getJumlahPoin()));
                break;
            case "Poin Terendah":
                filteredData.sort((a, b) -> Integer.compare(a.getJumlahPoin(), b.getJumlahPoin()));
                break;
            case "Status: Pending":
                filteredData.setAll(allData.filtered(d -> "pending".equalsIgnoreCase(d.getStatus())));
                break;
            case "Status: Approved":
                filteredData.setAll(allData.filtered(d -> "approved".equalsIgnoreCase(d.getStatus())));
                break;
        }

        currentPage = 1;
        updateTable();
    }

    private void updateTable() {
        int start = (currentPage - 1) * rowsPerPage;
        int end = Math.min(start + rowsPerPage, filteredData.size());

        tableWithdrawal.setItems(FXCollections.observableArrayList(
                filteredData.subList(start, end)
        ));

        lblPageNumber.setText(String.valueOf(currentPage));
        lblPagination.setText(String.format("Menampilkan %d-%d dari %d data",
                start + 1, end, filteredData.size()));
    }

    private void updateStats() {
        lblTotalRequest.setText(String.valueOf(allData.size()));

        long pendingCount = allData.stream()
                .filter(d -> "pending".equalsIgnoreCase(d.getStatus()))
                .count();
        lblPending.setText(String.valueOf(pendingCount));

        int totalPoin = allData.stream()
                .filter(d -> "approved".equalsIgnoreCase(d.getStatus()))
                .mapToInt(WithdrawalData::getJumlahPoin)
                .sum();
        lblTotalPoinDitarik.setText(String.format("%,d", totalPoin));
    }

    @FXML
    private void handlePrevPage() {
        if (currentPage > 1) {
            currentPage--;
            updateTable();
        }
    }

    @FXML
    private void handleNextPage() {
        int maxPage = (int) Math.ceil((double) filteredData.size() / rowsPerPage);
        if (currentPage < maxPage) {
            currentPage++;
            updateTable();
        }
    }

    @FXML
    private void handleExport() {
        System.out.println("Export to CSV functionality");
        // TODO: Implement CSV export
    }

    private void handleApprove(WithdrawalData data) {
        data.setStatus("Approved");
        tableWithdrawal.refresh();
        updateStats();
        System.out.println("Approved: " + data.getUsername());
    }

    private void handleReject(WithdrawalData data) {
        data.setStatus("Rejected");
        tableWithdrawal.refresh();
        updateStats();
        System.out.println("Rejected: " + data.getUsername());
    }

    private void handleView(WithdrawalData data) {
        System.out.println("View details for: " + data.getUsername());
        // TODO: Show detail dialog
    }

    @FXML
    private void handleDashboard() {
        Main.showDashboardView();
    }

    @FXML
    private void handleUsers() {
        Main.showDaftarPenggunaView();
    }

    @FXML
    private void handleWasteIncoming() {
        Main.showPemasukanSampahView();
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
    // Data Model Class
    public static class WithdrawalData {
        private final StringProperty username;
        private final StringProperty idKartu;
        private final StringProperty tanggal;
        private final IntegerProperty jumlahPoin;
        private final ObjectProperty<BigDecimal> nilaiRupiah;
        private final StringProperty metode;
        private final StringProperty status;

        public WithdrawalData(String username, String idKartu, String tanggal,
                              int jumlahPoin, BigDecimal nilaiRupiah, String metode, String status) {
            this.username = new SimpleStringProperty(username);
            this.idKartu = new SimpleStringProperty(idKartu);
            this.tanggal = new SimpleStringProperty(tanggal);
            this.jumlahPoin = new SimpleIntegerProperty(jumlahPoin);
            this.nilaiRupiah = new SimpleObjectProperty<>(nilaiRupiah);
            this.metode = new SimpleStringProperty(metode);
            this.status = new SimpleStringProperty(status);
        }

        // Properties
        public StringProperty usernameProperty() { return username; }
        public StringProperty idKartuProperty() { return idKartu; }
        public StringProperty tanggalProperty() { return tanggal; }
        public IntegerProperty jumlahPoinProperty() { return jumlahPoin; }
        public ObjectProperty<BigDecimal> nilaiRupiahProperty() { return nilaiRupiah; }
        public StringProperty metodeProperty() { return metode; }
        public StringProperty statusProperty() { return status; }

        // Getters
        public String getUsername() { return username.get(); }
        public String getIdKartu() { return idKartu.get(); }
        public String getTanggal() { return tanggal.get(); }
        public int getJumlahPoin() { return jumlahPoin.get(); }
        public BigDecimal getNilaiRupiah() { return nilaiRupiah.get(); }
        public String getMetode() { return metode.get(); }
        public String getStatus() { return status.get(); }

        // Setters
        public void setStatus(String status) { this.status.set(status); }
    }
}
