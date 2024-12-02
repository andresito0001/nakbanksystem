package utility.monedas;

public enum MoneyType {
    BOLIVARES("Bolivares", "VES"),
    DOLARES_EFECTIVO("Dolares", "USD"),
    BINANCE_USDT("Binance USDT", "USDT"),
    ZELLE("Zelle USD", "USD");
    
    MoneyType(final String moneyType, final String codigo) {
        this.moneyType = moneyType;
        this.codigo = codigo;
    }
    
    public final String getNombre() { return this.moneyType; }
    public final String getCodigo() { return this.codigo; }

    private final String moneyType;
    private final String codigo;
}
