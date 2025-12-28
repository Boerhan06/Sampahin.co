package dao;

import models.TitikPengumpulan;
import models.Mitra;
import util.DatabaseConnection;
import util.HashingUtils;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MitraDAO {

    private Connection connection;
    private TitikPengumpulanDAO titikPengumpulanDAO;

    public MitraDAO() {
        this.connection = DatabaseConnection.getInstance();
        this.titikPengumpulanDAO = new TitikPengumpulanDAO();
    }

    // --- CREATE (C) ---
    public boolean save(Mitra mitra, String plainPassword) {
        // PERBAIKAN: Nama kolom disesuaikan dengan SQL Baru
        String sql = "INSERT INTO mitra (nama_lengkap, alamat, no_telepon, email, username, hashed_password, " +
                "is_active, created_at, updated_at, id_mitra, id_lokasi_tugas) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String hashedPassword = HashingUtils.hashPassword(plainPassword);

            stmt.setString(1, mitra.getNamaLengkap());
            stmt.setString(2, mitra.getAlamat());
            stmt.setString(3, mitra.getNoTelepon());
            stmt.setString(4, mitra.getEmail());
            stmt.setString(5, mitra.getUsername());
            stmt.setString(6, hashedPassword);
            stmt.setBoolean(7, true);
            stmt.setObject(8, LocalDateTime.now());
            stmt.setObject(9, LocalDateTime.now());
            stmt.setString(10, mitra.getIdMitra());

            // Handle Lokasi Tugas (Cek Null)
            if (mitra.getLokasiTugas() != null) {
                stmt.setInt(11, mitra.getLokasiTugas().getIdLokasi());
            } else {
                stmt.setNull(11, java.sql.Types.INTEGER);
            }

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- READ (R) - All ---
    public List<Mitra> getAllMitra() {
        String sql = "SELECT * FROM mitra ORDER BY created_at DESC";
        List<Mitra> mitraList = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                // Ambil Object TitikPengumpulan berdasarkan ID (Foreign Key)
                int idLokasi = rs.getInt("id_lokasi_tugas");
                TitikPengumpulan lokasi = titikPengumpulanDAO.getById(idLokasi);

                // Mapping ResultSet ke Object Mitra
                Mitra mitra = new Mitra(
                        rs.getInt("id_akun"),          // Primary Key Auto Increment
                        rs.getString("nama_lengkap"),
                        rs.getString("alamat"),
                        rs.getString("no_telepon"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("hashed_password"),
                        rs.getBoolean("is_active"),
                        rs.getObject("created_at", LocalDateTime.class),
                        rs.getObject("updated_at", LocalDateTime.class),
                        rs.getString("id_mitra"),      // Kode String (M-001)
                        lokasi                         // Object Lokasi
                );
                mitraList.add(mitra);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mitraList;
    }

    // --- READ (R) - Single by Username ---
    public Mitra getMitraByUsername(String username) {
        String sql = "SELECT * FROM mitra WHERE username = ?";
        return getMitraByQuery(sql, username);
    }

    // --- READ (R) - Single by ID ---
    public Mitra getMitraById(int id) {
        String sql = "SELECT * FROM mitra WHERE id_akun = ?";
        return getMitraByQuery(sql, id);
    }

    // Helper method biar codingan tidak berulang-ulang
    private Mitra getMitraByQuery(String sql, Object param) {
        Mitra mitra = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (param instanceof Integer) stmt.setInt(1, (Integer) param);
            else stmt.setString(1, (String) param);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int idLokasi = rs.getInt("id_lokasi_tugas");
                TitikPengumpulan lokasi = titikPengumpulanDAO.getById(idLokasi);

                mitra = new Mitra(
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
                        rs.getString("id_mitra"),
                        lokasi
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mitra;
    }

    // --- UPDATE (U) ---
    public boolean update(Mitra mitra) {
        String sql = "UPDATE mitra SET nama_lengkap = ?, alamat = ?, no_telepon = ?, email = ?, " +
                "username = ?, is_active = ?, updated_at = ?, id_mitra = ?, id_lokasi_tugas = ? " +
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

            stmt.setInt(10, mitra.getIdAkun());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- DELETE (D) ---
    public boolean delete(int idAkun) {
        String sql = "DELETE FROM mitra WHERE id_akun = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAkun);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}