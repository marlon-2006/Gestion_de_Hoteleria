package model;
import exception.HabitacionNoDisponibleException;
import exception.ReservaInvalidaException;
import model.enums.EstadoReserva;
import model.enums.EstadoHabitacion;
import model.interfaces.IFacturable;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class Reserva implements IFacturable, java.io.Serializable {
    private String idReserva;
    private Date fechaEntrada;
    private Date fechaSalida;
    private EstadoReserva estado;

    private Cliente cliente;
    private Habitacion habitacion;
    private List<Servicio> serviciosAdicionales;
    private double descuento;

    public Reserva(String idReserva, Cliente cliente, Habitacion habitacion, Date fechaEntrada, Date fechaSalida)
    throws ReservaInvalidaException, HabitacionNoDisponibleException {
        if(fechaSalida.before(fechaEntrada) || fechaSalida.equals(fechaEntrada)){
            throw new ReservaInvalidaException("Error: La fecha de salida debe ser posterior a la fecha de entrada.");
        }

        if (habitacion.getEstado() != EstadoHabitacion.DISPONIBLE){
            throw new HabitacionNoDisponibleException("Error: La habitacion N° " + habitacion.getNumero() + " no esta disponible actualmente (" + habitacion.getEstado() + ").");
        }


        this.idReserva = idReserva;
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.estado = EstadoReserva.PENDIENTE;
        this.serviciosAdicionales = new ArrayList<>();
        this.descuento = 0.0;
    }

    public void agregarServicio(Servicio servicio) {
        this.serviciosAdicionales.add(servicio);
        servicio.aplicar();
    }

    public int calcularNoches() {
        long diferenciaMillis = Math.abs(fechaSalida.getTime() - fechaEntrada.getTime());
        long dias = TimeUnit.DAYS.convert(diferenciaMillis, TimeUnit.MILLISECONDS);
        return (dias == 0) ? 1 : (int) dias; // Al menos 1 noche
    }

    @Override
    public double calcularTotal() {
        int noches = calcularNoches();
        double costoHabitacion = habitacion.calcularPrecio() * noches;

        double costoServicios = 0.0;
        for(Servicio s: serviciosAdicionales){
            costoServicios += s.getCosto();
        }
        double subtotal = costoHabitacion + costoServicios;
        double totalConDescuento = subtotal - (subtotal * descuento);
        return totalConDescuento;
    }
    public void confirmar() {
        this.estado = EstadoReserva.CONFIRMADA;
        this.habitacion.cambiarEstado(EstadoHabitacion.OCUPADA);
        System.out.println("Reserva " + idReserva + " confirmada con éxito para " + cliente.getNombre());
    }
    public void cancelar() {
        this.estado = EstadoReserva.CANCELADA;
        this.habitacion.cambiarEstado(EstadoHabitacion.DISPONIBLE);
        System.out.println("Reserva " + idReserva + " ha sido cancelada.");
    }


    // GETTERS Y SETTERS
    public String getIdReserva() {
        return idReserva;
    }
    public Date getFechaEntrada() {
        return fechaEntrada;
    }
    public Date getFechaSalida() {
        return fechaSalida;
    }
    public EstadoReserva getEstado() {
        return estado;
    }

    public Cliente getCliente() {
        return cliente;
    }
    public Habitacion getHabitacion() {
        return habitacion;
    }
    public List<Servicio> getServiciosAdicionales() {
        return serviciosAdicionales;
    }
    public double getDescuento() {
        return descuento;
    }
    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }
}



