package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;

public class Admin extends Akun {

    private final StringProperty idAdmin;
    private byte[] fotoProfil;

    public Admin() {
        super();
        this.idAdmin = new SimpleStringProperty("");
        this.fotoProfil = null;
    }

    public Admin(int idAkun, String namaLengkap, String alamat, String noTelepon,
                 String email, String username, String dbHashedPassword,
                 boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt,
                 String idAdmin, byte[] fotoProfil) {

        super(idAkun, namaLengkap, alamat, noTelepon, email, username,
                dbHashedPassword, isActive, createdAt, updatedAt);

        this.idAdmin = new SimpleStringProperty(idAdmin);
        this.fotoProfil = fotoProfil;
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    public void registrasiPengguna(Pengguna pengguna) {
        System.out.println("[LOG] Admin " + this.getNamaLengkap() + " mendaftarkan user: " + pengguna.getNamaLengkap());
    }

    public void kelolaDataMitra(Mitra mitra) {
        System.out.println("[LOG] Admin " + this.getNamaLengkap() + " mengelola mitra: " + mitra.getNamaLengkap());
    }

    public String getIdAdmin() {
        return idAdmin.get();
    }

    public void setIdAdmin(String idAdmin) {
        this.idAdmin.set(idAdmin);
    }

    public StringProperty idAdminProperty() {
        return idAdmin;
    }

    public byte[] getFotoProfil() {
        return fotoProfil;
    }

    public void setFotoProfil(byte[] fotoProfil) {
        this.fotoProfil = fotoProfil;
    }
}