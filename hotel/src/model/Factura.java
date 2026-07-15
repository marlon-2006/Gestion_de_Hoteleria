package model;

import model.interfaces.IFacturable;
import java.util.Date;

public class Factura implements IFacturable, java.io.Serializable {
    private String idFactura;
    private Date fechaEmision;
    private Reserva reserva;
    private final double IVA = 0.15;

    public Factura (String idFactura, Reserva reserva){
        this.idFactura = idFactura;
        this.reserva = reserva;
        this.fechaEmision = new Date();
    }

    public double calcularSubtotal() {
        return reserva.calcularTotal(); // Subtotal obtenido del cálculo de la reserva
    }

    public double calcularIVA() {
        return calcularSubtotal() * IVA;
    }


    @Override
    public double calcularTotal() {
        return calcularSubtotal() + calcularIVA();
    }
    public void imprimirFactura() {
        System.out.println("==================================================");
        System.out.println("            HOTEL JW MARRIOTT QUITO               ");
        System.out.println("              FACTURA ELECTRÓNICA                 ");
        System.out.println("==================================================");
        System.out.println("Nro Factura : " + idFactura);
        System.out.println("Fecha       : " + fechaEmision);
        System.out.println("Cliente     : " + reserva.getCliente().getNombre() + " " + reserva.getCliente().getApellido());
        System.out.println("Cédula/RUC  : " + reserva.getCliente().getCedula());
        System.out.println("Habitación  : N° " + reserva.getHabitacion().getNumero() + " (" + reserva.getHabitacion().getClass().getSimpleName() + ")");
        System.out.println("Noches      : " + reserva.calcularNoches());
        System.out.println("--------------------------------------------------");
        System.out.printf("Subtotal    : $%.2f%n", calcularSubtotal());
        System.out.printf("IVA (15%%)   : $%.2f%n", calcularIVA());
        System.out.printf("TOTAL A PAGAR: $%.2f%n", calcularTotal());
        System.out.println("==================================================");
    }


    public String getIdFactura(){
        return idFactura;
    }
    public Date getFechaEmision(){
        return fechaEmision;
    }
    public Reserva getReserva(){
        return reserva;
    }


}
