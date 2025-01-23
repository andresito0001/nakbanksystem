package main.java.services;

import java.sql.Connection;

import main.java.entities.Transactions;
import main.java.util.TimeZone;
import main.java.util.ULID;
import main.java.util.monedas.MoneyType;

public class TransactionService {
    TransactionService(final Connection conn) {
        this.conn = conn;
    }
    
    public void registerTransaction(final Transactions transactions) {
        // byte[] entropy = new byte[] { 0x1, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };
        // final String id = ULID.generate(System.currentTimeMillis(), entropy);
        // final String cicle_id = "1";

        // final String query = "insert into trans(id, cicle_id, cedula_cliente, admin, fecha, tipo, cantidad_recibida, moneda_recibida, metodo_recibido, cantidad_enviada, moneda_enviada, metodo_enviado, status, tasa, ganancia, ref_bancaria) " + 
        // "values (" + "'" + id + "', "  + "'" + cicle_id + "', " + "'V-11222599', " + "'" + username + "', " + "'" + TimeZone.getDateZoneCaracas() + "', " + "'" + transType + "', "
        // + "'" + cantRecibida + "', " + "'" + typeMoneyReceived + "', " + "'" + metodoRecibido + "', " + "'" + cantEnviada + "', " + "'" + typeMoneySent + "', " + "'" + metodoEnviado + "', " + "'OK', " + "'" + tasa + "', " + "'" + gananciaPerdida + "', " + "'123456789555'" + ");"; 
    }

    private final Connection conn;
}
