package models;

import javafx.beans.property.*;

public class TitikPengumpulan {

    private IntegerProperty idLokasi;
    private StringProperty namaLokasi;
    private StringProperty alamat; // UBAH: dari alamatLokasi jadi alamat (sesuai DB)
    private DoubleProperty latitude;
    private DoubleProperty longitude;

    // Constructor 1 (Untuk Data Baru - ID otomatis 0)
    public TitikPengumpulan(String namaLokasi, String alamat, double lat, double lng) {
        this.idLokasi = new SimpleIntegerProperty(0);
        this.namaLokasi = new SimpleStringProperty(namaLokasi);
        this.alamat = new SimpleStringProperty(alamat); // Update
        this.latitude = new SimpleDoubleProperty(lat);
        this.longitude = new SimpleDoubleProperty(lng);
    }

    // Constructor 2 (Untuk Data dari Database)
    public TitikPengumpulan(int idLokasi, String namaLokasi, String alamat, double lat, double lng) {
        this.idLokasi = new SimpleIntegerProperty(idLokasi);
        this.namaLokasi = new SimpleStringProperty(namaLokasi);
        this.alamat = new SimpleStringProperty(alamat); // Update
        this.latitude = new SimpleDoubleProperty(lat);
        this.longitude = new SimpleDoubleProperty(lng);
    }

    // --- ID LOKASI ---
    public int getIdLokasi() { return idLokasi.get(); }
    public void setIdLokasi(int idLokasi) { this.idLokasi.set(idLokasi); }
    public IntegerProperty idLokasiProperty() { return idLokasi; }

    // --- NAMA LOKASI ---
    public String getNamaLokasi() { return namaLokasi.get(); }
    public void setNamaLokasi(String namaLokasi) { this.namaLokasi.set(namaLokasi); }
    public StringProperty namaLokasiProperty() { return namaLokasi; }

    // --- ALAMAT (Disesuaikan) ---
    // Sekarang methodnya jadi getAlamat() bukan getAlamatLokasi()
    public String getAlamat() { return alamat.get(); }
    public void setAlamat(String alamat) { this.alamat.set(alamat); }
    public StringProperty alamatProperty() { return alamat; }

    // --- LATITUDE ---
    public double getLatitude() { return latitude.get(); }
    public void setLatitude(double latitude) { this.latitude.set(latitude); }
    public DoubleProperty latitudeProperty() { return latitude; }

    // --- LONGITUDE ---
    public double getLongitude() { return longitude.get(); }
    public void setLongitude(double longitude) { this.longitude.set(longitude); }
    public DoubleProperty longitudeProperty() { return longitude; }

    @Override
    public String toString() {
        return getNamaLokasi();
    }
}