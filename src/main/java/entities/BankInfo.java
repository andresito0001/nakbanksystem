package main.java.entities;

public class BankInfo {
    // Constructor
    public BankInfo(String code, String bankName, String balance, String moneyType, String mail, String numberAccount) {
        this.code = code;
        this.bankName = bankName;
        this.balance = balance;
        this.moneyType = moneyType;
        this.mail = mail;
        this.numberAccount = numberAccount;
    }

    public BankInfo(String code, String bankName, String balance, String moneyType) {
        this.code = code;
        this.bankName = bankName;
        this.balance = balance;
        this.moneyType = moneyType;
    }

    public String getBalance() {
        return balance;
    }

    public String getBankName() {
        return bankName;
    }

    public String getCode() {
        return code;
    }

    public String getMail() {
        return mail;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public String getNumberAccount() {
        return numberAccount;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public void setNumberAccount(String numberAccount) {
        this.numberAccount = numberAccount;
    }

    private String code;
    private String bankName;
    private String balance;
    private String moneyType;
    private String mail;
    private String numberAccount;

}
