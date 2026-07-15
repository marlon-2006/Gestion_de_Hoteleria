package model;

public class SuiteEjecutiva extends Habitacion{

    private boolean incluyeDesayuno;

    public SuiteEjecutiva(int numero, double precioPorNoche, boolean incluyeDesayuno) {
        super(numero, precioPorNoche);
        this.incluyeDesayuno = incluyeDesayuno;
    }

    @Override
    public double calcularPrecio() {
        double cargoDesayuno = incluyeDesayuno ? 25.00 : 0.0;
        return this.precioPorNoche + cargoDesayuno;

    }

    public boolean isIncluyeDesayuno(){
        return incluyeDesayuno;
    }
    public void setIncluyeDesayuno(boolean incluyeDesayuno){
        this.incluyeDesayuno = incluyeDesayuno;
    }




}
