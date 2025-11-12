/* * PERBAIKAN: package adalah 'dao'
 */
package dao;

/* * PERBAIKAN: import duplikat dihapus, dan disesuaikan ke 'models' dan 'util'
 */
import models.Mitra;
import models.Pengguna;
import models.Sampah;
import models.TransaksiSampah;
import models.TitikPengumpulan;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransaksiSampahDAO {

    private Connection connection;
    
    private PenggunaDAO penggunaDAO;
    private MitraDAO mitraDAO;
    private TitikPengumpulanDAO titikPengumpulanDAO;
    private SampahDAO sampahDAO;

    public TransaksiSampahDAO() {
        this.connection = DatabaseConnection.getInstance();
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
        int idPengguna = rs.getInt("id_pengguna");
        int idMitra = rs.getInt("id_mitra");
        int idLokasi = rs.getInt("id_lokasi");
        int idSampah = rs.getInt("id_sampah");

        Pengguna pengguna = penggunaDAO.getPenggunaById(idPengguna);
        Mitra mitra = mitraDAO.getMitraById(idMitra); 
        TitikPengumpulan lokasi = titikPengumpulanDAO.getById(idLokasi);
        Sampah sampah = sampahDAO.getById(idSampah); 

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