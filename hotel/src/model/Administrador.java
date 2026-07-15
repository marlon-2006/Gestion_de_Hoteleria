package model;

import java.util.Date;

public class Administrador extends Empleado {
    private int nivelAcceso;
    private Date fechaRegistro;

    public Administrador(String nombre, String apellido, String cedula, String telefono, double salario, String turno, String codigoEmp, int nivelAcceso) {
        super(nombre, apellido, cedula, telefono, salario, turno, codigoEmp);
        this.nivelAcceso = nivelAcceso;
        this.fechaRegistro = new Date();
    }

    public void gestionarUsuario(){
        System.out.println("El Administrador " + nombre + " está gestionando las credenciales y roles de los usuarios.");
    }

    public void configurarSistema() {
        System.out.println("Configurando los parámetros globales del hotel (tarifas de temporada alta, etc.).");
    }


    @Override
    public void trabajar() {
        System.out.println("El Administrador está supervisando el correcto funcionamiento técnico y operativo del sistema.");
    }

    @Override
    public double calcularSalario() {
        return this.salario;
    }

    @Override
    public String getDatosCompletos() {
        return "Administrador: " + nombre + " " + apellido +
                " | Cedula: " + cedula +
                " | Nivel de Acceso: " + nivelAcceso +
                " | Regristrado el: " + fechaRegistro;
    }

    @Override
    public void registrarse() {
        System.out.println("Registrando cuenta de administrador con credenciales del alto nivel en el sistema.");
    }

    public int getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(int nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }
    @Override public String toString() { return codigoEmp + " - " + nombre + " " + apellido + " (Administrador)"; }
}
