package dao;

import models.Admin;
import models.TitikPengumpulan;
import models.TransaksiMitra;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransaksiMitraDAO {

    private Connection connection;
    private TitikPengumpulanDAO titikPengumpulanDAO;
    private AdminDAO adminDAO;

    /**
     * Constructor menginisialisasi koneksi database dan DAO pendukung.
     */
    public TransaksiMitraDAO() {
        // Mengambil instance koneksi dari wrapper Singleton DatabaseConnection
        this.connection = DatabaseConnection.getInstance().getConnection();

        // Inisialisasi DAO lain untuk menangani Relasi (Foreign Key)
        this.titikPengumpulanDAO = new TitikPengumpulanDAO();
        this.adminDAO = new AdminDAO();
    }

    // ========================================================================
    //                               CREATE (INSERT)
    // ========================================================================

    /**
     * Menyimpan data transaksi baru ke database.
     * Mendukung Transaksi Sampah (Lengkap) maupun Top Up (Simple).
     */
    public boolean save(TransaksiMitra trx) {
        // PERBAIKAN: Menambahkan kolom 'jenis', 'keterangan', dan 'timestamp'
        String sql = "INSERT INTO transaksimitra (titikPengumpulan, adminPencatat, namaMitraBisnis, " +
                "kategoriSampahTerjual, beratTotalKg, nilaiRupiahDidapat, status, timestamp, jenis, keterangan) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            // 1. Titik Pengumpulan (Handle NULL jika Top Up)
            if (trx.getTitikPengumpulan() != null) {
                stmt.setInt(1, trx.getTitikPengumpulan().getIdLokasi());
            } else {
                stmt.setNull(1, Types.INTEGER); // Pastikan kolom di DB boleh NULL
            }

            // 2. Admin Pencatat (Handle NULL jika Top Up mandiri)
            if (trx.getAdminPencatat() != null) {
                stmt.setInt(2, trx.getAdminPencatat().getIdAkun());
            } else {
                stmt.setNull(2, Types.INTEGER); // Pastikan kolom di DB boleh NULL
            }

            // 3. Data String & Angka
            stmt.setString(3, trx.getNamaMitraBisnis());
            stmt.setString(4, trx.getKategoriSampahTerjual());
            stmt.setBigDecimal(5, trx.getBeratTotalKg());
            stmt.setBigDecimal(6, trx.getNominal()); // Nominal / Nilai Rupiah
            stmt.setString(7, trx.getStatus());

            // 4. Timestamp (Waktu Transaksi)
            if (trx.getTimestamp() != null) {
                stmt.setTimestamp(8, Timestamp.valueOf(trx.getTimestamp()));
            } else {
                stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            }

            // 5. Jenis Transaksi (Enum ke String)
            if (trx.getJenis() != null) {
                stmt.setString(9, trx.getJenis().name()); // "PEMASUKAN" atau "PENGELUARAN"
            } else {
                stmt.setString(9, "PENGELUARAN");
            }

            // 6. Keterangan
            stmt.setString(10, trx.getKeterangan());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            // CETAK ERROR AGAR KETAUAN PENYEBABNYA
            System.err.println("❌ GAGAL SAVE TRANSAKSI MITRA: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ========================================================================
    //                               READ (SELECT & MAPPING)
    // ========================================================================

    /**
     * Helper Method: Mengubah baris ResultSet menjadi objek TransaksiMitra.
     * Menangani mapping data lengkap termasuk Jenis dan Keterangan.
     */
    private TransaksiMitra mapResultSetToTransaksi(ResultSet rs) throws SQLException {
        // 1. Ambil Foreign Keys
        int idLokasi = rs.getInt("titikPengumpulan"); // Return 0 jika NULL
        int idAdmin = rs.getInt("adminPencatat");     // Return 0 jika NULL

        // 2. Ambil Objek Relasi (Handle Null Safety)
        TitikPengumpulan lokasi = null;
        if (idLokasi != 0) {
            lokasi = titikPengumpulanDAO.getById(idLokasi);
        }

        Admin admin = null;
        if (idAdmin != 0) {
            admin = adminDAO.getAdminById(idAdmin);
        }

        // 3. Handle TIMESTAMP
        LocalDateTime waktuTransaksi;
        try {
            Timestamp ts = rs.getTimestamp("timestamp");
            if (ts != null) {
                waktuTransaksi = ts.toLocalDateTime();
            } else {
                waktuTransaksi = LocalDateTime.now();
            }
        } catch (SQLException e) {
            waktuTransaksi = LocalDateTime.now();
        }

        // 4. Handle JENIS (Enum Conversion)
        String jenisStr = rs.getString("jenis");
        TransaksiMitra.JenisTransaksi jenisEnum = TransaksiMitra.JenisTransaksi.PENGELUARAN; // Default
        if (jenisStr != null) {
            try {
                jenisEnum = TransaksiMitra.JenisTransaksi.valueOf(jenisStr);
            } catch (IllegalArgumentException e) {
                // Ignore invalid enum, keep default
            }
        }

        // 5. Handle KETERANGAN
        String ket = rs.getString("keterangan");
        if (ket == null) ket = "-";

        // 6. Construct Objek Lengkap
        return new TransaksiMitra(
                rs.getInt("IdTransaksi"),
                lokasi,
                admin,
                rs.getString("namaMitraBisnis"),
                rs.getString("kategoriSampahTerjual"),
                rs.getBigDecimal("beratTotalKg"),
                rs.getBigDecimal("nilaiRupiahDidapat"),
                waktuTransaksi,
                rs.getString("status"),
                jenisEnum, // Parameter Baru
                ket        // Parameter Baru
        );
    }

    /**
     * Mengambil data berdasarkan Nama Mitra (Untuk Dashboard).
     * Filter berdasarkan Nama Mitra ATAU Keterangan (untuk menangkap Top Up).
     */
    public List<TransaksiMitra> getAllByNamaMitra(String namaMitra) {
        List<TransaksiMitra> listTrx = new ArrayList<>();

        // Query: Cari berdasarkan pemilik (namaMitraBisnis) ATAU jika nama ada di keterangan
        String sql = "SELECT * FROM transaksimitra WHERE namaMitraBisnis = ? OR keterangan LIKE ? ORDER BY IdTransaksi DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, namaMitra);
            stmt.setString(2, "%" + namaMitra + "%"); // Pencarian loose
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                listTrx.add(mapResultSetToTransaksi(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error get by Nama: " + e.getMessage());
            e.printStackTrace();
        }
        return listTrx;
    }

    /**
     * Mengambil semua data transaksi berdasarkan lokasi tertentu.
     */
    public List<TransaksiMitra> getAllByLokasi(int idLokasi) {
        List<TransaksiMitra> listTrx = new ArrayList<>();
        String sql = "SELECT * FROM transaksimitra WHERE titikPengumpulan = ? ORDER BY IdTransaksi DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idLokasi);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                listTrx.add(mapResultSetToTransaksi(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listTrx;
    }

    /**
     * Mengambil semua data transaksi (Global) untuk Admin.
     */
    public List<TransaksiMitra> getAll() {
        List<TransaksiMitra> listTrx = new ArrayList<>();
        String sql = "SELECT * FROM transaksimitra ORDER BY IdTransaksi DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                listTrx.add(mapResultSetToTransaksi(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listTrx;
    }

    /**
     * Mengambil satu transaksi berdasarkan ID.
     */
    public TransaksiMitra getById(int idTransaksi) {
        String sql = "SELECT * FROM transaksimitra WHERE IdTransaksi = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idTransaksi);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToTransaksi(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ========================================================================
    //                               UPDATE (EDIT)
    // ========================================================================

    /**
     * Mengupdate status pembayaran transaksi.
     */
    public boolean updateStatus(int idTransaksi, String newStatus) {
        String sql = "UPDATE transaksimitra SET status = ? WHERE IdTransaksi = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, idTransaksi);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating status: " + e.getMessage());
            return false;
        }
    }

    // ========================================================================
    //                               DELETE (REMOVE)
    // ========================================================================

    public boolean delete(int idTransaksi) {
        String sql = "DELETE FROM transaksimitra WHERE IdTransaksi = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idTransaksi);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}