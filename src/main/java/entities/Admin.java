package main.java.entities;

public class Admin {
    Admin(final String userName, final String password, final String status, final String rol) {
        this.userName = userName;
        this.password = password;
        this.status = status;
        this.rol = rol;
    }
    
    // getters
    public final String getUserName() { return this.userName; }
    public final String getPassword() { return this.password; }
    public String getStatus() { return this.status; }
    public String getRol() { return this.rol; }
    
    private final String userName;
    private final String password;
    private final String status;
    private final String rol;
}
