package main.java.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class CxC {
    private StringProperty idTransaction;
    private StringProperty cliente;
    private DoubleProperty montoTransaccion;
    private DoubleProperty abonadoTransaccion;
    private DoubleProperty pendienteTransaccion;

    public CxC (String idTrans, String cliente, Double monto, Double abonado, Double pendiente) {
        this.idTransaction = new SimpleStringProperty(idTrans);
        this.cliente = new SimpleStringProperty(cliente);
        this.montoTransaccion = new SimpleDoubleProperty(monto);
        this.abonadoTransaccion = new SimpleDoubleProperty(abonado);
        this.pendienteTransaccion = new SimpleDoubleProperty(pendiente);
    }

    public String getIdTransaction () {
        return idTransaction.get();
    }
    public String getCliente() {
        return cliente.get();
    }
    public Double getMontoTransaccion() {
        return montoTransaccion.get();
    }
    public Double getAbonadoTransaccion() {
        return abonadoTransaccion.get();
    }
    public Double getPendienteTransaccion() {
        return pendienteTransaccion.get();
    }
    public static void generarLista (Connection conn, ObservableList<CxC> listaCxC)
    {

        try {
            String query = "select id_trans, cliente, monto_transaccion, abonado, pendiente from CUENTASXCOBRAR where pendiente > 0";
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                listaCxC.add(
                    new CxC(rs.getString("id_trans"), rs.getString("cliente"), rs.getDouble("monto_transaccion"), rs.getDouble("abonado"), rs.getDouble("pendiente"))
                );
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
