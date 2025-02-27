package main.java.entities;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CxC {
    private StringProperty idTransaction;
    private StringProperty cliente;
    private DoubleProperty montoTransaccion;
    private DoubleProperty abonadoTransaccion;
    private DoubleProperty pendienteTransaccion;
    private StringProperty moneda;
    private StringProperty tipo;
    private String tipoDeCuenta;
    
        public CxC (String idTrans, String cliente, Double monto, String moneda, Double abonado, Double pendiente, String tipo, String tipoDeCuenta) {
            this.idTransaction = new SimpleStringProperty(idTrans);
            this.cliente = new SimpleStringProperty(cliente);
            this.moneda = new SimpleStringProperty(moneda);
            this.tipo = new SimpleStringProperty(tipo);
            this.montoTransaccion = new SimpleDoubleProperty(monto);
            this.abonadoTransaccion = new SimpleDoubleProperty(abonado);
            this.pendienteTransaccion = new SimpleDoubleProperty(pendiente);
            this.tipoDeCuenta = tipoDeCuenta;
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
        public String getMonedaTransaccion() {
            return moneda.get();
        }
        public String getTipo() {
            return tipo.get();
        }
        public String getTipoCuenta() {
            return tipoDeCuenta;
        }
        public void setPendienteTransaccion (Double pendienteTransaccion) {
            this.pendienteTransaccion.set(pendienteTransaccion);
        }

}
