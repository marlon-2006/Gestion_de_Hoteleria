package gui;

import model.*;
import model.enums.EstadoHabitacion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaHousekeeping extends JFrame {
    private final HotelJWMarriottQuito hotel;
    private final DefaultTableModel modelo=new DefaultTableModel(new String[]{"Número","Tipo","Estado","Precio"},0){@Override public boolean isCellEditable(int r,int c){return false;}};
    private final JTable tabla=new JTable(modelo);
    public VentanaHousekeeping(HotelJWMarriottQuito hotel){
        this.hotel=hotel; setTitle("Control de limpieza de habitaciones"); setSize(650,420); setLocationRelativeTo(null); setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JButton checkout=new JButton("Enviar a limpieza (checkout)"); JButton limpiar=new JButton("Marcar como limpia"); JButton cerrar=new JButton("Cerrar");
        checkout.addActionListener(e->cambiar(EstadoHabitacion.OCUPADA,EstadoHabitacion.LIMPIEZA));
        limpiar.addActionListener(e->cambiar(EstadoHabitacion.LIMPIEZA,EstadoHabitacion.DISPONIBLE)); cerrar.addActionListener(e->dispose());
        JPanel p=new JPanel();p.add(checkout);p.add(limpiar);p.add(cerrar); add(new JScrollPane(tabla),BorderLayout.CENTER);add(p,BorderLayout.SOUTH);cargar();
    }
    private void cargar(){modelo.setRowCount(0);for(Habitacion h:hotel.getHabitaciones())modelo.addRow(new Object[]{h.getNumero(),h.getClass().getSimpleName(),h.getEstado(),String.format("$%.2f",h.calcularPrecio())});}
    private void cambiar(EstadoHabitacion requerido,EstadoHabitacion nuevo){int f=tabla.getSelectedRow();if(f<0){JOptionPane.showMessageDialog(this,"Seleccione una habitación.");return;}Habitacion h=hotel.getHabitaciones().get(f);if(h.getEstado()!=requerido){JOptionPane.showMessageDialog(this,"La habitación debe estar en estado "+requerido+".");return;}h.cambiarEstado(nuevo);DataStore.guardar(hotel);cargar();JOptionPane.showMessageDialog(this,"Habitación "+h.getNumero()+" actualizada a "+nuevo+".");}
}
