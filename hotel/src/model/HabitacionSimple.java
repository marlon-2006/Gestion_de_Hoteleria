package model;

public class HabitacionSimple extends Habitacion {
    private final int capacidad = 1;

    public HabitacionSimple(int numero, double precioPorNoche) {
        super(numero, precioPorNoche);
    }


    @Override
    public double calcularPrecio() {
        return this.precioPorNoche;
    }
}
