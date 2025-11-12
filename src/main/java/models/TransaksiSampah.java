/* * PERBAIKAN: package adalah 'models' (sesuai struktur Anda)
 */
package models;

import javafx.beans.property.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Kelas Model untuk TransaksiSampah.
 * (Versi Lengkap dengan Getter/Setter/Constructor yang sudah diperbaiki)
 */
public class TransaksiSampah {
    
    private IntegerProperty idTransaksi;
    private ObjectProperty<Pengguna> pengguna;
    private ObjectProperty<Mitra> mitra;
    private ObjectProperty<TitikPengumpulan> lokasi; // Atribut yang error
    private ObjectProperty<Sampah> sampah;
    private ObjectProperty<BigDecimal> beratKg;
    private ObjectProperty<BigDecimal> totalPoinDidapat;
    private ObjectProperty<LocalDateTime> timestamp;

    /**
     * Constructor 1: Untuk membuat Transaksi BARU (dipanggil oleh Mitra).
     */
    public TransaksiSampah(Pengguna pengguna, Mitra mitra, TitikPengumpulan lokasi, Sampah sampah, BigDecimal beratKg) {
        this.idTransaksi = new SimpleIntegerProperty(0); // 0 sebagai penanda 'baru'
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
    
    /**
     * PERBAIKAN: Constructor 2: Untuk memuat data dari DATABASE.
     * (Dipanggil oleh TransaksiSampahDAO.mapResultSetToTransaksi)
     * Implementasi placeholder-nya sudah dihapus dan diganti dengan kode yang benar.
     */
    public TransaksiSampah(int idTransaksi, Pengguna pengguna, Mitra mitra, TitikPengumpulan lokasi, 
                           Sampah sampah, BigDecimal beratKg, BigDecimal totalPoinDidapat, LocalDateTime timestamp) {
        
        this.idTransaksi = new SimpleIntegerProperty(idTransaksi);
        this.pengguna = new SimpleObjectProperty<>(pengguna);
        this.mitra = new SimpleObjectProperty<>(mitra);
        this.lokasi = new SimpleObjectProperty<>(lokasi);
        this.sampah = new SimpleObjectProperty<>(sampah);
        this.beratKg = new SimpleObjectProperty<>(beratKg);
        this.totalPoinDidapat = new SimpleObjectProperty<>(totalPoinDidapat);
        this.timestamp = new SimpleObjectProperty<>(timestamp);
    }

    // --- KUMPULAN GETTER, SETTER, DAN PROPERTY METHODS ---
    // (Sekarang sudah lengkap)

    // --- ID Transaksi ---
    public int getIdTransaksi() { return idTransaksi.get(); }
    public void setIdTransaksi(int id) { this.idTransaksi.set(id); }
    public IntegerProperty idTransaksiProperty() { return idTransaksi; }

    // --- Pengguna ---
    public Pengguna getPengguna() { return pengguna.get(); }
    public void setPengguna(Pengguna p) { this.pengguna.set(p); }
    public ObjectProperty<Pengguna> penggunaProperty() { return pengguna; }

    // --- Mitra ---
    public Mitra getMitra() { return mitra.get(); }
    public void setMitra(Mitra m) { this.mitra.set(m); }
    public ObjectProperty<Mitra> mitraProperty() { return mitra; }

    
    /* * PERBAIKAN UTAMA ANDA ADA DI SINI *
     * Getter untuk 'lokasi' yang tadinya error.
     */
    // --- Lokasi ---
    public TitikPengumpulan getLokasi() { 
        return lokasi.get(); // Mengembalikan isi dari property 'lokasi'
    }
    public void setLokasi(TitikPengumpulan l) { this.lokasi.set(l); }
    public ObjectProperty<TitikPengumpulan> lokasiProperty() { return lokasi; }

    
    // --- Sampah ---
    public Sampah getSampah() { return sampah.get(); }
    public void setSampah(Sampah s) { this.sampah.set(s); }
    public ObjectProperty<Sampah> sampahProperty() { return sampah; }

    // --- BeratKg ---
    public BigDecimal getBeratKg() { return beratKg.get(); }
    public void setBeratKg(BigDecimal kg) { this.beratKg.set(kg); }
    public ObjectProperty<BigDecimal> beratKgProperty() { return beratKg; }
    
    // --- TotalPoinDidapat ---
    public BigDecimal getTotalPoinDidapat() { return totalPoinDidapat.get(); }
    public void setTotalPoinDidapat(BigDecimal poin) { this.totalPoinDidapat.set(poin); }
    public ObjectProperty<BigDecimal> totalPoinDidapatProperty() { return totalPoinDidapat; }
    
    // --- Timestamp ---
    public LocalDateTime getTimestamp() { return timestamp.get(); }
    public void setTimestamp(LocalDateTime time) { this.timestamp.set(time); }
    public ObjectProperty<LocalDateTime> timestampProperty() { return timestamp; }
}