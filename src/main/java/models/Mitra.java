package models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Mitra extends Akun {
    private StringProperty idMitra;
    // Menggunakan ObjectProperty agar relasinya kuat dengan object TitikPengumpulan
    private ObjectProperty<TitikPengumpulan> lokasiTugas;

    // Constructor 1: Untuk pembuatan Mitra Baru (sebelum masuk DB)
    public Mitra(String namaLengkap, String alamat, String noTelepon, String email, String username, String plainPassword,
                 String idMitra, TitikPengumpulan lokasiTugas) {
        super(namaLengkap, alamat, noTelepon, email, username, plainPassword);
        this.idMitra = new SimpleStringProperty(idMitra);
        this.lokasiTugas = new SimpleObjectProperty<>(lokasiTugas);
    }

    // Constructor 2: Untuk pengambilan data dari Database (Lengkap dengan ID Akun & Timestamp)
    public Mitra(int idAkun, String namaLengkap, String alamat, String noTelepon, String email, String username,
                 String dbHashedPassword, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt,
                 String idMitra, TitikPengumpulan lokasiTugas) {
        super(idAkun, namaLengkap, alamat, noTelepon, email, username, dbHashedPassword, isActive, createdAt, updatedAt);
        this.idMitra = new SimpleStringProperty(idMitra);
        this.lokasiTugas = new SimpleObjectProperty<>(lokasiTugas);
    }

    @Override
    public String getRole() {
        return "Mitra";
    }

    // --- Helper UI: Ambil Inisial untuk Avatar Chat (Contoh: "Budi Santoso" -> "BS") ---
    public String getInisial() {
        String nama = getNamaLengkap();
        if (nama == null || nama.isEmpty()) return "?";
        String[] parts = nama.split(" ");
        String inisial = String.valueOf(parts[0].charAt(0));
        if (parts.length > 1) inisial += parts[1].charAt(0);
        return inisial.toUpperCase();
    }

    // --- Logic Transaksi ---
    public TransaksiSampah buatTransaksi(Pengguna pengguna, Sampah sampah, BigDecimal beratKg) {
        if (this.lokasiTugas.get() == null) {
            System.err.println("Error: Mitra " + getNamaLengkap() + " tidak memiliki lokasi tugas!");
            return null;
        }
        System.out.println("Mitra " + this.getNamaLengkap() + " memvalidasi setoran...");
        return new TransaksiSampah(pengguna, this, this.lokasiTugas.get(), sampah, beratKg);
    }

    // --- Getters & Setters Properties ---
    public String getIdMitra() { return idMitra.get(); }
    public void setIdMitra(String idMitra) { this.idMitra.set(idMitra); }
    public StringProperty idMitraProperty() { return idMitra; }

    public TitikPengumpulan getLokasiTugas() { return lokasiTugas.get(); }
    public void setLokasiTugas(TitikPengumpulan lokasiTugas) { this.lokasiTugas.set(lokasiTugas); }
    public ObjectProperty<TitikPengumpulan> lokasiTugasProperty() { return lokasiTugas; }
}