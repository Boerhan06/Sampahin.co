package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection (Singleton Wrapper) - Versi Stabil (Auto Reconnect)
 */
public class DatabaseConnection {

    // ==============================================================================
    // KONFIGURASI DATABASE
    // ==============================================================================
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_sampahin";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    // ==============================================================================

    private static DatabaseConnection instance;
    private Connection connection;

    /**
     * Constructor Private.
     * Hanya load driver sekali saja saat aplikasi mulai.
     */
    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ [DatabaseConnection] Driver MySQL tidak ditemukan: " + e.getMessage());
        }
    }

    /**
     * Mendapatkan Instance Singleton Wrapper.
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * ✅ LOGIKA PERBAIKAN DI SINI:
     * Method ini akan mengecek apakah koneksi NULL atau CLOSED.
     * Jika ya, dia akan membuat koneksi BARU secara otomatis.
     * Ini mengatasi error "No operations allowed after connection closed".
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                System.out.println("♻️ [DatabaseConnection] Koneksi Re-Open / Baru berhasil dibuat.");
            }
        } catch (SQLException e) {
            System.err.println("❌ [DatabaseConnection] Gagal konek database: " + e.getMessage());
        }
        return connection;
    }

    /**
     * Menutup koneksi database secara manual saat aplikasi exit.
     */
    public static void shutdown() {
        if (instance != null && instance.connection != null) {
            try {
                if (!instance.connection.isClosed()) {
                    instance.connection.close();
                    System.out.println("🔌 [DatabaseConnection] Koneksi database ditutup aman.");
                }
            } catch (SQLException e) {
                System.err.println("❌ [DatabaseConnection] Gagal menutup koneksi: " + e.getMessage());
            } finally {
                instance = null;
            }
        }
    }

    /**
     * Method utilitas untuk test koneksi awal (misal di halaman Login).
     */
    public static boolean testConnection() {
        try (Connection testConn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            return testConn != null && !testConn.isClosed();
        } catch (Exception e) {
            System.err.println("❌ [DatabaseConnection] Test koneksi gagal: " + e.getMessage());
            return false;
        }
    }
}