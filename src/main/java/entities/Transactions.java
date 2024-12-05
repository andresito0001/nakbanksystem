package main.java.entities;

import java.sql.Connection;

public class Transactions {
    Transactions(final Connection conn, final Clients client, final Admin admin, final String date, final String time, final String type,
    final Double quantityReceived, final String currencyReceived, final String receivedMethod,
    final Double sentQuantity, final String sentCurrency, final String sentMethod, final String status, 
    final Double rate, final Double revenue, final String bankRef) {

        this.client = client;
        this.admin = admin;
        this.date = date;
        this.time = time;
        this.type = type;
        this.quantityReceived = quantityReceived;
        this.currencyReceived = currencyReceived;
        this.receivedMethod = receivedMethod;
        this.sentQuantity = sentQuantity;
        this.sentCurrency = sentCurrency;
        this.sentMethod = sentMethod;
        this.status = status;
        this.rate = rate;
        this.revenue = revenue;
        this.bankRef = bankRef;
    }
    
    private final Clients client;
    private final Admin admin;
    private final String date;
    private final String time;
    private final String type;
    private final Double quantityReceived;
    private final String currencyReceived;
    private final String receivedMethod;
    private final Double sentQuantity;
    private final String sentCurrency;
    private final String sentMethod;
    private final String status;
    private final Double rate; 
    private final Double revenue;
    private final String bankRef;
}
