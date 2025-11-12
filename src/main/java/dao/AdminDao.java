package com.sampahin.dao;

import com.sampahin.model.Admin;
import com.sampahin.util.DatabaseConnection;
import com.sampahin.util.HashingUtils; // Kita butuh ini untuk C (Create)

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    private Connection connection;

    public AdminDAO() {
        this.connection = DatabaseConnection.getInstance();
    }

    // --- CREATE (C) ---
    public boolean save(Admin admin, String plainPassword) {
        String sql = "INSERT INTO admin (nama_lengkap, alamat, no_telepon, email, username, hashed_password, " +
                "is_active, created_at, updated_at, id_admin) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Hash password di sini
            String hashedPassword = HashingUtils.hashPassword(plainPassword);

            stmt.setString(1, admin.getNamaLengkap());
            stmt.setString(2, admin.getAlamat());
            stmt.setString(3, admin.getNoTelepon());
            stmt.setString(4, admin.getEmail());
            stmt.setString(5, admin.getUsername());
            stmt.setString(6, hashedPassword);
            stmt.setBoolean(7, true);
            stmt.setObject(8, LocalDateTime.now());
            stmt.setObject(9, LocalDateTime.now());
            stmt.setString(10, admin.getIdAdmin());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- READ (R) ---
    public Admin getAdminByUsername(String username) {
        String sql = "SELECT * FROM admin WHERE username = ?";
        Admin admin = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                admin = new Admin(
                        rs.getInt("id_akun"), // Asumsi ada kolom id_akun (PK)
                        rs.getString("nama_lengkap"),
                        rs.getString("alamat"),
                        rs.getString("no_telepon"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("hashed_password"),
                        rs.getBoolean("is_active"),
                        rs.getObject("created_at", LocalDateTime.class),
                        rs.getObject("updated_at", LocalDateTime.class),
                        rs.getString("id_admin")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin; // null jika tidak ditemukan
    }


    // --- READ (R) - All ---
    public List<Admin> getAllAdmins() {
        String sql = "SELECT * FROM admin";
        List<Admin> adminList = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Admin admin = new Admin(
                        rs.getInt("id_akun"),
                        rs.getString("nama_lengkap"),
                        rs.getString("alamat"),
                        rs.getString("no_telepon"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("hashed_password"),
                        rs.getBoolean("is_active"),
                        rs.getObject("created_at", LocalDateTime.class),
                        rs.getObject("updated_at", LocalDateTime.class),
                        rs.getString("id_admin")
                );
                adminList.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminList;
    }


    // --- UPDATE (U) ---
    public boolean update(Admin admin) {
        String sql = "UPDATE admin SET nama_lengkap = ?, alamat = ?, no_telepon = ?, email = ?, " +
                "username = ?, is_active = ?, updated_at = ?, id_admin = ? " +
                "WHERE id_akun = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, admin.getNamaLengkap());
            stmt.setString(2, admin.getAlamat());
            stmt.setString(3, admin.getNoTelepon());
            stmt.setString(4, admin.getEmail());
            stmt.setString(5, admin.getUsername());
            stmt.setBoolean(6, admin.getIsActive());
            stmt.setObject(7, LocalDateTime.now());
            stmt.setString(8, admin.getIdAdmin());
            stmt.setInt(9, admin.getIdAkun()); // Klausul WHERE

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // --- DELETE (D) ---
    public boolean delete(int idAkun) {
        String sql = "DELETE FROM admin WHERE id_akun = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAkun);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}