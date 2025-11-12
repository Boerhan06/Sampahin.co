package models;

// package models;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.math.BigDecimal;

public class SampahB3 extends Sampah {

    // Atribut khusus B3 (Bahan Berbahaya & Beracun)
    private StringProperty petunjukPenanganan;

    public SampahB3(String jenisSampah, BigDecimal hargaPoinPerKg, String petunjukPenanganan) {
        super(jenisSampah, hargaPoinPerKg); // Panggil constructor Induk
        this.petunjukPenanganan = new SimpleStringProperty(petunjukPenanganan);
    }

    public SampahB3(int idSampah, String jenisSampah, BigDecimal hargaPoinPerKg, String petunjukPenanganan) {
        super(idSampah, jenisSampah, hargaPoinPerKg); // Panggil constructor Induk
        this.petunjukPenanganan = new SimpleStringProperty(petunjukPenanganan);
    }

    @Override
    public String getKategori() {
        return "B3";
    }

    // --- Getter/Setter/Property untuk atribut khusus ---
    public String getPetunjukPenanganan() { return petunjukPenanganan.get(); }
    public void setPetunjukPenanganan(String petunjuk) { this.petunjukPenanganan.set(petunjuk); }
    public StringProperty petunjukPenangananProperty() { return petunjukPenanganan; }
}