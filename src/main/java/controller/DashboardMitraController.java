package controller;

import dao.MitraDAO;
import dao.TransaksiMitraDAO;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import models.Mitra;
import models.TransaksiMitra;
import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class DashboardMitraController extends BaseController implements Initializable {
    
    @FXML private VBox contentArea;
    @FXML private ImageView mascotImage;
    @FXML private Circle shape1;
    @FXML private Circle shape2;
    @FXML private Circle shape3;
    @FXML private Label lblTanggal;
    @FXML private Label lblSaldo;
    @FXML private Label lblTotalSampah;
    @FXML private Label lblGreeting;
    @FXML private Button btnLogout;
    @FXML private Button btnBayarSampah;
    @FXML private Button btnTarikSaldo;
    @FXML private Button btnTopUp;
    @FXML private TableView<TransaksiMitra> tableTransaksi;
    @FXML private TableColumn<TransaksiMitra, LocalDateTime> colTanggal;
    @FXML private TableColumn<TransaksiMitra, BigDecimal> colNominal;
    @FXML private TableColumn<TransaksiMitra, BigDecimal> colBeratSampah;
    @FXML private TableColumn<TransaksiMitra, String> colBuktiFoto; // Digunakan untuk KETERANGAN


    private final TransaksiMitraDAO transaksiDAO = new TransaksiMitraDAO();
    private final MitraDAO mitraDAO = new MitraDAO();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", new Locale("id", "ID"));
    private final Map<String, Integer> hargaSampahMap = new HashMap<>();
    private final String[] metodeTopUpList = {"DANA", "BCA", "OVO", "GOPAY"};
    private final String[] bankTujuanList = {"BCA", "BNI", "BSI", "MANDIRI"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inisialisasi Harga Sampah
        hargaSampahMap.put("Plastik", 3000);
        hargaSampahMap.put("Kertas / Kardus", 2500);
        hargaSampahMap.put("Besi / Logam", 5000);
        hargaSampahMap.put("Kaca", 1500);
        hargaSampahMap.put("Organik", 1000);

        setupTableColumns();
        setupCurrentDate();
        playEntranceAnimations();
        animateBackground();
        setupNavigationButtons();
        System.out.println("DEBUG: Dashboard Mitra Initialized.");
    }

    @Override
    protected void updateUI() {
        if (currentAkun instanceof Mitra) {
            Mitra sessionMitra = (Mitra) currentAkun;
            Mitra freshMitra = mitraDAO.getMitraByUsername(sessionMitra.getUsername());
            if (freshMitra != null) {
                this.currentAkun = freshMitra;
                sessionMitra = freshMitra;
            }
            if (lblGreeting != null) {
                lblGreeting.setText("Halo, " + sessionMitra.getUsername() + "!");
            }
            if (sessionMitra.getSaldo() != null) {
                lblSaldo.setText(currencyFormat.format(sessionMitra.getSaldo()));
            } else {
                lblSaldo.setText("Rp 0");
            }
            loadTransactionTable(sessionMitra.getUsername());
        }
    }

    private void setupTableColumns() {
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colTanggal.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : item.format(dateFormatter));
            }
        });

        colBuktiFoto.setText("KETERANGAN");
        colBuktiFoto.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
        colBuktiFoto.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #4B5563;");
                }
            }
        });

        colNominal.setCellValueFactory(new PropertyValueFactory<>("nominal"));
        colNominal.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    TransaksiMitra trx = getTableView().getItems().get(getIndex());
                    String formattedMoney = currencyFormat.format(item);

                    if (trx.getJenis() == TransaksiMitra.JenisTransaksi.PEMASUKAN) {
                        setText("+ " + formattedMoney);
                        setStyle("-fx-text-fill: #10B981; -fx-font-weight: bold;");
                    } else {
                        setText("- " + formattedMoney);
                        setStyle("-fx-text-fill: #EF4444; -fx-font-weight: bold;");
                    }
                }
            }
        });


        colBeratSampah.setCellValueFactory(new PropertyValueFactory<>("beratSampah"));
        colBeratSampah.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.compareTo(BigDecimal.ZERO) == 0) {
                    setText("-");
                } else {
                    setText(item + " Kg");
                }
            }
        });
    }

    private void loadTransactionTable(String namaMitra) {
        List<TransaksiMitra> dataList = transaksiDAO.getAllByNamaMitra(namaMitra);
        ObservableList<TransaksiMitra> observableData = FXCollections.observableArrayList(dataList);
        tableTransaksi.setItems(observableData);

        if (dataList.isEmpty()) {
            tableTransaksi.setPlaceholder(new Label("Belum ada riwayat transaksi."));
            lblTotalSampah.setText("0 Kg");
        } else {
            BigDecimal totalBerat = BigDecimal.ZERO;
            for (TransaksiMitra trx : dataList) {
                if (trx.getBeratSampah() != null) {
                    totalBerat = totalBerat.add(trx.getBeratSampah());
                }
            }
            lblTotalSampah.setText(totalBerat + " Kg");
        }
    }

    private void setupCurrentDate() {
        lblTanggal.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("id", "ID"))));
    }

    private void setupNavigationButtons() {
        if (btnLogout != null) {
            btnLogout.setOnAction(e -> {
                playButtonAnimation(btnLogout, () -> super.handleLogout(null));
            });
        }
    }



    @FXML
    private void handleInputSampah() {
        playButtonAnimation(btnBayarSampah, () -> Platform.runLater(this::showInputSampahPopup));
    }

    @FXML
    private void handleTarikSaldo() {
        playButtonAnimation(btnTarikSaldo, () -> Platform.runLater(this::showTarikSaldoPopup));
    }

    @FXML
    private void handleTopUp() {
        playButtonAnimation(btnTopUp, () -> Platform.runLater(this::showTopUpPopup));
    }




    private void showTopUpPopup() {
        Stage popupStage = createPopupStage();
        VBox dialogCard = createDialogContainer();
        // Header Ungu
        VBox headerBox = createHeader("Top Up Saldo", "Isi saldo dompet mitra anda");
        VBox contentBox = new VBox();
        contentBox.getStyleClass().add("custom-content-box");
        contentBox.setSpacing(10);

        contentBox.getChildren().add(createStyledLabel("Nominal Top Up (Min. Rp 20.000)"));
        TextField txtNominal = new TextField();
        txtNominal.setPromptText("Contoh: 50000");
        txtNominal.getStyleClass().add("form-field");
        contentBox.getChildren().add(txtNominal);

        contentBox.getChildren().add(createStyledLabel("Metode Pembayaran"));
        ComboBox<String> cmbMetode = new ComboBox<>();
        cmbMetode.getItems().addAll(metodeTopUpList);
        cmbMetode.setPromptText("Pilih Pembayaran");
        cmbMetode.setMaxWidth(Double.MAX_VALUE);
        // Styling combobox inline agar match dengan form-field CSS
        cmbMetode.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 10; -fx-padding: 5;");
        contentBox.getChildren().add(cmbMetode);

        Button btnProses = new Button("Bayar Top Up");
        btnProses.getStyleClass().add("btn-save");


        btnProses.setOnAction(e -> {
            try {
                String nominalStr = txtNominal.getText();
                String metode = cmbMetode.getValue();

                if (nominalStr.isEmpty() || metode == null) {
                    showAlert(Alert.AlertType.WARNING, "Peringatan", "Mohon lengkapi semua data.");
                    return;
                }

                double nominal = Double.parseDouble(nominalStr);

                if (nominal < 20000) {
                    showAlert(Alert.AlertType.ERROR, "Gagal", "Minimal Top Up adalah Rp 20.000");
                    return;
                }

                Mitra mitra = (Mitra) currentAkun;
                BigDecimal saldoBaru = mitra.getSaldo().add(BigDecimal.valueOf(nominal));

                boolean successSaldo = mitraDAO.updateSaldo(mitra.getIdAkun(), saldoBaru);

                if (successSaldo) {
                    mitra.setSaldo(saldoBaru);
                    lblSaldo.setText(currencyFormat.format(saldoBaru));

                    TransaksiMitra trxBaru = new TransaksiMitra(
                            0, "Top Up via " + metode, BigDecimal.valueOf(nominal),
                            BigDecimal.ZERO, TransaksiMitra.JenisTransaksi.PEMASUKAN, LocalDateTime.now()
                    );
                    trxBaru.setNamaMitraBisnis(mitra.getUsername());
                    transaksiDAO.save(trxBaru);

                    loadTransactionTable(mitra.getUsername());
                    popupStage.close();
                    showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Top Up Berhasil! Saldo bertambah.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menghubungi database.");
                }

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Nominal harus berupa angka.");
            }
        });

        setupFooter(popupStage, dialogCard, headerBox, contentBox, btnProses);
        showStage(popupStage, dialogCard);
    }


    private void showInputSampahPopup() {
        Stage popupStage = createPopupStage();
        VBox dialogCard = createDialogContainer();
        VBox headerBox = createHeader("Input Sampah", "Hitung dan bayar sampah nasabah");
        VBox contentBox = new VBox();
        contentBox.getStyleClass().add("custom-content-box");
        contentBox.setSpacing(10);


        contentBox.getChildren().add(createStyledLabel("Jenis Sampah"));
        ComboBox<String> cmbKategori = new ComboBox<>();
        cmbKategori.getItems().addAll(hargaSampahMap.keySet());
        cmbKategori.setPromptText("- Pilih Kategori -");
        cmbKategori.setMaxWidth(Double.MAX_VALUE);
        cmbKategori.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 10; -fx-padding: 5;");
        contentBox.getChildren().add(cmbKategori);

        Label lblHargaInfo = new Label("Harga: - / Kg");
        lblHargaInfo.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 11px; -fx-font-style: italic;");
        contentBox.getChildren().add(lblHargaInfo);


        contentBox.getChildren().add(createStyledLabel("Berat (Kg)"));
        TextField txtBerat = new TextField();
        txtBerat.setPromptText("0.0");
        txtBerat.getStyleClass().add("form-field");
        contentBox.getChildren().add(txtBerat);


        contentBox.getChildren().add(createStyledLabel("Sumber Dana Pembayaran"));
        ComboBox<String> cmbSumberDana = new ComboBox<>();
        cmbSumberDana.getItems().addAll("Tunai (Cash)", "Saldo Dompet Mitra");
        cmbSumberDana.setValue("Saldo Dompet Mitra");
        cmbSumberDana.setMaxWidth(Double.MAX_VALUE);
        cmbSumberDana.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 10; -fx-padding: 5;");
        contentBox.getChildren().add(cmbSumberDana);


        Label lblTotal = new Label("Total Bayar: Rp 0");

        lblTotal.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #685BAB; -fx-padding: 10 0 0 0;");
        contentBox.getChildren().add(lblTotal);

        final double[] totalFinal = {0.0};


        Runnable hitung = () -> {
            try {
                String kat = cmbKategori.getValue();
                String ber = txtBerat.getText();
                if (kat != null && !ber.isEmpty()) {
                    double berat = Double.parseDouble(ber);
                    int harga = hargaSampahMap.get(kat);
                    double tot = berat * harga;
                    totalFinal[0] = tot;

                    lblHargaInfo.setText("Harga: " + currencyFormat.format(harga) + " / Kg");
                    lblTotal.setText("Total Bayar: " + currencyFormat.format(tot));
                }
            } catch (Exception e) { /* Ignore */ }
        };
        cmbKategori.setOnAction(e -> hitung.run());
        txtBerat.textProperty().addListener((obs, old, newVal) -> hitung.run());

        Button btnSubmit = new Button("Proses Transaksi");
        btnSubmit.getStyleClass().add("btn-save");


        btnSubmit.setOnAction(e -> {
            if (totalFinal[0] <= 0) {
                showAlert(Alert.AlertType.WARNING, "Error", "Data sampah tidak valid.");
                return;
            }

            Mitra mitra = (Mitra) currentAkun;
            BigDecimal tagihan = BigDecimal.valueOf(totalFinal[0]);
            String sumberDana = cmbSumberDana.getValue();
            boolean updateSaldoNeeded = "Saldo Dompet Mitra".equals(sumberDana);

            if (updateSaldoNeeded && mitra.getSaldo().compareTo(tagihan) < 0) {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Saldo Dompet Mitra tidak cukup.");
                return;
            }

            boolean successSaldo = true;
            if (updateSaldoNeeded) {
                BigDecimal sisaSaldo = mitra.getSaldo().subtract(tagihan);
                successSaldo = mitraDAO.updateSaldo(mitra.getIdAkun(), sisaSaldo);
                if (successSaldo) {
                    mitra.setSaldo(sisaSaldo);
                    lblSaldo.setText(currencyFormat.format(sisaSaldo));
                }
            }

            if (successSaldo) {
                try {
                    double beratDouble = Double.parseDouble(txtBerat.getText());
                    TransaksiMitra trxBaru = new TransaksiMitra(
                            0, "Beli " + cmbKategori.getValue() + " (Nasabah)",
                            tagihan, BigDecimal.valueOf(beratDouble),
                            TransaksiMitra.JenisTransaksi.PENGELUARAN, LocalDateTime.now()
                    );
                    trxBaru.setNamaMitraBisnis(mitra.getUsername());
                    trxBaru.setKategoriSampahTerjual(cmbKategori.getValue());

                    transaksiDAO.save(trxBaru);
                    loadTransactionTable(mitra.getUsername());
                    popupStage.close();
                    showAlert(Alert.AlertType.INFORMATION, "Sukses", "Transaksi berhasil diproses!");
                } catch(Exception ex) { ex.printStackTrace(); }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal update saldo di database.");
            }
        });

        setupFooter(popupStage, dialogCard, headerBox, contentBox, btnSubmit);
        showStage(popupStage, dialogCard);
    }


    private void showTarikSaldoPopup() {
        Stage popupStage = createPopupStage();
        VBox dialogCard = createDialogContainer();
        VBox headerBox = createHeader("Tarik Saldo", "Cairkan pendapatan anda ke rekening");
        VBox contentBox = new VBox();
        contentBox.getStyleClass().add("custom-content-box");
        contentBox.setSpacing(10);

        contentBox.getChildren().add(createStyledLabel("Nominal Penarikan (Rp)"));
        TextField txtNominal = new TextField();
        txtNominal.setPromptText("Minimal Rp 50.000");
        txtNominal.getStyleClass().add("form-field");
        contentBox.getChildren().add(txtNominal);

        contentBox.getChildren().add(createStyledLabel("Bank Tujuan"));
        ComboBox<String> cmbBank = new ComboBox<>();
        cmbBank.getItems().addAll(bankTujuanList);
        cmbBank.setPromptText("Pilih Bank");
        cmbBank.setMaxWidth(Double.MAX_VALUE);
        cmbBank.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 10; -fx-padding: 5;");
        contentBox.getChildren().add(cmbBank);

        Button btnTarik = new Button("Ajukan Penarikan");
        btnTarik.getStyleClass().add("btn-save");

        btnTarik.setOnAction(e -> {
            try {
                String nominalStr = txtNominal.getText();
                String selectedBank = cmbBank.getValue();

                if (nominalStr.isEmpty() || selectedBank == null) {
                    showAlert(Alert.AlertType.WARNING, "Peringatan", "Mohon lengkapi nominal dan pilih bank.");
                    return;
                }

                double nominal = Double.parseDouble(nominalStr);
                Mitra mitra = (Mitra) currentAkun;

                if (mitra.getSaldo().compareTo(BigDecimal.valueOf(nominal)) < 0) {
                    showAlert(Alert.AlertType.ERROR, "Gagal", "Saldo tidak mencukupi.");
                    return;
                }

                BigDecimal sisaSaldo = mitra.getSaldo().subtract(BigDecimal.valueOf(nominal));
                boolean success = mitraDAO.updateSaldo(mitra.getIdAkun(), sisaSaldo);

                if (success) {
                    mitra.setSaldo(sisaSaldo);
                    lblSaldo.setText(currencyFormat.format(sisaSaldo));

                    TransaksiMitra trx = new TransaksiMitra(
                            0, "Penarikan ke " + selectedBank, BigDecimal.valueOf(nominal),
                            BigDecimal.ZERO, TransaksiMitra.JenisTransaksi.PENGELUARAN, LocalDateTime.now()
                    );
                    trx.setNamaMitraBisnis(mitra.getUsername());
                    transaksiDAO.save(trx);

                    loadTransactionTable(mitra.getUsername());
                    popupStage.close();
                    showAlert(Alert.AlertType.INFORMATION, "Info", "Permintaan penarikan ke " + selectedBank + " berhasil dikirim.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menghubungi database.");
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Nominal harus berupa angka.");
            }
        });

        setupFooter(popupStage, dialogCard, headerBox, contentBox, btnTarik);
        showStage(popupStage, dialogCard);
    }



    private Stage createPopupStage() {
        Stage stage = new Stage();
        stage.initOwner(contentArea.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        return stage;
    }

    private VBox createDialogContainer() {
        VBox v = new VBox();
        v.getStyleClass().add("dialog-card");
        v.setMinWidth(420);
        return v;
    }

    private VBox createHeader(String title, String subtitle) {
        VBox h = new VBox(5);
        h.getStyleClass().add("custom-header-box");

        Label t = new Label(title);
        t.getStyleClass().add("dialog-title");

        t.setStyle("-fx-text-fill: white;");

        Label s = new Label(subtitle);
        s.getStyleClass().add("dialog-subtitle");

        s.setStyle("-fx-text-fill: rgba(255,255,255,0.8);");

        h.getChildren().addAll(t, s);
        return h;
    }

    private Label createStyledLabel(String text) {
        Label l = new Label(text);

        l.getStyleClass().add("dialog-label-bold");
        return l;
    }

    private void setupFooter(Stage stage, VBox card, VBox header, VBox content, Button actionBtn) {
        HBox footer = new HBox();
        footer.getStyleClass().add("custom-footer-box");

        Button cancel = new Button("Batal");
        cancel.getStyleClass().add("btn-cancel");
        cancel.setOnAction(e -> stage.close());

        footer.getChildren().addAll(cancel, actionBtn);
        card.getChildren().addAll(header, content, footer);
    }

    private void showStage(Stage stage, VBox root) {
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        URL css = getClass().getResource("/css/popup-style.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }



    private void playEntranceAnimations() {

        contentArea.setOpacity(0);
        contentArea.setTranslateY(50);

        FadeTransition contentFade = new FadeTransition(Duration.seconds(1.0), contentArea);
        contentFade.setToValue(1);

        TranslateTransition contentSlide = new TranslateTransition(Duration.seconds(1.0), contentArea);
        contentSlide.setToY(0);
        contentSlide.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition contentEntrance = new ParallelTransition(contentFade, contentSlide);
        contentEntrance.play();


        if (mascotImage != null) {
            Timeline mascotFloat = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(mascotImage.translateYProperty(), 0)),
                    new KeyFrame(Duration.seconds(2.5), new KeyValue(mascotImage.translateYProperty(), -20, Interpolator.EASE_BOTH)),
                    new KeyFrame(Duration.seconds(5.0), new KeyValue(mascotImage.translateYProperty(), 0, Interpolator.EASE_BOTH))
            );
            mascotFloat.setCycleCount(Animation.INDEFINITE);
            mascotFloat.play();
        }
    }


    private void animateBackground() {
        if (shape1 != null) moveShape(shape1, 30, -30, 4);
        if (shape2 != null) moveShape(shape2, -50, 50, 6);
        if (shape3 != null) moveShape(shape3, 40, 20, 5);
    }

    private void moveShape(Circle circle, double x, double y, double durationSeconds) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(durationSeconds), circle);
        transition.setByX(x);
        transition.setByY(y);
        transition.setAutoReverse(true);
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.setInterpolator(Interpolator.EASE_BOTH);
        transition.play();
    }

    private void playButtonAnimation(Node button, Runnable onFinished) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
        scale.setToX(0.95);
        scale.setToY(0.95);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.setOnFinished(e -> onFinished.run());
        scale.play();
    }
}
