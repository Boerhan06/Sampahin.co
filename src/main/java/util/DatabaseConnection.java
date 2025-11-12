package util; 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_sampahin";
    private static final String DB_USER = "root";
    private static final String DB_PASS = ""; // Kosongkan jika tidak ada password

    // 2. Satu-satunya instance koneksi (Prinsip Singleton)
    private static Connection instance;

    // 3. Constructor dibuat private agar tidak bisa di-new dari luar
    private DatabaseConnection() {
        // Mencegah pembuatan objek
    }

    public static Connection getInstance() {
        if (instance == null) {
            // Jika belum ada, buat baru (thread-safe sederhana)
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    try {
                        // Daftarkan Driver JDBC
                        // Class.forName("com.mysql.cj.jdbc.Driver"); // (Opsional di JDBC 4.0+)

                        // Buat koneksi
                        instance = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                        System.out.println("Koneksi Database Berhasil!");
                    } catch (SQLException e) {
                        System.err.println("Koneksi Database Gagal: " + e.getMessage());
                        // Di aplikasi nyata, gunakan AlertUtils untuk menampilkan error ini
                        e.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }
}