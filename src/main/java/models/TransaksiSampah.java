package models;

import javafx.beans.property.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransaksiSampah {

    private final IntegerProperty idTransaksi;
    private final ObjectProperty<Pengguna> pengguna;
    private final ObjectProperty<Mitra> mitra;
    private final ObjectProperty<TitikPengumpulan> lokasi;
    private final ObjectProperty<Sampah> sampah;
    private final ObjectProperty<BigDecimal> beratKg;
    private final ObjectProperty<BigDecimal> totalPoinDidapat;
    private final ObjectProperty<LocalDateTime> timestamp;

    public TransaksiSampah(Pengguna pengguna, Mitra mitra, TitikPengumpulan lokasi, Sampah sampah, BigDecimal beratKg) {
        this.idTransaksi = new SimpleIntegerProperty(0);
        this.pengguna = new SimpleObjectProperty<>(pengguna);
        this.mitra = new SimpleObjectProperty<>(mitra);
        this.lokasi = new SimpleObjectProperty<>(lokasi);
        this.sampah = new SimpleObjectProperty<>(sampah);
        this.beratKg = new SimpleObjectProperty<>(beratKg);
        this.timestamp = new SimpleObjectProperty<>(LocalDateTime.now());

        BigDecimal hargaPerKg = sampah.getHargaPoinPerKg();
        if (hargaPerKg == null) hargaPerKg = BigDecimal.ZERO; // Safety check

        BigDecimal poin = beratKg.multiply(hargaPerKg);
        this.totalPoinDidapat = new SimpleObjectProperty<>(poin);
    }

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

    public int getIdTransaksi() {
        return idTransaksi.get();
    }

    public void setIdTransaksi(int id) {
        this.idTransaksi.set(id);
    }

    public IntegerProperty idTransaksiProperty() {
        return idTransaksi;
    }

    public Pengguna getPengguna() {
        return pengguna.get();
    }

    public void setPengguna(Pengguna p) {
        this.pengguna.set(p);
    }

    public ObjectProperty<Pengguna> penggunaProperty() {
        return pengguna;
    }

    public Mitra getMitra() {
        return mitra.get();
    }

    public void setMitra(Mitra m) {
        this.mitra.set(m);
    }

    public ObjectProperty<Mitra> mitraProperty() {
        return mitra;
    }

    public TitikPengumpulan getLokasi() {
        return lokasi.get();
    }

    public void setLokasi(TitikPengumpulan l) {
        this.lokasi.set(l);
    }

    public ObjectProperty<TitikPengumpulan> lokasiProperty() {
        return lokasi;
    }

    public Sampah getSampah() {
        return sampah.get();
    }

    public void setSampah(Sampah s) {
        this.sampah.set(s);
    }

    public ObjectProperty<Sampah> sampahProperty() {
        return sampah;
    }

    public BigDecimal getBeratKg() {
        return beratKg.get();
    }

    public void setBeratKg(BigDecimal kg) {
        this.beratKg.set(kg);
    }

    public ObjectProperty<BigDecimal> beratKgProperty() {
        return beratKg;
    }

    public BigDecimal getTotalPoinDidapat() {
        return totalPoinDidapat.get();
    }

    public void setTotalPoinDidapat(BigDecimal poin) {
        this.totalPoinDidapat.set(poin);
    }

    public ObjectProperty<BigDecimal> totalPoinDidapatProperty() {
        return totalPoinDidapat;
    }

    public LocalDateTime getTimestamp() {
        return timestamp.get();
    }

    public void setTimestamp(LocalDateTime time) {
        this.timestamp.set(time);
    }

    public ObjectProperty<LocalDateTime> timestampProperty() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "TransaksiSampah{" +
                "id=" + getIdTransaksi() +
                ", pengguna=" + (getPengguna() != null ? getPengguna().getUsername() : "null") +
                ", sampah=" + (getSampah() != null ? getSampah().getJenisSampah() : "null") +
                ", poin=" + getTotalPoinDidapat() +
                '}';
    }
}