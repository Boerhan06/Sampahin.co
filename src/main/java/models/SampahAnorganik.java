package models;

import java.math.BigDecimal;

public class SampahAnorganik extends Sampah {

    public SampahAnorganik(String jenisSampah, BigDecimal hargaPoinPerKg) {
        super(jenisSampah, hargaPoinPerKg); // Panggil constructor Induk
    }

    public SampahAnorganik(int idSampah, String jenisSampah, BigDecimal hargaPoinPerKg) {
        super(idSampah, jenisSampah, hargaPoinPerKg); // Panggil constructor Induk
    }

    @Override
    public String getKategori() {
        return "Anorganik";
    }
}