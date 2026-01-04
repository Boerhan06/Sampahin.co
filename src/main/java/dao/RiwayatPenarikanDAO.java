package dao;

import models.RiwayatPenarikan;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RiwayatPenarikanDAO {

    // Ambil semua data riwayat (SELECT)
    public List<RiwayatPenarikan> getAll() {
        List<RiwayatPenarikan> list = new ArrayList<>();

        String query = "SELECT r.*, a.username FROM riwayat_penarikan r " +
                "JOIN akun a ON r.idAkun = a.idAkun " +
                "ORDER BY r.waktu_transaksi DESC";

        Connection conn = DatabaseConnection.getInstance().getConnection();

        if (conn == null) return list;

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter viewFormat = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", new Locale("id", "ID"));

            while (rs.next()) {
                String id = "WD-" + rs.getInt("id_penarikan");
                String username = rs.getString("username");
                String metode = rs.getString("metode_penarikan");
                double jumlah = rs.getDouble("jumlah_penarikan");
                String status = rs.getString("status_penarikan");
                String rawDate = rs.getString("waktu_transaksi");

                LocalDateTime ldt = LocalDateTime.now();
                String formattedDate = rawDate;

                try {
                    if (rawDate != null && rawDate.contains(".")) {
                        rawDate = rawDate.substring(0, rawDate.indexOf("."));
                    }
                    ldt = LocalDateTime.parse(rawDate, dbFormat);
                    formattedDate = ldt.format(viewFormat);
                } catch (Exception e) {
                    System.err.println("Date Parse Error: " + e.getMessage());
                }

                list.add(new RiwayatPenarikan(id, username, formattedDate, ldt, metode, jumlah, status));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
