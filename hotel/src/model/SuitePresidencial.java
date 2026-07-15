package model;

public class SuitePresidencial extends Habitacion{

    private boolean incluyeServicioVIP;

    public SuitePresidencial(int numero, double precioPorNoche, boolean incluyeServicioVIP) {
        super(numero, precioPorNoche);
        this.incluyeServicioVIP = incluyeServicioVIP;
    }


    @Override
    public double calcularPrecio() {
        double cargoVIP = incluyeServicioVIP ? 100.00: 0.0;
        return this.precioPorNoche + cargoVIP;
    }

    public boolean isIncluyeServicioVIP() {
        return incluyeServicioVIP;
    }


    public void setIncluyeServicioVIP(boolean incluyeServicioVIP) {
        this.incluyeServicioVIP = incluyeServicioVIP;
    }
}
