package main.java.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import main.java.util.ConnectionPool;
import main.java.util.DatabaseUtils;

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
