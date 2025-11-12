// package models;
import javafx.beans.property.*;
import java.math.BigDecimal;

/**
 * REVISI: Kelas Induk ABSTRAK untuk semua jenis sampah.
 * Tidak bisa dibuat objeknya langsung (new Sampah()), harus melalui anaknya.
 */
public abstract class Sampah {

    protected IntegerProperty idSampah;
    protected StringProperty jenisSampah; // Misal: "Botol PET", "Sisa Makanan"
    protected ObjectProperty<BigDecimal> hargaPoinPerKg; // Harga dalam Poin

    /**
     * Constructor untuk kelas Induk.
     * Akan dipanggil oleh anak-anaknya (Organik, Anorganik, B3).
     */
    public Sampah(int idSampah, String jenisSampah, BigDecimal hargaPoinPerKg) {
        this.idSampah = new SimpleIntegerProperty(idSampah);
        this.jenisSampah = new SimpleStringProperty(jenisSampah);
        this.hargaPoinPerKg = new SimpleObjectProperty<>(hargaPoinPerKg);
    }

    // Constructor untuk data BARU (ID akan di-set oleh DB)
    public Sampah(String jenisSampah, BigDecimal hargaPoinPerKg) {
        this.idSampah = new SimpleIntegerProperty(0); // Penanda baru
        this.jenisSampah = new SimpleStringProperty(jenisSampah);
        this.hargaPoinPerKg = new SimpleObjectProperty<>(hargaPoinPerKg);
    }

    // --- Method Abstract ---
    /**
     * Method ini WAJIB di-implementasi oleh setiap kelas anak.
     * @return String kategori (misal: "Organik", "Anorganik", "B3")
     */
    public abstract String getKategori();

    // --- JavaFX Getters, Setters, dan Property Methods ---

    public int getIdSampah() { return idSampah.get(); }
    public void setIdSampah(int id) { this.idSampah.set(id); }
    public IntegerProperty idSampahProperty() { return idSampah; }

    public String getJenisSampah() { return jenisSampah.get(); }
    public void setJenisSampah(String jenis) { this.jenisSampah.set(jenis); }
    public StringProperty jenisSampahProperty() { return jenisSampah; }

    public BigDecimal getHargaPoinPerKg() { return hargaPoinPerKg.get(); }
    public void setHargaPoinPerKg(BigDecimal harga) { this.hargaPoinPerKg.set(harga); }
    public ObjectProperty<BigDecimal> hargaPoinPerKgProperty() { return hargaPoinPerKg; }

    // Berguna untuk ComboBox, sekarang menampilkan kategori secara dinamis
    @Override
    public String toString() {
        return getJenisSampah() + " (" + getKategori() + ")";
    }
}