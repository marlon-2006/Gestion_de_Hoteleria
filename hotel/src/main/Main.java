package main;

import gui.VentanaLogin;
import model.DataStore;
import model.HotelJWMarriottQuito;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        HotelJWMarriottQuito hotel = DataStore.cargar();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> DataStore.guardar(hotel)));
        java.awt.EventQueue.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            new VentanaLogin(hotel).setVisible(true);
        });
    }
}
