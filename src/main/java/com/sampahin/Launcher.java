package com.sampahin;

public class Launcher {
    public static void main(String[] args) {
        System.out.println("=== Launching Sampahin.co ===");
        System.out.println("Java Version: " + System.getProperty("java.version"));

        System.setProperty("javafx.version", "17.0.12");

        try {
            com.sampahin.Main.main(args);
        } catch (Exception e) {

            System.err.println("Failed to launch application:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}