package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;
import util.HashingUtils;

public class Akun {

    private int idAkun;

    private final StringProperty namaLengkap = new SimpleStringProperty();
    private final StringProperty alamat = new SimpleStringProperty();
    private final StringProperty noTelepon = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty username = new SimpleStringProperty();

    private String password;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Akun() {
        this.namaLengkap.set("");
        this.alamat.set("");
        this.noTelepon.set("");
        this.email.set("");
        this.username.set("");
        this.password = "";
    }

    public Akun(String namaLengkap, String alamat, String noTelepon, String email, String username, String password) {
        this.namaLengkap.set(namaLengkap);
        this.alamat.set(alamat);
        this.noTelepon.set(noTelepon);
        this.email.set(email);
        this.username.set(username);
        this.password = password;

        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

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

    public boolean checkPassword(String plainPassword) {
        if (plainPassword == null || this.password == null) {
            return false;
        }
        return HashingUtils.checkPassword(plainPassword, this.password);
    }

    public int getIdAkun() {
        return idAkun;
    }
    public void setIdAkun(int idAkun) {
        this.idAkun = idAkun;
    }

    public String getNamaLengkap() {
        return namaLengkap.get();
    }
    public void setNamaLengkap(String nama) {
        this.namaLengkap.set(nama);
    }
    public StringProperty namaLengkapProperty() {
        return namaLengkap;
    }

    public String getAlamat() {
        return alamat.get();
    }
    public void setAlamat(String alamat) {
        this.alamat.set(alamat);
    }
    public StringProperty alamatProperty() {
        return alamat;
    }

    public String getNoTelepon() {
        return noTelepon.get();
    }
    public void setNoTelepon(String noTelepon) {
        this.noTelepon.set(noTelepon);
    }
    public StringProperty noTeleponProperty() {
        return noTelepon;
    }

    public String getEmail() {
        return email.get();
    }
    public void setEmail(String email) {
        this.email.set(email);
    }
    public StringProperty emailProperty() {
        return email;
    }

    public String getUsername() {
        return username.get();
    }
    public void setUsername(String username) {
        this.username.set(username);
    }
    public StringProperty usernameProperty() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getHashedPassword() {
        return password;
    }
    public void setHashedPassword(String password) {
        this.password = password;
    }

    public boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRole() {
        return "User";
    }
}