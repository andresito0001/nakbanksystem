package main.java.entities;

public class Clients {
    public Clients(final String ci, final String name, final String lastName, final String alias) {
        this.name = name;
        this.lastName = lastName;
        this.ci = ci;
        this.alias = alias;
    }
    
    // Getters
    public String getName() { return name; }
    public String getLastName() { return lastName; }
    public String getCi() { return ci; }
    public String getAlias() { return alias; }

    private final String ci;
    private final String name;
    private final String lastName;
    private final String alias;
}