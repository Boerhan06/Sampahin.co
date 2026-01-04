package models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Mitra extends Akun {

    private final StringProperty idMitra;
    private final ObjectProperty<TitikPengumpulan> lokasiTugas;

    private final ObjectProperty<BigDecimal> saldo;

    public Mitra(String namaLengkap, String alamat, String noTelepon, String email, String username, String plainPassword,
                 String idMitra, TitikPengumpulan lokasiTugas) {
        super(namaLengkap, alamat, noTelepon, email, username, plainPassword);

        this.idMitra = new SimpleStringProperty(idMitra);
        this.lokasiTugas = new SimpleObjectProperty<>(lokasiTugas);
        this.saldo = new SimpleObjectProperty<>(BigDecimal.ZERO);
    }

    public Mitra(int idAkun, String namaLengkap, String alamat, String noTelepon, String email, String username,
                 String dbHashedPassword, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt,
                 String idMitra, TitikPengumpulan lokasiTugas, BigDecimal saldo) {

        super(idAkun, namaLengkap, alamat, noTelepon, email, username, dbHashedPassword, isActive, createdAt, updatedAt);

        this.idMitra = new SimpleStringProperty(idMitra);
        this.lokasiTugas = new SimpleObjectProperty<>(lokasiTugas);

        if (saldo == null) {
            this.saldo = new SimpleObjectProperty<>(BigDecimal.ZERO);
        } else {
            this.saldo = new SimpleObjectProperty<>(saldo);
        }
    }

    @Override
    public String getRole() {
        return "Mitra";
    }

    public String getInisial() {
        String nama = getNamaLengkap();
        if (nama == null || nama.isEmpty()) return "?";

        String[] parts = nama.split(" ");
        String inisial = String.valueOf(parts[0].charAt(0));

        if (parts.length > 1) {
            inisial += parts[1].charAt(0);
        }

        return inisial.toUpperCase();
    }

    public String getIdMitra() {
        return idMitra.get();
    }

    public void setIdMitra(String idMitra) {
        this.idMitra.set(idMitra);
    }

    public StringProperty idMitraProperty() {
        return idMitra;
    }

    public TitikPengumpulan getLokasiTugas() {
        return lokasiTugas.get();
    }

    public void setLokasiTugas(TitikPengumpulan lokasiTugas) {
        this.lokasiTugas.set(lokasiTugas);
    }

    public ObjectProperty<TitikPengumpulan> lokasiTugasProperty() {
        return lokasiTugas;
    }

    public BigDecimal getSaldo() {
        return saldo.get();
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo.set(saldo);
    }

    public ObjectProperty<BigDecimal> saldoProperty() {
        return saldo;
    }
}