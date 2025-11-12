/* * PERBAIKAN: package diubah menjadi "dao" (sesuai folder Anda)
 */
package dao; 

/* * PERBAIKAN: import diubah untuk mengambil dari package "models" dan "util"
 */
import models.TitikPengumpulan;
import models.Mitra;
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


public class MitraDAO {

    private Connection connection;
    private TitikPengumpulanDAO titikPengumpulanDAO;

    public MitraDAO() {
        this.connection = DatabaseConnection.getInstance();
        this.titikPengumpulanDAO = new TitikPengumpulanDAO(); 
    }


    // --- CREATE (C) ---
    public boolean save(Mitra mitra, String plainPassword) {
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
            stmt.setInt(11, mitra.getLokasiTugas().getIdLokasi());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // --- READ (R) - Single by Username ---
    public Mitra getMitraByUsername(String username) {
        String sql = "SELECT * FROM mitra WHERE username = ?";
        Mitra mitra = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
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

    // --- READ (R) - Single by ID ---
    public Mitra getMitraById(int id) {
        String sql = "SELECT * FROM mitra WHERE id_akun = ?";
        Mitra mitra = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
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

    // --- READ (R) - All ---
    public List<Mitra> getAllMitra() {
        String sql = "SELECT * FROM mitra";
        List<Mitra> mitraList = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int idLokasi = rs.getInt("id_lokasi_tugas");
                TitikPengumpulan lokasi = titikPengumpulanDAO.getById(idLokasi); 

                Mitra mitra = new Mitra(
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
                mitraList.add(mitra);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mitraList;
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
            stmt.setInt(9, mitra.getLokasiTugas().getIdLokasi()); 
            stmt.setInt(10, mitra.getIdAkun()); 

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
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
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}