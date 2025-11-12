// package models;
import javafx.beans.property.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Kelas Model untuk mencatat transaksi Bisnis (B2B).
 * Yaitu, transaksi dari Titik Pengumpulan ke Pengepul/Industri.
 * Menggunakan JavaFX Properties untuk UI Binding.
 */
public class TransaksiMitra {

    private IntegerProperty idTransaksiMitra;
    private ObjectProperty<TitikPengumpulan> titikPengumpulan; // Siapa/di mana yang menjual
    private ObjectProperty<Admin> adminPencatat; // Siapa Admin yang mencatat
    private StringProperty namaMitraBisnis; // Pengepul/Industri yang membeli
    private StringProperty kategoriSampahTerjual; // Misal: "Kardus (Batch #1)", "PET (Batch #3)"
    private ObjectProperty<BigDecimal> beratTotalKg; // Berat total
    private ObjectProperty<BigDecimal> nilaiRupiahDidapat; // Uang yang didapat Titik Kumpul
    private ObjectProperty<LocalDateTime> timestamp;
    private StringProperty status; // Misal: "Lunas", "Belum Dibayar"

    /**
     * Constructor 1: Untuk membuat Transaksi Mitra BARU
     */
    public TransaksiMitra(TitikPengumpulan titikPengumpulan, Admin adminPencatat, String namaMitraBisnis,
                          String kategoriSampahTerjual, BigDecimal beratTotalKg, BigDecimal nilaiRupiahDidapat, String status) {

        this.idTransaksiMitra = new SimpleIntegerProperty(0); // Penanda baru
        this.titikPengumpulan = new SimpleObjectProperty<>(titikPengumpulan);
        this.adminPencatat = new SimpleObjectProperty<>(adminPencatat);
        this.namaMitraBisnis = new SimpleStringProperty(namaMitraBisnis);
        this.kategoriSampahTerjual = new SimpleStringProperty(kategoriSampahTerjual);
        this.beratTotalKg = new SimpleObjectProperty<>(beratTotalKg);
        this.nilaiRupiahDidapat = new SimpleObjectProperty<>(nilaiRupiahDidapat);
        this.timestamp = new SimpleObjectProperty<>(LocalDateTime.now());
        this.status = new SimpleStringProperty(status);
    }

    /**
     * Constructor 2: Untuk memuat dari DATABASE
     */
    public TransaksiMitra(int idTransaksiMitra, TitikPengumpulan titikPengumpulan, Admin adminPencatat, String namaMitraBisnis,
                          String kategoriSampahTerjual, BigDecimal beratTotalKg, BigDecimal nilaiRupiahDidapat,
                          LocalDateTime timestamp, String status) {

        this.idTransaksiMitra = new SimpleIntegerProperty(idTransaksiMitra);
        this.titikPengumpulan = new SimpleObjectProperty<>(titikPengumpulan);
        this.adminPencatat = new SimpleObjectProperty<>(adminPencatat);
        this.namaMitraBisnis = new SimpleStringProperty(namaMitraBisnis);
        this.kategoriSampahTerjual = new SimpleStringProperty(kategoriSampahTerjual);
        this.beratTotalKg = new SimpleObjectProperty<>(beratTotalKg);
        this.nilaiRupiahDidapat = new SimpleObjectProperty<>(nilaiRupiahDidapat);
        this.timestamp = new SimpleObjectProperty<>(timestamp);
        this.status = new SimpleStringProperty(status);
    }

    // --- JavaFX Getters, Setters, dan Property Methods ---
    // (Tambahkan semua getter/setter/property di sini...)

    // Contoh:
    public int getIdTransaksiMitra() { return idTransaksiMitra.get(); }
    public IntegerProperty idTransaksiMitraProperty() { return idTransaksiMitra; }

    public TitikPengumpulan getTitikPengumpulan() { return titikPengumpulan.get(); }
    public ObjectProperty<TitikPengumpulan> titikPengumpulanProperty() { return titikPengumpulan; }

    public Admin getAdminPencatat() { return adminPencatat.get(); }
    public ObjectProperty<Admin> adminPencatatProperty() { return adminPencatat; }

    public String getNamaMitraBisnis() { return namaMitraBisnis.get(); }
    public StringProperty namaMitraBisnisProperty() { return namaMitraBisnis; }

    public BigDecimal getBeratTotalKg() { return beratTotalKg.get(); }
    public ObjectProperty<BigDecimal> beratTotalKgProperty() { return beratTotalKg; }

    public BigDecimal getNilaiRupiahDidapat() { return nilaiRupiahDidapat.get(); }
    public ObjectProperty<BigDecimal> nilaiRupiahDidapatProperty() { return nilaiRupiahDidapat; }

    public LocalDateTime getTimestamp() { return timestamp.get(); }
    public ObjectProperty<LocalDateTime> timestampProperty() { return timestamp; }

    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }
}