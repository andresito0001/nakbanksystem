package main.java.entities;

public class Transactions extends Cycle {
    public Transactions(final String id, final String parentID, final Clients client, final String adminUsername, final String date, final String time, final String type,
    final Double quantityReceived, final String currencyReceived, final String receivedMethod,
    final Double sentQuantity, final String sentCurrency, final String sentMethod, final String status, 
    final Double rate, final Double revenue, final String bankRef) {
        super(id, client, adminUsername, date, time, quantityReceived,
        currencyReceived, receivedMethod, sentQuantity, sentCurrency, sentMethod, status, rate, bankRef);
        
        this. parent_id = parentID;
        this.revenue = revenue;
        this.type = type;
    }

    public String getType() { return type; }
    public String getParent_id() { return parent_id; }
    public Double getRevenue() { return revenue; }

    private final String type;
    private final String parent_id;
    private final Double revenue;
}
