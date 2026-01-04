package controller;

import com.sampahin.Main;
import dao.AdminDAO;
import dao.MitraDAO;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import models.Admin;
import models.Mitra;
import org.mindrot.jbcrypt.BCrypt;
import javafx.scene.Node;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class LoginController implements Initializable {


    @FXML private AnchorPane rootPane;
    @FXML private VBox formContainer;
    @FXML private Label lblGreeting;
    @FXML private Label lblSubGreeting;
    @FXML private Label errorLabel;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberCheckBox;

    @FXML private Button btnAdminMode;
    @FXML private Button btnMitraMode;
    @FXML private Button loginButton;

    @FXML private HBox registerBox;
    @FXML private ImageView imgMaskot;


    private AdminDAO adminDAO;
    private MitraDAO mitraDAO;
    private boolean isMitraMode = false;


    private final Preferences prefs = Preferences.userNodeForPackage(LoginController.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.adminDAO = new AdminDAO();
        this.mitraDAO = new MitraDAO();


        if (errorLabel != null) {
            errorLabel.setText("");
        }


        checkRememberedUser();


        switchToAdmin();


        playEntranceAnimation();
    }


    private void playEntranceAnimation() {
        if (formContainer != null) {

            FadeTransition fade = new FadeTransition(Duration.seconds(1.2), formContainer);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();


            TranslateTransition translate = new TranslateTransition(Duration.seconds(1.2), formContainer);
            translate.setFromY(50);
            translate.setToY(0);
            translate.play();
        }
    }


    @FXML
    private void switchToAdmin() {
        isMitraMode = false;


        if (lblGreeting != null) lblGreeting.setText("Hello, Admin!");
        if (lblSubGreeting != null) lblSubGreeting.setText("Silakan masuk untuk mengelola sampah.");


        if (btnAdminMode != null) {
            btnAdminMode.getStyleClass().remove("toggle-btn");
            btnAdminMode.getStyleClass().add("toggle-btn-active");
        }
        if (btnMitraMode != null) {
            btnMitraMode.getStyleClass().remove("toggle-btn-active");
            btnMitraMode.getStyleClass().add("toggle-btn");
        }


        if (rootPane != null) {
            rootPane.getStyleClass().remove("mitra-mode");
        }


        if (loginButton != null) {
            loginButton.setText("LOGIN ADMIN");
        }


        if (registerBox != null) {
            registerBox.setVisible(true);
        }


        loadMaskot("/image/maskot.png");
    }


    @FXML
    private void switchToMitra() {
        isMitraMode = true;


        if (lblGreeting != null) lblGreeting.setText("Halo, Mitra!");
        if (lblSubGreeting != null) lblSubGreeting.setText("Selamat bekerja untuk lingkungan bersih.");


        if (btnMitraMode != null) {
            btnMitraMode.getStyleClass().remove("toggle-btn");
            btnMitraMode.getStyleClass().add("toggle-btn-active");
        }
        if (btnAdminMode != null) {
            btnAdminMode.getStyleClass().remove("toggle-btn-active");
            btnAdminMode.getStyleClass().add("toggle-btn");
        }


        if (rootPane != null && !rootPane.getStyleClass().contains("mitra-mode")) {
            rootPane.getStyleClass().add("mitra-mode");
        }


        if (loginButton != null) {
            loginButton.setText("LOGIN MITRA");
        }


        if (registerBox != null) {
            registerBox.setVisible(false);
        }


        loadMaskot("/image/maskot_green.png");
    }


    private void loadMaskot(String path) {
        if (imgMaskot == null) return;

        try {
            InputStream is = getClass().getResourceAsStream(path);
            if (is != null) {
                Image img = new Image(is);
                imgMaskot.setImage(img);
            } else {

                System.out.println("ℹ️ Info: Gambar maskot tidak ditemukan di path: " + path + ". Menggunakan gambar default.");

                InputStream defaultIs = getClass().getResourceAsStream("/image/maskot.png");
                if (defaultIs != null) imgMaskot.setImage(new Image(defaultIs));
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
    }


    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();


        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username dan Password wajib diisi!");
            shakeAnimation(usernameField); // Efek getar jika salah
            shakeAnimation(passwordField);
            return;
        }


        if (isMitraMode) {
            loginAsMitra(username, password);
        } else {
            loginAsAdmin(username, password);
        }
    }


    private void shakeAnimation(Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), node);
        tt.setFromX(0f);
        tt.setByX(10f);
        tt.setCycleCount(4);
        tt.setAutoReverse(true);
        tt.play();
    }


    private void loginAsAdmin(String user, String pass) {
        try {
            Admin admin = adminDAO.getAdminByUsername(user);

            if (admin != null && checkPassword(pass, admin.getHashedPassword())) {

                handleSuccessfulLogin(user, pass);


                Main.showDashboardView(admin);
            } else {
                errorLabel.setText("Username atau Password Admin salah!");
                shakeAnimation(loginButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Gagal terhubung ke database!");
        }
    }


    private void loginAsMitra(String user, String pass) {
        try {
            Mitra mitra = mitraDAO.getMitraByUsername(user);

            if (mitra != null && checkPassword(pass, mitra.getHashedPassword())) {

                handleSuccessfulLogin(user, pass);


                Main.showDashboardMitraView(mitra);
            } else {
                errorLabel.setText("Username atau Password Mitra salah!");
                shakeAnimation(loginButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Gagal terhubung ke database!");
        }
    }


    private boolean checkPassword(String plain, String hashed) {
        if (hashed == null) return false;


        if (hashed.startsWith("$2a$")) {
            return BCrypt.checkpw(plain, hashed);
        }


        return plain.equals(hashed);
    }


    private void handleSuccessfulLogin(String user, String pass) {
        if (rememberCheckBox.isSelected()) {
            prefs.put("remember_user", user);
            prefs.put("remember_pass", pass);
            prefs.putBoolean("remember_state", true);
        } else {
            prefs.remove("remember_user");
            prefs.remove("remember_pass");
            prefs.putBoolean("remember_state", false);
        }
    }


    private void checkRememberedUser() {
        if (prefs.getBoolean("remember_state", false)) {
            usernameField.setText(prefs.get("remember_user", ""));
            passwordField.setText(prefs.get("remember_pass", ""));
            rememberCheckBox.setSelected(true);
        }
    }

    @FXML
    private void handleRegister() {
        Main.showRegisterView();
    }


    @FXML
    private void handleForgotPassword() {
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            errorLabel.setText("Ketik username Anda untuk reset!");
            shakeAnimation(usernameField);
            return;
        }


        String resetCode = String.format("%06d", new Random().nextInt(999999));


        System.out.println("\n========== SAMPAHIN SECURITY ==========");
        System.out.println("USER: " + username + " | KODE: " + resetCode);
        System.out.println("========================================\n");


        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Reset Password");
        dialog.setHeaderText("Verifikasi Kode Keamanan");
        dialog.setContentText("Masukkan kode 6 digit yang muncul di console:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && result.get().equals(resetCode)) {

            TextInputDialog passDialog = new TextInputDialog();
            passDialog.setTitle("Password Baru");
            passDialog.setHeaderText("Reset Password Berhasil");
            passDialog.setContentText("Masukkan password baru:");

            Optional<String> newPass = passDialog.showAndWait();

            if (newPass.isPresent() && !newPass.get().isEmpty()) {

                String hashed = BCrypt.hashpw(newPass.get(), BCrypt.gensalt());

                boolean success;
                if (isMitraMode) {
                    success = mitraDAO.updatePassword(username, hashed);
                } else {
                    success = adminDAO.updatePassword(username, hashed);
                }

                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sukses");
                    alert.setHeaderText(null);
                    alert.setContentText("Password berhasil diperbarui! Silakan login.");
                    alert.showAndWait();
                } else {
                    errorLabel.setText("Gagal update password di database.");
                }
            }
        } else {
            if (result.isPresent()) {
                errorLabel.setText("Kode verifikasi salah!");
            }
        }
    }
}