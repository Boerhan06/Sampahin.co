package controller;

import dao.SampahDAO; 
import models.Sampah; 

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.math.BigDecimal;
import java.util.List;

public class DashboardController {

    @FXML
    private TableView<Sampah> tabelSampah;

    @FXML
    private TableColumn<Sampah, Number> kolomID;
    
    @FXML
    private TableColumn<Sampah, String> kolomJenis;
    
    @FXML
    private TableColumn<Sampah, String> kolomKategori;
    
    @FXML
    private TableColumn<Sampah, BigDecimal> kolomHarga;

    private SampahDAO sampahDAO;
    private ObservableList<Sampah> dataSampahList;

 
    @FXML
    public void initialize() {
        this.sampahDAO = new SampahDAO();
        
        kolomID.setCellValueFactory(cellData -> cellData.getValue().idSampahProperty());
        kolomJenis.setCellValueFactory(cellData -> cellData.getValue().jenisSampahProperty());

        kolomKategori.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getKategori())
        );
        
        kolomHarga.setCellValueFactory(cellData -> cellData.getValue().hargaPoinPerKgProperty());

        loadDataDariDB();
    }

    @FXML
    private void loadDataDariDB() {
        System.out.println("Mengambil data dari database...");

        List<Sampah> listDariDB = sampahDAO.getAllSampah();

        dataSampahList = FXCollections.observableArrayList(listDariDB);

        tabelSampah.setItems(dataSampahList);
        
        System.out.println("Data berhasil dimuat: " + listDariDB.size() + " baris.");
    }
}
