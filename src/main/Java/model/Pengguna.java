// package models;
import java.util.Date;

public class Pengguna extends Akun {
    // Atribut khusus Pengguna
    private String idKartu;
    private String nomorKartu;
    private double saldoPoin;
    private double saldo; // Saldo Rupiah
    private Date tanggalDaftar;

    // Constructor
    public Pengguna(String namaLengkap, String alamat, String noTelepon, String email, String username, String password, String idKartu, String nomorKartu) {
        // Panggil constructor 'Akun' (induk)
        super(namaLengkap, alamat, noTelepon, email, username, password);
        this.idKartu = idKartu;
        this.nomorKartu = nomorKartu;
        this.saldoPoin = 0.0;
        this.saldo = 0.0;
        this.tanggalDaftar = new Date(); // Set tanggal hari ini
    }

    // Override method dari Akun
    @Override
    public String getRole() {
        return "Pengguna";
    }

    // Method khusus Pengguna
    public void tambahPoin(double poin) {
        this.saldoPoin += poin;
    }

    public boolean tukarPoinKeSaldo(double poin) {
        if (this.saldoPoin >= poin) {
            this.saldoPoin -= poin;
            this.saldo += (poin * 1); // Asumsi 1 Poin = Rp 1 [cite: 48]
            return true;
        }
        return false;
    }

    // Getter dan Setter
    public String getNomorKartu() {
        return nomorKartu;
    }
    
    public double getSaldoPoin() {
        return saldoPoin;
    }
    // ... getter/setter lainnya ...
}
