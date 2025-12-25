package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_sampahin";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private static Connection instance;

    private DatabaseConnection() {
    }

    public static Connection getInstance() {
        try {
            if (instance == null || instance.isClosed()) {
                synchronized (DatabaseConnection.class) {
                    if (instance == null || instance.isClosed()) {
                        try {
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            instance = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                            System.out.println("[DatabaseConnection] Koneksi database berhasil!");
                        } catch (ClassNotFoundException e) {
                            System.err.println("[DatabaseConnection] Driver tidak ditemukan: " + e.getMessage());
                        } catch (SQLException e) {
                            System.err.println("[DatabaseConnection] Koneksi database gagal: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public static void closeConnection() {
        if (instance != null) {
            try {
                instance.close();
                instance = null;
                System.out.println("[DatabaseConnection] Koneksi database ditutup.");
            } catch (SQLException e) {
                System.err.println("[DatabaseConnection] Gagal menutup koneksi: " + e.getMessage());
            }
        }
    }

    public static boolean testConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection testConn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            boolean isConnected = testConn != null && !testConn.isClosed();
            testConn.close();
            return isConnected;
        } catch (Exception e) {
            System.err.println("[DatabaseConnection] Test koneksi gagal: " + e.getMessage());
            return false;
        }
    }

    public static boolean isConnected() {
        try {
            return instance != null && !instance.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}