package main.java.entities;

public class Inventory {
    public Inventory(String reference, String date, String movementType, Double quantity,
    String moneyType, String method, String operationType, String time, String transId) {
        this.reference = reference;
        this.date = date;
        this.movementType = movementType;
        this.quantity = quantity;
        this.moneyType = moneyType;
        this.method = method;
        this.operationType = operationType;
        this.time = time;
        this.transId = transId;
    }

    // getters
    public String getDate() { return date; }
    public String getMethod() { return method; }
    public String getMoneyType() { return moneyType; }
    public String getMovementType() { return movementType; }
    public String getOperationType() { return operationType; }
    public Double getQuantity() { return quantity; }
    public String getReference() { return reference; }
    public String getTime() { return time; }
    public String getTransId() { return transId; }

    private String reference;
    private String date;
    private String movementType;
    private Double quantity;
    private String moneyType;
    private String method;
    private String operationType;
    private String time;
    private String transId;
}