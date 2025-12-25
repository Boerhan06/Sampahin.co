package controller;

import com.sampahin.Main;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Locale;

public class DaftarPenggunaController {

    // FXML Components - Table
    @FXML private TableView<UserData> tableUsers;
    @FXML private TableColumn<UserData, String> colIdKartu;
    @FXML private TableColumn<UserData, String> colNomorKartu;
    @FXML private TableColumn<UserData, BigDecimal> colSaldoPoin;
    @FXML private TableColumn<UserData, BigDecimal> colSaldo;
    @FXML private TableColumn<UserData, Void> colActions;

    // FXML Components - Controls
    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private Button btnAddUser;
    @FXML private Label lblTotalUsers;
    @FXML private Label lblPaginationInfo;
    @FXML private Button btnPrevPage;
    @FXML private Button btnNextPage;

    // Data
    private ObservableList<UserData> userData = FXCollections.observableArrayList();
    private FilteredList<UserData> filteredData;
    private SortedList<UserData> sortedData;

    // Pagination
    private int currentPage = 1;
    private final int itemsPerPage = 50;

    // Currency Formatter
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("id", "ID"));

    @FXML
    public void initialize() {
        setupTableColumns();
        loadSampleData();
        setupSearchFilter();
        setupSortComboBox();
        updatePaginationInfo();
    }

    private void setupTableColumns() {
        // ID Kartu Column
        colIdKartu.setCellValueFactory(new PropertyValueFactory<>("idKartu"));
        colIdKartu.setStyle("-fx-alignment: CENTER-LEFT;");

        // Nomor Kartu Column
        colNomorKartu.setCellValueFactory(new PropertyValueFactory<>("nomorKartu"));
        colNomorKartu.setCellFactory(column -> new TableCell<UserData, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-font-family: 'Consolas', 'Courier New', monospace; -fx-font-size: 12px; -fx-text-fill: #4b5563;");
                }
            }
        });

        // Saldo Poin Column
        colSaldoPoin.setCellValueFactory(new PropertyValueFactory<>("saldoPoin"));
        colSaldoPoin.setCellFactory(column -> new TableCell<UserData, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(numberFormat.format(item) + " poin");
                    setStyle("-fx-font-weight: 700; -fx-text-fill: #059669; -fx-font-size: 14px;");
                }
            }
        });

        // Saldo Column
        colSaldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));
        colSaldo.setCellFactory(column -> new TableCell<UserData, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(currencyFormat.format(item));
                    setStyle("-fx-font-weight: 700; -fx-text-fill: #685BAB; -fx-font-size: 14px;");
                }
            }
        });

        // Actions Column
        colActions.setCellFactory(column -> new TableCell<UserData, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Hapus");
            private final HBox container = new HBox(8);

            {
                editButton.getStyleClass().add("btn-table-edit");
                deleteButton.getStyleClass().add("btn-table-delete");

                container.setAlignment(Pos.CENTER);
                container.getChildren().addAll(editButton, deleteButton);

                editButton.setOnAction(event -> {
                    UserData user = getTableView().getItems().get(getIndex());
                    handleEditUser(user);
                });

                deleteButton.setOnAction(event -> {
                    UserData user = getTableView().getItems().get(getIndex());
                    handleDeleteUser(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
    }

    private void loadSampleData() {
        // Generate sample user data
        for (int i = 1; i <= 100; i++) {
            userData.add(new UserData(
                    "USR" + String.format("%05d", i),
                    "4532-" + (1000 + i) + "-" + (2000 + i) + "-" + (3000 + i),
                    BigDecimal.valueOf(Math.random() * 10000).setScale(0, BigDecimal.ROUND_HALF_UP),
                    BigDecimal.valueOf(Math.random() * 5000000).setScale(0, BigDecimal.ROUND_HALF_UP)
            ));
        }

        filteredData = new FilteredList<>(userData, p -> true);
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableUsers.comparatorProperty());
        tableUsers.setItems(sortedData);
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (user.getIdKartu().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (user.getNomorKartu().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
            updatePaginationInfo();
        });
    }

    private void setupSortComboBox() {
        ObservableList<String> sortOptions = FXCollections.observableArrayList(
                "ID Kartu (A-Z)",
                "ID Kartu (Z-A)",
                "Saldo Poin (Tertinggi)",
                "Saldo Poin (Terendah)",
                "Saldo (Tertinggi)",
                "Saldo (Terendah)"
        );
        sortComboBox.setItems(sortOptions);
        sortComboBox.setValue("ID Kartu (A-Z)");

        sortComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                applySorting(newValue);
            }
        });
    }

    private void applySorting(String sortOption) {
        Comparator<UserData> comparator = null;

        switch (sortOption) {
            case "ID Kartu (A-Z)":
                comparator = Comparator.comparing(UserData::getIdKartu);
                break;
            case "ID Kartu (Z-A)":
                comparator = Comparator.comparing(UserData::getIdKartu).reversed();
                break;
            case "Saldo Poin (Tertinggi)":
                comparator = Comparator.comparing(UserData::getSaldoPoin).reversed();
                break;
            case "Saldo Poin (Terendah)":
                comparator = Comparator.comparing(UserData::getSaldoPoin);
                break;
            case "Saldo (Tertinggi)":
                comparator = Comparator.comparing(UserData::getSaldo).reversed();
                break;
            case "Saldo (Terendah)":
                comparator = Comparator.comparing(UserData::getSaldo);
                break;
        }

        if (comparator != null) {
            FXCollections.sort(userData, comparator);
        }
    }

    private void updatePaginationInfo() {
        int totalItems = filteredData.size();
        int startItem = totalItems > 0 ? ((currentPage - 1) * itemsPerPage) + 1 : 0;
        int endItem = Math.min(currentPage * itemsPerPage, totalItems);

        lblPaginationInfo.setText(String.format("Menampilkan %d-%d dari %s data",
                startItem, endItem, numberFormat.format(totalItems)));
        lblTotalUsers.setText(String.format("Total: %s pengguna", numberFormat.format(totalItems)));

        btnPrevPage.setDisable(currentPage <= 1);
        btnNextPage.setDisable(endItem >= totalItems);
    }

    // Navigation Methods
    @FXML
    private void handleDashboard() {
        Main.showDashboardView();
    }

    @FXML
    private void handleSampah() {
        Main.showPemasukanSampahView();
    }

    @FXML
    private void handlePenarikan() {
        Main.showRiwayatPenarikanView();
    }

    @FXML
    private void handleLogout() {
        System.out.println("[v0] Logout");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Logout");
        alert.setHeaderText("Apakah Anda yakin ingin keluar?");
        alert.setContentText("Anda akan kembali ke halaman login.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // TODO: Implement logout logic
                Stage stage = (Stage) btnAddUser.getScene().getWindow();
                stage.close();
            }
        });
    }

    @FXML
    private void handleAddUser() {
        System.out.println("[v0] Add new user");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tambah Pengguna");
        alert.setHeaderText("Fitur Tambah Pengguna");
        alert.setContentText("Form tambah pengguna akan ditampilkan di sini.");
        alert.showAndWait();
        // TODO: Open add user dialog
    }

    private void handleEditUser(UserData user) {
        System.out.println("[v0] Edit user: " + user.getIdKartu());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Pengguna");
        alert.setHeaderText("Edit Data Pengguna");
        alert.setContentText("Mengedit pengguna: " + user.getIdKartu());
        alert.showAndWait();
        // TODO: Open edit user dialog
    }

    private void handleDeleteUser(UserData user) {
        System.out.println("[v0] Delete user: " + user.getIdKartu());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText("Hapus Data Pengguna");
        alert.setContentText("Apakah Anda yakin ingin menghapus pengguna " + user.getIdKartu() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                userData.remove(user);
                updatePaginationInfo();
                System.out.println("[v0] User deleted successfully");
            }
        });
    }

    @FXML
    private void handlePrevPage() {
        if (currentPage > 1) {
            currentPage--;
            updatePaginationInfo();
        }
    }

    @FXML
    private void handleNextPage() {
        int totalPages = (int) Math.ceil((double) filteredData.size() / itemsPerPage);
        if (currentPage < totalPages) {
            currentPage++;
            updatePaginationInfo();
        }
    }

    // Inner Class: UserData Model
    public static class UserData {
        private final StringProperty idKartu;
        private final StringProperty nomorKartu;
        private final ObjectProperty<BigDecimal> saldoPoin;
        private final ObjectProperty<BigDecimal> saldo;

        public UserData(String idKartu, String nomorKartu, BigDecimal saldoPoin, BigDecimal saldo) {
            this.idKartu = new SimpleStringProperty(idKartu);
            this.nomorKartu = new SimpleStringProperty(nomorKartu);
            this.saldoPoin = new SimpleObjectProperty<>(saldoPoin);
            this.saldo = new SimpleObjectProperty<>(saldo);
        }

        // Getters
        public String getIdKartu() { return idKartu.get(); }
        public StringProperty idKartuProperty() { return idKartu; }

        public String getNomorKartu() { return nomorKartu.get(); }
        public StringProperty nomorKartuProperty() { return nomorKartu; }

        public BigDecimal getSaldoPoin() { return saldoPoin.get(); }
        public ObjectProperty<BigDecimal> saldoPoinProperty() { return saldoPoin; }

        public BigDecimal getSaldo() { return saldo.get(); }
        public ObjectProperty<BigDecimal> saldoProperty() { return saldo; }

        // Setters
        public void setIdKartu(String value) { idKartu.set(value); }
        public void setNomorKartu(String value) { nomorKartu.set(value); }
        public void setSaldoPoin(BigDecimal value) { saldoPoin.set(value); }
        public void setSaldo(BigDecimal value) { saldo.set(value); }
    }
}
