package util;

import models.Admin;

public class SessionManager {
    private static SessionManager instance;
    private Admin loggedInAdmin; // Untuk menyimpan siapa yang login

    // --- Data Sementara Registrasi (Temp Data) ---
    private String tempNama;
    private String tempEmail;
    private String tempTelepon;
    private String tempAlamat;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // --- Getter & Setter untuk Registrasi ---
    public void setTempRegistrationData(String nama, String email, String telepon, String alamat) {
        this.tempNama = nama;
        this.tempEmail = email;
        this.tempTelepon = telepon;
        this.tempAlamat = alamat;
    }

    public String getTempNama() { return tempNama; }
    public String getTempEmail() { return tempEmail; }
    public String getTempTelepon() { return tempTelepon; }
    public String getTempAlamat() { return tempAlamat; }

    public void clearRegistration() {
        this.tempNama = null;
        this.tempEmail = null;
        this.tempTelepon = null;
        this.tempAlamat = null;
    }

    // --- Getter & Setter Login ---
    public void setLoggedInAkun(Admin admin) {
        this.loggedInAdmin = admin;
    }

    public Admin getLoggedInAkun() {
        return loggedInAdmin;
    }

    public void clearSession() {
        this.loggedInAdmin = null;
    }
}