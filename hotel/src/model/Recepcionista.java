package model;

import java.util.List;

import java.util.ArrayList;

public class Recepcionista extends Empleado{

    private String numExtension;
    private List<String> idiomas;

    public Recepcionista(String nombre, String apellido, String cedula, String telefono,
                         double salario, String turno, String codigoEmp,String numExtension) {
        super(nombre, apellido, cedula, telefono, salario, turno, codigoEmp);
        this.numExtension = numExtension;
        this.idiomas = new ArrayList<>();
    }

    public void atenderCliente(){
        System.out.println("El recepcionista " + nombre + " está atendiendo check-in/check-out.");
    }

    @Override
    public void trabajar() {
        System.out.println("Gestionando reservas en el sistema durante el turno de la: "+ turno);

    }
    @Override
    public double calcularSalario() {
        return this.salario;
    }


    @Override
    public String getDatosCompletos() {
        return "Recepcionista: " + nombre + " " + apellido +
                " | Cedula: " + cedula +
                " | Extensión: " + numExtension +
                " | Turno: " + turno;
    }

    @Override
    public void registrarse() {
        System.out.println("Registrando al recepcionista " + nombre + " en el sistema");
    }
    public String getNumExtension() { return numExtension; }
    @Override public String toString() { return codigoEmp + " - " + nombre + " " + apellido + " (Recepcionista)"; }
}
