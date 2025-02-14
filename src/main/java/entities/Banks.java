package main.java.entities;

public class Banks {
    
    public Banks (String codigo, String nombre, String moneda, Double saldo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.moneda = moneda;
        this.saldo = saldo;
    }

    
    public String getCodigo () {
        return codigo;
    }
    public String getNombre () {
        return nombre;
    }
    public String getMoneda () {
        return moneda;
    }
    public Double getSaldo () {
        return saldo;
    }

    String codigo;
    String nombre;
    String moneda;
    Double saldo;
    
}
