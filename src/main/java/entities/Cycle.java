package main.java.entities;

public class Cycle {
    public Cycle (final String id, final Clients client, final String adminUsername,
    final String date, final String time,
    final Double quantityReceived, final String currencyReceived, final String receivedMethod,
    final Double sentQuantity, final String sentCurrency, final String sentMethod, final String status, 
    final Double rate, final String bankRef, final String status_recepcion) {
        
        this.id = id;
        this.client = client;
        this.adminUsername = adminUsername;
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
        this.status_recepcion = status_recepcion;
    }
    
    //getters 
    public String getId() { return id; }
    public Clients getClient() { return client; }
    public String getAdmin() { return adminUsername; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public Double getQuantityReceived() { return quantityReceived; }
    public String getCurrencyReceived() { return currencyReceived; }
    public String getReceivedMethod() { return receivedMethod;}
    public String getSentCurrency() { return sentCurrency; }
    public String getSentMethod() { return sentMethod; }
    public Double getSentQuantity() { return sentQuantity; }
    public String getStatus() { return status; }
    public Double getRate() { return rate; }
    public String getBankRef() { return bankRef;}
    public String getStatusRecepcion () { return status_recepcion; }

    private final String id;
    private final Clients client;
    private final String adminUsername;
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
    private final String status_recepcion;
}
