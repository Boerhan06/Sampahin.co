package com.sampahin;

import util.DatabaseConnection;
import java.sql.Connection;

public class TesKoneksi {
    public static void main(String[] args) {
        System.out.println("Mencoba menghubungi database...");
        
        Connection conn = DatabaseConnection.getInstance();
        
        if (conn != null) {
            System.out.println("✅ SUKSES! Java berhasil konek ke MySQL.");
        } else {
            System.out.println("❌ GAGAL! Cek pesan error merah di atas.");
        }
    }
}