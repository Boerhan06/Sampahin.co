/* * PERBAIKAN: package adalah 'dao'
 */
package dao;

/* * PERBAIKAN: import duplikat dihapus, dan disesuaikan ke 'models' dan 'util'
 */
import models.Pengguna;
import models.Penukaran;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PenukaranDAO {

    private Connection connection;
    private PenggunaDAO penggunaDAO; 

    public PenukaranDAO() {
        this.connection = DatabaseConnection.getInstance();
        this.penggunaDAO = new PenggunaDAO(); 
    }

    // --- CREATE (C) ---
    public boolean save(Penukaran penukaran) {
        String sql = "INSERT INTO penukaran (id_pengguna, poin_ditukar, nilai_rupiah, keterangan, timestamp, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, penukaran.getPengguna().getIdAkun());
            stmt.setBigDecimal(2, penukaran.getPoinDitukar());
            stmt.setBigDecimal(3, penukaran.getNilaiRupiah());
            stmt.setString(4, penukaran.getKeterangan());
            stmt.setObject(5, penukaran.getTimestamp());
            stmt.setString(6, penukaran.getStatus());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- READ (R) - Helper Method ---
    private Penukaran mapResultSetToPenukaran(ResultSet rs) throws SQLException {
        int idPengguna = rs.getInt("id_pengguna");
        Pengguna pengguna = penggunaDAO.getPenggunaById(idPengguna); 

        return new Penukaran(
                rs.getInt("id_penukaran"),
                pengguna, 
                rs.getBigDecimal("poin_ditukar"),
                rs.getBigDecimal("nilai_rupiah"),
                rs.getString("keterangan"),
                rs.getObject("timestamp", LocalDateTime.class),
                rs.getString("status")
        );
    }

    // --- READ (R) - Single by ID ---
    public Penukaran getById(int id) {
        String sql = "SELECT * FROM penukaran WHERE id_penukaran = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToPenukaran(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --- READ (R) - All by Pengguna (Paling Berguna) ---
    public List<Penukaran> getAllByPengguna(int idPengguna) {
        String sql = "SELECT * FROM penukaran WHERE id_pengguna = ? ORDER BY timestamp DESC";
        List<Penukaran> listPenukaran = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPengguna);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                listPenukaran.add(mapResultSetToPenukaran(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listPenukaran;
    }

    // --- UPDATE (U) ---
    public boolean updateStatus(int idPenukaran, String newStatus) {
        String sql = "UPDATE penukaran SET status = ?, timestamp = ? WHERE id_penukaran = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setObject(2, LocalDateTime.now()); 
            stmt.setInt(3, idPenukaran);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}