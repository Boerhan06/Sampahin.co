package models;

import java.math.BigDecimal;

public class SampahAnorganik extends Sampah {

    public SampahAnorganik(String jenisSampah, BigDecimal hargaPoinPerKg) {
        super(jenisSampah, hargaPoinPerKg);
    }

    public SampahAnorganik(int idSampah, String jenisSampah, BigDecimal hargaPoinPerKg) {
        super(idSampah, jenisSampah, hargaPoinPerKg);
    }

    @Override
    public String getKategori() {
        return "Anorganik";
    }
}