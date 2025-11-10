// package models;

public class SampahAnorganik extends Sampah {

    public SampahAnorganik(String namaSampah, double nilaiPoinPerKg) {
        super(namaSampah, nilaiPoinPerKg);
    }

    @Override
    public String getKategori() {
        return "Anorganik";
    }
}
