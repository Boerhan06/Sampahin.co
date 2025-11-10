public class Admin extends Akun {
    private int idAdmin;
    private String namaLengkap;
    private String alamat;
    private String noTelepon;
    private String email;

    public Admin(int idAkun, int idAdmin, String namaLengkap, String alamat,
                 String noTelepon, String email, String username, String password) {
        super(idAkun, username, password); // Memanggil konstruktor dari Akun
        this.idAdmin = idAdmin;
        this.namaLengkap = namaLengkap;
        this.alamat = alamat;
        this.noTelepon = noTelepon;
        this.email = email;
    }

    // Getter dan Setter (opsional)
}
