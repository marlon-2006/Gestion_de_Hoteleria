package model;

import java.util.ArrayList;
import java.util.List;

public class Housekeeper extends Empleado{
    private String zona;
    private List<Integer> habitAsignada;

    public Housekeeper(String nombre, String apellido, String cedula, String telefono, double salario, String turno, String codigoEmp,String zona) {
        super(nombre, apellido, cedula, telefono, salario, turno, codigoEmp);
        this.zona = zona;
        this.habitAsignada = new ArrayList<>();
    }



    public void limpiarHabitacion(Habitacion habitacion){
        System.out.println("Housekeeér " + nombre + " está limpiando la habitacion " + habitacion.getNumero());
        habitacion.limpiar();
    }



    @Override
    public void trabajar() {
        System.out.println("Revisando y limpiando las habitaciones asignadas en la zona: " + zona);
    }

    @Override
    public double calcularSalario() {
        return this.salario;
    }

    public String getZona() { return zona; }
    @Override public String getDatosCompletos() { return "Housekeeper: " + nombre + " " + apellido + " | Zona: " + zona + " | Turno: " + turno; }
    @Override public void registrarse() { System.out.println("Registrando housekeeper " + nombre); }
    @Override public String toString() { return codigoEmp + " - " + nombre + " " + apellido + " (Housekeeper)"; }
}
