package controller;

import com.sampahin.Main;
import dao.MitraDAO;
import dao.TitikPengumpulanDAO;
import models.Akun;
import models.Mitra;
import models.TitikPengumpulan;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.StageStyle;
import netscape.javascript.JSObject;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    // --- FXML COMPONENTS ---
    @FXML private ScrollPane mainScrollPane;
    @FXML private WebView mapView;
    @FXML private AreaChart<String, Number> chartMitra;
    @FXML private AreaChart<String, Number> chartPengguna;
    @FXML private Button btnZoomIn;
    @FXML private Button btnZoomOut;
    @FXML private TextField txtMapSearch;
    @FXML private Button btnPinPoint;
    @FXML private TextField txtMitraSearch;
    @FXML private VBox mitraListContainer;

    private WebEngine webEngine;
    private boolean isAddMode = false;
    private TitikPengumpulanDAO lokasiDAO;
    private MitraDAO mitraDAO;
    private ObservableList<TitikPengumpulan> listLokasi;
    private ObservableList<Mitra> listMitra;
    private Akun currentAkun;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lokasiDAO = new TitikPengumpulanDAO();
        mitraDAO = new MitraDAO();
        listLokasi = FXCollections.observableArrayList();
        listMitra = FXCollections.observableArrayList();

        initMap();
        setupZoomControls();
        fixMapInteraction();
        initTanggal();

        if (chartMitra != null) populateCharts();

        // PENTING: Update class style untuk search field sidebar kanan agar sesuai tema ungu
        if (txtMitraSearch != null) {
            txtMitraSearch.getStyleClass().add("search-field-modern");
        }

        loadMitraList();
    }
    private void initTanggal() {
        // 1. Ambil tanggal sekarang
        LocalDate today = LocalDate.now();

        // 2. Buat format Bahasa Indonesia
        // Pola: "EEEE, dd MMMM yyyy" -> "Senin, 20 Mei 2024"
        Locale indonesia = new Locale("id", "ID");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", indonesia);

        // 3. Set ke Label
        if (lblTanggal != null) {
            lblTanggal.setText(today.format(formatter));
        }
    }

    public void setAkunData(Akun akun) {
        this.currentAkun = akun;
        if (akun != null) System.out.println("Login: " + akun.getNamaLengkap());
    }
    @FXML
    private void handleCekAktivitas() {
        // Panggil layar yang diinginkan (Contoh: Riwayat Penarikan)
        Main.showRiwayatPenarikanView();

        // ATAU jika "Aktivitas" maksudnya Pemasukan Sampah, pakai ini:
        // Main.showPemasukanSampahView();
    }

    // =================================================================================
    //  POPUP 1: VALIDASI ADMIN (Fixed Transparency & Style)
    // =================================================================================
    @FXML
    private void handleAddMitra() {
        Dialog<String> dialog = new Dialog<>();
        setupModernDialogStyle(dialog);

        // --- 1. HEADER MANUAL ---
        VBox headerBox = createCustomHeader("Akses Administrator");

        // --- 2. ISI KONTEN ---
        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(20));
        contentBox.setStyle("-fx-background-color: white;");

        Label lblInstruksi = new Label("Masukkan kode keamanan:");
        lblInstruksi.setStyle("-fx-text-fill: #4B5563; -fx-font-weight: bold;");

        PasswordField txtKode = new PasswordField();
        txtKode.setPromptText("Kode Admin");
        txtKode.getStyleClass().add("form-field");

        contentBox.getChildren().addAll(lblInstruksi, txtKode);

        // --- 3. GABUNGKAN ---
        VBox mainContainer = new VBox(headerBox, contentBox);
        mainContainer.setStyle("-fx-background-color: transparent; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 4);");
        dialog.getDialogPane().setContent(mainContainer);

        // --- TOMBOL ---
        ButtonType btnAkses = new ButtonType("Buka Akses", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnAkses, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> btn == btnAkses ? txtKode.getText() : null);

        dialog.showAndWait().ifPresent(kode -> {
            if ("ADMIN123".equals(kode)) showFormTambahMitra();
            else showAlert(Alert.AlertType.ERROR, "Akses Ditolak", "Kode salah!");
        });
    }

    // =================================================================================
    //  POPUP 2: FORM DATA MITRA (Fixed Layout & Style)
    // =================================================================================
    private void showFormTambahMitra() {
        Dialog<Mitra> dialog = new Dialog<>();
        setupModernDialogStyle(dialog);

        // --- 1. HEADER MANUAL ---
        VBox headerBox = createCustomHeader("Tambah Mitra Baru");

        // --- 2. ISI KONTEN (GRID) ---
        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(20));
        contentBox.setStyle("-fx-background-color: white;");

        GridPane grid = new GridPane();
        grid.setHgap(15); grid.setVgap(15);

        // Components
        TextField txtNama = new TextField(); txtNama.setPromptText("Nama Lengkap");
        TextField txtAlamat = new TextField(); txtAlamat.setPromptText("Alamat");
        TextField txtTelp = new TextField(); txtTelp.setPromptText("No. Telepon");
        TextField txtEmail = new TextField(); txtEmail.setPromptText("Email");
        TextField txtUser = new TextField(); txtUser.setPromptText("Username");
        PasswordField txtPass = new PasswordField(); txtPass.setPromptText("Password");
        TextField txtId = new TextField(); txtId.setPromptText("ID Mitra");

        ComboBox<TitikPengumpulan> cbLokasi = new ComboBox<>(listLokasi);
        cbLokasi.setPromptText("-- Pilih Lokasi --");
        cbLokasi.setPrefWidth(250);
        cbLokasi.getStyleClass().add("form-field");

        TextField[] fields = {txtNama, txtAlamat, txtTelp, txtEmail, txtUser, txtId};
        for(TextField f : fields) f.getStyleClass().add("form-field");
        txtPass.getStyleClass().add("form-field");

        // Layout Grid
        addToGrid(grid, "Nama:", txtNama, 0);   addToGrid(grid, "Username:", txtUser, 4);
        addToGrid(grid, "Alamat:", txtAlamat, 1); addToGrid(grid, "Password:", txtPass, 5);
        addToGrid(grid, "No. HP:", txtTelp, 2);   addToGrid(grid, "ID Mitra:", txtId, 6);
        addToGrid(grid, "Email:", txtEmail, 3);   addToGrid(grid, "Lokasi:", cbLokasi, 7);

        contentBox.getChildren().add(grid);

        // --- 3. GABUNGKAN ---
        VBox mainContainer = new VBox(headerBox, contentBox);
        mainContainer.setStyle("-fx-background-color: transparent; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 4);");
        dialog.getDialogPane().setContent(mainContainer);

        // --- TOMBOL ---
        ButtonType btnSimpan = new ButtonType("Simpan Data", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnSimpan, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == btnSimpan && !txtNama.getText().isEmpty() && cbLokasi.getValue() != null) {
                return new Mitra(txtNama.getText(), txtAlamat.getText(), txtTelp.getText(), txtEmail.getText(),
                        txtUser.getText(), txtPass.getText(), txtId.getText(), cbLokasi.getValue());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(mitra -> {
            if(mitraDAO.save(mitra, txtPass.getText())) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data tersimpan!");
                loadMitraList();
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Database error.");
            }
        });
    }

    // --- HELPER UNTUK STYLE DIALOG (NUCLEAR OPTION) ---
    private void setupModernDialogStyle(Dialog<?> dialog) {
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/dashboard-style.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("my-dialog-pane");
        dialog.getDialogPane().setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        dialog.setOnShown(e -> {
            Scene scene = dialog.getDialogPane().getScene();
            if(scene != null) scene.setFill(Color.TRANSPARENT);
        });
    }

    private VBox createCustomHeader(String title) {
        Label lbl = new Label(title);
        lbl.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        VBox box = new VBox(lbl);
        box.setStyle("-fx-background-color: linear-gradient(to right, #685BAB, #8B7CCF); -fx-padding: 15 20; -fx-background-radius: 15 15 0 0;");
        return box;
    }

    private void addToGrid(GridPane grid, String label, javafx.scene.Node node, int row) {
        Label lbl = new Label(label); lbl.setStyle("-fx-font-weight:bold; -fx-text-fill:#4B5563;");
        grid.add(lbl, 0, row); grid.add(node, 1, row);
    }

    // =============================================================
    // BAGIAN LOAD CHAT MITRA (DESIGN MODERN UNGU) - FIXED
    // =============================================================
    private void loadMitraList() {
        mitraListContainer.getChildren().clear();
        mitraListContainer.setSpacing(10);

        listMitra.setAll(mitraDAO.getAllMitra());

        for (Mitra mitra : listMitra) {
            // 1. Container Utama (Kartu)
            HBox card = new HBox(12);
            card.setAlignment(Pos.CENTER_LEFT);
            card.getStyleClass().add("chat-card");

            // 2. Avatar
            StackPane avatar = new StackPane();
            Circle circle = new Circle(20);
            circle.getStyleClass().add("chat-avatar-bg");

            String inisial = mitra.getInisial().toUpperCase();
            Label lblInisial = new Label(inisial.isEmpty() ? "?" : inisial);
            lblInisial.getStyleClass().add("chat-avatar-text");

            avatar.getChildren().addAll(circle, lblInisial);

            // 3. Info
            VBox info = new VBox(3);
            info.setAlignment(Pos.CENTER_LEFT);

            Label lblNama = new Label(mitra.getNamaLengkap());
            lblNama.getStyleClass().add("chat-name");

            Label lblMsg = new Label("Aktif di: " + mitra.getLokasiTugas().getNamaLokasi());
            lblMsg.getStyleClass().add("chat-msg");
            lblMsg.setWrapText(true);
            lblMsg.setMaxWidth(160);

            info.getChildren().addAll(lblNama, lblMsg);
            card.getChildren().addAll(avatar, info);
            card.setOnMouseClicked(e -> showChatDialog(mitra));
            mitraListContainer.getChildren().add(card);
        }
    }

    private void showChatDialog(Mitra mitra) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info Mitra");
        alert.setHeaderText(mitra.getNamaLengkap());
        alert.setContentText("Username: " + mitra.getUsername() + "\nLokasi Tugas: " + mitra.getLokasiTugas().getNamaLokasi());
        alert.show();
    }

    // --- LOGIC MAP & LAINNYA ---
    private void initMap() {
        if (mapView == null) return;
        webEngine = mapView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaBridge", new JavaBridge());
                loadLokasiFromDatabase();
            }
        });
        webEngine.loadContent(getHtmlMap());
    }

    private void loadLokasiFromDatabase() {
        List<TitikPengumpulan> dataDB = lokasiDAO.getAll();
        listLokasi.setAll(dataDB);
        for (TitikPengumpulan titik : listLokasi) addMarkerToMap(titik);
    }

    private void addMarkerToMap(TitikPengumpulan titik) {
        webEngine.executeScript("addMarker(" + titik.getLatitude() + ", " + titik.getLongitude() + ", '" + titik.getNamaLokasi() + "')");
    }

    @FXML private void handleToggleAddPoint() {
        isAddMode = !isAddMode;
        if(isAddMode) { btnPinPoint.setText("Batal"); webEngine.executeScript("setAddMode(true)"); }
        else { btnPinPoint.setText("Tambah Titik"); webEngine.executeScript("setAddMode(false)"); }
    }

    public class JavaBridge {}

    private void populateCharts() {
        XYChart.Series<String, Number> s1 = new XYChart.Series<>();
        s1.getData().add(new XYChart.Data<>("Sen", 10));
        s1.getData().add(new XYChart.Data<>("Sel", 20));
        s1.getData().add(new XYChart.Data<>("Rab", 15));
        chartMitra.getData().add(s1);

        XYChart.Series<String, Number> s2 = new XYChart.Series<>();
        s2.getData().add(new XYChart.Data<>("Sen", 50));
        s2.getData().add(new XYChart.Data<>("Sel", 40));
        s2.getData().add(new XYChart.Data<>("Rab", 60));
        chartPengguna.getData().add(s2);
    }

    private void setupZoomControls() { if(btnZoomIn!=null) btnZoomIn.setOnAction(e->webEngine.executeScript("jsZoomIn()")); if(btnZoomOut!=null) btnZoomOut.setOnAction(e->webEngine.executeScript("jsZoomOut()")); }

    private void fixMapInteraction() {
        if(mapView==null) return;
        mapView.setOnMouseEntered(e->mainScrollPane.setPannable(false));
        mapView.setOnMouseExited(e->mainScrollPane.setPannable(true));
        mapView.addEventFilter(MouseEvent.ANY, e->{ if(e.getEventType()==MouseEvent.MOUSE_PRESSED) mapView.requestFocus(); });
        mapView.setOnMouseClicked(e -> {
            if (isAddMode && e.getButton() == MouseButton.PRIMARY) {
                try {
                    String latLngStr = (String) webEngine.executeScript("getLatLngFromPoint(" + e.getX() + "," + e.getY() + ")");
                    if (latLngStr != null) {
                        String[] parts = latLngStr.split(",");
                        // Panggil dialog add point di sini jika perlu
                    }
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type); alert.setTitle(title); alert.setContentText(content); alert.showAndWait();
    }

    private String getHtmlMap() {
        return "<!DOCTYPE html><html><head><meta charset=\"utf-8\" /><link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.7.1/dist/leaflet.css\" /><script src=\"https://unpkg.com/leaflet@1.7.1/dist/leaflet.js\"></script><style>html,body,#map{margin:0;padding:0;height:100%;width:100%;}</style></head><body><div id=\"map\"></div><script>var map=L.map('map',{zoomControl:false}).setView([-6.556488, 107.442125],13);L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',{maxZoom:19}).addTo(map);function addMarker(lat,lng,name){L.marker([lat,lng]).addTo(map).bindPopup(\"<b>\"+name+\"</b>\");}function jsZoomIn(){map.zoomIn();}function jsZoomOut(){map.zoomOut();}function setAddMode(enabled){if(enabled)document.getElementById('map').style.cursor='crosshair';else document.getElementById('map').style.cursor='grab';}function getLatLngFromPoint(x,y){var p=L.point(x,y);var l=map.containerPointToLatLng(p);return l.lat+','+l.lng;}</script></body></html>";
    }

    @FXML private void handleNavigateToDashboard() { Main.showDashboardView(currentAkun); }
    @FXML private void handleNavigateToDaftarPengguna() { Main.showDaftarPenggunaView(); }
    @FXML private void handleNavigateToPemasukanSampah() { Main.showPemasukanSampahView(); }
    @FXML private void handleNavigateToRiwayatPenarikan() { Main.showRiwayatPenarikanView(); }
    @FXML private void handleLogout() { Main.showLoginView(); }
    @FXML private void handleNavigateToEditProfile() { Main.showEditProfilView(); }
    @FXML private void handleMapSearch() {}
    @FXML private void handleMitraSearch() {}
    @FXML private Label lblTanggal;
}