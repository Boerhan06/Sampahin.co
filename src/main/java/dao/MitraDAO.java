package dao;

import models.Mitra;
import models.TitikPengumpulan;
import util.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) untuk entitas Mitra.
 * Menangani seluruh operasi CRUD ke tabel 'mitra' di database.
 */
public class MitraDAO {

    private Connection connection;
    private TitikPengumpulanDAO titikPengumpulanDAO;

    public MitraDAO() {
        // Mengambil koneksi dari Singleton DatabaseConnection
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.titikPengumpulanDAO = new TitikPengumpulanDAO();
    }

    /**
     * Memastikan koneksi tetap terbuka sebelum melakukan operasi.
     * Mencegah error "No operations allowed after connection closed".
     */
    private void ensureConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DatabaseConnection.getInstance().getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =================================================================================
    //  CREATE (C)
    // =================================================================================

    /**
     * Menyimpan data mitra baru ke database dengan password yang di-hash.
     */
    public boolean save(Mitra mitra, String plainPassword) {
        ensureConnection();
        // Menambahkan kolom 'saldo' ke dalam INSERT
        String sql = "INSERT INTO mitra (nama_lengkap, alamat, no_telepon, email, username, hashed_password, " +
                "is_active, created_at, updated_at, id_mitra, id_lokasi_tugas, saldo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Menggunakan BCrypt untuk keamanan password
            String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

            stmt.setString(1, mitra.getNamaLengkap());
            stmt.setString(2, mitra.getAlamat());
            stmt.setString(3, mitra.getNoTelepon());
            stmt.setString(4, mitra.getEmail());
            stmt.setString(5, mitra.getUsername());
            stmt.setString(6, hashedPassword);
            stmt.setBoolean(7, true); // Default aktif
            stmt.setObject(8, LocalDateTime.now());
            stmt.setObject(9, LocalDateTime.now());
            stmt.setString(10, mitra.getIdMitra());

            // Set Foreign Key Lokasi Tugas
            if (mitra.getLokasiTugas() != null) {
                stmt.setInt(11, mitra.getLokasiTugas().getIdLokasi());
            } else {
                stmt.setNull(11, java.sql.Types.INTEGER);
            }

            // Simpan Saldo Awal (Gunakan BigDecimal.ZERO jika null)
            BigDecimal saldoAwal = (mitra.getSaldo() != null) ? mitra.getSaldo() : BigDecimal.ZERO;
            stmt.setBigDecimal(12, saldoAwal);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error saat menyimpan mitra: " + e.getMessage());
            return false;
        }
    }

    // =================================================================================
    //  READ (R)
    // =================================================================================

    /**
     * Mengambil daftar seluruh mitra.
     */
    public List<Mitra> getAllMitra() {
        ensureConnection();
        String sql = "SELECT * FROM mitra ORDER BY created_at DESC";
        List<Mitra> mitraList = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                mitraList.add(mapResultSetToMitra(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mitraList;
    }

    /**
     * Mencari mitra berdasarkan username (digunakan untuk login).
     */
    public Mitra getMitraByUsername(String username) {
        ensureConnection();
        String sql = "SELECT * FROM mitra WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMitra(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mencari mitra berdasarkan ID Akun (Primary Key).
     */
    public Mitra getMitraById(int id) {
        ensureConnection();
        String sql = "SELECT * FROM mitra WHERE id_akun = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMitra(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // =================================================================================
    //  UPDATE (U)
    // =================================================================================

    /**
     * Memperbarui data profil mitra.
     */
    public boolean update(Mitra mitra) {
        ensureConnection();
        String sql = "UPDATE mitra SET nama_lengkap = ?, alamat = ?, no_telepon = ?, email = ?, " +
                "username = ?, is_active = ?, updated_at = ?, id_mitra = ?, id_lokasi_tugas = ?, saldo = ? " +
                "WHERE id_akun = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, mitra.getNamaLengkap());
            stmt.setString(2, mitra.getAlamat());
            stmt.setString(3, mitra.getNoTelepon());
            stmt.setString(4, mitra.getEmail());
            stmt.setString(5, mitra.getUsername());
            stmt.setBoolean(6, mitra.getIsActive());
            stmt.setObject(7, LocalDateTime.now());
            stmt.setString(8, mitra.getIdMitra());

            if (mitra.getLokasiTugas() != null) {
                stmt.setInt(9, mitra.getLokasiTugas().getIdLokasi());
            } else {
                stmt.setNull(9, java.sql.Types.INTEGER);
            }

            stmt.setBigDecimal(10, mitra.getSaldo());

            // PENTING: Menggunakan getIdAkun() (Int) sebagai referensi update
            stmt.setInt(11, mitra.getIdAkun());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Memperbarui password mitra.
     */
    public boolean updatePassword(String username, String hashedPass) {
        ensureConnection();
        String sql = "UPDATE mitra SET hashed_password = ?, updated_at = NOW() WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hashedPass);
            stmt.setString(2, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method untuk update saldo.
     * @param idAkun : ID Database (Integer), BUKAN ID String (misal "M-001")
     * @param newSaldo : Nilai saldo baru
     */
    public boolean updateSaldo(int idAkun, BigDecimal newSaldo) {
        ensureConnection();
        String sql = "UPDATE mitra SET saldo = ?, updated_at = NOW() WHERE id_akun = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, newSaldo);
            stmt.setInt(2, idAkun);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal update saldo id_akun=" + idAkun + ": " + e.getMessage());
            return false;
        }
    }

    // =================================================================================
    //  DELETE (D)
    // =================================================================================

    public boolean delete(int idAkun) {
        ensureConnection();
        String sql = "DELETE FROM mitra WHERE id_akun = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAkun);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =================================================================================
    //  HELPER METHODS
    // =================================================================================

    /**
     * Helper untuk memetakan baris ResultSet menjadi objek Mitra.
     */
    private Mitra mapResultSetToMitra(ResultSet rs) throws SQLException {
        int idLokasi = rs.getInt("id_lokasi_tugas");
        TitikPengumpulan lokasi = null;

        // Ambil objek lokasi jika ID tersedia
        if (idLokasi != 0) {
            lokasi = titikPengumpulanDAO.getById(idLokasi);
        }

        // Ambil saldo dengan penanganan null agar aman
        BigDecimal saldo = rs.getBigDecimal("saldo");
        if (saldo == null) {
            saldo = BigDecimal.ZERO;
        }

        return new Mitra(
                rs.getInt("id_akun"),          // Parameter 1: ID Akun (Integer)
                rs.getString("nama_lengkap"),
                rs.getString("alamat"),
                rs.getString("no_telepon"),
                rs.getString("email"),
                rs.getString("username"),
                rs.getString("hashed_password"),
                rs.getBoolean("is_active"),
                rs.getObject("created_at", LocalDateTime.class),
                rs.getObject("updated_at", LocalDateTime.class),
                rs.getString("id_mitra"),      // Parameter 11: ID Mitra (String)
                lokasi,
                saldo                          // Parameter 13: Saldo
        );
    }
}