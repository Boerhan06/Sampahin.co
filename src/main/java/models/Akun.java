package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;
import util.HashingUtils; // Pastikan import ini ada

public class Akun {
    private int idAkun;
    private final StringProperty namaLengkap = new SimpleStringProperty();
    private final StringProperty alamat = new SimpleStringProperty();
    private final StringProperty noTelepon = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty username = new SimpleStringProperty();

    private String password; // Ini menyimpan HASH password
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // --- 1. Konstruktor Kosong ---
    public Akun() {
        this.namaLengkap.set("");
        this.alamat.set("");
        this.noTelepon.set("");
        this.email.set("");
        this.username.set("");
        this.password = "";
    }

    // --- 2. Konstruktor untuk Register Baru ---
    public Akun(String namaLengkap, String alamat, String noTelepon, String email, String username, String password) {
        this.namaLengkap.set(namaLengkap);
        this.alamat.set(alamat);
        this.noTelepon.set(noTelepon);
        this.email.set(email);
        this.username.set(username);
        this.password = password;

        // Default value
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // --- 3. Konstruktor Database ---
    public Akun(int idAkun, String namaLengkap, String alamat, String noTelepon, String email, String username,
                String password, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idAkun = idAkun;
        this.namaLengkap.set(namaLengkap);
        this.alamat.set(alamat);
        this.noTelepon.set(noTelepon);
        this.email.set(email);
        this.username.set(username);
        this.password = password;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ==========================================
    //  METHOD BARU: CHECK PASSWORD
    // ==========================================
    public boolean checkPassword(String plainPassword) {
        if (plainPassword == null || this.password == null) {
            return false;
        }
        // Menggunakan HashingUtils untuk mencocokkan password inputan dengan Hash di DB
        return HashingUtils.checkPassword(plainPassword, this.password);
    }

    // ==========================================
    //  GETTER & SETTER
    // ==========================================

    // Nama Lengkap
    public String getNamaLengkap() { return namaLengkap.get(); }
    public void setNamaLengkap(String nama) { this.namaLengkap.set(nama); }
    public StringProperty namaLengkapProperty() { return namaLengkap; }

    // Alamat
    public String getAlamat() { return alamat.get(); }
    public void setAlamat(String alamat) { this.alamat.set(alamat); }
    public StringProperty alamatProperty() { return alamat; }

    // No Telepon
    public String getNoTelepon() { return noTelepon.get(); }
    public void setNoTelepon(String noTelepon) { this.noTelepon.set(noTelepon); }
    public StringProperty noTeleponProperty() { return noTelepon; }

    // Email
    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }

    // Username
    public String getUsername() { return username.get(); }
    public void setUsername(String username) { this.username.set(username); }
    public StringProperty usernameProperty() { return username; }

    // Password & Hashed Password
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // Alias untuk DAO (biar jelas kalau ini Hash)
    public String getHashedPassword() { return password; }
    public void setHashedPassword(String password) { this.password = password; }

    // ID Akun
    public int getIdAkun() { return idAkun; }
    public void setIdAkun(int idAkun) { this.idAkun = idAkun; }

    // Is Active
    public boolean getIsActive() { return isActive; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }
    public boolean isIsActive() { return isActive; } // Alias untuk JavaFX property wrapper kadang butuh is...

    // Role
    public String getRole() { return "User"; }
}