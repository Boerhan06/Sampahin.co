package com.sampahin.dao;

import com.sampahin.model.Penukaran;
import com.sampahin.model.Pengguna;
import com.sampahin.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class PenukaranDAO {

    private Connection connection;
    private PenggunaDAO penggunaDAO; // <-- Dependensi ke DAO lain

    public PenukaranDAO() {
        this.connection = DatabaseConnection.getInstance();
        this.penggunaDAO = new PenggunaDAO(); // Inisialisasi DAO lain
    }


    // --- CREATE (C) ---
    public boolean save(Penukaran penukaran) {
        String sql = "INSERT INTO penukaran (id_pengguna, poin_ditukar, nilai_rupiah, keterangan, timestamp, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Ambil ID dari objek Pengguna
            stmt.setInt(1, penukaran.getPengguna().getIdAkun());
            stmt.setBigDecimal(2, penukaran.getPoinDitukar());
            stmt.setBigDecimal(3, penukaran.getNilaiRupiah());
            stmt.setString(4, penukaran.getKeterangan());
            stmt.setObject(5, penukaran.getTimestamp());
            stmt.setString(6, penukaran.getStatus());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // --- READ (R) - Helper Method ---
    private Penukaran mapResultSetToPenukaran(ResultSet rs) throws SQLException {
        // 1. Ambil Foreign Key (ID Pengguna)
        int idPengguna = rs.getInt("id_pengguna");

        // 2. Gunakan PenggunaDAO untuk mengambil objek Pengguna lengkap
        Pengguna pengguna = penggunaDAO.getPenggunaById(idPengguna); // Asumsi Anda punya/buat method ini

        // 3. Buat objek Penukaran lengkap
        return new Penukaran(
                rs.getInt("id_penukaran"),
                pengguna, // Masukkan objek Pengguna, bukan cuma ID
                rs.getBigDecimal("poin_ditukar"),
                rs.getBigDecimal("nilai_rupiah"),
                rs.getString("keterangan"),
                rs.getObject("timestamp", LocalDateTime.class),
                rs.getString("status")
        );
    }


    // --- READ (R) - Single by ID ---
    public Penukaran getById(int id) {
        String sql = "SELECT * FROM penukaran WHERE id_penukaran = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToPenukaran(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // --- READ (R) - All by Pengguna (Paling Berguna) ---
    public List<Penukaran> getAllByPengguna(int idPengguna) {
        String sql = "SELECT * FROM penukaran WHERE id_pengguna = ? ORDER BY timestamp DESC";
        List<Penukaran> listPenukaran = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPengguna);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                listPenukaran.add(mapResultSetToPenukaran(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listPenukaran;
    }

    // --- UPDATE (U) ---
    public boolean updateStatus(int idPenukaran, String newStatus) {
        String sql = "UPDATE penukaran SET status = ?, timestamp = ? WHERE id_penukaran = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setObject(2, LocalDateTime.now()); // Update timestamp-nya juga
            stmt.setInt(3, idPenukaran);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}