package model;

import exception.HabitacionNoDisponibleException;
import exception.ReservaInvalidaException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Cliente extends Persona{



    private String email;
    private String numTarjeta;
    private List<String> historialReservas;


    public Cliente(String nombre, String apellido, String cedula, String telefono,String email,String numTarjeta) {
        super(nombre, apellido, cedula, telefono);
        this.email = email;
        this.numTarjeta = numTarjeta;
        this.historialReservas = new ArrayList<>();
    }

    @Override
    public String getDatosCompletos() {
        return "Cliente: " + nombre + " " + apellido + " | Cédula: " + cedula + " | Email: " + email;
    }

    @Override
    public void registrarse() {
        System.out.println("Cliente registrado correctamente en la base de datos del JW Marriott.");
    }

    public String getNumTarjeta() {
        return numTarjeta;
    }

    public void setNumTarjeta(String numTarjeta) {
        this.numTarjeta = numTarjeta;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Reserva hacerReserva(String idReserva, Habitacion habitacion, Date entrada,Date salida) throws ReservaInvalidaException, HabitacionNoDisponibleException {
        Reserva nuevaReserva = new Reserva(idReserva,this,habitacion,entrada,salida);
        System.out.println("Reserva básica creada para el cliente " + nombre);
        return nuevaReserva;
    }

    public Reserva hacerReserva(String idReserva, Habitacion habitacion, Date entrada, Date salida, List<Servicio> servicios) throws ReservaInvalidaException, HabitacionNoDisponibleException {
        Reserva nuevaReserva = new Reserva(idReserva, this, habitacion, entrada, salida);
        for (Servicio s : servicios) {
            nuevaReserva.agregarServicio(s);
        }
        System.out.println("Reserva con " + servicios.size() + " servicio(s) adicional(es) creada.");
        return nuevaReserva;
    }
    public Reserva hacerReserva(String idReserva, Habitacion habitacion, Date entrada, Date salida, List<Servicio> servicios, double porcentajeDescuento) throws ReservaInvalidaException, HabitacionNoDisponibleException {
        Reserva nuevaReserva = hacerReserva(idReserva, habitacion, entrada, salida, servicios);
        nuevaReserva.setDescuento(porcentajeDescuento);
        System.out.println("¡Descuento de cliente frecuente aplicado!: " + (porcentajeDescuento * 100) + "%");
        return nuevaReserva;
    }



}
