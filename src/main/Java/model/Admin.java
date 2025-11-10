// package models;

public class Admin extends Akun {
    // Atribut khusus Admin
    private String idAdmin; // Misal: "ADM001"

    // Constructor
    public Admin(String namaLengkap, String alamat, String noTelepon, String email, String username, String password, String idAdmin) {
        super(namaLengkap, alamat, noTelepon, email, username, password);
        this.idAdmin = idAdmin;
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    // Method khusus Admin (nanti diisi logika)
    public void registrasiPengguna(Pengguna pengguna) {
        System.out.println("Admin " + this.namaLengkap + " mendaftarkan " + pengguna.getNamaLengkap());
    }
    
    public void kelolaDataMitra(Mitra mitra) {
        // Logika CRUD Mitra
    }

    // Getter dan Setter
    public String getIdAdmin() {
        return idAdmin;
    }
}
