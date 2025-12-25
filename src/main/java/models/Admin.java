package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;

public class Admin extends Akun {

    private final StringProperty idAdmin;

    // --- Konstruktor Default (Untuk keperluan Register/Form Kosong) ---
    public Admin() {
        super();
        this.idAdmin = new SimpleStringProperty("");
    }

    // --- Konstruktor Lengkap (Untuk pengambilan data dari Database via DAO) ---
    public Admin(int idAkun, String namaLengkap, String alamat, String noTelepon,
                 String email, String username, String dbHashedPassword,
                 boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt,
                 String idAdmin) {

        // Mengirim data umum ke Parent Class (Akun)
        super(idAkun, namaLengkap, alamat, noTelepon, email, username,
                dbHashedPassword, isActive, createdAt, updatedAt);

        this.idAdmin = new SimpleStringProperty(idAdmin);
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    // --- Method Khusus Admin ---
    // Contoh method placeholder logika bisnis
    public void registrasiPengguna(Pengguna pengguna) {
        // Logika untuk menyimpan pengguna baru ke DB bisa dipanggil di sini atau di Service
        System.out.println("[LOG] Admin " + this.getNamaLengkap() + " mendaftarkan user: " + pengguna.getNamaLengkap());
    }

    public void kelolaDataMitra(Mitra mitra) {
        System.out.println("[LOG] Admin " + this.getNamaLengkap() + " mengelola mitra: " + mitra.getNamaLengkap());
    }

    // --- Getter & Setter ID Admin (JavaFX Property Style) ---
    public String getIdAdmin() {
        return idAdmin.get();
    }

    public void setIdAdmin(String idAdmin) {
        this.idAdmin.set(idAdmin);
    }

    public StringProperty idAdminProperty() {
        return idAdmin;
    }
}