package models;

import javafx.beans.property.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransaksiMitra {

    public enum JenisTransaksi {
        PEMASUKAN,  // Uang Masuk (Top Up, Jual ke Pengepul) -> HIJAU
        PENGELUARAN // Uang Keluar (Beli dari Nasabah, Tarik Saldo) -> MERAH
    }

    private final IntegerProperty idTransaksiMitra;
    private final ObjectProperty<TitikPengumpulan> titikPengumpulan;
    private final ObjectProperty<Admin> adminPencatat;
    private final StringProperty namaMitraBisnis;
    private final StringProperty kategoriSampahTerjual;
    private final ObjectProperty<BigDecimal> beratTotalKg;
    private final ObjectProperty<BigDecimal> nilaiRupiahDidapat; // Ini kita anggap sebagai NOMINAL
    private final ObjectProperty<LocalDateTime> timestamp;
    private final StringProperty status;

    private final ObjectProperty<JenisTransaksi> jenis; // PENTING: Untuk logika warna
    private final StringProperty keterangan;            // PENTING: Contoh "Top Up via DANA"

    public TransaksiMitra(int idTransaksiMitra, TitikPengumpulan titikPengumpulan, Admin adminPencatat,
                          String namaMitraBisnis, String kategoriSampahTerjual, BigDecimal beratTotalKg,
                          BigDecimal nilaiRupiahDidapat, LocalDateTime timestamp, String status,
                          JenisTransaksi jenis, String keterangan) {

        this.idTransaksiMitra = new SimpleIntegerProperty(idTransaksiMitra);
        this.titikPengumpulan = new SimpleObjectProperty<>(titikPengumpulan);
        this.adminPencatat = new SimpleObjectProperty<>(adminPencatat);
        this.namaMitraBisnis = new SimpleStringProperty(namaMitraBisnis);
        this.kategoriSampahTerjual = new SimpleStringProperty(kategoriSampahTerjual);
        this.beratTotalKg = new SimpleObjectProperty<>(beratTotalKg);
        this.nilaiRupiahDidapat = new SimpleObjectProperty<>(nilaiRupiahDidapat);
        this.timestamp = new SimpleObjectProperty<>(timestamp);
        this.status = new SimpleStringProperty(status);

        // Field baru
        this.jenis = new SimpleObjectProperty<>(jenis);
        this.keterangan = new SimpleStringProperty(keterangan);
    }

    public TransaksiMitra(int idTransaksi, String keterangan, BigDecimal nominal,
                          BigDecimal beratSampah, JenisTransaksi jenis, LocalDateTime timestamp) {

        this.idTransaksiMitra = new SimpleIntegerProperty(idTransaksi);
        this.titikPengumpulan = new SimpleObjectProperty<>(null);
        this.adminPencatat = new SimpleObjectProperty<>(null);
        this.namaMitraBisnis = new SimpleStringProperty("-");
        this.kategoriSampahTerjual = new SimpleStringProperty("-");
        this.status = new SimpleStringProperty("Selesai"); // Default status

        this.keterangan = new SimpleStringProperty(keterangan);
        this.nilaiRupiahDidapat = new SimpleObjectProperty<>(nominal); // Nominal masuk ke sini
        this.beratTotalKg = new SimpleObjectProperty<>(beratSampah != null ? beratSampah : BigDecimal.ZERO);
        this.jenis = new SimpleObjectProperty<>(jenis);
        this.timestamp = new SimpleObjectProperty<>(timestamp);
    }

    public BigDecimal getNominal() {
        return nilaiRupiahDidapat.get();
    }
    public ObjectProperty<BigDecimal> nominalProperty() {
        return nilaiRupiahDidapat;
    }
    public BigDecimal getBeratSampah() {
        return beratTotalKg.get();
    }

    public JenisTransaksi getJenis() { return jenis.get(); }
    public ObjectProperty<JenisTransaksi> jenisProperty() { return jenis; }
    public void setJenis(JenisTransaksi jenis) { this.jenis.set(jenis); }

    public String getKeterangan() { return keterangan.get(); }
    public StringProperty keteranganProperty() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan.set(keterangan); }



    public int getIdTransaksiMitra() { return idTransaksiMitra.get(); }
    public IntegerProperty idTransaksiMitraProperty() { return idTransaksiMitra; }
    public void setIdTransaksiMitra(int id) { this.idTransaksiMitra.set(id); }

    public TitikPengumpulan getTitikPengumpulan() { return titikPengumpulan.get(); }
    public ObjectProperty<TitikPengumpulan> titikPengumpulanProperty() { return titikPengumpulan; }
    public void setTitikPengumpulan(TitikPengumpulan tp) { this.titikPengumpulan.set(tp); }

    public Admin getAdminPencatat() { return adminPencatat.get(); }
    public ObjectProperty<Admin> adminPencatatProperty() { return adminPencatat; }
    public void setAdminPencatat(Admin admin) { this.adminPencatat.set(admin); }

    public String getNamaMitraBisnis() { return namaMitraBisnis.get(); }
    public StringProperty namaMitraBisnisProperty() { return namaMitraBisnis; }
    public void setNamaMitraBisnis(String nama) { this.namaMitraBisnis.set(nama); }

    public String getKategoriSampahTerjual() { return kategoriSampahTerjual.get(); }
    public StringProperty kategoriSampahTerjualProperty() { return kategoriSampahTerjual; }
    public void setKategoriSampahTerjual(String kat) { this.kategoriSampahTerjual.set(kat); }

    public BigDecimal getBeratTotalKg() { return beratTotalKg.get(); }
    public ObjectProperty<BigDecimal> beratTotalKgProperty() { return beratTotalKg; }
    public void setBeratTotalKg(BigDecimal berat) { this.beratTotalKg.set(berat); }

    public BigDecimal getNilaiRupiahDidapat() { return nilaiRupiahDidapat.get(); }
    public ObjectProperty<BigDecimal> nilaiRupiahDidapatProperty() { return nilaiRupiahDidapat; }
    public void setNilaiRupiahDidapat(BigDecimal nilai) { this.nilaiRupiahDidapat.set(nilai); }

    public LocalDateTime getTimestamp() { return timestamp.get(); }
    public ObjectProperty<LocalDateTime> timestampProperty() { return timestamp; }
    public void setTimestamp(LocalDateTime time) { this.timestamp.set(time); }

    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }
    public void setStatus(String stat) { this.status.set(stat); }
}