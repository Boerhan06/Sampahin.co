/* * PERBAIKAN: package adalah 'dao'
 */
package dao;

/* * PERBAIKAN: import duplikat dihapus, dan disesuaikan ke 'models' dan 'util'
 */
import models.TitikPengumpulan;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TitikPengumpulanDAO {

    private Connection connection;

    public TitikPengumpulanDAO() {
        this.connection = DatabaseConnection.getInstance();
    }

    // --- CREATE (C) ---
    public boolean save(TitikPengumpulan lokasi) {
        String sql = "INSERT INTO titik_pengumpulan (nama_lokasi, alamat_lokasi) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lokasi.getNamaLokasi());
            stmt.setString(2, lokasi.getAlamatLokasi());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- READ (R) - Single by ID ---
    public TitikPengumpulan getById(int id) {
        String sql = "SELECT * FROM titik_pengumpulan WHERE id_lokasi = ?";
        TitikPengumpulan lokasi = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                lokasi = new TitikPengumpulan(
                        rs.getInt("id_lokasi"),
                        rs.getString("nama_lokasi"),
                        rs.getString("alamat_lokasi")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lokasi;
    }

    // --- READ (R) - All ---
    public List<TitikPengumpulan> getAll() {
        String sql = "SELECT * FROM titik_pengumpulan";
        List<TitikPengumpulan> listLokasi = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                TitikPengumpulan lokasi = new TitikPengumpulan(
                        rs.getInt("id_lokasi"),
                        rs.getString("nama_lokasi"),
                        rs.getString("alamat_lokasi")
                );
                listLokasi.add(lokasi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listLokasi;
    }

    // --- UPDATE (U) ---
    public boolean update(TitikPengumpulan lokasi) {
        String sql = "UPDATE titik_pengumpulan SET nama_lokasi = ?, alamat_lokasi = ? WHERE id_lokasi = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lokasi.getNamaLokasi());
            stmt.setString(2, lokasi.getAlamatLokasi());
            stmt.setInt(3, lokasi.getIdLokasi()); 

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- DELETE (D) ---
    public boolean delete(int id) {
        String sql = "DELETE FROM titik_pengumpulan WHERE id_lokasi = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}