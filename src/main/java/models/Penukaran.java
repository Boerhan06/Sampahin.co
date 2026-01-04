package models;

import javafx.beans.property.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Penukaran {

    private IntegerProperty idPenukaran;
    private ObjectProperty<Pengguna> pengguna;
    private ObjectProperty<BigDecimal> poinDitukar;
    private ObjectProperty<BigDecimal> nilaiRupiah;
    private StringProperty keterangan;
    private ObjectProperty<LocalDateTime> timestamp;
    private StringProperty status;

    public Penukaran(Pengguna pengguna, BigDecimal poinDitukar, BigDecimal nilaiRupiah, String keterangan) {
        this.idPenukaran = new SimpleIntegerProperty(0);
        this.pengguna = new SimpleObjectProperty<>(pengguna);
        this.poinDitukar = new SimpleObjectProperty<>(poinDitukar);
        this.nilaiRupiah = new SimpleObjectProperty<>(nilaiRupiah);
        this.keterangan = new SimpleStringProperty(keterangan);
        this.timestamp = new SimpleObjectProperty<>(LocalDateTime.now());
        this.status = new SimpleStringProperty("Berhasil");
    }

    public Penukaran(int idPenukaran, Pengguna pengguna, BigDecimal poinDitukar, BigDecimal nilaiRupiah,
                     String keterangan, LocalDateTime timestamp, String status) {
        this.idPenukaran = new SimpleIntegerProperty(idPenukaran);
        this.pengguna = new SimpleObjectProperty<>(pengguna);
        this.poinDitukar = new SimpleObjectProperty<>(poinDitukar);
        this.nilaiRupiah = new SimpleObjectProperty<>(nilaiRupiah);
        this.keterangan = new SimpleStringProperty(keterangan);
        this.timestamp = new SimpleObjectProperty<>(timestamp);
        this.status = new SimpleStringProperty(status);
    }

    public int getIdPenukaran() { return idPenukaran.get(); }
    public void setIdPenukaran(int id) { this.idPenukaran.set(id); }
    public IntegerProperty idPenukaranProperty() { return idPenukaran; }

    public Pengguna getPengguna() { return pengguna.get(); }
    public void setPengguna(Pengguna pengguna) { this.pengguna.set(pengguna); }
    public ObjectProperty<Pengguna> penggunaProperty() { return pengguna; }

    public BigDecimal getPoinDitukar() { return poinDitukar.get(); }
    public void setPoinDitukar(BigDecimal poin) { this.poinDitukar.set(poin); }
    public ObjectProperty<BigDecimal> poinDitukarProperty() { return poinDitukar; }

    public BigDecimal getNilaiRupiah() { return nilaiRupiah.get(); }
    public void setNilaiRupiah(BigDecimal nilai) { this.nilaiRupiah.set(nilai); }
    public ObjectProperty<BigDecimal> nilaiRupiahProperty() { return nilaiRupiah; }

    public String getKeterangan() { return keterangan.get(); }
    public void setKeterangan(String ket) { this.keterangan.set(ket); }
    public StringProperty keteranganProperty() { return keterangan; }

    public LocalDateTime getTimestamp() { return timestamp.get(); }
    public void setTimestamp(LocalDateTime time) { this.timestamp.set(time); }
    public ObjectProperty<LocalDateTime> timestampProperty() { return timestamp; }

    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }
    public StringProperty statusProperty() { return status; }
}