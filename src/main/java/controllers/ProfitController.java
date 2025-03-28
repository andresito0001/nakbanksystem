package main.java.controllers;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import main.java.dao.CicleDAO;
import main.java.entities.Cycle;
import main.java.services.CicleService;
import main.java.util.DatabaseUtils;

public class ProfitController {
    @FXML
    private ListView<String> listView;
    @FXML
    private DatePicker fromDatepicker;
    @FXML
    private DatePicker toDatePicker;
    

    public void initialize () {

    }

    public void updateTable () throws SQLException {


        if (fromDatepicker.getValue() != null && toDatePicker.getValue() != null) {
           
            CicleDAO cicleDAO = new CicleDAO();
            List<Cycle> listaCiclos = new ArrayList<>();

            cicleDAO.fillByDate(listaCiclos, Date.valueOf(fromDatepicker.getValue()), Date.valueOf(toDatePicker.getValue()), null);
            
            if (!listaCiclos.isEmpty()) {
                Double profit = 0.0, average = 0.0, avCycles = 0.0;

                CicleService cicleService = new CicleService();
                DatabaseUtils databaseUtils = new DatabaseUtils();
    
             for (int i = 0 ; i < listaCiclos.size(); i++ ) {
                    average += cicleService.averagePurchaseRate("tasa", "transacciones", "tipo = 'COMPRA' and moneda_enviada = 'VES' and cicle_id = '" + listaCiclos.get(i).getId() + "';");
                    avCycles += listaCiclos.get(i).getRate();
                    profit += databaseUtils.sumColumn("ganancia", "transacciones", "cicle_id = '" + listaCiclos.get(i).getId() + "'");
               //     profit = profit - databaseUtils.sumColumn("monto_equivalente", "gastos", "null");
                }
    
                listView.getItems().addAll(
                    "Cantidad de Ciclos: " + listaCiclos.size(), 
                    "Ganancia Total: " + profit + " USD", 
                    "Tasa Promedio de Compra: " + average,
                    "Tasa Promedio de Ciclos: " + (avCycles / listaCiclos.size())
                );
            } else {
                System.err.println("No hay ciclos");
            }


        } else {
            Alert alert = new Alert(AlertType.ERROR, "Debe seleccionar las fechas.");
            alert.showAndWait();
        }




    }
    
}
