// package models;

// Ini adalah Mitra petugas di titik kumpul (sesuai Use Case)
// BUKAN mitra bisnis (pembeli sampah), walau namanya sama.
public class Mitra extends Akun { 
    // Atribut
    private String idMitra; // Misal: "MTR001"
    private TitikPengumpulan lokasiTugas;

    // Constructor
    public Mitra(String namaLengkap, String alamat, String noTelepon, String email, String username, String password, String idMitra) {
        super(namaLengkap, alamat, noTelepon, email, username, password);
        this.idMitra = idMitra;
    }

    @Override
    public String getRole() {
        return "Mitra";
    }

    // Method khusus Mitra
    public TransaksiSampah buatTransaksi(Pengguna pengguna, Sampah sampah, double berat) {
        System.out.println("Mitra " + this.namaLengkap + " memvalidasi setoran...");
        // Logika membuat transaksi baru
        return new TransaksiSampah(pengguna, this.lokasiTugas, sampah, berat);
    }
    
    // Getter dan Setter
    public void setLokasiTugas(TitikPengumpulan lokasiTugas) {
        this.lokasiTugas = lokasiTugas;
    }
    // ...
}
