package main.java.entities;

import java.sql.Date;

public class Gastos {
    public Gastos(String id_gasto, Cycle cicle_id, String admin, Date fecha, String departamento, 
    Accounts elementoContable, String proveedor, String descripcion, Double monto, Banks bank, Double usd_equivalente) {
        this.id_gasto = id_gasto;
        this.cicle_id = cicle_id.getId();
        this.admin = admin;
        this.fecha = fecha;
        this.departamento = departamento;
        this.elementoContable = elementoContable;
        this.proveedor = proveedor;
        this.descripcion = descripcion;
        this.monto = monto;
        this.moneda = bank.getMoneda();
        this.usd_equivalente = usd_equivalente;
            if (moneda.equals("VES"))
                this.usd_equivalente = monto / cicle_id.getRate();
        metodo = bank.getCodigo();
    }

    public String getId_Gasto () { return id_gasto; }
    public String getCicle_Id() { return cicle_id; }
    public String getAdmin() { return admin; }
    public Date getFecha() { return fecha; }
    public String getDepartamento() { return departamento; }
    public Accounts getElementoContable() { return elementoContable; }
    public String getProveedor () { return proveedor; }
    public String getDescripcion () { return descripcion; }
    public Double getMonto () { return monto; }
    public String getMoneda () { return moneda; }
    public String getMetodo () { return metodo; }
    public Double getUsd_Equivalente () { return usd_equivalente; }
    
    private String id_gasto;
    private String cicle_id;
    private String admin;
    private Date fecha;
    private String departamento;
    private Accounts elementoContable;
    private String proveedor;
    private String descripcion;
    private Double monto;
    private Double usd_equivalente;
    private String moneda;
    private String metodo;
    
}
