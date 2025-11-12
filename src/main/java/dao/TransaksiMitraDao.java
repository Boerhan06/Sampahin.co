package com.sampahin.dao;

import com.sampahin.model.Admin;
import com.sampahin.model.TitikPengumpulan;
import com.sampahin.model.TransaksiMitra;
import com.sampahin.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class TransaksiMitraDAO {

    private Connection connection;

    // --- Dependensi DAO ---
    private TitikPengumpulanDAO titikPengumpulanDAO;
    private AdminDAO adminDAO;

    public TransaksiMitraDAO() {
        this.connection = DatabaseConnection.getInstance();
        this.titikPengumpulanDAO = new TitikPengumpulanDAO();
        this.adminDAO = new AdminDAO();
    }

    // --- CREATE (C) ---
    public boolean save(TransaksiMitra trx) {
        String sql = "INSERT INTO transaksi_mitra (id_lokasi, id_admin, nama_mitra_bisnis, " +
                "kategori_sampah_terjual, berat_total_kg, nilai_rupiah_didapat, timestamp, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, trx.getTitikPengumpulan().getIdLokasi());
            stmt.setInt(2, trx.getAdminPencatat().getIdAkun());
            stmt.setString(3, trx.getNamaMitraBisnis());
            stmt.setString(4, trx.getKategoriSampahTerjual());
            stmt.setBigDecimal(5, trx.getBeratTotalKg());
            stmt.setBigDecimal(6, trx.getNilaiRupiahDidapat());
            stmt.setObject(7, trx.getTimestamp());
            stmt.setString(8, trx.getStatus());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // --- READ (R) - Helper Method ---
    private TransaksiMitra mapResultSetToTransaksi(ResultSet rs) throws SQLException {
        // 1. Ambil Foreign Keys
        int idLokasi = rs.getInt("id_lokasi");
        int idAdmin = rs.getInt("id_admin");

        // 2. "Hidupkan" Objek
        TitikPengumpulan lokasi = titikPengumpulanDAO.getById(idLokasi);
        Admin admin = adminDAO.getAdminById(idAdmin); // Anda perlu buat method ini

        // 3. Buat Objek TransaksiMitra lengkap
        return new TransaksiMitra(
                rs.getInt("id_transaksi_mitra"),
                lokasi,
                admin,
                rs.getString("nama_mitra_bisnis"),
                rs.getString("kategori_sampah_terjual"),
                rs.getBigDecimal("berat_total_kg"),
                rs.getBigDecimal("nilai_rupiah_didapat"),
                rs.getObject("timestamp", LocalDateTime.class),
                rs.getString("status")
        );
    }


    // --- READ (R) - All by Lokasi ---
    public List<TransaksiMitra> getAllByLokasi(int idLokasi) {
        String sql = "SELECT * FROM transaksi_mitra WHERE id_lokasi = ? ORDER BY timestamp DESC";
        List<TransaksiMitra> listTrx = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idLokasi);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                listTrx.add(mapResultSetToTransaksi(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listTrx;
    }


    // --- UPDATE (U) ---
    public boolean updateStatus(int idTransaksi, String newStatus) {
        String sql = "UPDATE transaksi_mitra SET status = ? WHERE id_transaksi_mitra = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, idTransaksi);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}