package main.test.java.main;

import javafx.beans.property.StringProperty;
import main.java.entities.Clients;
import main.java.entities.Transactions;
import javafx.beans.property.SimpleStringProperty;

public class Main {
    public static void main(String[] args) {
        Clients andresito = new Clients("28057599", "Andres", "Rebanales", "andresito");
        Transactions t = new Transactions (
            "12345687888",
            "1234567888888", 
            andresito,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );

        System.out.println(andresito.getName());
        final String date = t.getDate();

        //... los demas parametros que quieras

        StringProperty nameProperty = new SimpleStringProperty(date);
        System.out.println(nameProperty.get());
    }
}
