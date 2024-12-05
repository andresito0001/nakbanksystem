package main.java.util;

public enum TransTypes {
    INVERSION("INVERSION"),
    COMPRA("COMPRA"),
    VENTA("VENTA"),
    SWAP("SWAP");

    TransTypes(final String transType) {
        this.transType = transType;
    }

    public final String getTransType() { return transType; }
    
    private final String transType;
}