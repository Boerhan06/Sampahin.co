package models;

import javafx.beans.property.*;

public class TitikPengumpulan {

    private final IntegerProperty idLokasi;
    private final StringProperty namaLokasi;
    private final StringProperty alamat;
    private final DoubleProperty latitude;
    private final DoubleProperty longitude;

    public TitikPengumpulan() {
        this.idLokasi = new SimpleIntegerProperty(0);
        this.namaLokasi = new SimpleStringProperty("");
        this.alamat = new SimpleStringProperty("");
        this.latitude = new SimpleDoubleProperty(0.0);
        this.longitude = new SimpleDoubleProperty(0.0);
    }

    public TitikPengumpulan(int idLokasi, String namaLokasi, double latitude, double longitude) {
        this.idLokasi = new SimpleIntegerProperty(idLokasi);
        this.namaLokasi = new SimpleStringProperty(namaLokasi);
        this.alamat = new SimpleStringProperty("-"); // Default strip jika alamat kosong
        this.latitude = new SimpleDoubleProperty(latitude);
        this.longitude = new SimpleDoubleProperty(longitude);
    }

    public TitikPengumpulan(int idLokasi, String namaLokasi, String alamat, double latitude, double longitude) {
        this.idLokasi = new SimpleIntegerProperty(idLokasi);
        this.namaLokasi = new SimpleStringProperty(namaLokasi);
        this.alamat = new SimpleStringProperty(alamat);
        this.latitude = new SimpleDoubleProperty(latitude);
        this.longitude = new SimpleDoubleProperty(longitude);
    }

    // --- ID LOKASI ---
    public int getIdLokasi() {
        return idLokasi.get();
    }

    public void setIdLokasi(int idLokasi) {
        this.idLokasi.set(idLokasi);
    }

    public IntegerProperty idLokasiProperty() {
        return idLokasi;
    }

    // --- NAMA LOKASI ---
    public String getNamaLokasi() {
        return namaLokasi.get();
    }

    public void setNamaLokasi(String namaLokasi) {
        this.namaLokasi.set(namaLokasi);
    }

    public StringProperty namaLokasiProperty() {
        return namaLokasi;
    }

    // --- ALAMAT ---
    public String getAlamat() {
        return alamat.get();
    }

    public void setAlamat(String alamat) {
        this.alamat.set(alamat);
    }

    public StringProperty alamatProperty() {
        return alamat;
    }

    // --- LATITUDE ---
    public double getLatitude() {
        return latitude.get();
    }

    public void setLatitude(double latitude) {
        this.latitude.set(latitude);
    }

    public DoubleProperty latitudeProperty() {
        return latitude;
    }

    // --- LONGITUDE ---
    public double getLongitude() {
        return longitude.get();
    }

    public void setLongitude(double longitude) {
        this.longitude.set(longitude);
    }

    public DoubleProperty longitudeProperty() {
        return longitude;
    }


    @Override
    public String toString() {
        return getNamaLokasi();
    }
}