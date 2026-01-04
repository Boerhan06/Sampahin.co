package com.sampahin;

import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;

public class CekDatabase {
    public static void main(String[] args) {
        System.out.println("=== MULAI PENGECEKAN DATABASE ===");

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();

            // 1. Cek Nama Database yang Konek
            String namaDB = conn.getCatalog();
            System.out.println("👉 Java terhubung ke database bernama: [" + namaDB + "]");

            // 2. Cek Isi Tabel Sampah
            String sql = "SELECT * FROM sampah";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            int hitung = 0;
            while(rs.next()) {
                hitung++;
                System.out.println("   Data ke-" + hitung + ": " + rs.getString("jenis_sampah"));
            }

            if (hitung == 0) {
                System.out.println("❌ Tabel 'sampah' di database [" + namaDB + "] KOSONG MELOMPONG!");
                System.out.println("   TAPI di phpMyAdmin kamu ada isinya kan?");
                System.out.println("   ARTINYA: Nama database di kodingan SALAH.");
            } else {
                System.out.println("✅ Ditemukan " + hitung + " data. Harusnya muncul di popup.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}