package model;
import model.enums.TipoServicio;



public class Servicio implements java.io.Serializable {
    private String idServicio;
    private String nombre;
    private double costo;
    private TipoServicio tipo;


    public Servicio (String idServicio, String nombre, double costo, TipoServicio tipo) {
        this.idServicio = idServicio;
        this.nombre = nombre;
        this.costo = costo;
        this.tipo = tipo;
    }

    public void aplicar() {
        System.out.println("Aplicando servicio: " + nombre + " ($" + costo + ") a la reserva.");
    }


    public String getIdServicio(){
        return idServicio;
    }
    public String getNombre(){
        return nombre;
    }
    public double getCosto() {
        return costo;
    }
    public void setCosto(double costo) {
        this.costo = costo;
    }
    public TipoServicio getTipo() {
        return tipo;
    }



}
