package main.java.entities;

import java.sql.Date;

public class TransactionFill {
    public TransactionFill(String clientCi, String status, String type, Date fromDate, Date toDate) {
        this.clientCi = clientCi;
        this.status = status;
        this.type = type;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getClientCi() { return clientCi; }
    public Date getFromDate() { return fromDate; }
    public String getStatus() { return status; }
    public Date getToDate() { return toDate; }
    public String getType() {  return type; }

    @Override
    public String toString() {
        return "TransactionFill{" +
                "clientCi='" + clientCi + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", fromDate=" + fromDate.toString() +
                ", toDate=" + toDate.toString() +
                '}';
    }
    
    private String clientCi;
    private String status;
    private String type;
    private Date fromDate;
    private Date toDate;

}
