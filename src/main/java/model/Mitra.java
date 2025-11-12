// package models;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Kelas Mitra (Petugas di Titik Kumpul)
 * Dibuat dengan JavaFX Properties untuk UI Binding.
 */
public class Mitra extends Akun {

    // --- Atribut Khusus Mitra ---
    private StringProperty idMitra; // Misal: "MTR001"
    private ObjectProperty<TitikPengumpulan> lokasiTugas;

    /**
     * Constructor 1: Untuk membuat Mitra BARU
     */
    public Mitra(String namaLengkap, String alamat, String noTelepon, String email, String username, String plainPassword,
                 String idMitra, TitikPengumpulan lokasiTugas) {

        // Panggil constructor Akun (induk) untuk user BARU
        super(namaLengkap, alamat, noTelepon, email, username, plainPassword);

        // Set atribut spesifik Mitra
        this.idMitra = new SimpleStringProperty(idMitra);
        this.lokasiTugas = new SimpleObjectProperty<>(lokasiTugas);
    }

    /**
     * Constructor 2: Untuk memuat Mitra dari DATABASE
     */
    public Mitra(int idAkun, String namaLengkap, String alamat, String noTelepon, String email, String username,
                 String dbHashedPassword, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, // <-- Atribut Akun
                 String idMitra, TitikPengumpulan lokasiTugas) { // <-- Atribut Mitra

        // Panggil constructor Akun (induk) untuk data dari DB
        super(idAkun, namaLengkap, alamat, noTelepon, email, username, dbHashedPassword, isActive, createdAt, updatedAt);

        // Set atribut spesifik Mitra
        this.idMitra = new SimpleStringProperty(idMitra);
        this.lokasiTugas = new SimpleObjectProperty<>(lokasiTugas);
    }


    @Override
    public String getRole() {
        return "Mitra";
    }

    // --- Method Khusus Mitra ---

    /**
     * Membuat objek Transaksi baru berdasarkan input dari UI.
     * @param pengguna Objek Pengguna yang menyetor
     * @param sampah Objek Sampah yang disetor
     * @param beratKg Berat sampah (gunakan BigDecimal)
     * @return Objek TransaksiSampah yang baru
     */
    public TransaksiSampah buatTransaksi(Pengguna pengguna, Sampah sampah, BigDecimal beratKg) {
        // Pastikan lokasiTugas tidak null
        if (this.lokasiTugas.get() == null) {
            // Ini adalah error, Mitra seharusnya punya lokasi tugas
            // Di aplikasi nyata, lempar Exception
            System.err.println("Error: Mitra " + getNamaLengkap() + " tidak memiliki lokasi tugas!");
            return null;
        }

        System.out.println("Mitra " + this.getNamaLengkap() + " memvalidasi setoran...");

        // Logika membuat transaksi baru
        // Kita teruskan 'this' (Mitra) dan lokasiTugas-nya
        TransaksiSampah transaksi = new TransaksiSampah(pengguna, this, this.lokasiTugas.get(), sampah, beratKg);

        // Di aplikasi nyata, logika ini (menyimpan ke DB dan menambah poin)
        // sebaiknya ada di kelas "TransaksiService", BUKAN di model.
        // --- Contoh Placeholder ---
        // 1. Simpan 'transaksi' ke database (misal: transaksiDAO.save(transaksi))
        // 2. Update poin pengguna (misal: pengguna.tambahPoin(transaksi.getTotalPoinDidapat()))
        // 3. Simpan 'pengguna' yang sudah terupdate (misal: penggunaDAO.update(pengguna))
        // --- Batas Placeholder ---

        return transaksi;
    }


    // --- JavaFX Getters, Setters, dan Property Methods ---

    // --- ID Mitra ---
    public String getIdMitra() { return idMitra.get(); }
    public void setIdMitra(String idMitra) { this.idMitra.set(idMitra); }
    public StringProperty idMitraProperty() { return idMitra; }

    // --- Lokasi Tugas ---
    public TitikPengumpulan getLokasiTugas() { return lokasiTugas.get(); }
    public void setLokasiTugas(TitikPengumpulan lokasiTugas) { this.lokasiTugas.set(lokasiTugas); }
    public ObjectProperty<TitikPengumpulan> lokasiTugasProperty() { return lokasiTugas; }
}