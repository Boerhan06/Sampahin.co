package models;

import javafx.beans.property.*;
import java.math.BigDecimal;

public abstract class Sampah {

    protected IntegerProperty idSampah;
    protected StringProperty jenisSampah;
    protected ObjectProperty<BigDecimal> hargaPoinPerKg;

    public Sampah(int idSampah, String jenisSampah, BigDecimal hargaPoinPerKg) {
        this.idSampah = new SimpleIntegerProperty(idSampah);
        this.jenisSampah = new SimpleStringProperty(jenisSampah);
        this.hargaPoinPerKg = new SimpleObjectProperty<>(hargaPoinPerKg);
    }

    public Sampah(String jenisSampah, BigDecimal hargaPoinPerKg) {
        this.idSampah = new SimpleIntegerProperty(0);
        this.jenisSampah = new SimpleStringProperty(jenisSampah);
        this.hargaPoinPerKg = new SimpleObjectProperty<>(hargaPoinPerKg);
    }

    public abstract String getKategori();

    public int getIdSampah() { return idSampah.get(); }
    public void setIdSampah(int id) { this.idSampah.set(id); }
    public IntegerProperty idSampahProperty() { return idSampah; }

    public String getJenisSampah() { return jenisSampah.get(); }
    public void setJenisSampah(String jenis) { this.jenisSampah.set(jenis); }
    public StringProperty jenisSampahProperty() { return jenisSampah; }

    public BigDecimal getHargaPoinPerKg() { return hargaPoinPerKg.get(); }
    public void setHargaPoinPerKg(BigDecimal harga) { this.hargaPoinPerKg.set(harga); }
    public ObjectProperty<BigDecimal> hargaPoinPerKgProperty() { return hargaPoinPerKg; }

    @Override
    public String toString() {
        return getJenisSampah() + " (" + getKategori() + ")";
    }
}