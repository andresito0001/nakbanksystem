package main.java.entities;

public class Inventory {
    Inventory(final String date, final String time, final String typeMov, final Double quantity, 
            final String typeMoney, final String method, final String type) {
        this.date = date;
        this.time = time;
        this.typeMov = typeMov;
        this.quantity = quantity;
        this.typeMoney = typeMoney;
        this.method = method;
        this.type = type;
    }
    
    // getters
    public String getDate() { return this.date; }
    public String getTime() { return time; }
    public String getMethod() { return this.method; }
    public Double getQuantity() { return this.quantity; }
    public String getType() { return this.type; }
    public String getTypeMoney() { return this.typeMoney; }
    public String getTypeMov() { return this.typeMov; }

    private final String date;
    private final String time;
    private final String typeMov;
    private final Double quantity;
    private final String typeMoney;
    private final String method;
    private final String type;
}