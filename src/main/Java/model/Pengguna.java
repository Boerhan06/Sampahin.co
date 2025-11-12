// package models;
import javafx.beans.property.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Kelas Pengguna (turunan Akun)
 * Dibuat dengan JavaFX Properties untuk UI Binding.
 */
public class Pengguna extends Akun {

    // --- Atribut Khusus Pengguna (dengan JavaFX Properties) ---
    private StringProperty idKartu;
    private StringProperty nomorKartu;
    private ObjectProperty<BigDecimal> saldoPoin;
    private ObjectProperty<BigDecimal> saldo; // Saldo Rupiah (WAJIB BigDecimal)
    private ObjectProperty<LocalDate> tanggalDaftar;

    /**
     * Constructor 1: Untuk membuat Pengguna BARU (misal: saat registrasi)
     */
    public Pengguna(String namaLengkap, String alamat, String noTelepon, String email, String username, String plainPassword,
                    String idKartu, String nomorKartu) {

        // Panggil constructor Akun (induk) untuk user BARU
        super(namaLengkap, alamat, noTelepon, email, username, plainPassword);

        // Inisialisasi properti spesifik Pengguna
        this.idKartu = new SimpleStringProperty(idKartu);
        this.nomorKartu = new SimpleStringProperty(nomorKartu);
        this.saldoPoin = new SimpleObjectProperty<>(BigDecimal.ZERO); // Awal: 0
        this.saldo = new SimpleObjectProperty<>(BigDecimal.ZERO);     // Awal: 0
        this.tanggalDaftar = new SimpleObjectProperty<>(LocalDate.now()); // Hari ini
    }

    /**
     * Constructor 2: Untuk memuat Pengguna dari DATABASE
     */
    public Pengguna(int idAkun, String namaLengkap, String alamat, String noTelepon, String email, String username,
                    String dbHashedPassword, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, // <-- Atribut Akun
                    String idKartu, String nomorKartu, BigDecimal saldoPoin, BigDecimal saldo, LocalDate tanggalDaftar) { // <-- Atribut Pengguna

        // Panggil constructor Akun (induk) untuk data dari DB
        super(idAkun, namaLengkap, alamat, noTelepon, email, username, dbHashedPassword, isActive, createdAt, updatedAt);

        // Set properti spesifik Pengguna
        this.idKartu = new SimpleStringProperty(idKartu);
        this.nomorKartu = new SimpleStringProperty(nomorKartu);
        this.saldoPoin = new SimpleObjectProperty<>(saldoPoin);
        this.saldo = new SimpleObjectProperty<>(saldo);
        this.tanggalDaftar = new SimpleObjectProperty<>(tanggalDaftar);
    }

    @Override
    public String getRole() {
        return "Pengguna";
    }

    // --- Method Khusus Pengguna (menggunakan BigDecimal) ---

    public void tambahPoin(BigDecimal poin) {
        // BigDecimal bersifat immutable, jadi harus di-set ulang
        this.saldoPoin.set(this.saldoPoin.get().add(poin));
    }

    public boolean tukarPoinKeSaldo(BigDecimal poin) {
        // Gunakan compareTo untuk membandingkan BigDecimal
        // (saldoPoin >= poin)
        if (this.saldoPoin.get().compareTo(poin) >= 0) {
            BigDecimal kursTukar = new BigDecimal("1.0"); // Asumsi 1 Poin = Rp 1
            BigDecimal nilaiRupiah = poin.multiply(kursTukar);

            this.saldoPoin.set(this.saldoPoin.get().subtract(poin));
            this.saldo.set(this.saldo.get().add(nilaiRupiah));
            return true;
        }
        return false;
    }

    // --- JavaFX Getters, Setters, dan Property Methods ---

    // --- Id Kartu ---
    public String getIdKartu() { return idKartu.get(); }
    public void setIdKartu(String idKartu) { this.idKartu.set(idKartu); }
    public StringProperty idKartuProperty() { return idKartu; }

    // --- Nomor Kartu ---
    public String getNomorKartu() { return nomorKartu.get(); }
    public void setNomorKartu(String nomorKartu) { this.nomorKartu.set(nomorKartu); }
    public StringProperty nomorKartuProperty() { return nomorKartu; }

    // --- Saldo Poin ---
    public BigDecimal getSaldoPoin() { return saldoPoin.get(); }
    public void setSaldoPoin(BigDecimal saldoPoin) { this.saldoPoin.set(saldoPoin); }
    public ObjectProperty<BigDecimal> saldoPoinProperty() { return saldoPoin; }

    // --- Saldo ---
    public BigDecimal getSaldo() { return saldo.get(); }
    public void setSaldo(BigDecimal saldo) { this.saldo.set(saldo); }
    public ObjectProperty<BigDecimal> saldoProperty() { return saldo; }

    // --- Tanggal Daftar ---
    public LocalDate getTanggalDaftar() { return tanggalDaftar.get(); }
    public void setTanggalDaftar(LocalDate tanggalDaftar) { this.tanggalDaftar.set(tanggalDaftar); }
    public ObjectProperty<LocalDate> tanggalDaftarProperty() { return tanggalDaftar; }
}