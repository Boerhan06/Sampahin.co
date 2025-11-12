// package models;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;

/**
 * Kelas Admin (turunan Akun)
 * Dibuat dengan JavaFX Properties untuk UI Binding.
 */
public class Admin extends Akun {

    // --- Atribut Khusus Admin ---
    private StringProperty idAdmin; // Misal: "ADM001"

    /**
     * Constructor 1: Untuk membuat Admin BARU
     */
    public Admin(String namaLengkap, String alamat, String noTelepon, String email, String username, String plainPassword, String idAdmin) {
        // Panggil constructor Akun (induk) untuk user BARU
        super(namaLengkap, alamat, noTelepon, email, username, plainPassword);
        this.idAdmin = new SimpleStringProperty(idAdmin);
    }

    /**
     * Constructor 2: Untuk memuat Admin dari DATABASE
     */
    public Admin(int idAkun, String namaLengkap, String alamat, String noTelepon, String email, String username,
                 String dbHashedPassword, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, // <-- Atribut Akun
                 String idAdmin) { // <-- Atribut Admin

        // Panggil constructor Akun (induk) untuk data dari DB
        super(idAkun, namaLengkap, alamat, noTelepon, email, username, dbHashedPassword, isActive, createdAt, updatedAt);
        this.idAdmin = new SimpleStringProperty(idAdmin);
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    // --- Method Khusus Admin ---

    public void registrasiPengguna(Pengguna pengguna) {
        // Di aplikasi nyata, ini akan memanggil AuthService atau UserDAO
        System.out.println("Admin " + this.getNamaLengkap() + " mendaftarkan " + pengguna.getNamaLengkap());
        // Contoh: userDao.save(pengguna);
    }

    public void kelolaDataMitra(Mitra mitra) {
        // Logika CRUD Mitra
        System.out.println("Admin " + this.getNamaLengkap() + " mengelola mitra " + mitra.getNamaLengkap());
    }

    // --- JavaFX Getters, Setters, dan Property Methods ---

    // --- ID Admin ---
    public String getIdAdmin() { return idAdmin.get(); }
    public void setIdAdmin(String idAdmin) { this.idAdmin.set(idAdmin); }
    public StringProperty idAdminProperty() { return idAdmin; }
}