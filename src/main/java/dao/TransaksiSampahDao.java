package com.sampahin.dao;

import com.sampahin.model.*; // Import semua model
import com.sampahin.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransaksiSampahDAO {

    private Connection connection;

    // --- SEMUA DEPENDENSI DAO ---
    private PenggunaDAO penggunaDAO;
    private MitraDAO mitraDAO;
    private TitikPengumpulanDAO titikPengumpulanDAO;
    private SampahDAO sampahDAO;

    public TransaksiSampahDAO() {
        this.connection = DatabaseConnection.getInstance();
        // Inisialisasi semua DAO yang dibutuhkan
        this.penggunaDAO = new PenggunaDAO();
        this.mitraDAO = new MitraDAO();
        this.titikPengumpulanDAO = new TitikPengumpulanDAO();
        this.sampahDAO = new SampahDAO();
    }


    // --- CREATE (C) ---
    public boolean save(TransaksiSampah trx) {
        String sql = "INSERT INTO transaksi_sampah (id_pengguna, id_mitra, id_lokasi, id_sampah, " +
                "berat_kg, total_poin_didapat, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Ambil semua ID dari objek-objek terkait
            stmt.setInt(1, trx.getPengguna().getIdAkun());
            stmt.setInt(2, trx.getMitra().getIdAkun());
            stmt.setInt(3, trx.getLokasi().getIdLokasi());
            stmt.setInt(4, trx.getSampah().getIdSampah());
            stmt.setBigDecimal(5, trx.getBeratKg());
            stmt.setBigDecimal(6, trx.getTotalPoinDidapat());
            stmt.setObject(7, trx.getTimestamp());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // --- READ (R) - Helper Method ---
    private TransaksiSampah mapResultSetToTransaksi(ResultSet rs) throws SQLException {
        // 1. Ambil semua Foreign Key (ID)
        int idPengguna = rs.getInt("id_pengguna");
        int idMitra = rs.getInt("id_mitra");
        int idLokasi = rs.getInt("id_lokasi");
        int idSampah = rs.getInt("id_sampah");

        // 2. Gunakan DAO lain untuk "menghidupkan" objek
        // (Asumsi Anda punya/buat method getById di semua DAO ini)
        Pengguna pengguna = penggunaDAO.getPenggunaById(idPengguna);
        Mitra mitra = mitraDAO.getMitraById(idMitra); // Anda perlu buat method ini
        TitikPengumpulan lokasi = titikPengumpulanDAO.getById(idLokasi);
        Sampah sampah = sampahDAO.getById(idSampah); // Anda perlu buat method ini

        // 3. Buat objek TransaksiSampah lengkap (perlu constructor baru)
        // Pastikan model TransaksiSampah punya constructor ini
        return new TransaksiSampah(
                rs.getInt("id_transaksi"),
                pengguna,
                mitra,
                lokasi,
                sampah,
                rs.getBigDecimal("berat_kg"),
                rs.getBigDecimal("total_poin_didapat"),
                rs.getObject("timestamp", LocalDateTime.class)
        );
    }


    // --- READ (R) - All by Pengguna (Paling Berguna) ---
    public List<TransaksiSampah> getAllByPengguna(int idPengguna) {
        String sql = "SELECT * FROM transaksi_sampah WHERE id_pengguna = ? ORDER BY timestamp DESC";
        List<TransaksiSampah> listTrx = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPengguna);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                listTrx.add(mapResultSetToTransaksi(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listTrx;
    }

}