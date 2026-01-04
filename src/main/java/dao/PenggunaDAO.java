package dao;

import models.Pengguna;
import util.DatabaseConnection;
// import util.HashingUtils; // Uncomment jika ingin menggunakan HashingUtils

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PenggunaDAO {

    public PenggunaDAO() {
        // Constructor kosong, koneksi diambil per method agar thread-safe
    }

    /**
     * Menyimpan pengguna baru ke dua tabel (akun & pengguna) secara atomik (Transaksi).
     */
    public boolean registerPenggunaBaru(Pengguna pengguna, String plainPassword) {
        String sqlAkun = "INSERT INTO akun (namaLengkap, alamat, NoTelepon, email, Username, hashedPassword) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlPengguna = "INSERT INTO pengguna (IdKartu, idAkun, nomorKartu, saldoPoin, saldo, tanggalDaftar) VALUES (?, ?, ?, ?, ?, ?)";

        // PERBAIKAN: Mengambil koneksi dari Instance Singleton
        Connection conn = DatabaseConnection.getInstance().getConnection();

        if (conn == null) {
            System.err.println("[PenggunaDAO] Gagal mendapatkan koneksi database!");
            return false;
        }

        try {
            // 1. Matikan auto-commit untuk memulai transaksi manual
            conn.setAutoCommit(false);

            // 2. Simpan data ke tabel 'akun' terlebih dahulu
            // Note: Password disimpan plain sesuai snippet kamu.
            // Jika ingin di-hash, ganti plainPassword dengan HashingUtils.hashPassword(plainPassword)
            try (PreparedStatement stmtAkun = conn.prepareStatement(sqlAkun, Statement.RETURN_GENERATED_KEYS)) {
                stmtAkun.setString(1, pengguna.getNamaLengkap());
                stmtAkun.setString(2, pengguna.getAlamat());
                stmtAkun.setString(3, pengguna.getNoTelepon());
                stmtAkun.setString(4, pengguna.getEmail());
                stmtAkun.setString(5, pengguna.getUsername());
                stmtAkun.setString(6, plainPassword);

                int affectedRows = stmtAkun.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Gagal membuat data akun, tidak ada baris yang terpengaruh.");
                }

                // 3. Ambil ID Akun yang baru saja dihasilkan (Auto Increment)
                try (ResultSet generatedKeys = stmtAkun.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newIdAkun = generatedKeys.getInt(1);

                        // 4. Simpan data ke tabel 'pengguna' menggunakan ID Akun yang didapat
                        try (PreparedStatement stmtPengguna = conn.prepareStatement(sqlPengguna)) {
                            stmtPengguna.setString(1, pengguna.getIdKartu());
                            stmtPengguna.setInt(2, newIdAkun);
                            stmtPengguna.setString(3, pengguna.getNomorKartu());
                            stmtPengguna.setBigDecimal(4, BigDecimal.ZERO);
                            stmtPengguna.setBigDecimal(5, BigDecimal.ZERO);
                            stmtPengguna.setObject(6, LocalDate.now());

                            stmtPengguna.executeUpdate();
                        }
                    } else {
                        throw new SQLException("Gagal mendapatkan ID Akun baru, tidak ada ID yang dikembalikan.");
                    }
                }
            }

            // 5. Jika semua langkah berhasil, simpan permanen (Commit)
            conn.commit();
            System.out.println("[PenggunaDAO] Berhasil mendaftarkan pengguna baru: " + pengguna.getUsername());
            return true;

        } catch (SQLException e) {
            // 6. Jika terjadi error, batalkan semua perubahan (Rollback)
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("[PenggunaDAO] Transaksi Gagal: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // 7. Bersihkan koneksi dan kembalikan auto-commit ke default
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    // conn.close(); // Opsional: Jangan di-close jika ingin koneksi tetap hidup (Singleton)
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Mengambil data pengguna berdasarkan ID Akun.
     */
    public Pengguna getPenggunaById(int idAkun) {
        String sql = "SELECT a.*, p.IdKartu, p.nomorKartu, p.saldoPoin, p.saldo, p.tanggalDaftar " +
                "FROM akun a JOIN pengguna p ON a.idAkun = p.idAkun WHERE a.idAkun = ?";

        // PERBAIKAN: Mengambil koneksi dari Instance Singleton
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAkun);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Pengguna(
                            rs.getInt("idAkun"),
                            rs.getString("namaLengkap"),
                            rs.getString("alamat"),
                            rs.getString("NoTelepon"),
                            rs.getString("email"),
                            rs.getString("Username"),
                            rs.getString("hashedPassword"),
                            true, // isActive assumed true
                            LocalDateTime.now(), // createdAt dummy placeholder
                            LocalDateTime.now(), // updatedAt dummy placeholder
                            rs.getString("IdKartu"),
                            rs.getString("nomorKartu"),
                            rs.getBigDecimal("saldoPoin"),
                            rs.getBigDecimal("saldo"),
                            rs.getObject("tanggalDaftar", LocalDate.class)
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Menghitung total seluruh pengguna aktif untuk Dashboard.
     */
    public int countTotalPengguna() {
        String sql = "SELECT COUNT(*) FROM pengguna";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Menghitung total registrasi baru pada bulan berjalan.
     */
    public int countRegistrasiBulanIni() {
        String sql = "SELECT COUNT(*) FROM pengguna WHERE MONTH(tanggalDaftar) = MONTH(CURRENT_DATE()) AND YEAR(tanggalDaftar) = YEAR(CURRENT_DATE())";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Menghitung jumlah akumulasi seluruh poin di sistem.
     */
    public BigDecimal sumTotalSaldoPoin() {
        String sql = "SELECT SUM(saldoPoin) FROM pengguna";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                BigDecimal res = rs.getBigDecimal(1);
                return res != null ? res : BigDecimal.ZERO;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    /**
     * Memperbarui saldo dan poin.
     */
    public boolean updateSaldoPoin(Pengguna pengguna) {
        String sql = "UPDATE pengguna SET saldoPoin = ?, saldo = ? WHERE IdKartu = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, pengguna.getSaldoPoin());
            stmt.setBigDecimal(2, pengguna.getSaldo());
            stmt.setString(3, pengguna.getIdKartu());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Memperbarui data profil pengguna (Tabel Akun).
     */
    public boolean updateDataPengguna(Pengguna p) {
        String sql = "UPDATE akun SET namaLengkap = ?, email = ?, NoTelepon = ?, Username = ?, hashedPassword = ? WHERE idAkun = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNamaLengkap());
            stmt.setString(2, p.getEmail());
            stmt.setString(3, p.getNoTelepon());
            stmt.setString(4, p.getUsername());
            stmt.setString(5, p.getHashedPassword()); // Pastikan controller mengirim password yang benar
            stmt.setInt(6, p.getIdAkun());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}