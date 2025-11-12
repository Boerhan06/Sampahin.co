// package models;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.math.BigDecimal;
import java.time.LocalDate;

public class SampahOrganik extends Sampah {

    // Atribut khusus Organik
    private ObjectProperty<LocalDate> perkiraanBusuk;

    /**
     * Constructor 1: Untuk membuat data BARU
     */
    public SampahOrganik(String jenisSampah, BigDecimal hargaPoinPerKg, LocalDate perkiraanBusuk) {
        // Panggil constructor Induk (Sampah)
        super(jenisSampah, hargaPoinPerKg);
        this.perkiraanBusuk = new SimpleObjectProperty<>(perkiraanBusuk);
    }

    /**
     * Constructor 2: Untuk memuat dari DATABASE
     */
    public SampahOrganik(int idSampah, String jenisSampah, BigDecimal hargaPoinPerKg, LocalDate perkiraanBusuk) {
        // Panggil constructor Induk (Sampah)
        super(idSampah, jenisSampah, hargaPoinPerKg);
        this.perkiraanBusuk = new SimpleObjectProperty<>(perkiraanBusuk);
    }

    /**
     * Implementasi method abstract dari Induk
     */
    @Override
    public String getKategori() {
        return "Organik";
    }

    // --- Getter/Setter/Property untuk atribut khusus ---
    public LocalDate getPerkiraanBusuk() { return perkiraanBusuk.get(); }
    public void setPerkiraanBusuk(LocalDate tanggal) { this.perkiraanBusuk.set(tanggal); }
    public ObjectProperty<LocalDate> perkiraanBusukProperty() { return perkiraanBusuk; }
}