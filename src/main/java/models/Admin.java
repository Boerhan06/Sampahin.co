package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;

public class Admin extends Akun {
    private StringProperty idAdmin;

    
    public Admin(String namaLengkap, String alamat, String noTelepon, String email, String username, String plainPassword, String idAdmin) {
        super(namaLengkap, alamat, noTelepon, email, username, plainPassword);
        this.idAdmin = new SimpleStringProperty(idAdmin);
    }

    public Admin(int idAkun, String namaLengkap, String alamat, String noTelepon, String email, String username,
                 String dbHashedPassword, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, // <-- Atribut Akun
                 String idAdmin) {
        super(idAkun, namaLengkap, alamat, noTelepon, email, username, dbHashedPassword, isActive, createdAt, updatedAt);
        this.idAdmin = new SimpleStringProperty(idAdmin);
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    // --- Method Khusus Admin ---
    public void registrasiPengguna(Pengguna pengguna) {
        // Memanggil AuthService atau UserDAO
        System.out.println("Admin " + this.getNamaLengkap() + " mendaftarkan " + pengguna.getNamaLengkap());
    }

    public void kelolaDataMitra(Mitra mitra) {
        // Logika CRUD Mitra
        System.out.println("Admin " + this.getNamaLengkap() + " mengelola mitra " + mitra.getNamaLengkap());
    }

    // --- ID Admin ---
    public String getIdAdmin() { return idAdmin.get(); }
    public void setIdAdmin(String idAdmin) { this.idAdmin.set(idAdmin); }
    public StringProperty idAdminProperty() { return idAdmin; }
}