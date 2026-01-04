package models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.math.BigDecimal;
import java.time.LocalDate;

public class SampahOrganik extends Sampah {

    private ObjectProperty<LocalDate> perkiraanBusuk;

    public SampahOrganik(String jenisSampah, BigDecimal hargaPoinPerKg, LocalDate perkiraanBusuk) {
        super(jenisSampah, hargaPoinPerKg);
        this.perkiraanBusuk = new SimpleObjectProperty<>(perkiraanBusuk);
    }

    public SampahOrganik(int idSampah, String jenisSampah, BigDecimal hargaPoinPerKg, LocalDate perkiraanBusuk) {
        super(idSampah, jenisSampah, hargaPoinPerKg);
        this.perkiraanBusuk = new SimpleObjectProperty<>(perkiraanBusuk);
    }

    @Override
    public String getKategori() {
        return "Organik";
    }

    public LocalDate getPerkiraanBusuk() { return perkiraanBusuk.get(); }
    public void setPerkiraanBusuk(LocalDate tanggal) { this.perkiraanBusuk.set(tanggal); }
    public ObjectProperty<LocalDate> perkiraanBusukProperty() { return perkiraanBusuk; }
}