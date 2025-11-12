import javafx.beans.property.*;
import java.time.LocalDateTime;
// Nanti tambahkan library hashing, contoh: import org.mindrot.jbcrypt.BCrypt;

public abstract class Akun {

    protected IntegerProperty idAkun;
    protected StringProperty namaLengkap;
    protected StringProperty alamat;
    protected StringProperty noTelepon;
    protected StringProperty email;
    protected StringProperty username;
    protected StringProperty hashedPassword; // jadi pwnya diubah ke HASH euy

    // Atribut metadata istilah katanya "datanya dari data" wkwk
    protected BooleanProperty isActive; // Untuk status (aktif/dinonaktifkan)
    protected ObjectProperty<LocalDateTime> createdAt; // Kapan akun dibuat
    protected ObjectProperty<LocalDateTime> updatedAt; // Kapan terakhir diubah

    //Konstruktor 1
    public Akun(String namaLengkap, String alamat, String noTelepon, String email, String username, String plainPassword) {
        this.idAkun = new SimpleIntegerProperty(0); // 0 atau -1 sebagai penanda 'baru'
        this.namaLengkap = new SimpleStringProperty(namaLengkap);
        this.alamat = new SimpleStringProperty(alamat);
        this.noTelepon = new SimpleStringProperty(noTelepon);
        this.email = new SimpleStringProperty(email);
        this.username = new SimpleStringProperty(username);
        this.hashedPassword = new SimpleStringProperty(); // Akan diisi oleh method

        // Set metadata
        this.isActive = new SimpleBooleanProperty(true); // Akun baru langsung aktif
        this.createdAt = new SimpleObjectProperty<>(LocalDateTime.now());
        this.updatedAt = new SimpleObjectProperty<>(LocalDateTime.now());

        // Panggil method untuk hash password
        this.setAndHashPassword(plainPassword);
    }

    //konstruktor 2
    public Akun(int idAkun, String namaLengkap, String alamat, String noTelepon, String email, String username,
                String dbHashedPassword, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idAkun = new SimpleIntegerProperty(idAkun);
        this.namaLengkap = new SimpleStringProperty(namaLengkap);
        this.alamat = new SimpleStringProperty(alamat);
        this.noTelepon = new SimpleStringProperty(noTelepon);
        this.email = new SimpleStringProperty(email);
        this.username = new SimpleStringProperty(username);
        this.hashedPassword = new SimpleStringProperty(dbHashedPassword);
        this.isActive = new SimpleBooleanProperty(isActive);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
        this.updatedAt = new SimpleObjectProperty<>(updatedAt);
    }

    // Abstract method (wajib di-override oleh anak anak yooo)
    public abstract String getRole();


    // --- Penanganan Password yang Aman (Alias di has sih hehe) ---
    public void setAndHashPassword(String plainPassword) {
        // DI SINI Anda implementasikan library hashing. Contoh BCrypt:
        // String hash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        // this.hashedPassword.set(hash);

        // --- !! PENTING: Ini HANYA placeholder !! ---
        // Ganti ini dengan library hashing seperti BCrypt
        // Jangan pernah simpan password seperti ini di produksi!
        String placeholderHash = "HASHED_" + plainPassword;
        this.hashedPassword.set(placeholderHash);
        // --- !! Batas Placeholder !! ---
    }


    //Ngecek sama enggak sih paswordnya sama yang udah di hash
    public boolean checkPassword(String plainPassword) {
        // Contoh implementasi pengecekan BCrypt:
        // return BCrypt.checkpw(plainPassword, this.hashedPassword.get());

        // --- !! PENTING: Ini HANYA placeholder !! ---
        String placeholderHash = "HASHED_" + plainPassword;
        return this.hashedPassword.get().equals(placeholderHash);
        // --- !! Batas Placeholder !! ---
    }



    //              JavaFX Getters, Setters, dan Property Methods

    // --- ID Akun
    public int getIdAkun() { return idAkun.get(); }
    public void setIdAkun(int idAkun) { this.idAkun.set(idAkun); }
    public IntegerProperty idAkunProperty() { return idAkun; }

    // --- Nama Lengkap
    public String getNamaLengkap() { return namaLengkap.get(); }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap.set(namaLengkap); }
    public StringProperty namaLengkapProperty() { return namaLengkap; }

    // --- Alamat
    public String getAlamat() { return alamat.get(); }
    public void setAlamat(String alamat) { this.alamat.set(alamat); }
    public StringProperty alamatProperty() { return alamat; }

    // --- No Telepon
    public String getNoTelepon() { return noTelepon.get(); }
    public void setNoTelepon(String noTelepon) { this.noTelepon.set(noTelepon); }
    public StringProperty noTeleponProperty() { return noTelepon; }

    // --- Email
    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }

    // --- Username
    public String getUsername() { return username.get(); }
    public void setUsername(String username) { this.username.set(username); }
    public StringProperty usernameProperty() { return username; }

    // --- Hashed Password
    // (Hati-hati, biasanya kita tidak membuat setter publik untuk hash)
    public String getHashedPassword() { return hashedPassword.get(); }
    public StringProperty hashedPasswordProperty() { return hashedPassword; }

    // --- Is Active
    public boolean getIsActive() { return isActive.get(); }
    public void setIsActive(boolean isActive) { this.isActive.set(isActive); }
    public BooleanProperty isActiveProperty() { return isActive; }

    // --- Created At
    public LocalDateTime getCreatedAt() { return createdAt.get(); }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt.set(createdAt); }
    public ObjectProperty<LocalDateTime> createdAtProperty() { return createdAt; }

    // --- Updated At
    public LocalDateTime getUpdatedAt() { return updatedAt.get(); }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt.set(updatedAt); }
    public ObjectProperty<LocalDateTime> updatedAtProperty() { return updatedAt; }
}