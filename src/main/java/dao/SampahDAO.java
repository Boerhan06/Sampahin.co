package dao;

import models.Sampah;
import models.SampahAnorganik;
import models.SampahB3;
import models.SampahOrganik;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SampahDAO {

    // Constructor kosong (tidak perlu simpan connection di sini)
    public SampahDAO() {
    }

    // --- CREATE (C) ---
    public boolean save(Sampah sampah) {
        String sql = "INSERT INTO sampah (jenis_sampah, harga_poin_per_kg, kategori, perkiraan_busuk, petunjuk_penanganan) " +
                "VALUES (?, ?, ?, ?, ?)";

        // 1. Ambil Koneksi FRESH (Jangan ditaruh di dalam try-with-resources)
        Connection conn = DatabaseConnection.getInstance().getConnection();

        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sampah.getJenisSampah());
            stmt.setBigDecimal(2, sampah.getHargaPoinPerKg());
            stmt.setString(3, sampah.getKategori());

            if (sampah instanceof SampahOrganik) {
                stmt.setObject(4, ((SampahOrganik) sampah).getPerkiraanBusuk());
                stmt.setNull(5, Types.VARCHAR);
            } else if (sampah instanceof SampahB3) {
                stmt.setNull(4, Types.DATE);
                stmt.setString(5, ((SampahB3) sampah).getPetunjukPenanganan());
            } else { // Anorganik
                stmt.setNull(4, Types.DATE);
                stmt.setNull(5, Types.VARCHAR);
            }

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- READ (R) - Helper Method (Logic Pemetaan Data) ---
    private Sampah mapResultSetToSampah(ResultSet rs) throws SQLException {
        String kategori = rs.getString("kategori");
        if (kategori == null) return null;

        kategori = kategori.trim();
        Sampah sampah = null;

        if (kategori.equalsIgnoreCase("Organik")) {
            sampah = new SampahOrganik(
                    rs.getInt("id_sampah"), // Pastikan nama kolom DB benar
                    rs.getString("jenis_sampah"),
                    rs.getBigDecimal("harga_poin_per_kg"),
                    rs.getObject("perkiraan_busuk", LocalDate.class)
            );
        } else if (kategori.equalsIgnoreCase("Anorganik")) {
            sampah = new SampahAnorganik(
                    rs.getInt("id_sampah"),
                    rs.getString("jenis_sampah"),
                    rs.getBigDecimal("harga_poin_per_kg")
            );
        } else if (kategori.equalsIgnoreCase("B3")) {
            sampah = new SampahB3(
                    rs.getInt("id_sampah"),
                    rs.getString("jenis_sampah"),
                    rs.getBigDecimal("harga_poin_per_kg"),
                    rs.getString("petunjuk_penanganan")
            );
        } else {
            // Fallback default
            sampah = new SampahAnorganik(
                    rs.getInt("id_sampah"),
                    rs.getString("jenis_sampah"),
                    rs.getBigDecimal("harga_poin_per_kg")
            );
        }
        return sampah;
    }

    // --- READ (R) - Single by ID ---
    public Sampah getById(int id) {
        String sql = "SELECT * FROM sampah WHERE id_sampah = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        if (conn == null) return null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSampah(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --- READ (R) - All (INI YANG DIPANGGIL DI POPUP) ---
    public List<Sampah> getAllSampah() {
        String sql = "SELECT * FROM sampah";
        List<Sampah> sampahList = new ArrayList<>();

        // 1. Ambil koneksi FRESH di sini
        Connection conn = DatabaseConnection.getInstance().getConnection();

        // Safety check
        if (conn == null) {
            System.err.println("❌ SampahDAO: Koneksi NULL/Gagal.");
            return sampahList;
        }

        // 2. Hanya Statement dan ResultSet yang di-close otomatis
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Sampah sampah = mapResultSetToSampah(rs);
                if (sampah != null) {
                    sampahList.add(sampah);
                }
            }

            // Debug log
            if (sampahList.isEmpty()) {
                System.out.println("⚠️ SampahDAO: Data kosong! Cek tabel 'sampah' di DB.");
            } else {
                System.out.println("✅ SampahDAO: Berhasil memuat " + sampahList.size() + " data.");
            }

        } catch (SQLException e) {
            System.err.println("❌ SampahDAO Error: " + e.getMessage());
            e.printStackTrace();
        }
        return sampahList;
    }

    // --- UPDATE (U) ---
    public boolean update(Sampah sampah) {
        String sql = "UPDATE sampah SET jenis_sampah = ?, harga_poin_per_kg = ?, kategori = ?, " +
                "perkiraan_busuk = ?, petunjuk_penanganan = ? " +
                "WHERE id_sampah = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sampah.getJenisSampah());
            stmt.setBigDecimal(2, sampah.getHargaPoinPerKg());
            stmt.setString(3, sampah.getKategori());

            if (sampah instanceof SampahOrganik) {
                stmt.setObject(4, ((SampahOrganik) sampah).getPerkiraanBusuk());
                stmt.setNull(5, Types.VARCHAR);
            } else if (sampah instanceof SampahB3) {
                stmt.setNull(4, Types.DATE);
                stmt.setString(5, ((SampahB3) sampah).getPetunjukPenanganan());
            } else {
                stmt.setNull(4, Types.DATE);
                stmt.setNull(5, Types.VARCHAR);
            }

            stmt.setInt(6, sampah.getIdSampah());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- DELETE (D) ---
    public boolean delete(int idSampah) {
        String sql = "DELETE FROM sampah WHERE id_sampah = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSampah);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}