package controller;

import com.sampahin.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import models.Admin;
import models.Akun;
import java.io.ByteArrayInputStream;

public class BaseController {
    protected Akun currentAkun;
    @FXML protected Circle imgSidebarFoto;
    @FXML protected Label lblSidebarNama;

    public void setAkunData(Akun akun) {
        this.currentAkun = akun;
        updateSidebar();
        updateUI();
    }


    private void updateSidebar() {
        if (currentAkun == null) return;
        if (lblSidebarNama != null) {
            lblSidebarNama.setText(currentAkun.getNamaLengkap());
        }

        if (imgSidebarFoto != null) {
            if (currentAkun instanceof Admin) {
                Admin admin = (Admin) currentAkun;
                byte[] fotoBlob = admin.getFotoProfil();

                if (fotoBlob != null && fotoBlob.length > 0) {
                    try {
                        ByteArrayInputStream bis = new ByteArrayInputStream(fotoBlob);
                        Image profileImage = new Image(bis);
                        imgSidebarFoto.setFill(new ImagePattern(profileImage));
                    } catch (Exception e) {
                        System.err.println("Gagal memuat foto sidebar: " + e.getMessage());
                    }
                } else {

                }
            }
        }
    }


    protected void updateUI() {
    }

    @FXML
    public void handleNavigateToDashboard(MouseEvent event) {
        System.out.println("Navigasi ke: Dashboard");
        Main.showDashboardView(currentAkun);
    }

    @FXML
    public void handleNavigateToDaftarPengguna(MouseEvent event) {
        System.out.println("Navigasi ke: Daftar Pengguna");
        Main.showDaftarPenggunaView(currentAkun);
    }

    @FXML
    public void handleNavigateToPemasukanSampah(MouseEvent event) {
        System.out.println("Navigasi ke: Pemasukan Sampah");
        Main.showPemasukanSampahView(currentAkun);
    }

    @FXML
    public void handleNavigateToRiwayatPenarikan(MouseEvent event) {
        System.out.println("Navigasi ke: Riwayat Penarikan");
        Main.showRiwayatPenarikanView(currentAkun);
    }

    @FXML
    public void handleNavigateToEditProfile(MouseEvent event) {
        System.out.println("Navigasi ke: Edit Profil");
        Main.showEditProfilView(currentAkun);
    }

    @FXML
    public void handleLogout(MouseEvent event) {
        System.out.println("Proses Logout...");
        this.currentAkun = null; // Hapus sesi
        Main.showLoginView();
    }
}
