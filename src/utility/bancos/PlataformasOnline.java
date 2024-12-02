package utility.bancos;

public enum PlataformasOnline {
    BINANCE("Binance"),
    ZELLE("Zelle"),
    BANESCO_PANAMA("Banesco Panama"),
    PAYPAL("Paypal");

    PlataformasOnline(final String nombre) {
        this.nombre = nombre;
    }

    public final String getNombre() { return this.nombre; }
    
    private final String nombre;
}
