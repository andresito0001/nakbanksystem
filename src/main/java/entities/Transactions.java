package main.java.entities;

import java.sql.Connection;

public class Transactions extends Cicle {
    Transactions(final Connection conn, final String id, final String parent_id, final Clients client, final Admin admin, final String date, final String time, final String type,
    final Double quantityReceived, final String currencyReceived, final String receivedMethod,
    final Double sentQuantity, final String sentCurrency, final String sentMethod, final String status, 
    final Double rate, final Double revenue, final String bankRef) {
        super(conn, id, client, admin, date, time, quantityReceived,
        currencyReceived, receivedMethod, sentQuantity, sentCurrency, sentMethod, status, rate, bankRef);
        
        this. parent_id = parent_id;
        this.revenue = revenue;
        this.type = type;
    }
    
    private final String type;
    private final String parent_id;
    private final Double revenue;
}
