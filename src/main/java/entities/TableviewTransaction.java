package main.java.entities;

import java.sql.Time;

public class TableviewTransaction {
    public TableviewTransaction(String ID, Time hora, String clientCi, String qtyReciveWithCurrency, String receivedMethod, String qtySendWithCurrency, String sentMethod, Double exchangeRate, Double revenue) {
        this.ID = ID;
        this.hora = hora;
        this.clientCi = clientCi;
        this.qtyReciveWithCurrency = qtyReciveWithCurrency;
        this.receivedMethod = receivedMethod;
        this.qtySendWithCurrency = qtySendWithCurrency;
        this.sentMethod = sentMethod;
        this.exchangeRate = exchangeRate;
        this.revenue = revenue;
    }

    public String getClientCi() { return clientCi; }
    public Double getExchangeRate() { return exchangeRate; }
    public Time getHora() { return hora; }
    public String getID() { return ID; }
    public String getQtyReciveWithCurrency() { return qtyReciveWithCurrency; }
    public String getQtySendWithCurrency() { return qtySendWithCurrency; }
    public String getReceivedMethod() { return receivedMethod; }
    public Double getRevenue() { return revenue; }
    public String getSentMethod() { return sentMethod; }

    private String ID;
    private Time hora;
    private String clientCi;
    private String qtyReciveWithCurrency;
    private String receivedMethod;
    private String qtySendWithCurrency;
    private String sentMethod;
    private Double exchangeRate;
    private Double revenue;
}
