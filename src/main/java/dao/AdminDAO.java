package dao;

import models.Admin;
import util.DatabaseConnection;
import util.HashingUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) untuk entitas Admin.
 * Menangani seluruh operasi CRUD ke tabel 'admin' di database,
 * termasuk penanganan Foto Profil (BLOB).
 */
public class AdminDAO {
    private Connection connection;

    public AdminDAO() {
        // Mengambil connection dari wrapper Singleton dengan .getConnection()
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Memastikan koneksi tetap aktif sebelum melakukan operasi database.
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

    // ========================================================================
    //  METHOD KHUSUS: UPDATE PROFIL DARI FORM EDIT PROFIL
    // ========================================================================

    /**
     * Memperbarui profil admin secara parsial (Nama, Username, Password, dan Foto).
     * @param newPhoto byte array gambar (bisa null jika tidak ada perubahan foto)
     */
    public boolean updateProfilePartial(int idAkun, String nama, String username, String plainPassword, byte[] newPhoto) {
        ensureConnection();
        StringBuilder sql = new StringBuilder("UPDATE admin SET nama_lengkap = ?, username = ?, updated_at = ?");

        boolean isPasswordChange = (plainPassword != null && !plainPassword.trim().isEmpty());
        boolean isPhotoChange = (newPhoto != null && newPhoto.length > 0);

        if (isPasswordChange) {
            sql.append(", password = ?");
        }

        if (isPhotoChange) {
            sql.append(", foto_profil = ?");
        }

        sql.append(" WHERE id_akun = ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            stmt.setString(1, nama);
            stmt.setString(2, username);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

            int paramIndex = 4;

            if (isPasswordChange) {
                String hashedPassword = HashingUtils.hashPassword(plainPassword);
                stmt.setString(paramIndex++, hashedPassword);
            }

            if (isPhotoChange) {
                stmt.setBytes(paramIndex++, newPhoto);
            }

            stmt.setInt(paramIndex, idAkun);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Memperbarui password admin (Digunakan untuk fitur Lupa Password).
     */
    public boolean updatePassword(String username, String hashedPass) {
        ensureConnection();
        String sql = "UPDATE admin SET password = ?, updated_at = NOW() WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hashedPass);
            stmt.setString(2, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ========================================================================
    //  METHOD CRUD DASAR
    // ========================================================================

    public boolean addAdmin(Admin admin) {
        ensureConnection();
        // Menambahkan kolom foto_profil ke INSERT
        String sql = "INSERT INTO admin (nama_lengkap, alamat, no_telepon, email, username, password, is_active, created_at, updated_at, id_admin, foto_profil) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

            // Set foto profil (bisa null)
            stmt.setBytes(11, admin.getFotoProfil());

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
        ensureConnection();
        String sql = "SELECT * FROM admin WHERE username = ?";
        Admin admin = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    admin = mapResultSetToAdmin(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }

    public Admin getAdminById(int id) {
        ensureConnection();
        String sql = "SELECT * FROM admin WHERE id_akun = ?";
        Admin admin = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    admin = mapResultSetToAdmin(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }

    public List<Admin> getAllAdmins() {
        ensureConnection();
        String sql = "SELECT * FROM admin";
        List<Admin> adminList = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                adminList.add(mapResultSetToAdmin(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminList;
    }

    public boolean update(Admin admin) {
        ensureConnection();
        // Update standar termasuk foto_profil
        String sql = "UPDATE admin SET nama_lengkap = ?, alamat = ?, no_telepon = ?, email = ?, username = ?, is_active = ?, updated_at = ?, id_admin = ?, foto_profil = ? WHERE id_akun = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, admin.getNamaLengkap());
            stmt.setString(2, admin.getAlamat());
            stmt.setString(3, admin.getNoTelepon());
            stmt.setString(4, admin.getEmail());
            stmt.setString(5, admin.getUsername());
            stmt.setBoolean(6, admin.getIsActive());
            stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(8, admin.getIdAdmin());

            // Set foto profil
            stmt.setBytes(9, admin.getFotoProfil());

            stmt.setInt(10, admin.getIdAkun());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int idAkun) {
        ensureConnection();
        String sql = "DELETE FROM admin WHERE id_akun = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAkun);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Helper untuk memetakan ResultSet ke objek Admin.
     */
    private Admin mapResultSetToAdmin(ResultSet rs) throws SQLException {
        // Ambil data BLOB foto
        byte[] fotoBlob = rs.getBytes("foto_profil");

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
                rs.getString("id_admin"),
                fotoBlob // Masukkan ke Constructor Admin yang sudah direvisi
        );
    }
}