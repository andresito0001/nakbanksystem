package main.java.entities;

public class Clients {
    Clients(final String ci, final String name, final String lastName, final String alias) {
        this.ci = ci;
        this.name = name;
        this.lastName = lastName;
        this.alias = alias;
    }
    
    // getters
    public String getCi() { return this.ci; }
    public String getName() { return this.name; }
    public String getLastName() { return this.lastName; }
    public String getAlias() { return this.alias; }

    private final String ci;
    private final String name;
    private final String lastName;
    private final String alias;
}