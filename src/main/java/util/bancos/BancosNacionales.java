package main.java.util.bancos;

public enum BancosNacionales {
    BANCO_DE_VENEZUELA("Banco de Venezuela", "0102"),
    BANCO_MERCANTIL("Banco Mercantil", "0105"),
    BANCO_PROVINCIAL("Banco Provincial", "0108"),
    BANCO_NACIONAL_DE_CREDITO("Banco Nacional de Credito", "0191"),
    BANESCO("Banesco", "0134"),
    BANCAAMIGA("Bancamiga", "0172"),
    BANCO_PLAZA("Banco Plaza", "0138"),
    BANCO_SOFITASA("Banco Sofitasa", "0137"),
    BANCO_VENEZOLANO_DE_CREDITO("Banco Venezolano de Credito", "0104"),
    BANCRECER("Bancrecer", "0168"),
    BANFANB("Banfanb", "0177"),
    BANGENTE("Bangente", "0146"),
    BANPLUS("Banplus", "0174"),
    BBVA_PROVINCIAL("BBVA Provincial", "0108"),
    DELSUR_BANCO_UNIVERSAL("Delsur Banco Universal", "0157"),
    MI_BANCO("Mi Banco", "0169"),
    N58_BANCO_DIGITAL_BANCO_MICROFINANCIERO("Banco Microfinanciero", "0178"),
    BANCO_100("100% Banco", "0156"),
    BANCARIBE("Bancaribe", "0114"),
    BANCO_ACTIVO("Banco Activo", "0171"),
    BANCO_AGRICOLA_DE_VENEZUELA("Banco Agricola de Venezuela", "0166"),
    BANCO_BICENTENARIO_DEL_PUEBLO("Banco Bicentenario", "0175"),
    BANCO_CARONI("Banco Caroni", "0128"),
    BANCO_DEL_TESORO("Banco del Tesoro", "0163"),
    BANCO_EXTERIOR("Banco Exterior", "0115"),
    BANCO_FONDO_COMUN("Banco Fondo Comun", "0151"),
    BANCO_INTERNACIONAL_DE_DESARROLLO("Banco Internacional de Desarrollo", "0173");

    BancosNacionales(final String nombre, final String codigo) {
        this.codigo = codigo;
        this.nombre = nombre;
    }
    
    public final String getNombre() { return this.nombre; } 
    public final String getCodigo() { return this.codigo; }
    
    private final String nombre;
    private final String codigo;
}
