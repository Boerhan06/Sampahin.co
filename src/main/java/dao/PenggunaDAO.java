package dao;

import models.Pengguna;
import util.DatabaseConnection;
import util.HashingUtils; // Pastikan ini diimport untuk hashing

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PenggunaDAO {
    private Connection connection;

    public PenggunaDAO() {
        this.connection = DatabaseConnection.getInstance();
    }

    // --- CREATE (C)
    public boolean registerPengguna(Pengguna pengguna) {
        String sql = "INSERT INTO pengguna (namaLengkap, alamat, noTelepon, email, username, plainPassword, " +
                "isActive, createdAt, updatedAt, idKartu, nomorKartu, saldoPoin, saldoRupiah, tanggalDaftar) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Hash password agar aman (meskipun nama kolomnya plainPassword)
            String hashedPassword = HashingUtils.hashPassword(pengguna.getHashedPassword());

            stmt.setString(1, pengguna.getNamaLengkap());
            stmt.setString(2, pengguna.getAlamat());
            stmt.setString(3, pengguna.getNoTelepon());
            stmt.setString(4, pengguna.getEmail());
            stmt.setString(5, pengguna.getUsername());
            stmt.setString(6, hashedPassword); 
            stmt.setBoolean(7, true); // isActive
            stmt.setObject(8, LocalDateTime.now()); // createdAt
            stmt.setObject(9, LocalDateTime.now()); // updatedAt
            stmt.setString(10, pengguna.getIdKartu());
            stmt.setString(11, pengguna.getNomorKartu());
            stmt.setBigDecimal(12, BigDecimal.ZERO); // saldoPoin
            stmt.setBigDecimal(13, BigDecimal.ZERO); // saldoRupiah
            stmt.setObject(14, LocalDate.now()); // tanggalDaftar

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
                        rs.getInt("id_akun"), // PK biasanya tetap snake_case atau id
                        rs.getString("namaLengkap"), // GANTI: nama_lengkap -> namaLengkap
                        rs.getString("alamat"),
                        rs.getString("noTelepon"),   // GANTI: no_telepon -> noTelepon
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("plainPassword"), // GANTI: hashed_password -> plainPassword
                        rs.getBoolean("isActive"),     // GANTI: is_active -> isActive
                        rs.getObject("createdAt", LocalDateTime.class), // GANTI: created_at -> createdAt
                        rs.getObject("updatedAt", LocalDateTime.class), // GANTI: updated_at -> updatedAt
                        rs.getString("idKartu"),       // GANTI: id_kartu -> idKartu
                        rs.getString("nomorKartu"),    // GANTI: nomor_kartu -> nomorKartu
                        rs.getBigDecimal("saldoPoin"), // GANTI: saldo_poin -> saldoPoin
                        rs.getBigDecimal("saldoRupiah"), // GANTI: saldo_rupiah -> saldoRupiah
                        rs.getObject("tanggalDaftar", LocalDate.class) // GANTI: tanggal_daftar -> tanggalDaftar
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pengguna; 
    }

    // --- READ (R) - Single by ID
    public Pengguna getPenggunaById(int id) {
        String sql = "SELECT * FROM pengguna WHERE id_akun = ?";
        Pengguna pengguna = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                pengguna = new Pengguna(
                        rs.getInt("id_akun"),
                        rs.getString("namaLengkap"),
                        rs.getString("alamat"),
                        rs.getString("noTelepon"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("plainPassword"),
                        rs.getBoolean("isActive"),
                        rs.getObject("createdAt", LocalDateTime.class),
                        rs.getObject("updatedAt", LocalDateTime.class),
                        rs.getString("idKartu"),
                        rs.getString("nomorKartu"),
                        rs.getBigDecimal("saldoPoin"),
                        rs.getBigDecimal("saldoRupiah"),
                        rs.getObject("tanggalDaftar", LocalDate.class)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pengguna;
    }


    // --- UPDATE (U) ---
    public boolean updateSaldoPoin(Pengguna pengguna) {
        // Sesuaikan query update dengan nama kolom camelCase
        String sql = "UPDATE pengguna SET saldoPoin = ?, saldoRupiah = ?, updatedAt = ? WHERE id_akun = ?";

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