package models;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class RiwayatPenarikan {

    private final StringProperty idPenarikan;
    private final StringProperty username;
    private final StringProperty tanggal;
    private final LocalDateTime rawDateTime;
    private final StringProperty metode;
    private final DoubleProperty jumlah;
    private final StringProperty status;

    public RiwayatPenarikan(String idPenarikan, String username, String tanggal,
                            LocalDateTime rawDateTime, String metode, double jumlah, String status) {
        this.idPenarikan = new SimpleStringProperty(idPenarikan);
        this.username = new SimpleStringProperty(username);
        this.tanggal = new SimpleStringProperty(tanggal);
        this.rawDateTime = rawDateTime;
        this.metode = new SimpleStringProperty(metode);
        this.jumlah = new SimpleDoubleProperty(jumlah);
        this.status = new SimpleStringProperty(status);
    }

    public String getIdPenarikan() {
        return idPenarikan.get();
    }

    public void setIdPenarikan(String value) {
        this.idPenarikan.set(value);
    }

    public StringProperty idPenarikanProperty() {
        return idPenarikan;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String value) {
        this.username.set(value);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getTanggal() {
        return tanggal.get();
    }

    public void setTanggal(String value) {
        this.tanggal.set(value);
    }

    public StringProperty tanggalProperty() {
        return tanggal;
    }

    public LocalDateTime getRawDateTime() {
        return rawDateTime;
    }

    public String getMetode() {
        return metode.get();
    }

    public void setMetode(String value) {
        this.metode.set(value);
    }

    public StringProperty metodeProperty() {
        return metode;
    }

    public double getJumlah() {
        return jumlah.get();
    }

    public void setJumlah(double value) {
        this.jumlah.set(value);
    }

    public DoubleProperty jumlahProperty() {
        return jumlah;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String value) {
        this.status.set(value);
    }

    public StringProperty statusProperty() {
        return status;
    }
}