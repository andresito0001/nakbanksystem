package main.java.entities;

public class CardModel {
    public CardModel(String code, String bankName, String balance) {
        this.code = code;
        this.bankName = bankName;
        this.balance = balance;
    }

    public String getCode() { return code; }
    public String getBalance() { return balance; }
    public String getBankName() { return bankName; }

    private String code;
    private String bankName;
    private String balance;
}