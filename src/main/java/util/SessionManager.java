package util;

import models.Admin;

public class SessionManager {

    // Instance static untuk Singleton
    private static SessionManager instance;

    // --- 1. DATA SESSION LOGIN (Siapa yang sedang aktif?) ---
    private Admin loggedInAdmin;

    // --- 2. DATA SEMENTARA REGISTRASI (Estafet Tahap 1 -> Tahap 2) ---
    private String tempNama;
    private String tempEmail;
    private String tempTelepon;
    private String tempAlamat;

    // Constructor Private (Agar class ini tidak bisa di-new sembarangan)
    private SessionManager() {}

    // Method Singleton: Satu pintu akses untuk seluruh aplikasi
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // =================================================================
    //  BAGIAN 1: PENGATURAN USER LOGIN (SESSION)
    // =================================================================

    /**
     * Menyimpan data admin yang berhasil login.
     * Dipanggil di LoginController.
     */
    public void setLoggedInAkun(Admin admin) {
        this.loggedInAdmin = admin;
    }

    /**
     * Mengambil data admin yang sedang login.
     * Dipanggil di Dashboard atau EditProfil untuk menampilkan nama user.
     */
    public Admin getLoggedInAkun() {
        return loggedInAdmin;
    }

    /**
     * Mengecek apakah ada user yang sedang login.
     */
    public boolean isLoggedIn() {
        return loggedInAdmin != null;
    }

    // =================================================================
    //  BAGIAN 2: PENGATURAN DATA SEMENTARA REGISTRASI
    // =================================================================

    /**
     * Menyimpan data dari Form Registrasi Tahap 1.
     * Dipanggil di RegisterController.
     */
    public void setTempRegistrationData(String nama, String email, String telepon, String alamat) {
        this.tempNama = nama;
        this.tempEmail = email;
        this.tempTelepon = telepon;
        this.tempAlamat = alamat;
    }

    public String getTempNama() {
        return tempNama;
    }

    public String getTempEmail() {
        return tempEmail;
    }

    public String getTempTelepon() {
        return tempTelepon;
    }

    public String getTempAlamat() {
        return tempAlamat;
    }

    /**
     * Menghapus data sementara registrasi.
     * Dipanggil saat Registrasi selesai atau User membatalkan pendaftaran.
     */
    public void clearRegistration() {
        this.tempNama = null;
        this.tempEmail = null;
        this.tempTelepon = null;
        this.tempAlamat = null;
    }

    // =================================================================
    //  BAGIAN 3: LOGOUT / RESET APLIKASI
    // =================================================================

    /**
     * Menghapus semua data sesi (Login & Registrasi).
     * Dipanggil saat tombol Logout ditekan.
     */
    public void logout() {
        this.loggedInAdmin = null;
        clearRegistration(); // Pastikan data sampah registrasi juga bersih
    }
}