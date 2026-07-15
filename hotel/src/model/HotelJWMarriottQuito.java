package model;
import model.enums.EstadoHabitacion;
import java.util.List;
import java.util.ArrayList;

public class HotelJWMarriottQuito implements java.io.Serializable {
    private String nombre;
    private String direccion;
    private List<Habitacion> habitaciones;
    private List<Empleado> empleados;
    private List<Cliente> clientes;
    private List<Reserva> reservas;

    public HotelJWMarriottQuito() {
        this.nombre = "JW Marriott Quito";
        this.direccion = "Av. Orellana 1172 y Av. Amazonas, Quito";
        this.habitaciones = new ArrayList<>();
        this.empleados = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.reservas = new ArrayList<>();
    }
    public void agregarHabitacion(Habitacion h) {
        habitaciones.add(h);
    }

    public List<Habitacion> buscarHabitacionesDisponibles() {
        List<Habitacion> disponibles = new ArrayList<>();
        for (Habitacion h : habitaciones) {
            if (h.getEstado() == EstadoHabitacion.DISPONIBLE) {
                disponibles.add(h);
            }
        }
        return disponibles;
    }

    public void registrarEmpleado(Empleado e) {
        empleados.add(e);
    }

    public void registrarCliente(Cliente c) {
        clientes.add(c);
    }

    public void registrarReserva(Reserva r) {
        reservas.add(r);
        r.confirmar(); // Cambia el estado de la reserva y la habitación
    }

    // Getters de colecciones
    public List<Habitacion> getHabitaciones() { return habitaciones; }
    public List<Empleado> getEmpleados() { return empleados; }
    public List<Cliente> getClientes() { return clientes; }
    public List<Reserva> getReservas() { return reservas; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }



}
