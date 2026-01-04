package models;

import javafx.beans.property.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Pengguna extends Akun {

    private StringProperty idKartu;
    private StringProperty nomorKartu;
    private ObjectProperty<BigDecimal> saldoPoin;
    private ObjectProperty<BigDecimal> saldo;
    private ObjectProperty<LocalDate> tanggalDaftar;

    private static final BigDecimal NILAI_TUKAR = new BigDecimal("10");

    public Pengguna(String namaLengkap, String alamat, String noTelepon, String email, String username, String plainPassword,
                    String idKartu, String nomorKartu) {

        super(namaLengkap, alamat, noTelepon, email, username, plainPassword);

        this.idKartu = new SimpleStringProperty(idKartu);
        this.nomorKartu = new SimpleStringProperty(nomorKartu);
        this.saldoPoin = new SimpleObjectProperty<>(BigDecimal.ZERO);
        this.saldo = new SimpleObjectProperty<>(BigDecimal.ZERO);
        this.tanggalDaftar = new SimpleObjectProperty<>(LocalDate.now());
    }

    public Pengguna(int idAkun, String namaLengkap, String alamat, String noTelepon, String email, String username,
                    String dbHashedPassword, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt,
                    String idKartu, String nomorKartu, BigDecimal saldoPoin, BigDecimal saldo, LocalDate tanggalDaftar) {

        super(idAkun, namaLengkap, alamat, noTelepon, email, username, dbHashedPassword, isActive, createdAt, updatedAt);

        this.idKartu = new SimpleStringProperty(idKartu);
        this.nomorKartu = new SimpleStringProperty(nomorKartu);
        this.saldoPoin = new SimpleObjectProperty<>(saldoPoin);
        this.saldo = new SimpleObjectProperty<>(saldo);
        this.tanggalDaftar = new SimpleObjectProperty<>(tanggalDaftar);
    }

    @Override
    public String getRole() {
        return "Pengguna";
    }

    public void tambahPoin(BigDecimal poin) {
        this.saldoPoin.set(this.saldoPoin.get().add(poin));
    }

    public boolean tukarPoinKeSaldo(BigDecimal poinYangDitukar) {
        if (this.saldoPoin.get().compareTo(poinYangDitukar) >= 0) {

            BigDecimal nilaiRupiah = poinYangDitukar.multiply(NILAI_TUKAR);

            this.saldoPoin.set(this.saldoPoin.get().subtract(poinYangDitukar));

            this.saldo.set(this.saldo.get().add(nilaiRupiah));

            return true;
        }
        return false;
    }

    public String getIdKartu() { return idKartu.get(); }
    public void setIdKartu(String idKartu) { this.idKartu.set(idKartu); }
    public StringProperty idKartuProperty() { return idKartu; }

    public String getNomorKartu() { return nomorKartu.get(); }
    public void setNomorKartu(String nomorKartu) { this.nomorKartu.set(nomorKartu); }
    public StringProperty nomorKartuProperty() { return nomorKartu; }

    public BigDecimal getSaldoPoin() { return saldoPoin.get(); }
    public void setSaldoPoin(BigDecimal saldoPoin) { this.saldoPoin.set(saldoPoin); }
    public ObjectProperty<BigDecimal> saldoPoinProperty() { return saldoPoin; }

    public BigDecimal getSaldo() { return saldo.get(); }
    public void setSaldo(BigDecimal saldo) { this.saldo.set(saldo); }
    public ObjectProperty<BigDecimal> saldoProperty() { return saldo; }

    public LocalDate getTanggalDaftar() { return tanggalDaftar.get(); }
    public void setTanggalDaftar(LocalDate tanggalDaftar) { this.tanggalDaftar.set(tanggalDaftar); }
    public ObjectProperty<LocalDate> tanggalDaftarProperty() { return tanggalDaftar; }
}