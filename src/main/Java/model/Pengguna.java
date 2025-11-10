public class Pengguna {
    private int idPengguna;
    private String idKartu;
    private String nomorKartu;
    private double saldoPoin;
    private double saldo;
    private String noTelp;
    private String alamat;
    private String namaLengkap;
    private String email;
    private java.util.Date tanggalDaftar;

    public Pengguna(int idPengguna, String idKartu, String nomorKartu, double saldoPoin,
                    double saldo, String noTelp, String alamat, String namaLengkap,
                    String email, java.util.Date tanggalDaftar) {
        this.idPengguna = idPengguna;
        this.idKartu = idKartu;
        this.nomorKartu = nomorKartu;
        this.saldoPoin = saldoPoin;
        this.saldo = saldo;
        this.noTelp = noTelp;
        this.alamat = alamat;
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.tanggalDaftar = tanggalDaftar;
    }
}
