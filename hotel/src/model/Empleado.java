package model;

public abstract class Empleado extends Persona {

    protected double salario;
    protected String turno;
    protected String codigoEmp;

    public Empleado(String nombre, String apellido, String cedula, String telefono,double salario,String turno,String codigoEmp) {
        super(nombre, apellido, cedula, telefono);
        this.salario = salario;
        this.turno = turno;
        this.codigoEmp = codigoEmp;
    }

    public abstract void trabajar();
    public abstract double calcularSalario();

    // GETTERS Y SETTERS

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getCodigoEmp() {
        return codigoEmp;
    }
}
