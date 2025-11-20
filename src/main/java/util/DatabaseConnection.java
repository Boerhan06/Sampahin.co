package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sampahin"; 
    private static final String DB_USER = "root";
    private static final String DB_PASS = ""; 

    private static Connection instance;

    private DatabaseConnection() {}

    public static Connection getInstance() {
        if (instance == null) {
            try {
                instance = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                System.out.print("koneksi Berhasi");
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("GAGAL KONEK DATABASE: " + e.getMessage());
            }
        }
        return instance;
    }
}