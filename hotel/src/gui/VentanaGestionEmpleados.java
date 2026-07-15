package gui;

import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaGestionEmpleados extends JFrame {
    private final HotelJWMarriottQuito hotel;
    private final JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Recepcionista", "Administrador", "Housekeeper"});
    private final JTextField txtNombre = new JTextField(), txtApellido = new JTextField(), txtCedula = new JTextField();
    private final JTextField txtTelefono = new JTextField(), txtSalario = new JTextField(), txtTurno = new JTextField();
    private final JTextField txtCodigo = new JTextField(), txtEspecifico = new JTextField();
    private final JLabel lblEspecifico = new JLabel("Número de extensión:");
    private final DefaultTableModel modelo = new DefaultTableModel(new String[]{"Código","Nombre","Tipo","Turno","Salario","Dato específico"},0) {
        @Override public boolean isCellEditable(int r,int c){ return false; }
    };
    private final JTable tabla = new JTable(modelo);

    public VentanaGestionEmpleados(HotelJWMarriottQuito hotel) {
        this.hotel = hotel;
        setTitle("Gestión de empleados");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(850, 560);
        setLocationRelativeTo(null);
        construir();
        cargarTabla();
    }

    private void construir() {
        JPanel formulario = new JPanel(new GridLayout(0,4,8,8));
        formulario.setBorder(BorderFactory.createTitledBorder("Registrar empleado"));
        agregar(formulario,"Tipo:",cmbTipo); agregar(formulario,"Nombre:",txtNombre);
        agregar(formulario,"Apellido:",txtApellido); agregar(formulario,"Cédula:",txtCedula);
        agregar(formulario,"Teléfono:",txtTelefono); agregar(formulario,"Salario:",txtSalario);
        agregar(formulario,"Turno:",txtTurno); agregar(formulario,"Código:",txtCodigo);
        formulario.add(lblEspecifico); formulario.add(txtEspecifico);
        JButton registrar = new JButton("Registrar"); JButton limpiar = new JButton("Limpiar");
        formulario.add(registrar); formulario.add(limpiar);
        cmbTipo.addActionListener(e -> actualizarEtiqueta());
        registrar.addActionListener(e -> registrar());
        limpiar.addActionListener(e -> limpiar());
        JButton cerrar = new JButton("Cerrar"); cerrar.addActionListener(e -> dispose());
        JPanel sur = new JPanel(); sur.add(cerrar);
        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(sur, BorderLayout.SOUTH);
    }

    private void agregar(JPanel p,String texto,JComponent c){ p.add(new JLabel(texto)); p.add(c); }
    private void actualizarEtiqueta(){
        String t=(String)cmbTipo.getSelectedItem();
        lblEspecifico.setText("Recepcionista".equals(t)?"Número de extensión:":"Administrador".equals(t)?"Nivel de acceso:":"Zona:");
    }
    private void registrar(){
        try {
            String tipo=(String)cmbTipo.getSelectedItem();
            String nombre=txtNombre.getText().trim(), apellido=txtApellido.getText().trim();
            String cedula=txtCedula.getText().trim(), telefono=txtTelefono.getText().trim();
            String turno=txtTurno.getText().trim(), codigo=txtCodigo.getText().trim(), esp=txtEspecifico.getText().trim();
            if(nombre.isEmpty()||apellido.isEmpty()||cedula.isEmpty()||telefono.isEmpty()||turno.isEmpty()||codigo.isEmpty()||esp.isEmpty()) throw new IllegalArgumentException("Complete todos los campos.");
            if(!nombre.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]+" )||!apellido.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]+")) throw new IllegalArgumentException("Nombre y apellido solo deben contener letras.");
            if(!cedula.matches("\\d{10}|\\d{13}")) throw new IllegalArgumentException("La cédula/RUC debe tener 10 o 13 dígitos.");
            if(!telefono.matches("\\d{7,10}")) throw new IllegalArgumentException("El teléfono debe tener entre 7 y 10 dígitos.");
            double salario=Double.parseDouble(txtSalario.getText().trim());
            if(salario<=0) throw new IllegalArgumentException("El salario debe ser mayor a cero.");
            for(Empleado e:hotel.getEmpleados()) if(e.getCodigoEmp().equalsIgnoreCase(codigo)||e.getCedula().equals(cedula)) throw new IllegalArgumentException("Ya existe un empleado con ese código o cédula.");
            Empleado emp;
            if("Recepcionista".equals(tipo)) emp=new Recepcionista(nombre,apellido,cedula,telefono,salario,turno,codigo,esp);
            else if("Administrador".equals(tipo)) emp=new Administrador(nombre,apellido,cedula,telefono,salario,turno,codigo,Integer.parseInt(esp));
            else emp=new Housekeeper(nombre,apellido,cedula,telefono,salario,turno,codigo,esp);
            hotel.registrarEmpleado(emp); DataStore.guardar(hotel); cargarTabla(); limpiar();
            JOptionPane.showMessageDialog(this,"Empleado registrado correctamente.");
        } catch(NumberFormatException ex){ JOptionPane.showMessageDialog(this,"Salario y nivel de acceso deben ser numéricos.","Error",JOptionPane.ERROR_MESSAGE); }
        catch(Exception ex){ JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE); }
    }
    private void cargarTabla(){
        modelo.setRowCount(0);
        for(Empleado e:hotel.getEmpleados()){
            String dato=e instanceof Recepcionista?r((Recepcionista)e):e instanceof Administrador?"Nivel "+((Administrador)e).getNivelAcceso():((Housekeeper)e).getZona();
            modelo.addRow(new Object[]{e.getCodigoEmp(),e.getNombre()+" "+e.getApellido(),e.getClass().getSimpleName(),e.getTurno(),String.format("$%.2f",e.getSalario()),dato});
        }
    }
    private String r(Recepcionista e){ return "Ext. "+e.getNumExtension(); }
    private void limpiar(){ for(JTextField t:new JTextField[]{txtNombre,txtApellido,txtCedula,txtTelefono,txtSalario,txtTurno,txtCodigo,txtEspecifico})t.setText(""); txtNombre.requestFocus(); }
}
