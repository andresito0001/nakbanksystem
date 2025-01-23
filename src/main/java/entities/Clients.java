package main.java.entities;

public class Clients {
    // private StringProperty cedula;
    // private StringProperty nombre;
    // private StringProperty apellido;
    // private StringProperty aliasProperty;

    public Clients(final String ci, final String name, final String lastName, final String alias) {
        this.name = name;
        this.lastName = lastName;
        this.ci = ci;
        this.alias = alias;
        // cedula = new SimpleStringProperty(this.ci);
        // nombre = new SimpleStringProperty(this.name);;
        // apellido =  new SimpleStringProperty(this.lastName);;
        // aliasProperty = new SimpleStringProperty(this.alias);;
    }

    // Getters
    public String getName() { return name; }
    public String getLastName() { return lastName; }
    public String getCi() { return ci; }
    public String getAlias() { return alias; }

    // public String getCedula () { return cedula.get(); }
    // public String getNombre () { return nombre.get(); }
    // public String getApellido () { return apellido.get(); }
    // public String getAliasProperty () { return aliasProperty.get(); }

    private final String ci;
    private final String name;
    private final String lastName;
    private final String alias;
}