package models;

// package models; // Sudah benar di package model
import javafx.beans.property.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Kelas Model untuk mencatat transaksi Penukaran Poin.
 * Ini adalah DATA, jadi lokasinya di package 'models' sudah tepat.
 * Menggunakan JavaFX Properties untuk UI Binding.
 */
public class Penukaran {

    private IntegerProperty idPenukaran;
    private ObjectProperty<Pengguna> pengguna; // Siapa yang menukar
    private ObjectProperty<BigDecimal> poinDitukar; // Jumlah poin yang dikurangi
    private ObjectProperty<BigDecimal> nilaiRupiah; // Hasil konversi (jika jadi saldo)
    private StringProperty keterangan; // Misal: "Tukar ke Saldo", "Pulsa 50rb"
    private ObjectProperty<LocalDateTime> timestamp;
    private StringProperty status; // Misal: "Berhasil", "Pending", "Gagal"

    /**
     * Constructor 1: Untuk membuat Penukaran BARU (misal: saat proses penukaran)
     */
    public Penukaran(Pengguna pengguna, BigDecimal poinDitukar, BigDecimal nilaiRupiah, String keterangan) {
        this.idPenukaran = new SimpleIntegerProperty(0); // Penanda baru
        this.pengguna = new SimpleObjectProperty<>(pengguna);
        this.poinDitukar = new SimpleObjectProperty<>(poinDitukar);
        this.nilaiRupiah = new SimpleObjectProperty<>(nilaiRupiah);
        this.keterangan = new SimpleStringProperty(keterangan);
        this.timestamp = new SimpleObjectProperty<>(LocalDateTime.now());
        this.status = new SimpleStringProperty("Berhasil"); // Default
    }

    /**
     * Constructor 2: Untuk memuat Penukaran dari DATABASE
     */
    public Penukaran(int idPenukaran, Pengguna pengguna, BigDecimal poinDitukar, BigDecimal nilaiRupiah,
                     String keterangan, LocalDateTime timestamp, String status) {
        this.idPenukaran = new SimpleIntegerProperty(idPenukaran);
        this.pengguna = new SimpleObjectProperty<>(pengguna);
        this.poinDitukar = new SimpleObjectProperty<>(poinDitukar);
        this.nilaiRupiah = new SimpleObjectProperty<>(nilaiRupiah);
        this.keterangan = new SimpleStringProperty(keterangan);
        this.timestamp = new SimpleObjectProperty<>(timestamp);
        this.status = new SimpleStringProperty(status);
    }

    // --- JavaFX Getters, Setters, dan Property Methods ---

    // --- ID Penukaran ---
    public int getIdPenukaran() { return idPenukaran.get(); }
    public void setIdPenukaran(int id) { this.idPenukaran.set(id); }
    public IntegerProperty idPenukaranProperty() { return idPenukaran; }

    // --- Pengguna ---
    public Pengguna getPengguna() { return pengguna.get(); }
    public void setPengguna(Pengguna pengguna) { this.pengguna.set(pengguna); }
    public ObjectProperty<Pengguna> penggunaProperty() { return pengguna; }

    // --- Poin Ditukar ---
    public BigDecimal getPoinDitukar() { return poinDitukar.get(); }
    public void setPoinDitukar(BigDecimal poin) { this.poinDitukar.set(poin); }
    public ObjectProperty<BigDecimal> poinDitukarProperty() { return poinDitukar; }

    // --- Nilai Rupiah ---
    public BigDecimal getNilaiRupiah() { return nilaiRupiah.get(); }
    public void setNilaiRupiah(BigDecimal nilai) { this.nilaiRupiah.set(nilai); }
    public ObjectProperty<BigDecimal> nilaiRupiahProperty() { return nilaiRupiah; }

    // --- Keterangan ---
    public String getKeterangan() { return keterangan.get(); }
    public void setKeterangan(String ket) { this.keterangan.set(ket); }
    public StringProperty keteranganProperty() { return keterangan; }

    // --- Timestamp ---
    public LocalDateTime getTimestamp() { return timestamp.get(); }
    public void setTimestamp(LocalDateTime time) { this.timestamp.set(time); }
    public ObjectProperty<LocalDateTime> timestampProperty() { return timestamp; }

    // --- Status ---
    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }
    public StringProperty statusProperty() { return status; }
}