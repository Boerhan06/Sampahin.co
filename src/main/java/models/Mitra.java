package models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;


public class Mitra extends Akun {
    private StringProperty idMitra;
    private ObjectProperty<TitikPengumpulan> lokasiTugas;

    public Mitra(String namaLengkap, String alamat, String noTelepon, String email, String username, String plainPassword,
                 String idMitra, TitikPengumpulan lokasiTugas) {
        super(namaLengkap, alamat, noTelepon, email, username, plainPassword);
        this.idMitra = new SimpleStringProperty(idMitra);
        this.lokasiTugas = new SimpleObjectProperty<>(lokasiTugas);
    }

    public Mitra(int idAkun, String namaLengkap, String alamat, String noTelepon, String email, String username,
                 String dbHashedPassword, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, // <-- Atribut Akun
                 String idMitra, TitikPengumpulan lokasiTugas) { // <-- Atribut Mitra
        super(idAkun, namaLengkap, alamat, noTelepon, email, username, dbHashedPassword, isActive, createdAt, updatedAt);
        this.idMitra = new SimpleStringProperty(idMitra);
        this.lokasiTugas = new SimpleObjectProperty<>(lokasiTugas);
    }

    @Override
    public String getRole() {
        return "Mitra";
    }

    // --- Method Khusus Mitra ---
    public TransaksiSampah buatTransaksi(Pengguna pengguna, Sampah sampah, BigDecimal beratKg) {
        // Pastikan lokasiTugas tidak null
        if (this.lokasiTugas.get() == null) {
            System.err.println("Error: Mitra " + getNamaLengkap() + " tidak memiliki lokasi tugas!");
            return null;
        }

        System.out.println("Mitra " + this.getNamaLengkap() + " memvalidasi setoran...");
        TransaksiSampah transaksi = new TransaksiSampah(pengguna, this, this.lokasiTugas.get(), sampah, beratKg);
        return transaksi;
    }

    // --- ID Mitra ---
    public String getIdMitra() { return idMitra.get(); }
    public void setIdMitra(String idMitra) { this.idMitra.set(idMitra); }
    public StringProperty idMitraProperty() { return idMitra; }

    // --- Lokasi Tugas ---
    public TitikPengumpulan getLokasiTugas() { return lokasiTugas.get(); }
    public void setLokasiTugas(TitikPengumpulan lokasiTugas) { this.lokasiTugas.set(lokasiTugas); }
    public ObjectProperty<TitikPengumpulan> lokasiTugasProperty() { return lokasiTugas; }
}