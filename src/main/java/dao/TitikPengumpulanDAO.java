package dao;

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
        // PERBAIKAN DI SINI: Tambahkan .getConnection()
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // --- CREATE (C) ---
    public boolean save(TitikPengumpulan lokasi) {
        String sql = "INSERT INTO titikkumpul (NamaLokasi, Alamat, Latitude, Longitude) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lokasi.getNamaLokasi());
            stmt.setString(2, lokasi.getAlamat());
            stmt.setDouble(3, lokasi.getLatitude());
            stmt.setDouble(4, lokasi.getLongitude());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- READ (R) - Single by ID ---
    public TitikPengumpulan getById(int id) {
        String sql = "SELECT * FROM titikkumpul WHERE idLokasi = ?";
        TitikPengumpulan lokasi = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                lokasi = new TitikPengumpulan(
                        rs.getInt("idLokasi"),
                        rs.getString("NamaLokasi"),
                        rs.getString("Alamat"),
                        rs.getDouble("Latitude"),
                        rs.getDouble("Longitude")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lokasi;
    }

    // --- READ (R) - All ---
    public List<TitikPengumpulan> getAll() {
        String sql = "SELECT * FROM titikkumpul";
        List<TitikPengumpulan> listLokasi = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                TitikPengumpulan lokasi = new TitikPengumpulan(
                        rs.getInt("idLokasi"),
                        rs.getString("NamaLokasi"),
                        rs.getString("Alamat"),
                        rs.getDouble("Latitude"),
                        rs.getDouble("Longitude")
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
        String sql = "UPDATE titikkumpul SET NamaLokasi = ?, Alamat = ?, Latitude = ?, Longitude = ? WHERE idLokasi = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lokasi.getNamaLokasi());
            stmt.setString(2, lokasi.getAlamat());
            stmt.setDouble(3, lokasi.getLatitude());
            stmt.setDouble(4, lokasi.getLongitude());
            stmt.setInt(5, lokasi.getIdLokasi());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- DELETE (D) ---
    public boolean delete(int id) {
        String sql = "DELETE FROM titikkumpul WHERE idLokasi = ?";

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