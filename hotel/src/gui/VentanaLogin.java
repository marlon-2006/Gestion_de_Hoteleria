package gui;

import model.*;
import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {
    private final HotelJWMarriottQuito hotel;
    private final JTextField codigo=new JTextField();
    public VentanaLogin(HotelJWMarriottQuito hotel){
        this.hotel=hotel;setTitle("Inicio de sesión - Hotel JW Marriott Quito");setSize(430,250);setLocationRelativeTo(null);setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel p=new JPanel(new GridLayout(0,1,8,8));p.setBorder(BorderFactory.createEmptyBorder(25,55,25,55));
        p.add(new JLabel("SISTEMA DE GESTIÓN HOTELERA",SwingConstants.CENTER));p.add(new JLabel("Código de empleado:"));p.add(codigo);
        JButton entrar=new JButton("Ingresar");entrar.addActionListener(e->ingresar());p.add(entrar);
        p.add(new JLabel("Prueba: ADM001 / REC001 / HK001",SwingConstants.CENTER));add(p);getRootPane().setDefaultButton(entrar);
    }
    private void ingresar(){String c=codigo.getText().trim();for(Empleado e:hotel.getEmpleados())if(e.getCodigoEmp().equalsIgnoreCase(c)){dispose();new VentanaMenuRol(hotel,e).setVisible(true);return;}JOptionPane.showMessageDialog(this,"Código de empleado no encontrado.","Acceso denegado",JOptionPane.ERROR_MESSAGE);}
}
