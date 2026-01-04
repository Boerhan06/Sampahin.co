package dao;

import models.Mitra;
import models.Pengguna;
import models.Sampah;
import models.TransaksiSampah;
import models.TitikPengumpulan;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object untuk tabel transaksi_sampah.
 */
public class TransaksiSampahDAO {

    private Connection connection;

    // Dependency DAOs untuk mengambil detail objek dari ID foreign key
    private PenggunaDAO penggunaDAO;
    private MitraDAO mitraDAO;
    private TitikPengumpulanDAO titikPengumpulanDAO;
    private SampahDAO sampahDAO;

    // Constructor: Inisialisasi koneksi dan Helper DAOs
    public TransaksiSampahDAO() {
        // PERBAIKAN: Mengambil connection dari wrapper Singleton
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.penggunaDAO = new PenggunaDAO();
        this.mitraDAO = new MitraDAO();
        this.titikPengumpulanDAO = new TitikPengumpulanDAO();
        this.sampahDAO = new SampahDAO();
    }

    /**
     * CREATE: Menyimpan transaksi baru ke database.
     * @param trx Objek TransaksiSampah yang akan disimpan
     * @return true jika berhasil, false jika gagal
     */
    public boolean save(TransaksiSampah trx) {
        String sql = "INSERT INTO transaksi_sampah " +
                "(id_pengguna, id_mitra, id_lokasi, id_sampah, berat_kg, total_poin_didapat, timestamp) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Mengambil ID dari objek-objek relasi
            stmt.setInt(1, trx.getPengguna().getIdAkun());
            stmt.setInt(2, trx.getMitra().getIdAkun());
            stmt.setInt(3, trx.getLokasi().getIdLokasi());
            stmt.setInt(4, trx.getSampah().getIdSampah());

            // Mengambil nilai BigDecimal dan LocalDateTime
            stmt.setBigDecimal(5, trx.getBeratKg());
            stmt.setBigDecimal(6, trx.getTotalPoinDidapat());
            stmt.setObject(7, trx.getTimestamp());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Gagal menyimpan transaksi: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Helper Method: Mengubah baris ResultSet menjadi objek TransaksiSampah.
     * Method ini otomatis mencari detail Pengguna, Mitra, dll berdasarkan ID-nya.
     */
    private TransaksiSampah mapResultSetToTransaksi(ResultSet rs) throws SQLException {
        // 1. Ambil Foreign Keys (ID)
        int idPengguna = rs.getInt("id_pengguna");
        int idMitra = rs.getInt("id_mitra");
        int idLokasi = rs.getInt("id_lokasi");
        int idSampah = rs.getInt("id_sampah");

        // 2. Gunakan DAO lain untuk mendapatkan Objek utuh berdasarkan ID tadi
        Pengguna pengguna = penggunaDAO.getPenggunaById(idPengguna);
        Mitra mitra = mitraDAO.getMitraById(idMitra);
        TitikPengumpulan lokasi = titikPengumpulanDAO.getById(idLokasi);
        Sampah sampah = sampahDAO.getById(idSampah);

        // 3. Return Objek TransaksiSampah menggunakan Constructor ke-2
        return new TransaksiSampah(
                rs.getInt("id_transaksi"),             // ID Transaksi
                pengguna,                              // Objek Pengguna
                mitra,                                 // Objek Mitra
                lokasi,                                // Objek TitikPengumpulan
                sampah,                                // Objek Sampah
                rs.getBigDecimal("berat_kg"),          // Berat
                rs.getBigDecimal("total_poin_didapat"),// Poin
                rs.getObject("timestamp", LocalDateTime.class) // Waktu
        );
    }

    /**
     * READ: Mengambil semua riwayat transaksi milik satu Pengguna tertentu.
     * Berguna untuk halaman "Riwayat" di dashboard pengguna.
     * @param idPengguna ID dari user yang sedang login
     * @return List transaksi
     */
    public List<TransaksiSampah> getAllByPengguna(int idPengguna) {
        String sql = "SELECT * FROM transaksi_sampah WHERE id_pengguna = ? ORDER BY timestamp DESC";
        List<TransaksiSampah> listTrx = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPengguna);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Panggil helper method mapping di atas
                TransaksiSampah trx = mapResultSetToTransaksi(rs);
                listTrx.add(trx);
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil data transaksi: " + e.getMessage());
            e.printStackTrace();
        }

        return listTrx;
    }
}