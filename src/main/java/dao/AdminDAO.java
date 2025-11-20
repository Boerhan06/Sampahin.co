package dao;

import models.Admin;
import util.DatabaseConnection;
import util.HashingUtils;

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
        // PERBAIKAN: Nama kolom disesuaikan dengan Database Anda (camelCase)
        // Dan 'hashed_password' diganti ke 'plainPassword' sesuai kolom DB Anda
        String sql = "INSERT INTO admin (namaLengkap, alamat, noTelepon, email, username, plainPassword, " +
                "is_active, created_at, updated_at, idAdmin) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Kita tetap Hash passwordnya untuk keamanan, tapi disimpan di kolom plainPassword
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

    // --- READ (R) - Single by Username ---
    public Admin getAdminByUsername(String username) {
         this.connection = DatabaseConnection.getInstance();
        String sql = "SELECT * FROM admin WHERE username = ?";
        Admin admin = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // PERBAIKAN: Mengambil data menggunakan nama kolom yang benar dari DB Anda
                admin = new Admin(
                        rs.getInt("id_akun"), // Pastikan kolom PK ini benar (id_akun atau id)
                        rs.getString("namaLengkap"), // Dari nama_lengkap -> namaLengkap
                        rs.getString("alamat"),
                        rs.getString("noTelepon"),   // Dari no_telepon -> noTelepon
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("plainPassword"), // Dari hashed_password -> plainPassword
                        rs.getBoolean("is_active"),
                        rs.getObject("created_at", LocalDateTime.class),
                        rs.getObject("updated_at", LocalDateTime.class),
                        rs.getString("idAdmin")      // Dari id_admin -> idAdmin
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin; 
    }
    
    // --- READ (R) - Single by ID ---
    public Admin getAdminById(int id) {
        String sql = "SELECT * FROM admin WHERE id_akun = ?";
        Admin admin = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                 admin = new Admin(
                        rs.getInt("id_akun"), 
                        rs.getString("namaLengkap"), // Update nama kolom
                        rs.getString("alamat"),
                        rs.getString("noTelepon"),   // Update nama kolom
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("plainPassword"), // Update nama kolom
                        rs.getBoolean("is_active"),
                        rs.getObject("created_at", LocalDateTime.class),
                        rs.getObject("updated_at", LocalDateTime.class),
                        rs.getString("idAdmin")      // Update nama kolom
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
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
                        rs.getString("namaLengkap"), // Update nama kolom
                        rs.getString("alamat"),
                        rs.getString("noTelepon"),   // Update nama kolom
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("plainPassword"), // Update nama kolom
                        rs.getBoolean("is_active"),
                        rs.getObject("created_at", LocalDateTime.class),
                        rs.getObject("updated_at", LocalDateTime.class),
                        rs.getString("idAdmin")      // Update nama kolom
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
        // PERBAIKAN: Update query SQL dengan nama kolom yang benar
        String sql = "UPDATE admin SET namaLengkap = ?, alamat = ?, noTelepon = ?, email = ?, " +
                "username = ?, is_active = ?, updated_at = ?, idAdmin = ? " +
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
            stmt.setInt(9, admin.getIdAkun()); 

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