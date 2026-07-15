package model;

import model.enums.EstadoHabitacion;
import model.interfaces.ILimpiable;

public abstract class Habitacion implements ILimpiable, java.io.Serializable {

    protected int numero;
    protected double precioPorNoche;
    protected EstadoHabitacion estado;

    public Habitacion(int numero,double precioPorNoche){
        this.numero = numero;
        this.precioPorNoche = precioPorNoche;
        this.estado = EstadoHabitacion.DISPONIBLE;
    }


    public abstract double calcularPrecio();

    public void cambiarEstado(EstadoHabitacion nuevoEstado){
        this.estado = nuevoEstado;
    }
    @Override
    public void limpiar(){
        System.out.println("Limpiando habitación número: " + numero);
        cambiarEstado(EstadoHabitacion.DISPONIBLE);
    }

    //GETTERS Y SETTERS

    public int getNumero(){
        return numero;
    }

    public double getPrecioPorNoche() {
        return precioPorNoche;
    }

    public EstadoHabitacion getEstado() {
        return estado;
    }
}
