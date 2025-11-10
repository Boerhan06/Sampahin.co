// package models;
import java.util.Date;

public class TransaksiSampah {
    // Atribut
    private int idTransaksi;
    private Date tanggalTransaksi;
    private double beratSampah;
    private double totalPoin;
    
    // Relasi Objek (Sangat Penting!)
    private Pengguna pengguna;
    private TitikPengumpulan titikKumpul;
    private Sampah sampah; // Jenis sampah yg disetor

    // Constructor
    public TransaksiSampah(Pengguna pengguna, TitikPengumpulan titikKumpul, Sampah sampah, double beratSampah) {
        this.pengguna = pengguna;
        this.titikKumpul = titikKumpul;
        this.sampah = sampah;
        this.beratSampah = beratSampah;
        this.tanggalTransaksi = new Date();
        
        // Langsung hitung poin saat transaksi dibuat
        this.totalPoin = sampah.hitungPoin(beratSampah);
    }

    // Getter dan Setter
    public int getIdTransaksi() { return idTransaksi; }
    public void setIdTransaksi(int idTransaksi) { this.idTransaksi = idTransaksi; }
    public Date getTanggalTransaksi() { return tanggalTransaksi; }
    public double getTotalPoin() { return totalPoin; }
    public Pengguna getPengguna() { return pengguna; }
    // ...
}
