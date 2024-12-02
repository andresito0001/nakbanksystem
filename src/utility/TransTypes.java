package utility;

public enum TransTypes {
    INVERSION("INVERSION"),
    COMPRA("COMPRA"),
    VENTA("VENTA");

    TransTypes(final String transType) {
        this.transType = transType;
    }

    public final String getTransType() { return transType; }
    
    private final String transType;
}