// package models; // Sesuaikan nama package Anda
import java.util.Date;

// Abstrak, karena tidak boleh ada objek "Akun"
public abstract class Akun {
    // Atribut (protected agar bisa diakses turunan)
    protected int idAkun;
    protected String namaLengkap;
    protected String alamat;
    protected String noTelepon;
    protected String email;
    protected String username;
    protected String password; // Nanti harus di-hash!

    // Constructor
    public Akun(String namaLengkap, String alamat, String noTelepon, String email, String username, String password) {
        this.namaLengkap = namaLengkap;
        this.alamat = alamat;
        this.noTelepon = noTelepon;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    // Abstract method (wajib di-override oleh anak)
    public abstract String getRole();

    // Getter dan Setter (buat untuk semua atribut)
    public int getIdAkun() {
        return idAkun;
    }

    public void setIdAkun(int idAkun) {
        this.idAkun = idAkun;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    // ... getter/setter lainnya untuk alamat, noTelepon, email ...
}
