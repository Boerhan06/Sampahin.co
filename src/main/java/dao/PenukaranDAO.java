package dao;

import models.Pengguna;
import models.Penukaran;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PenukaranDAO {

    private Connection connection;
    private PenggunaDAO penggunaDAO;

    public PenukaranDAO() {
        // PERBAIKAN: Konsisten menggunakan getInstance().getConnection()
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.penggunaDAO = new PenggunaDAO();
    }

    /**
     * Menyimpan data transaksi penukaran poin ke database.
     */
    public boolean save(Penukaran penukaran) {
        String sql = "INSERT INTO penukaran (idAkun, poinDitukar, nilaiRupiah, keterangan, timestamp, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, penukaran.getPengguna().getIdAkun());
            stmt.setBigDecimal(2, penukaran.getPoinDitukar());
            stmt.setBigDecimal(3, penukaran.getNilaiRupiah());
            stmt.setString(4, penukaran.getKeterangan());
            stmt.setObject(5, penukaran.getTimestamp());
            stmt.setString(6, penukaran.getStatus());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[PenukaranDAO] Gagal save transaksi: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Helper Method: Memetakan baris database ke objek Java Penukaran.
     */
    private Penukaran mapResultSetToPenukaran(ResultSet rs) throws SQLException {
        // Ambil ID Akun dari hasil query penukaran
        int idAkun = rs.getInt("idAkun");

        // PENTING: Pastikan PenggunaDAO.getPenggunaById mengembalikan models.Pengguna
        Pengguna pengguna = penggunaDAO.getPenggunaById(idAkun);

        return new Penukaran(
                rs.getInt("id_penukaran"),
                pengguna,
                rs.getBigDecimal("poinDitukar"),
                rs.getBigDecimal("nilaiRupiah"),
                rs.getString("keterangan"),
                rs.getObject("timestamp", LocalDateTime.class),
                rs.getString("status")
        );
    }

    /**
     * Mencari satu data penukaran berdasarkan ID Primary Key.
     */
    public Penukaran getById(int id) {
        String sql = "SELECT * FROM penukaran WHERE id_penukaran = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPenukaran(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mengambil riwayat penukaran poin milik satu pengguna tertentu.
     */
    public List<Penukaran> getAllByPengguna(int idAkun) {
        String sql = "SELECT * FROM penukaran WHERE idAkun = ? ORDER BY timestamp DESC";
        List<Penukaran> listPenukaran = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAkun);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    listPenukaran.add(mapResultSetToPenukaran(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[PenukaranDAO] Gagal getAllByPengguna: " + e.getMessage());
            e.printStackTrace();
        }
        return listPenukaran;
    }

    /**
     * Memperbarui status penukaran (misal: 'BERHASIL', 'PENDING', atau 'BATAL').
     */
    public boolean updateStatus(int idPenukaran, String newStatus) {
        String sql = "UPDATE penukaran SET status = ?, timestamp = ? WHERE id_penukaran = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setObject(2, LocalDateTime.now());
            stmt.setInt(3, idPenukaran);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PenukaranDAO] Gagal update status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}