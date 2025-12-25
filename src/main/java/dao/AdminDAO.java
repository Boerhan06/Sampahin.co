package dao;

import models.Admin;
import util.DatabaseConnection;
import util.HashingUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    private Connection connection;

    public AdminDAO() {
        this.connection = DatabaseConnection.getInstance();
    }

    public boolean addAdmin(Admin admin) {
        String sql = "INSERT INTO admin (nama_lengkap, alamat, no_telepon, email, username, password, is_active, created_at, updated_at, id_admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, admin.getNamaLengkap());
            stmt.setString(2, admin.getAlamat());
            stmt.setString(3, admin.getNoTelepon());
            stmt.setString(4, admin.getEmail());
            stmt.setString(5, admin.getUsername());
            stmt.setString(6, admin.getHashedPassword());
            stmt.setBoolean(7, true);
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(10, admin.getIdAdmin());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean save(Admin adminBaru, String plainPassword) {
        String hashedPassword = HashingUtils.hashPassword(plainPassword);
        adminBaru.setHashedPassword(hashedPassword);
        return addAdmin(adminBaru);
    }

    public Admin getAdminByUsername(String username) {
        String sql = "SELECT * FROM admin WHERE username = ?";
        Admin admin = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                admin = mapResultSetToAdmin(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }

    public Admin getAdminById(int id) {
        String sql = "SELECT * FROM admin WHERE id_akun = ?";
        Admin admin = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                admin = mapResultSetToAdmin(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }

    public List<Admin> getAllAdmins() {
        String sql = "SELECT * FROM admin";
        List<Admin> adminList = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                adminList.add(mapResultSetToAdmin(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminList;
    }

    public boolean update(Admin admin) {
        String sql = "UPDATE admin SET nama_lengkap = ?, alamat = ?, no_telepon = ?, email = ?, username = ?, is_active = ?, updated_at = ?, id_admin = ? WHERE id_akun = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, admin.getNamaLengkap());
            stmt.setString(2, admin.getAlamat());
            stmt.setString(3, admin.getNoTelepon());
            stmt.setString(4, admin.getEmail());
            stmt.setString(5, admin.getUsername());
            stmt.setBoolean(6, admin.getIsActive());
            stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(8, admin.getIdAdmin());
            stmt.setInt(9, admin.getIdAkun());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int idAkun) {
        String sql = "DELETE FROM admin WHERE id_akun = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAkun);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Admin mapResultSetToAdmin(ResultSet rs) throws SQLException {
        return new Admin(
                rs.getInt("id_akun"),
                rs.getString("nama_lengkap"),
                rs.getString("alamat"),
                rs.getString("no_telepon"),
                rs.getString("email"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getBoolean("is_active"),
                rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                rs.getString("id_admin")
        );
    }
}