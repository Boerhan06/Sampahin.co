// package models;
import javafx.beans.property.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransaksiSampah {

    private IntegerProperty idTransaksi;
    private ObjectProperty<Pengguna> pengguna; // Siapa yang setor
    private ObjectProperty<Mitra> mitra; // Siapa yang validasi
    private ObjectProperty<TitikPengumpulan> lokasi; // Di mana
    private ObjectProperty<Sampah> sampah; // Apa yang disetor
    private ObjectProperty<BigDecimal> beratKg;
    private ObjectProperty<BigDecimal> totalPoinDidapat;
    private ObjectProperty<LocalDateTime> timestamp; // Kapan

    // Constructor: Dipanggil saat Mitra membuat transaksi BARU
    public TransaksiSampah(Pengguna pengguna, Mitra mitra, TitikPengumpulan lokasi, Sampah sampah, BigDecimal beratKg) {
        this.idTransaksi = new SimpleIntegerProperty(0); // Penanda baru
        this.pengguna = new SimpleObjectProperty<>(pengguna);
        this.mitra = new SimpleObjectProperty<>(mitra);
        this.lokasi = new SimpleObjectProperty<>(lokasi);
        this.sampah = new SimpleObjectProperty<>(sampah);
        this.beratKg = new SimpleObjectProperty<>(beratKg);
        this.timestamp = new SimpleObjectProperty<>(LocalDateTime.now());

        // Logika bisnis inti: Hitung poin
        BigDecimal hargaPerKg = sampah.getHargaPoinPerKg();
        BigDecimal poin = beratKg.multiply(hargaPerKg);
        this.totalPoinDidapat = new SimpleObjectProperty<>(poin);
    }

    // (Anda bisa tambahkan Constructor kedua untuk memuat dari DB jika diperlukan)

    // --- Getters, Setters, dan Properties ---
    // ... (Tambahkan semua getter/setter/property untuk semua atribut di atas) ...
    // Contoh:
    public Pengguna getPengguna() { return pengguna.get(); }
    public ObjectProperty<Pengguna> penggunaProperty() { return pengguna; }

    public Mitra getMitra() { return mitra.get(); }
    public ObjectProperty<Mitra> mitraProperty() { return mitra; }

    public Sampah getSampah() { return sampah.get(); }
    public ObjectProperty<Sampah> sampahProperty() { return sampah; }

    public BigDecimal getBeratKg() { return beratKg.get(); }
    public ObjectProperty<BigDecimal> beratKgProperty() { return beratKg; }

    public BigDecimal getTotalPoinDidapat() { return totalPoinDidapat.get(); }
    public ObjectProperty<BigDecimal> totalPoinDidapatProperty() { return totalPoinDidapat; }

    public LocalDateTime getTimestamp() { return timestamp.get(); }
    public ObjectProperty<LocalDateTime> timestampProperty() { return timestamp; }
}