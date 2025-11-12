package com.sampahin.dao;

import com.sampahin.model.Pengguna;
import com.sampahin.util.DatabaseConnection;
import com.sampahin.util.HashingUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

//CRUD ke database yaw
public class PenggunaDAO {
    private Connection connection;
    public PenggunaDAO() {
        this.connection = DatabaseConnection.getInstance();
    }


    // --- CREATE (C)
    public boolean registerPengguna(Pengguna pengguna) {
        String hashedPassword = pengguna.getHashedPassword();

        String sql = "INSERT INTO pengguna (nama_lengkap, alamat, no_telepon, email, username, hashed_password, " +
                "is_active, created_at, updated_at, id_kartu, nomor_kartu, saldo_poin, saldo_rupiah, tanggal_daftar) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pengguna.getNamaLengkap());
            stmt.setString(2, pengguna.getAlamat());
            stmt.setString(3, pengguna.getNoTelepon());
            stmt.setString(4, pengguna.getEmail());
            stmt.setString(5, pengguna.getUsername());
            stmt.setString(6, hashedPassword); // Simpan HASH
            stmt.setBoolean(7, true); // (isActive)
            stmt.setObject(8, LocalDateTime.now()); // (created_at)
            stmt.setObject(9, LocalDateTime.now()); // (updated_at)
            stmt.setString(10, pengguna.getIdKartu());
            stmt.setString(11, pengguna.getNomorKartu());
            stmt.setBigDecimal(12, BigDecimal.ZERO); // (saldo_poin awal)
            stmt.setBigDecimal(13, BigDecimal.ZERO); // (saldo_rupiah awal)
            stmt.setObject(14, LocalDate.now()); // (tanggal_daftar)

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // --- READ (R)
    public Pengguna getPenggunaByUsername(String username) {
        String sql = "SELECT * FROM pengguna WHERE username = ?";
        Pengguna pengguna = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                pengguna = new Pengguna(
                        rs.getInt("id_akun"),
                        rs.getString("nama_lengkap"),
                        rs.getString("alamat"),
                        rs.getString("no_telepon"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("hashed_password"), // Ambil hash, BUKAN plain text
                        rs.getBoolean("is_active"),
                        rs.getObject("created_at", LocalDateTime.class),
                        rs.getObject("updated_at", LocalDateTime.class),
                        rs.getString("id_kartu"),
                        rs.getString("nomor_kartu"),
                        rs.getBigDecimal("saldo_poin"),
                        rs.getBigDecimal("saldo_rupiah"),
                        rs.getObject("tanggal_daftar", LocalDate.class)
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pengguna; // return null jika tidak ditemukan
    }


    // --- UPDATE (U) ---
    public boolean updateSaldoPoin(Pengguna pengguna) {
        String sql = "UPDATE pengguna SET saldo_poin = ?, saldo_rupiah = ?, updated_at = ? WHERE id_akun = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, pengguna.getSaldoPoin());
            stmt.setBigDecimal(2, pengguna.getSaldo());
            stmt.setObject(3, LocalDateTime.now());
            stmt.setInt(4, pengguna.getIdAkun());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}