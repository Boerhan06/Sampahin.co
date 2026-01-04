package controller;

import dao.PenggunaDAO;
import dao.SampahDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import models.Pengguna;
import models.Sampah;
import util.DatabaseConnection;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class DaftarPenggunaController extends BaseController implements Initializable {


    @FXML private TableView<Pengguna> tableUsers;
    @FXML private TableColumn<Pengguna, String> colNomorKartu;
    @FXML private TableColumn<Pengguna, String> colNama;
    @FXML private TableColumn<Pengguna, BigDecimal> colSaldoPoin;
    @FXML private TableColumn<Pengguna, BigDecimal> colSaldo;
    @FXML private TableColumn<Pengguna, Void> colActions;

    @FXML private Label lblCardPenggunaAktif;
    @FXML private Label lblCardRegistrasiBaru;
    @FXML private Label lblCardTotalPoin;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private Button btnAddUser;
    @FXML private Label lblTotalUsers;
    @FXML private Label lblPaginationInfo;
    @FXML private Button btnPrevPage;
    @FXML private Button btnNextPage;


    private ObservableList<Pengguna> listPengguna = FXCollections.observableArrayList();
    private FilteredList<Pengguna> filteredData;
    private SortedList<Pengguna> sortedData;


    private final PenggunaDAO penggunaDAO = new PenggunaDAO();
    private final SampahDAO sampahDAO = new SampahDAO();


    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("id", "ID"));
    private int currentPage = 1;
    private final int itemsPerPage = 50;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (tableUsers != null) {
            tableUsers.getStyleClass().add("table-border-ungu");
        }

        setupTableColumns();
        loadDataFromDatabase();
        loadDashboardStatistics();
        setupSearchFilter();
        setupSortComboBox();
        updatePaginationInfo();
    }

    @Override
    protected void updateUI() {
        if (currentAkun != null) {
            System.out.println("✅ DaftarPengguna: Diakses oleh Admin " + currentAkun.getNamaLengkap());
        }
    }



    private void setupTableColumns() {


        colNomorKartu.setCellValueFactory(new PropertyValueFactory<>("nomorKartu"));
        colNomorKartu.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-font-family: 'Consolas', monospace; -fx-text-fill: #6B7280;");
                }
            }
        });


        colNama.setCellValueFactory(new PropertyValueFactory<>("namaLengkap"));
        colNama.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #111827; -fx-font-size: 13px;");
                }
            }
        });



        colSaldoPoin.setCellValueFactory(new PropertyValueFactory<>("saldoPoin"));
        colSaldoPoin.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null); setText(null);
                } else {
                    Label badge = new Label(numberFormat.format(item) + " Poin");
                    badge.setStyle("-fx-background-color: #ECFDF5; -fx-text-fill: #059669; -fx-background-radius: 6; -fx-padding: 4 8; -fx-font-weight: bold; -fx-font-size: 11px;");
                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        colSaldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));
        colSaldo.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Rp " + numberFormat.format(item));
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #7C3AED;");
                }
            }
        });


        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("Edit");
            private final Button btnTrans = new Button("Transaksi");
            private final HBox pane = new HBox(10, btnEdit, btnTrans);

            {
                pane.setAlignment(Pos.CENTER_LEFT);
                btnEdit.getStyleClass().add("btn-edit");
                btnTrans.getStyleClass().add("btn-tukar");


                btnEdit.setOnAction(event -> {
                    Pengguna data = getTableView().getItems().get(getIndex());
                    handleEditUserCheck(data);
                });

                btnTrans.setOnAction(event -> {
                    Pengguna data = getTableView().getItems().get(getIndex());
                    handleMenuTransaksi(data);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }



    private void handleEditUserCheck(Pengguna user) {
        Dialog<String> dialog = createModernDialog();
        VBox header = createModernHeader("Verifikasi Admin");

        VBox content = new VBox(15);
        content.getStyleClass().add("custom-content-box");
        content.setAlignment(Pos.CENTER);

        Label lblMsg = new Label("Masukkan Kode Admin untuk mengedit data user:");
        lblMsg.getStyleClass().add("dialog-subtitle");

        PasswordField txtKode = new PasswordField();
        txtKode.setPromptText("Kode Admin");
        txtKode.getStyleClass().add("form-field");

        content.getChildren().addAll(lblMsg, txtKode);

        HBox footer = createModernFooter();
        Button btnBatal = new Button("Batal"); btnBatal.getStyleClass().add("btn-cancel");
        Button btnAkses = new Button("Lanjut Edit"); btnAkses.getStyleClass().add("btn-save");
        footer.getChildren().addAll(btnAkses, btnBatal);

        assembleDialog(dialog, header, content, footer);

        btnAkses.setOnAction(e -> { dialog.setResult(txtKode.getText()); dialog.close(); });
        btnBatal.setOnAction(e -> { dialog.setResult(null); dialog.close(); });

        dialog.showAndWait().ifPresent(kode -> {
            if ("ADMIN123".equals(kode)) {
                showPopupEditUserForm(user);
            } else {
                showModernAlert(Alert.AlertType.ERROR, "Akses Ditolak", "Kode keamanan salah!");
            }
        });
    }

    private void showPopupEditUserForm(Pengguna user) {
        Dialog<Pengguna> dialog = createModernDialog();
        VBox header = createModernHeader("Edit Data Pengguna");

        VBox content = new VBox(10);
        content.getStyleClass().add("custom-content-box");

        TextField txtNama = new TextField(user.getNamaLengkap());
        TextField txtEmail = new TextField(user.getEmail());
        TextField txtTelp = new TextField(user.getNoTelepon());
        TextField txtUser = new TextField(user.getUsername());
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Isi jika ingin ubah password");

        TextField[] fields = {txtNama, txtEmail, txtTelp, txtUser};
        for(TextField f : fields) f.getStyleClass().add("form-field");
        txtPass.getStyleClass().add("form-field");

        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Nama:"), 0, 0); grid.add(txtNama, 1, 0);
        grid.add(new Label("Email:"), 0, 1); grid.add(txtEmail, 1, 1);
        grid.add(new Label("No. HP:"), 0, 2); grid.add(txtTelp, 1, 2);
        grid.add(new Label("Username:"), 0, 3); grid.add(txtUser, 1, 3);
        grid.add(new Label("Password Baru:"), 0, 4); grid.add(txtPass, 1, 4);

        content.getChildren().add(grid);

        HBox footer = createModernFooter();
        Button btnBatal = new Button("Batal"); btnBatal.getStyleClass().add("btn-cancel");
        Button btnSimpan = new Button("Simpan Perubahan"); btnSimpan.getStyleClass().add("btn-save");
        footer.getChildren().addAll(btnSimpan, btnBatal);

        assembleDialog(dialog, header, content, footer);

        btnSimpan.setOnAction(e -> {
            if (txtNama.getText().isEmpty() || txtUser.getText().isEmpty()) {
                showModernAlert(Alert.AlertType.WARNING, "Gagal", "Nama dan Username tidak boleh kosong.");
                return;
            }

            user.setNamaLengkap(txtNama.getText());
            user.setEmail(txtEmail.getText());
            user.setNoTelepon(txtTelp.getText());
            user.setUsername(txtUser.getText());

            if (!txtPass.getText().isEmpty()) {
                user.setHashedPassword(txtPass.getText());
            }

            dialog.setResult(user);
            dialog.close();
        });

        btnBatal.setOnAction(e -> { dialog.setResult(null); dialog.close(); });

        dialog.showAndWait().ifPresent(updatedUser -> {
            if (penggunaDAO.updateDataPengguna(updatedUser)) {
                showModernAlert(Alert.AlertType.INFORMATION, "Sukses", "Data pengguna diperbarui.");
                tableUsers.refresh();
            } else {
                showModernAlert(Alert.AlertType.ERROR, "Gagal", "Gagal mengupdate database.");
            }
        });
    }



    @FXML
    private void handleAddUser() {
        Dialog<Pengguna> dialog = createModernDialog();
        VBox header = createModernHeader("Tambah Pengguna");

        VBox content = new VBox(10);
        content.getStyleClass().add("custom-content-box");

        TextField txtNama = new TextField(); txtNama.setPromptText("Nama Lengkap");
        TextField txtEmail = new TextField(); txtEmail.setPromptText("Email");
        TextField txtTelp = new TextField(); txtTelp.setPromptText("No. Telepon");
        TextField txtUser = new TextField(); txtUser.setPromptText("Username");
        PasswordField txtPass = new PasswordField(); txtPass.setPromptText("Password");
        TextField txtKartu = new TextField(); txtKartu.setPromptText("No. Kartu (Auto/Manual)");

        TextField[] fields = {txtNama, txtEmail, txtTelp, txtUser, txtKartu};
        for(TextField f : fields) f.getStyleClass().add("form-field");
        txtPass.getStyleClass().add("form-field");

        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Nama:"), 0, 0); grid.add(txtNama, 1, 0);
        grid.add(new Label("Email:"), 0, 1); grid.add(txtEmail, 1, 1);
        grid.add(new Label("No. HP:"), 0, 2); grid.add(txtTelp, 1, 2);
        grid.add(new Label("User:"), 0, 3); grid.add(txtUser, 1, 3);
        grid.add(new Label("Pass:"), 0, 4); grid.add(txtPass, 1, 4);
        grid.add(new Label("Kartu:"), 0, 5); grid.add(txtKartu, 1, 5);

        content.getChildren().add(grid);

        HBox footer = createModernFooter();
        Button btnBatal = new Button("Batal"); btnBatal.getStyleClass().add("btn-cancel");
        Button btnSimpan = new Button("Simpan Data"); btnSimpan.getStyleClass().add("btn-save");
        footer.getChildren().addAll(btnSimpan, btnBatal);

        assembleDialog(dialog, header, content, footer);

        btnSimpan.setOnAction(e -> {
            if (txtNama.getText().isEmpty() || txtUser.getText().isEmpty() || txtPass.getText().isEmpty()) {
                showModernAlert(Alert.AlertType.WARNING, "Data Kurang", "Nama, Username, dan Password wajib diisi!");
                return;
            }

            String idKartu = txtKartu.getText().isEmpty() ? "USR-" + System.currentTimeMillis() : txtKartu.getText();

            Pengguna newUser = new Pengguna(
                    0, txtNama.getText(), "", txtTelp.getText(), txtEmail.getText(),
                    txtUser.getText(), txtPass.getText(), true, LocalDateTime.now(), LocalDateTime.now(),
                    idKartu, idKartu, BigDecimal.ZERO, BigDecimal.ZERO, LocalDate.now()
            );

            dialog.setResult(newUser);
            dialog.close();
        });

        btnBatal.setOnAction(e -> { dialog.setResult(null); dialog.close(); });

        dialog.showAndWait().ifPresent(user -> {
            if (penggunaDAO.registerPenggunaBaru(user, user.getHashedPassword())) {
                showModernAlert(Alert.AlertType.INFORMATION, "Sukses", "Pengguna berhasil ditambahkan!");
                loadDataFromDatabase();
                loadDashboardStatistics();
            } else {
                showModernAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menyimpan ke database.");
            }
        });
    }



    private void handleMenuTransaksi(Pengguna user) {
        Dialog<String> dialog = createModernDialog();
        VBox header = createModernHeader("Pilih Transaksi");

        VBox content = new VBox(15);
        content.getStyleClass().add("custom-content-box");
        content.setAlignment(Pos.CENTER);

        Label lblUser = new Label("Nasabah: " + user.getNamaLengkap());
        lblUser.setStyle("-fx-font-weight: bold; -fx-text-fill: #685BAB;");

        GridPane grid = new GridPane();
        grid.setHgap(15); grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        Button btnSetor = createMenuButton("Setor Sampah", "M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z", "#10B981");
        btnSetor.setOnAction(e -> { dialog.close(); showPopupTambahSampah(user); });

        Button btnKonversi = createMenuButton("Konversi Poin", "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 16h-2v-2h2v2zm0-4h-2V7h2v7z", "#F59E0B");
        btnKonversi.setOnAction(e -> { dialog.close(); showPopupKonversiPoin(user); });

        Button btnTarik = createMenuButton("Tarik Saldo", "M11.8 10.9c-2.27-.59-3-1.2-3-2.15 0-1.09 1.01-1.85 2.7-1.85 1.78 0 2.44.85 2.5 2.1h2.21c-.07-1.72-1.12-3.3-3.21-3.81V3h-3v2.16c-1.94.42-3.5 1.68-3.5 3.61 0 2.31 1.91 3.46 4.7 4.13 2.5.6 3 1.48 3 2.41 0 .69-.49 1.79-2.7 1.79-2.06 0-2.87-.92-2.98-2.1h-2.2c.12 2.19 1.76 3.42 3.68 3.83V21h3v-2.15c1.95-.37 3.5-1.5 3.5-3.55 0-2.84-2.43-3.81-4.7-4.4z", "#3B82F6");
        btnTarik.setOnAction(e -> { dialog.close(); showPopupTarikSaldo(user); });

        Button btnEdit = createMenuButton("Edit Poin", "M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z", "#EF4444");
        btnEdit.setOnAction(e -> { dialog.close(); verifyAdminAndShowEditPoin(user); });

        grid.add(btnSetor, 0, 0); grid.add(btnKonversi, 1, 0);
        grid.add(btnTarik, 0, 1); grid.add(btnEdit, 1, 1);

        content.getChildren().addAll(lblUser, grid);
        HBox footer = createModernFooter();
        Button btnCancel = new Button("Tutup"); btnCancel.getStyleClass().add("btn-cancel");
        btnCancel.setOnAction(e -> dialog.close());
        footer.getChildren().add(btnCancel);

        assembleDialog(dialog, header, content, footer);
        dialog.show();
    }

    private void showPopupTambahSampah(Pengguna user) {
        System.out.println("=== [DEBUG] Membuka Popup Setor Sampah ==="); // DEBUG 1

        Dialog<Void> dialog = createModernDialog();
        VBox header = createModernHeader("Setor Sampah");
        VBox content = new VBox(15);
        content.getStyleClass().add("custom-content-box");


        ObservableList<Sampah> jenisSampahList = getDaftarHargaSampah();


        if (jenisSampahList == null || jenisSampahList.isEmpty()) {
            System.err.println("❌ [DEBUG] BAHAYA: List Sampah KOSONG atau NULL!");
            System.err.println("   -> Cek apakah tabel 'sampah' di database sudah diisi?");
            System.err.println("   -> Cek apakah koneksi database berhasil?");
        } else {
            System.out.println("✅ [DEBUG] Berhasil mengambil " + jenisSampahList.size() + " jenis sampah:");
            for (Sampah s : jenisSampahList) {
                System.out.println("   - Item: " + s.getJenisSampah() + " | Harga: " + s.getHargaPoinPerKg());
            }
        }


        ComboBox<Sampah> cbJenis = new ComboBox<>(jenisSampahList);
        cbJenis.setPromptText("Pilih Jenis Sampah");
        cbJenis.setMaxWidth(Double.MAX_VALUE);
        cbJenis.getStyleClass().add("form-field");


        cbJenis.setConverter(new StringConverter<Sampah>() {
            @Override
            public String toString(Sampah object) {
                return object != null ? object.getJenisSampah() : "";
            }

            @Override
            public Sampah fromString(String string) {
                return cbJenis.getItems().stream()
                        .filter(s -> s.getJenisSampah().equals(string))
                        .findFirst().orElse(null);
            }
        });


        TextField txtBerat = new TextField();
        txtBerat.setPromptText("Berat (Kg)"); txtBerat.getStyleClass().add("form-field");

        Label lblEstimasi = new Label("Total Poin: 0");
        lblEstimasi.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #685BAB;");


        txtBerat.textProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("   [DEBUG] Input Berat: " + newVal);
            calculatePoin(cbJenis.getValue(), newVal, lblEstimasi);
        });

        cbJenis.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                System.out.println("   [DEBUG] Sampah Dipilih: " + newVal.getJenisSampah());
            }
            calculatePoin(newVal, txtBerat.getText(), lblEstimasi);
        });

        content.getChildren().addAll(new Label("Jenis Sampah:"), cbJenis, new Label("Berat (Kg):"), txtBerat, new Separator(), lblEstimasi);

        HBox footer = createModernFooter();
        Button btnBatal = new Button("Batal"); btnBatal.getStyleClass().add("btn-cancel");
        Button btnSimpan = new Button("Proses"); btnSimpan.getStyleClass().add("btn-save");
        footer.getChildren().addAll(btnSimpan, btnBatal);

        assembleDialog(dialog, header, content, footer);

        btnSimpan.setOnAction(e -> {
            System.out.println("=== [DEBUG] Tombol Proses Ditekan ==="); // DEBUG ACTION

            if (cbJenis.getValue() == null) {
                System.err.println("❌ [DEBUG] Gagal: Jenis Sampah belum dipilih (NULL)");
                showModernAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih jenis sampah dulu.");
                return;
            }

            if (txtBerat.getText().isEmpty()) {
                System.err.println("❌ [DEBUG] Gagal: Berat kosong");
                showModernAlert(Alert.AlertType.WARNING, "Peringatan", "Isi berat sampah.");
                return;
            }

            try {
                double berat = Double.parseDouble(txtBerat.getText());
                BigDecimal harga = cbJenis.getValue().getHargaPoinPerKg();
                BigDecimal totalPoin = harga.multiply(new BigDecimal(berat));

                System.out.println("✅ [DEBUG] Data Valid:");
                System.out.println("   - Sampah: " + cbJenis.getValue().getJenisSampah());
                System.out.println("   - Berat : " + berat);
                System.out.println("   - Total : " + totalPoin);

                // Simpan transaksi dan update saldo user secara atomik
                saveTransaksiSampah(user, cbJenis.getValue(), berat, totalPoin);

                dialog.close();
            } catch (NumberFormatException ex) {
                System.err.println("❌ [DEBUG] Error Parsing Angka: " + ex.getMessage());
                showModernAlert(Alert.AlertType.ERROR, "Error", "Berat harus angka valid (contoh: 1.5).");
            } catch (Exception ex) {
                System.err.println("❌ [DEBUG] Error Lainnya: ");
                ex.printStackTrace();
            }
        });

        btnBatal.setOnAction(e -> dialog.close());
        dialog.show();
    }

    private void calculatePoin(Sampah sampah, String beratStr, Label lblOutput) {
        if (sampah != null && !beratStr.isEmpty()) {
            try {
                double berat = Double.parseDouble(beratStr);
                BigDecimal total = sampah.getHargaPoinPerKg().multiply(new BigDecimal(berat));
                lblOutput.setText("Total Poin: " + numberFormat.format(total));
            } catch (NumberFormatException e) {
                lblOutput.setText("Total Poin: 0");
            }
        }
    }

    private void showPopupKonversiPoin(Pengguna user) {
        Dialog<Void> dialog = createModernDialog();
        VBox header = createModernHeader("Konversi Poin ke Saldo");
        VBox content = new VBox(15);
        content.getStyleClass().add("custom-content-box");

        Label lblSaldoPoin = new Label("Poin Saat Ini: " + numberFormat.format(user.getSaldoPoin()));
        lblSaldoPoin.getStyleClass().add("dialog-subtitle");

        TextField txtPoinTukar = new TextField();
        txtPoinTukar.setPromptText("Jumlah Poin ditukar"); txtPoinTukar.getStyleClass().add("form-field");

        Label lblDapatSaldo = new Label("Akan mendapat: Rp 0");
        lblDapatSaldo.setStyle("-fx-font-weight: bold; -fx-text-fill: #10B981;");

        txtPoinTukar.textProperty().addListener((obs, o, n) -> {
            try {
                double poin = Double.parseDouble(n);
                double rupiah = poin / 10.0;
                lblDapatSaldo.setText("Akan mendapat: " + currencyFormat.format(rupiah));
            } catch (Exception e) { lblDapatSaldo.setText("Akan mendapat: Rp 0"); }
        });

        content.getChildren().addAll(lblSaldoPoin, new Label("Tukar Poin:"), txtPoinTukar, lblDapatSaldo);

        HBox footer = createModernFooter();
        Button btnBatal = new Button("Batal"); btnBatal.getStyleClass().add("btn-cancel");
        Button btnProses = new Button("Tukar"); btnProses.getStyleClass().add("btn-save");
        footer.getChildren().addAll(btnProses, btnBatal);

        assembleDialog(dialog, header, content, footer);

        btnProses.setOnAction(e -> {
            try {
                BigDecimal poinTukar = new BigDecimal(txtPoinTukar.getText());
                if (poinTukar.compareTo(user.getSaldoPoin()) > 0) {
                    showModernAlert(Alert.AlertType.WARNING, "Gagal", "Poin tidak cukup!");
                    return;
                }

                if (user.tukarPoinKeSaldo(poinTukar)) {
                    penggunaDAO.updateSaldoPoin(user);
                    showModernAlert(Alert.AlertType.INFORMATION, "Sukses", "Saldo berhasil ditambahkan.");
                    loadDataFromDatabase();
                    loadDashboardStatistics();
                    dialog.close();
                }
            } catch (Exception ex) {
                showModernAlert(Alert.AlertType.ERROR, "Error", "Input tidak valid.");
            }
        });
        btnBatal.setOnAction(e -> dialog.close());
        dialog.show();
    }

    private void showPopupTarikSaldo(Pengguna user) {
        Dialog<Void> dialog = createModernDialog();
        VBox header = createModernHeader("Tarik Saldo");
        VBox content = new VBox(10);
        content.getStyleClass().add("custom-content-box");

        // Menampilkan saldo dari kolom 'saldo' di tabel pengguna
        Label lblSaldo = new Label("Saldo Tersedia: " + currencyFormat.format(user.getSaldo()));
        lblSaldo.getStyleClass().add("dialog-subtitle");

        ComboBox<String> cbMetode = new ComboBox<>(FXCollections.observableArrayList("Tunai (Cash)", "Transfer BCA", "Transfer BNI", "DANA", "Gopay"));
        cbMetode.setPromptText("Metode Pencairan");
        cbMetode.setMaxWidth(Double.MAX_VALUE);
        cbMetode.getStyleClass().add("form-field");

        TextField txtRekening = new TextField();
        txtRekening.setPromptText("Nomor Rekening / No. HP");
        txtRekening.getStyleClass().add("form-field");
        txtRekening.setDisable(true);

        cbMetode.setOnAction(e -> {
            txtRekening.setDisable(cbMetode.getValue().contains("Tunai"));
            if (txtRekening.isDisable()) txtRekening.clear();
        });

        TextField txtNominal = new TextField();
        txtNominal.setPromptText("Nominal Penarikan");
        txtNominal.getStyleClass().add("form-field");

        Label lblBiaya = new Label("Biaya Admin: Rp 0");
        lblBiaya.setStyle("-fx-text-fill: #EF4444; -fx-font-size: 11px;");

        cbMetode.valueProperty().addListener((obs, o, val) -> {
            if (val != null && !val.contains("Tunai")) lblBiaya.setText("Biaya Admin: Rp 2.500");
            else lblBiaya.setText("Biaya Admin: Rp 0");
        });

        content.getChildren().addAll(lblSaldo, new Label("Metode:"), cbMetode, txtRekening, new Label("Nominal:"), txtNominal, lblBiaya);

        HBox footer = createModernFooter();
        Button btnBatal = new Button("Batal"); btnBatal.getStyleClass().add("btn-cancel");
        Button btnTarik = new Button("Tarik Dana"); btnTarik.getStyleClass().add("btn-save");
        footer.getChildren().addAll(btnTarik, btnBatal);

        assembleDialog(dialog, header, content, footer);

        btnTarik.setOnAction(e -> {
            Connection conn = null;
            try {
                if (cbMetode.getValue() == null || txtNominal.getText().isEmpty()) {
                    showModernAlert(Alert.AlertType.WARNING, "Data Tidak Lengkap", "Silakan pilih metode dan isi nominal.");
                    return;
                }

                BigDecimal nominal = new BigDecimal(txtNominal.getText().replace(".", "").replace(",", ""));
                BigDecimal biaya = cbMetode.getValue().contains("Tunai") ? BigDecimal.ZERO : new BigDecimal("2500");
                BigDecimal totalPotong = nominal.add(biaya);

                if (totalPotong.compareTo(user.getSaldo()) > 0) {
                    showModernAlert(Alert.AlertType.WARNING, "Saldo Kurang", "Saldo Anda tidak mencukupi untuk penarikan + biaya admin.");
                    return;
                }


                conn = util.DatabaseConnection.getInstance().getConnection();
                if (conn == null || conn.isClosed()) {
                    showModernAlert(Alert.AlertType.ERROR, "Koneksi Error", "Tidak dapat terhubung ke database.");
                    return;
                }

                conn.setAutoCommit(false);


                String sqlUpdateSaldo = "UPDATE pengguna SET saldo = saldo - ? WHERE idAkun = ?";
                try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateSaldo)) {
                    psUpdate.setBigDecimal(1, totalPotong);
                    psUpdate.setInt(2, user.getIdAkun());
                    psUpdate.executeUpdate();
                }


                String sqlRiwayat = "INSERT INTO riwayat_penarikan (idAkun, jumlah_penarikan, metode_penarikan, nomor_rekening, status_penarikan, waktu_transaksi) " +
                        "VALUES (?, ?, ?, ?, 'Pending', NOW())";

                try (PreparedStatement psInsert = conn.prepareStatement(sqlRiwayat)) {
                    psInsert.setInt(1, user.getIdAkun());
                    psInsert.setBigDecimal(2, nominal);
                    psInsert.setString(3, cbMetode.getValue());
                    psInsert.setString(4, txtRekening.getText().isEmpty() ? "-" : txtRekening.getText());
                    psInsert.executeUpdate();
                }

                conn.commit();

                showModernAlert(Alert.AlertType.INFORMATION, "Sukses", "Permintaan penarikan berhasil disimpan!");


                user.setSaldo(user.getSaldo().subtract(totalPotong));
                loadDataFromDatabase();
                dialog.close();

            } catch (Exception ex) {

                if (conn != null) {
                    try {
                        if (!conn.isClosed()) conn.rollback();
                    } catch (SQLException exR) { exR.printStackTrace(); }
                }
                ex.printStackTrace();
                showModernAlert(Alert.AlertType.ERROR, "Error", "Gagal memproses transaksi: " + ex.getMessage());
            } finally {

                if (conn != null) {
                    try {
                        if (!conn.isClosed()) conn.setAutoCommit(true);
                    } catch (SQLException exA) { exA.printStackTrace(); }
                }
            }
        });

        btnBatal.setOnAction(e -> dialog.close());
        dialog.show();
    }

    private void verifyAdminAndShowEditPoin(Pengguna user) {
        Dialog<String> dialog = createModernDialog();
        VBox header = createModernHeader("Verifikasi Admin");
        VBox content = new VBox(15); content.getStyleClass().add("custom-content-box");

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Kode Admin"); txtPass.getStyleClass().add("form-field");
        content.getChildren().addAll(new Label("Masukkan Kode Keamanan:"), txtPass);

        HBox footer = createModernFooter();
        Button btnOk = new Button("Verifikasi"); btnOk.getStyleClass().add("btn-save");
        footer.getChildren().add(btnOk);
        assembleDialog(dialog, header, content, footer);

        btnOk.setOnAction(e -> { dialog.setResult(txtPass.getText()); dialog.close(); });

        dialog.showAndWait().ifPresent(code -> {
            if ("ADMIN123".equals(code)) showPopupEditPoin(user);
            else showModernAlert(Alert.AlertType.ERROR, "Akses Ditolak", "Kode salah!");
        });
    }

    private void showPopupEditPoin(Pengguna user) {
        Dialog<Void> dialog = createModernDialog();
        VBox header = createModernHeader("Edit Poin Manual");
        VBox content = new VBox(10);
        content.getStyleClass().add("custom-content-box");

        TextField txtPoinBaru = new TextField(user.getSaldoPoin().toString());
        txtPoinBaru.setPromptText("Jumlah Poin Baru"); txtPoinBaru.getStyleClass().add("form-field");

        TextArea txtAlasan = new TextArea();
        txtAlasan.setPromptText("Alasan Perubahan (Wajib diisi)");
        txtAlasan.setPrefRowCount(3);
        txtAlasan.getStyleClass().add("form-field");

        content.getChildren().addAll(new Label("Poin Baru:"), txtPoinBaru, new Label("Keterangan:"), txtAlasan);

        HBox footer = createModernFooter();
        Button btnSimpan = new Button("Simpan Perubahan"); btnSimpan.getStyleClass().add("btn-save");
        footer.getChildren().add(btnSimpan);

        assembleDialog(dialog, header, content, footer);

        btnSimpan.setOnAction(e -> {
            if (txtAlasan.getText().isEmpty()) {
                showModernAlert(Alert.AlertType.WARNING, "Wajib", "Alasan perubahan harus diisi!");
                return;
            }
            try {
                BigDecimal poinBaru = new BigDecimal(txtPoinBaru.getText());
                user.setSaldoPoin(poinBaru);
                penggunaDAO.updateSaldoPoin(user);

                showModernAlert(Alert.AlertType.INFORMATION, "Sukses", "Data poin diperbarui.");
                loadDataFromDatabase();
                loadDashboardStatistics();
                dialog.close();
            } catch (Exception ex) {
                showModernAlert(Alert.AlertType.ERROR, "Error", "Format angka salah.");
            }
        });
        dialog.show();
    }



    private void loadDataFromDatabase() {
        listPengguna.clear();
        String query = "SELECT a.*, p.IdKartu, p.nomorKartu, p.saldoPoin, p.saldo, p.tanggalDaftar " +
                "FROM pengguna p JOIN akun a ON p.idAkun = a.idAkun";


        Connection conn = DatabaseConnection.getInstance().getConnection();


        if (conn == null) {
            showModernAlert(Alert.AlertType.ERROR, "Koneksi Gagal", "Tidak dapat terhubung ke database.");
            return;
        }


        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                listPengguna.add(new Pengguna(
                        rs.getInt("idAkun"),
                        rs.getString("namaLengkap"),
                        rs.getString("alamat"),
                        rs.getString("NoTelepon"),
                        rs.getString("email"),
                        rs.getString("Username"),
                        rs.getString("hashedPassword"),
                        true,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        rs.getString("IdKartu"),
                        rs.getString("nomorKartu"),
                        rs.getBigDecimal("saldoPoin"),
                        rs.getBigDecimal("saldo"),
                        rs.getObject("tanggalDaftar", LocalDate.class)
                ));
            }

            filteredData = new FilteredList<>(listPengguna, b -> true);
            sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableUsers.comparatorProperty());
            tableUsers.setItems(sortedData);

            if (lblTotalUsers != null) {
                lblTotalUsers.setText("Total: " + numberFormat.format(listPengguna.size()) + " Pengguna");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showModernAlert(Alert.AlertType.ERROR, "Database Error", "Gagal memuat data pengguna.");
        }
    }

    private void loadDashboardStatistics() {

        int totalUser = penggunaDAO.countTotalPengguna();
        int newReg = penggunaDAO.countRegistrasiBulanIni();
        BigDecimal totalPoin = penggunaDAO.sumTotalSaldoPoin();

        if (lblCardPenggunaAktif != null) lblCardPenggunaAktif.setText(numberFormat.format(totalUser));
        if (lblCardRegistrasiBaru != null) lblCardRegistrasiBaru.setText(numberFormat.format(newReg));
        if (lblCardTotalPoin != null) lblCardTotalPoin.setText(formatCompactNumber(totalPoin));
    }

    private ObservableList<Sampah> getDaftarHargaSampah() {

        List<Sampah> dataDB = sampahDAO.getAllSampah();
        return FXCollections.observableArrayList(dataDB);
    }

    private void saveTransaksiSampah(Pengguna user, Sampah sampah, double berat, BigDecimal totalPoin) {
        String sqlInsert = "INSERT INTO transaksi_sampah (idAkun, jenis_sampah, kategori_sampah, berat_kg, poin_per_kg, total_poin, waktu_transaksi, keterangan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlUpdateUser = "UPDATE pengguna SET saldoPoin = saldoPoin + ? WHERE idAkun = ?";


        Connection conn = DatabaseConnection.getInstance().getConnection();

        try {

            conn.setAutoCommit(false);


            try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                stmtInsert.setInt(1, user.getIdAkun());
                stmtInsert.setString(2, sampah.getJenisSampah());
                stmtInsert.setString(3, sampah.getKategori());
                stmtInsert.setDouble(4, berat);
                stmtInsert.setBigDecimal(5, sampah.getHargaPoinPerKg());
                stmtInsert.setBigDecimal(6, totalPoin);
                stmtInsert.setObject(7, LocalDateTime.now());
                stmtInsert.setString(8, "Setoran Sampah via Admin");
                stmtInsert.executeUpdate();
            }


            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdateUser)) {
                stmtUpdate.setBigDecimal(1, totalPoin);
                stmtUpdate.setInt(2, user.getIdAkun());
                stmtUpdate.executeUpdate();
            }


            conn.commit();


            loadDataFromDatabase();
            loadDashboardStatistics();

            showModernAlert(Alert.AlertType.INFORMATION, "Sukses", "Transaksi disimpan & poin bertambah!");

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) { ex.printStackTrace(); }

            e.printStackTrace();
            showModernAlert(Alert.AlertType.ERROR, "DB Error", "Gagal menyimpan transaksi: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private String formatCompactNumber(BigDecimal value) {
        long val = value.longValue();
        if (val >= 1_000_000) return String.format("%.1fM", val / 1_000_000.0).replace(",", ".");
        if (val >= 1_000) return String.format("%.1fK", val / 1_000.0).replace(",", ".");
        return numberFormat.format(val);
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(user -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lower = newVal.toLowerCase();
                return user.getNomorKartu().contains(lower) || user.getNamaLengkap().toLowerCase().contains(lower);
            });
            updatePaginationInfo();
        });
    }

    private void setupSortComboBox() {
        sortComboBox.setItems(FXCollections.observableArrayList("Nama (A-Z)", "Poin Tertinggi", "Saldo Tertinggi"));
        sortComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;
            Comparator<Pengguna> comp = switch(newVal) {
                case "Nama (A-Z)" -> Comparator.comparing(Pengguna::getNamaLengkap);
                case "Poin Tertinggi" -> Comparator.comparing(Pengguna::getSaldoPoin).reversed();
                case "Saldo Tertinggi" -> Comparator.comparing(Pengguna::getSaldo).reversed();
                default -> null;
            };
            if (comp != null) FXCollections.sort(listPengguna, comp);
        });
    }

    private void updatePaginationInfo() {
        int total = filteredData.size();
        int start = total == 0 ? 0 : (currentPage - 1) * itemsPerPage + 1;
        int end = Math.min(currentPage * itemsPerPage, total);
        lblPaginationInfo.setText(String.format("Menampilkan %d-%d dari %s data", start, end, numberFormat.format(total)));
        btnPrevPage.setDisable(currentPage <= 1);
        btnNextPage.setDisable(end >= total);
    }

    @FXML private void handlePrevPage() { if (currentPage > 1) { currentPage--; updatePaginationInfo(); } }
    @FXML private void handleNextPage() { if (currentPage * itemsPerPage < filteredData.size()) { currentPage++; updatePaginationInfo(); } }


    private <T> Dialog<T> createModernDialog() {
        Dialog<T> dialog = new Dialog<>();
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/popup-style.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("my-dialog-pane");
        return dialog;
    }

    private VBox createModernHeader(String title) {
        VBox header = new VBox();
        header.getStyleClass().add("custom-header-box");
        Label lbl = new Label(title);
        lbl.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px;");

        StackPane iconPane = new StackPane();
        Circle bg = new Circle(24, Color.WHITE); bg.setOpacity(0.2);
        SVGPath icon = new SVGPath();
        icon.setContent("M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z");
        icon.setFill(Color.WHITE); icon.setScaleX(1.2); icon.setScaleY(1.2);
        iconPane.getChildren().addAll(bg, icon);

        header.getChildren().addAll(iconPane, lbl);
        header.setAlignment(Pos.CENTER);
        header.setSpacing(10);
        return header;
    }

    private HBox createModernFooter() {
        HBox footer = new HBox();
        footer.getStyleClass().add("custom-footer-box");
        return footer;
    }

    private void assembleDialog(Dialog<?> dialog, VBox header, VBox content, HBox footer) {
        VBox mainCard = new VBox();
        mainCard.getStyleClass().add("dialog-card");
        mainCard.getChildren().addAll(header, content, footer);
        dialog.getDialogPane().setContent(mainCard);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeBtn = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeBtn.setVisible(false);
    }

    private Button createMenuButton(String text, String svgContent, String colorHex) {
        Button btn = new Button();
        btn.setPrefSize(140, 100);
        btn.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2); -fx-background-radius: 10; -fx-cursor: hand;");

        VBox content = new VBox(8);
        content.setAlignment(Pos.CENTER);

        SVGPath icon = new SVGPath();
        icon.setContent(svgContent);
        icon.setFill(Color.web(colorHex));
        icon.setScaleX(2); icon.setScaleY(2);

        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #374151;");

        content.getChildren().addAll(icon, lbl);
        btn.setGraphic(content);

        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #F3F4F6; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 8, 0, 0, 4); -fx-background-radius: 10;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2); -fx-background-radius: 10;"));

        return btn;
    }

    private void showModernAlert(Alert.AlertType type, String title, String msg) {
        Dialog<Void> d = createModernDialog();
        VBox h = createModernHeader(title);
        VBox c = new VBox(10); c.getStyleClass().add("custom-content-box"); c.setAlignment(Pos.CENTER);
        c.getChildren().add(new Label(msg));
        HBox f = createModernFooter();
        Button ok = new Button("OK"); ok.getStyleClass().add("btn-save");
        ok.setOnAction(e -> d.close()); f.getChildren().add(ok);
        assembleDialog(d, h, c, f);
        d.show();
    }
}