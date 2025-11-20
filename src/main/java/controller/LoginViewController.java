package controller; 

import dao.AdminDAO;
import models.Admin;
import util.HashingUtils;
import util.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginViewController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink registerLink;

 
    private AdminDAO adminDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.adminDAO = new AdminDAO();
        errorLabel.setText(""); 
    }    

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username dan Password harus diisi!");
            return;
        }

        Admin admin = adminDAO.getAdminByUsername(username);

        if (admin != null) {
            String dbPassword = admin.getHashedPassword(); 

            boolean isPasswordValid = false;

            if (HashingUtils.checkPassword(password, dbPassword)) {
                isPasswordValid = true;
            } 
           
            else if (password.equals(dbPassword)) {
                isPasswordValid = true;
                System.out.println("PERINGATAN: Anda login menggunakan Password tidak terenkripsi!");
            }
            
            if (isPasswordValid) {
                System.out.println("Login Berhasil! Selamat datang " + admin.getNamaLengkap());
                SessionManager.getInstance().setLoggedInAkun(admin);
                bukaDashboard(event);
            } else {
                errorLabel.setText("Password salah!");
            }
        } else {
            errorLabel.setText("Username tidak ditemukan!");
        }
    }

    private void bukaDashboard(ActionEvent event) {
        try {
            URL fxmlUrl = getClass().getResource("/view/DashboardView.fxml"); 
            
            if (fxmlUrl == null) {
                errorLabel.setText("Error: File DashboardView.fxml tidak ditemukan!");
                return;
            }

            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            
            stage.setTitle("Dashboard Sampahin.co");
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Gagal memuat Dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void goToRegister(ActionEvent event) {
        try {
            URL fxmlUrl = getClass().getResource("/view/RegisterView.fxml");
            if (fxmlUrl == null) {
                System.err.println("File RegisterView.fxml tidak ditemukan!");
                return;
            }

            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            stage.setTitle("Daftar Akun Baru - Sampahin.co");
            stage.setScene(new Scene(root));
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}