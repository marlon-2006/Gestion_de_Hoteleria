package gui;

import model.*;
import javax.swing.*;
import java.awt.*;

public class VentanaMenuRol extends JFrame {
    private final HotelJWMarriottQuito hotel;
    private final Empleado empleado;
    public VentanaMenuRol(HotelJWMarriottQuito hotel, Empleado empleado){
        this.hotel=hotel;this.empleado=empleado;setTitle("Hotel JW Marriott - "+empleado.getClass().getSimpleName());setSize(430,470);setLocationRelativeTo(null);setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel p=new JPanel(new GridLayout(0,1,8,8));p.setBorder(BorderFactory.createEmptyBorder(20,45,20,45));
        p.add(new JLabel("Bienvenido/a: "+empleado.getNombre()+" "+empleado.getApellido(),SwingConstants.CENTER));
        p.add(new JLabel("Rol: "+empleado.getClass().getSimpleName()+" | Código: "+empleado.getCodigoEmp(),SwingConstants.CENTER));
        if(empleado instanceof Recepcionista){boton(p,"Registrar cliente",()->new VentanaRegistrarCliente(this,true,hotel).setVisible(true));boton(p,"Ver habitaciones",()->new VentanaHabitaciones(hotel).setVisible(true));boton(p,"Realizar reserva",()->new VentanaReserva(hotel).setVisible(true));boton(p,"Ver reservas",()->new ListaReservas(hotel).setVisible(true));boton(p,"Checkout / limpieza",()->new VentanaHousekeeping(hotel).setVisible(true));}
        else if(empleado instanceof Administrador){boton(p,"Gestionar empleados",()->new VentanaGestionEmpleados(hotel).setVisible(true));boton(p,"Ver habitaciones",()->new VentanaHabitaciones(hotel).setVisible(true));boton(p,"Ver reservas",()->new ListaReservas(hotel).setVisible(true));boton(p,"Control de limpieza",()->new VentanaHousekeeping(hotel).setVisible(true));}
        else {boton(p,"Habitaciones y limpieza",()->new VentanaHousekeeping(hotel).setVisible(true));boton(p,"Consultar habitaciones",()->new VentanaHabitaciones(hotel).setVisible(true));}
        boton(p,"Cerrar sesión",()->{DataStore.guardar(hotel);dispose();new VentanaLogin(hotel).setVisible(true);});
        add(p);
    }
    private void boton(JPanel p,String texto,Runnable accion){JButton b=new JButton(texto);b.addActionListener(e->accion.run());p.add(b);}
}
