package main.java.services;

import java.sql.Connection;

public class TransactionService {
    TransactionService(final Connection conn) {
        this.conn = conn;
    }
    
    private final Connection conn;
}
