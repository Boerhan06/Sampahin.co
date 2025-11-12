package com.sampahin.dao;

import com.sampahin.model.Sampah;
import com.sampahin.model.SampahAnorganik;
import com.sampahin.model.SampahB3;
import com.sampahin.model.SampahOrganik;
import com.sampahin.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SampahDAO {

    private Connection connection;

    public SampahDAO() {
        this.connection = DatabaseConnection.getInstance();
    }

    // --- CREATE (C) ---
    public boolean save(Sampah sampah) {
        String sql = "INSERT INTO sampah (jenis_sampah, harga_poin_per_kg, kategori, perkiraan_busuk, petunjuk_penanganan) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sampah.getJenisSampah());
            stmt.setBigDecimal(2, sampah.getHargaPoinPerKg());
            stmt.setString(3, sampah.getKategori()); // Ambil kategori dari method abstract

            // Cek tipe spesifik untuk kolom khusus
            if (sampah instanceof SampahOrganik) {
                stmt.setObject(4, ((SampahOrganik) sampah).getPerkiraanBusuk());
                stmt.setNull(5, Types.VARCHAR); // Kolom B3 dikosongkan
            } else if (sampah instanceof SampahB3) {
                stmt.setNull(4, Types.DATE); // Kolom Organik dikosongkan
                stmt.setString(5, ((SampahB3) sampah).getPetunjukPenanganan());
            } else { // Anorganik
                stmt.setNull(4, Types.DATE);
                stmt.setNull(5, Types.VARCHAR);
            }

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // --- READ (R) - All ---
    public List<Sampah> getAllSampah() {
        String sql = "SELECT * FROM sampah";
        List<Sampah> sampahList = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                // 1. Ambil kolom pembeda
                String kategori = rs.getString("kategori");

                // 2. Variabel untuk menampung objek
                Sampah sampah = null;

                // 3. Tentukan objek mana yang akan dibuat
                switch (kategori) {
                    case "Organik":
                        sampah = new SampahOrganik(
                                rs.getInt("id_sampah"),
                                rs.getString("jenis_sampah"),
                                rs.getBigDecimal("harga_poin_per_kg"),
                                rs.getObject("perkiraan_busuk", LocalDate.class)
                        );
                        break;

                    case "Anorganik":
                        sampah = new SampahAnorganik(
                                rs.getInt("id_sampah"),
                                rs.getString("jenis_sampah"),
                                rs.getBigDecimal("harga_poin_per_kg")
                        );
                        break;

                    case "B3":
                        sampah = new SampahB3(
                                rs.getInt("id_sampah"),
                                rs.getString("jenis_sampah"),
                                rs.getBigDecimal("harga_poin_per_kg"),
                                rs.getString("petunjuk_penanganan")
                        );
                        break;
                }

                // 4. Tambahkan ke list jika objek berhasil dibuat
                if (sampah != null) {
                    sampahList.add(sampah);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sampahList;
    }


    // --- UPDATE (U) ---
    public boolean update(Sampah sampah) {
        String sql = "UPDATE sampah SET jenis_sampah = ?, harga_poin_per_kg = ?, kategori = ?, " +
                "perkiraan_busuk = ?, petunjuk_penanganan = ? " +
                "WHERE id_sampah = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sampah.getJenisSampah());
            stmt.setBigDecimal(2, sampah.getHarga_poin_per_kg());
            stmt.setString(3, sampah.getKategori());

            // Logika 'instanceof' yang sama seperti save()
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

            stmt.setInt(6, sampah.getIdSampah()); // Klausul WHERE

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // --- DELETE (D) ---
    public boolean delete(int idSampah) {
        String sql = "DELETE FROM sampah WHERE id_sampah = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idSampah);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}