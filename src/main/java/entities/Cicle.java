package main.java.entities;

import java.sql.Connection;

public class Cicle {
    public Cicle (final Connection conn, final String id, final Clients client, final Admin admin,
    final String date, final String time,
    final Double quantityReceived, final String currencyReceived, final String receivedMethod,
    final Double sentQuantity, final String sentCurrency, final String sentMethod, final String status, 
    final Double rate, final String bankRef) {
        
        this.id = id;
        this.client = client;
        this.admin = admin;
        this.date = date;
        this.time = time;
        this.quantityReceived = quantityReceived;
        this.currencyReceived = currencyReceived;
        this.receivedMethod = receivedMethod;
        this.sentQuantity = sentQuantity;
        this.sentCurrency = sentCurrency;
        this.sentMethod = sentMethod;
        this.status = status;
        this.rate = rate;
        this.bankRef = bankRef;
    }
    
    private final String id;
    private final Clients client;
    private final Admin admin;
    private final String date;
    private final String time;
    private final Double quantityReceived;
    private final String currencyReceived;
    private final String receivedMethod;
    private final Double sentQuantity;
    private final String sentCurrency;
    private final String sentMethod;
    private final String status;
    private final Double rate; 
    private final String bankRef;
}
