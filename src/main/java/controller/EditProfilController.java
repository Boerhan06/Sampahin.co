package controller;

import com.sampahin.Main;
import dao.AdminDAO;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Admin;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class EditProfilController extends BaseController implements Initializable {


    @FXML private Circle profileImage;
    @FXML private TextField txtNama;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnSimpan;
    @FXML
    private Circle imgSidebarFoto;

    @FXML
    private Circle imgProfilMain;
    // --- Variabel Data ---
    private AdminDAO adminDAO;
    private byte[] newPhotoBytes = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.adminDAO = new AdminDAO();


        setupButtonAnimation();


        if (currentAkun != null) {
            updateUI();
        }
    }


    @Override
    protected void updateUI() {

        if (txtNama == null || txtUsername == null) {
            return;
        }

        if (currentAkun != null) {
            System.out.println("✅ [EditProfil] Mengisi form dengan data: " + currentAkun.getNamaLengkap());


            txtNama.setText(currentAkun.getNamaLengkap());
            txtUsername.setText(currentAkun.getUsername());
            txtPassword.setText("");


            if (currentAkun instanceof Admin) {
                Admin adminData = (Admin) currentAkun;
                if (adminData.getFotoProfil() != null && adminData.getFotoProfil().length > 0) {
                    try {
                        ByteArrayInputStream bis = new ByteArrayInputStream(adminData.getFotoProfil());
                        Image img = new Image(bis);
                        profileImage.setFill(new ImagePattern(img));
                    } catch (Exception e) {
                        System.err.println("Gagal memuat foto profil: " + e.getMessage());
                    }
                }
            }
        }
    }


    @FXML
    private void handleUbahFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Foto Profil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );


        if (btnSimpan.getScene() != null) {
            Stage stage = (Stage) btnSimpan.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                try {

                    Image image = new Image(selectedFile.toURI().toString());
                    profileImage.setFill(new ImagePattern(image));


                    newPhotoBytes = Files.readAllBytes(selectedFile.toPath());

                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Gagal membaca file gambar.");
                    e.printStackTrace();
                }
            }
        }
    }


    @FXML
    private void handleSimpan() {

        if (currentAkun == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Sesi tidak valid. Silakan login ulang.");
            return;
        }

        String namaBaru = txtNama.getText();
        String usernameBaru = txtUsername.getText();
        String passwordBaru = txtPassword.getText();


        if (namaBaru.isEmpty() || usernameBaru.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Nama dan Username tidak boleh kosong!");
            return;
        }


        System.out.println("⏳ Menyimpan perubahan profil...");


        boolean sukses = adminDAO.updateProfilePartial(
                currentAkun.getIdAkun(),
                namaBaru,
                usernameBaru,
                passwordBaru,
                newPhotoBytes
        );

        if (sukses) {

            currentAkun.setNamaLengkap(namaBaru);
            currentAkun.setUsername(usernameBaru);


            if (!passwordBaru.isEmpty()) {
                currentAkun.setPassword(passwordBaru);
            }


            if (newPhotoBytes != null && currentAkun instanceof Admin) {
                ((Admin) currentAkun).setFotoProfil(newPhotoBytes);
            }

            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Profil berhasil diperbarui!");


            Main.showDashboardView(currentAkun);

        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menyimpan perubahan ke database.");
        }
    }


    private void setupButtonAnimation() {
        if (btnSimpan == null) return;

        btnSimpan.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), btnSimpan);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });

        btnSimpan.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), btnSimpan);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }


    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}