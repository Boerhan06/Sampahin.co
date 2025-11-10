// package models;

public abstract class Sampah {
    protected int idSampah;
    protected String namaSampah;
    protected double nilaiPoinPerKg;

    public Sampah(String namaSampah, double nilaiPoinPerKg) {
        this.namaSampah = namaSampah;
        this.nilaiPoinPerKg = nilaiPoinPerKg;
    }

    // Method untuk hitung poin
    public double hitungPoin(double beratKg) {
        return this.nilaiPoinPerKg * beratKg;
    }

    public abstract String getKategori();

    // Getter dan Setter
    public int getIdSampah() { return idSampah; }
    public void setIdSampah(int idSampah) { this.idSampah = idSampah; }
    public String getNamaSampah() { return namaSampah; }
    // ...
}
