package main.java.entities;

public class User {
    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
    
    private String username;
}
