package model;

public abstract class Persona implements java.io.Serializable {

    protected String nombre;
    protected String apellido;
    protected String cedula;
    protected String telefono;

    public Persona(String nombre,String apellido,String cedula,String telefono){
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.telefono = telefono;

    }

    public abstract String getDatosCompletos();
    public abstract void registrarse();

    // GETERS Y SETTERS

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
