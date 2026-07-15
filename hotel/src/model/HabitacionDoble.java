package model;

public class HabitacionDoble extends Habitacion{

    private final int capacidad = 2;


    // CONSTRUCTOR
    public HabitacionDoble(int numero, double precioPorNoche) {
        super(numero, precioPorNoche);
    }


    // METODOS
    @Override
    public double calcularPrecio() {
        return this.precioPorNoche;
    }
}
