// package models;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TitikPengumpulan {

    private IntegerProperty idLokasi;
    private StringProperty namaLokasi;
    private StringProperty alamatLokasi;

    // Constructor 1: Baru (ID dari DB)
    public TitikPengumpulan(String namaLokasi, String alamatLokasi) {
        this.idLokasi = new SimpleIntegerProperty(0); // Penanda baru
        this.namaLokasi = new SimpleStringProperty(namaLokasi);
        this.alamatLokasi = new SimpleStringProperty(alamatLokasi);
    }

    // Constructor 2: Dari DB
    public TitikPengumpulan(int idLokasi, String namaLokasi, String alamatLokasi) {
        this.idLokasi = new SimpleIntegerProperty(idLokasi);
        this.namaLokasi = new SimpleStringProperty(namaLokasi);
        this.alamatLokasi = new SimpleStringProperty(alamatLokasi);
    }

    // --- Getters, Setters, dan Properties ---
    public int getIdLokasi() { return idLokasi.get(); }
    public void setIdLokasi(int idLokasi) { this.idLokasi.set(idLokasi); }
    public IntegerProperty idLokasiProperty() { return idLokasi; }

    public String getNamaLokasi() { return namaLokasi.get(); }
    public void setNamaLokasi(String namaLokasi) { this.namaLokasi.set(namaLokasi); }
    public StringProperty namaLokasiProperty() { return namaLokasi; }

    public String getAlamatLokasi() { return alamatLokasi.get(); }
    public void setAlamatLokasi(String alamatLokasi) { this.alamatLokasi.set(alamatLokasi); }
    public StringProperty alamatLokasiProperty() { return alamatLokasi; }

    // Berguna untuk ditampilkan di ComboBox
    @Override
    public String toString() {
        return getNamaLokasi();
    }
}