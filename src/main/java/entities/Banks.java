package main.java.entities;

public class Banks {
    
    public Banks (String codigo, String nombre, String moneda, Double saldo, String correo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.moneda = moneda;
        this.saldo = saldo;
        this.correo = correo;
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
    public String getCorreo () {
        return correo;
    }

    String codigo;
    String nombre;
    String moneda;
    Double saldo;
    String correo;
    
}
