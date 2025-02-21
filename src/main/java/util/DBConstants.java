package main.java.util;

import java.util.List;

public class DBConstants {
    public static final List<String> CYCLES_COLUM_LIST = List.of(
        "id",
        "cedula_cliente",
        "admin",
        "fecha",
        "hora",
        "cantidad_recibida",
        "moneda_recibida",
        "metodo_recibido",
        "cantidad_enviada",
        "moneda_enviada",
        "metodo_enviado",
        "status",
        "tasa",
        "ref_bancaria" 
    );

    public static final List<String> CYCLES_TYPES_LIST = List.of (     
"text primary key unique not null",
        "text references clienteS(cedula)",
        "text references administradores(nombre_usuario)",
        "date not null",
        "TIME WITH TIME ZONE DEFAULT timezone('America/Caracas', CURRENT_TIME)",
        "double precision",
        "text",
        "text references bancos(codigo)",
        "double precision",
        "text",
        "text references bancos(codigo)",
        "text",
        "double precision",
        "text" 
    );

    public static final List<String> TRANS_COLUMNS_LIST = List.of (     
        "id",
        "cicle_id",
        "cedula_cliente",
        "admin",
        "fecha",
        "hora",
        "tipo",
        "cantidad_recibida",
        "moneda_recibida",
        "metodo_recibido",
        "cantidad_enviada",
        "moneda_enviada",
        "metodo_enviado",
        "status",
        "tasa",
        "ganancia",
        "ref_bancaria"
    );

    public static final List<String> TRANS_TYPES_LIST = List.of (
        "text primary key unique not null",
        "text references ciclos(id)",
        "text references clienteS(cedula)",
        "text references administradores(nombre_usuario)",
        "date not null",
        "TIME WITH TIME ZONE DEFAULT timezone('America/Caracas', CURRENT_TIME)",
        "text",
        "double precision",
        "text",
        "text references bancos(codigo)",
        "double precision",
        "text",
        "text references bancos(codigo)",
        "text",
        "double precision",
        "double precision",
        "text not null"
    );

    public static final List<String> INVENTORY_COLUMNS_LIST = List.of (        
        "referencia",
        "id_trans",
        "fecha",
        "hora",
        "tipo_movimiento",
        "cantidad",
        "moneda",
        "metodo",
        "tipo"
    );

    public static final List<String> INVENTORY_TYPES_LIST = List.of(
        "serial primary key",
        "text references transacciones(id)",
        "date not null",
        "time with time zone",
        "text",
        "double precision",
        "text",
        "text",
        "text"
    );

    public static final List<String> BANKS_COLUMNS_LIST = List.of (
        "codigo",
        "nombre_banco",
        "numero_cuenta",
        "moneda",
        "saldo_actual",
        "correo"
    );

    public static final List<String> BANKS_TYPES_LIST = List.of (
        "text primary key unique not null",
        "text",
        "text",
        "text",
        "double precision",
        "text"
    );
    
    public static final List<String> ADMIN_COLUMS_LIST = List.of (
        "id_administrador",
        "nombre_usuario",
        "password",
        "estado",
        "rol"
    );
        
    public static final List<String> ADMIN_TYPES_LIST = List.of (
        "primary key text unique not null",
        "text unique not null",
        "text not null",
        "text",
        "text"
    );

    public static List<String> CLIENTS_COLUMNS_LIST = List.of (
        "cedula",
        "nombre",
        "apellido",
        "alias"
    );

    public static List<String> CLIENTS_TYPES_LIST = List.of (
        "text primary key unique not null",
        "text not null",
        "text not null",
        "text"
    );
}